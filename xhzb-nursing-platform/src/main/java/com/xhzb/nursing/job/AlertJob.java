package com.xhzb.nursing.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.nursing.domain.AlertData;
import com.xhzb.nursing.domain.AlertRule;
import com.xhzb.nursing.domain.DeviceData;
import com.xhzb.nursing.service.IAlertDataService;
import com.xhzb.nursing.service.IAlertRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 报警数据过滤与处理定时任务
 *
 * @author songyu
 * @date 2026-08-18
 */
@Component
@Slf4j
public class AlertJob {

    @Autowired
    private IAlertRuleService alertRuleService;

    @Autowired
    private IAlertDataService alertDataService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /** Redis Hash大key，存储设备最新上报数据 */
    private static final String IOT_DEVICE_LATEST_DATA_KEY = "iot:device_latest_data";

    /** Redis报警次数key前缀（规则id:设备id） */
    private static final String ALERT_COUNT_PREFIX = "alert:count:";

    /** Redis报警沉默周期key前缀（规则id:设备id） */
    private static final String ALERT_SILENT_PREFIX = "alert:silent:";

    /**
     * 设备数据报警过滤
     *
     * 步骤：1.查询所有正在生效的报警规则，没有则结束
     *      2.查询Redis中设备最新上报的数据，没有则结束
     *      3.遍历每条设备上报数据，查询匹配的报警规则
     *      4.遍历每条报警规则，校验阈值、沉默周期、持续周期，达到条件保存报警数据
     */
    public void deviceDataAlertFilter() {
        log.info("定时器执行!");

        //一、查询所有正在生效的报警规则，如果没有直接结束
        List<AlertRule> activeAlertRules = alertRuleService.list(new LambdaQueryWrapper<AlertRule>()
                .eq(AlertRule::getStatus, 1));
        if (CollUtil.isEmpty(activeAlertRules)) {
            log.info("没有正在生效的报警规则，结束定时任务");
            return;
        }

        //二、查询Redis中设备最新上报的数据
        Map<Object, Object> deviceLatestDataMap = stringRedisTemplate.opsForHash().entries(IOT_DEVICE_LATEST_DATA_KEY);
        if (CollUtil.isEmpty(deviceLatestDataMap)) {
            log.info("Redis中没有设备最新上报的数据，结束定时任务");
            return;
        }

        //2.1 把Hash结构中的value值收集到一个大List集合中
        List<DeviceData> allDeviceDataList = new ArrayList<>();
        for (Object value : deviceLatestDataMap.values()) {
            String json = (String) value;
            if (StringUtils.isEmpty(json)) {
                continue;
            }
            try {
                List<DeviceData> deviceDataList = JSONUtil.toList(json, DeviceData.class);
                if (CollUtil.isNotEmpty(deviceDataList)) {
                    allDeviceDataList.addAll(deviceDataList);
                }
            } catch (Exception e) {
                //单条设备数据解析失败不影响其他设备数据的处理
                log.warn("解析Redis中的设备上报数据失败：{}", json, e);
            }
        }

        //三、开始处理大List集合中的每一条设备上报的数据
        for (DeviceData deviceData : allDeviceDataList) {
            //3.1 判断设备上报的数据是否超过1分钟，如果超了结束本次循环
            if (isDataExpired(deviceData.getAlarmTime())) {
                continue;
            }

            //3.2 查询这条数据对应的报警规则（该产品物模型下所有规则 + 该产品物模型下对应设备规则，两个查询结果合并）
            List<AlertRule> rules = queryRulesByDeviceData(deviceData);

            //3.3 如果没有规则结束本次循环
            if (CollUtil.isEmpty(rules)) {
                continue;
            }

            //3.4 开始处理这条上报数据对应的每一条报警规则，校验判断是否需要报警处理
            for (AlertRule alertRule : rules) {
                processAlertRule(deviceData, alertRule);
            }
        }
    }

    /**
     * 处理一条设备上报数据对应的报警规则，校验判断是否需要报警处理
     *
     * 步骤：1.校验数据是否到达报警规则的阈值，未达则删除Redis中报警次数
     *      2.判断Redis中是否有沉默周期，有则跳过
     *      3.查询Redis中已报警次数加1，未达到持续周期则跳过
     *      4.达到持续周期，删除报警次数、添加沉默周期并保存报警数据
     *
     * @param deviceData 设备上报数据
     * @param alertRule 报警规则
     */
    private void processAlertRule(DeviceData deviceData, AlertRule alertRule) {
        //校验当前时间是否在报警生效时段内，不在则跳过本次规则校验
        if (!isInEffectivePeriod(alertRule.getAlertEffectivePeriod())) {
            return;
        }
        //4.1 校验数据是否到达报警规则的阈值，如果未达删除Redis中报警次数跳出本次循环
        if (!compareRuleValue(deviceData.getDataValue(), alertRule)) {
            stringRedisTemplate.delete(getAlertCountKey(alertRule, deviceData));
            return;
        }
        //4.2 判断Redis中是否有沉默周期，如果不为空跳出本次循环
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(getAlertSilentKey(alertRule, deviceData)))) {
            return;
        }
        //4.3 查询Redis中已报警次数，加1后比较是否等于报警规则的持续周期，如果不等于跳出本次循环
        if (alertRule.getDuration() == null || alertRule.getDuration() <= 0) {
            log.warn("报警规则【{}】的持续周期未配置", alertRule.getAlertRuleName());
            return;
        }
        String alertCountKey = getAlertCountKey(alertRule, deviceData);
        Long alertCount = stringRedisTemplate.opsForValue().increment(alertCountKey);
        if (alertCount == null || !alertCount.equals(alertRule.getDuration().longValue())) {
            return;
        }
        //4.4 达到持续周期，删除Redis记录的报警次数、添加沉默周期，并把这次上报的数据存储到报警数据表中
        stringRedisTemplate.delete(alertCountKey);
        if (alertRule.getAlertSilentPeriod() != null) {
            stringRedisTemplate.opsForValue().set(getAlertSilentKey(alertRule, deviceData), "1",
                    alertRule.getAlertSilentPeriod(), TimeUnit.MINUTES);
        }
        saveAlertData(deviceData, alertRule);
    }

    /**
     * 判断设备上报的数据是否超过1分钟
     *
     * @param alarmTime 设备上报时间（北京时间）
     * @return true：数据超过1分钟，跳过本次循环；false：数据在1分钟之内，需要处理
     */
    private boolean isDataExpired(LocalDateTime alarmTime) {
        if (alarmTime == null) {
            return true;
        }
        //上报时间在1分钟之前则视为过期数据
        return alarmTime.isBefore(LocalDateTime.now().minusMinutes(1));
    }

    /**
     * 查询设备上报数据对应的报警规则
     *
     * 需要查询该产品物模型下所有设备的规则和该产品物模型下对应设备的规则，两个查询的结果合并到一起
     *
     * @param deviceData 设备上报数据
     * @return 匹配的报警规则集合
     */
    private List<AlertRule> queryRulesByDeviceData(DeviceData deviceData) {
        //1.查询该产品物模型下所有设备的规则（iot_id为空或-1表示过滤该产品下的所有设备）
        List<AlertRule> productRules = alertRuleService.list(new LambdaQueryWrapper<AlertRule>()
                .eq(AlertRule::getProductKey, deviceData.getProductKey())
                .eq(AlertRule::getFunctionId, deviceData.getFunctionId())
                .eq(AlertRule::getStatus, 1)
                .and(w -> w.isNull(AlertRule::getIotId)
                        .or().eq(AlertRule::getIotId, "")
                        .or().eq(AlertRule::getIotId, "-1")));

        //2.查询该产品物模型下对应设备的规则
        List<AlertRule> deviceRules = alertRuleService.list(new LambdaQueryWrapper<AlertRule>()
                .eq(AlertRule::getProductKey, deviceData.getProductKey())
                .eq(AlertRule::getFunctionId, deviceData.getFunctionId())
                .eq(AlertRule::getIotId, deviceData.getIotId())
                .eq(AlertRule::getStatus, 1));

        //3.合并两个查询结果
        List<AlertRule> rules = new ArrayList<>();
        rules.addAll(productRules);
        rules.addAll(deviceRules);
        return rules;
    }

    /**
     * 判断设备上报的数据值是否到达报警规则的阈值
     *
     * @param dataValue 设备上报的数据值
     * @param alertRule 报警规则
     * @return true：到达阈值；false：未到达阈值
     */
    private boolean compareRuleValue(String dataValue, AlertRule alertRule) {
        if (StringUtils.isEmpty(dataValue) || alertRule.getValue() == null || StringUtils.isEmpty(alertRule.getOperator())) {
            return false;
        }
        try {
            double data = Double.parseDouble(dataValue);
            double threshold = alertRule.getValue();
            switch (alertRule.getOperator()) {
                case ">=":
                    return data >= threshold;
                case "<":
                    return data < threshold;
                case ">":
                    return data > threshold;
                case "<=":
                    return data <= threshold;
                case "==":
                    return data == threshold;
                case "!=":
                    return data != threshold;
                default:
                    //不支持的运算符不触发报警
                    log.warn("报警规则【{}】的运算符【{}】不支持", alertRule.getAlertRuleName(), alertRule.getOperator());
                    return false;
            }
        } catch (NumberFormatException e) {
            //数据值无法解析为数值时不触发报警
            log.warn("设备上报数据值【{}】无法解析为数值", dataValue);
            return false;
        }
    }

    /**
     * 判断当前时间是否在报警生效时段内
     *
     * @param alertEffectivePeriod 报警生效时段，格式：00:00:00~23:59:59
     * @return true：在生效时段内；false：不在生效时段内
     */
    private boolean isInEffectivePeriod(String alertEffectivePeriod) {
        if (StringUtils.isEmpty(alertEffectivePeriod)) {
            //未配置生效时段，默认全天生效
            return true;
        }
        try {
            String[] period = alertEffectivePeriod.split("~");
            if (period.length != 2) {
                return true;
            }
            LocalTime startTime = LocalTime.parse(period[0].trim());
            LocalTime endTime = LocalTime.parse(period[1].trim());
            LocalTime now = LocalTime.now();
            return !now.isBefore(startTime) && !now.isAfter(endTime);
        } catch (Exception e) {
            //生效时段解析失败时默认生效，避免影响正常报警
            log.error("报警生效时段【{}】解析失败", alertEffectivePeriod, e);
            return true;
        }
    }

    /**
     * 保存报警数据
     *
     * @param deviceData 设备上报数据
     * @param alertRule 报警规则
     */
    private void saveAlertData(DeviceData deviceData, AlertRule alertRule) {
        AlertData alertData = new AlertData();
        alertData.setIotId(deviceData.getIotId());
        alertData.setDeviceName(deviceData.getDeviceName());
        alertData.setProductKey(deviceData.getProductKey());
        alertData.setProductName(deviceData.getProductName());
        alertData.setFunctionId(deviceData.getFunctionId());
        alertData.setAccessLocation(deviceData.getAccessLocation());
        alertData.setLocationType(deviceData.getLocationType());
        alertData.setPhysicalLocationType(deviceData.getPhysicalLocationType());
        alertData.setDeviceDescription(deviceData.getDeviceDescription());
        alertData.setDataValue(deviceData.getDataValue());
        alertData.setAlertRuleId(alertRule.getId());
        //报警原因格式：功能名称+运算符+阈值+持续周期
        alertData.setAlertReason(alertRule.getFunctionName() + alertRule.getOperator() + alertRule.getValue()
                + ",持续" + alertRule.getDuration() + "个周期");
        alertData.setType(alertRule.getAlertDataType());
        //状态：0待处理
        alertData.setStatus(0);
        alertDataService.save(alertData);
        log.info("设备【{}】功能【{}】上报数据【{}】触发报警规则【{}】，报警数据已保存",
                deviceData.getIotId(), deviceData.getFunctionId(), deviceData.getDataValue(), alertRule.getAlertRuleName());
    }

    /**
     * 获取Redis中报警次数的key
     *
     * @param alertRule 报警规则
     * @param deviceData 设备上报数据
     * @return 报警次数key
     */
    private String getAlertCountKey(AlertRule alertRule, DeviceData deviceData) {
        return ALERT_COUNT_PREFIX + alertRule.getId() + ":" + deviceData.getIotId();
    }

    /**
     * 获取Redis中报警沉默周期的key
     *
     * @param alertRule 报警规则
     * @param deviceData 设备上报数据
     * @return 报警沉默周期key
     */
    private String getAlertSilentKey(AlertRule alertRule, DeviceData deviceData) {
        return ALERT_SILENT_PREFIX + alertRule.getId() + ":" + deviceData.getIotId();
    }
}
