package com.xhzb.nursing.service.impl;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.nursing.domain.Device;
import com.xhzb.nursing.domain.DeviceData;
import com.xhzb.nursing.mapper.DeviceDataMapper;
import com.xhzb.nursing.service.IDeviceDataService;
import com.xhzb.nursing.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备数据Service业务层处理
 *
 * @author ruoyi
 * @date 2026-08-15
 */
@Slf4j
@Service
public class DeviceDataServiceImpl extends ServiceImpl<DeviceDataMapper, DeviceData> implements IDeviceDataService
{
    @Autowired
    private DeviceDataMapper deviceDataMapper;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /** Redis Hash大key，存储设备最新上报数据 */
    private static final String IOT_DEVICE_LATEST_DATA_KEY = "iot:device_latest_data";

    /**
     * 查询设备数据
     *
     * @param id 设备数据主键
     * @return 设备数据
     */
    @Override
    public DeviceData selectDeviceDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询设备数据列表
     *
     * @param deviceData 设备数据
     * @return 设备数据集合
     */
    @Override
    public List<DeviceData> selectDeviceDataList(DeviceData deviceData)
    {
        return deviceDataMapper.selectDeviceDataList(deviceData);
    }

    /**
     * 新增设备数据
     *
     * @param deviceData 设备数据
     * @return 结果
     */
    @Override
    public int insertDeviceData(DeviceData deviceData)
    {
        return save(deviceData)? 1 : 0;
    }

    /**
     * 修改设备数据
     *
     * @param deviceData 设备数据
     * @return 结果
     */
    @Override
    public int updateDeviceData(DeviceData deviceData)
    {
        return updateById(deviceData)? 1 : 0;
    }

    /**
     * 批量删除设备数据
     *
     * @param ids 需要删除的设备数据主键集合
     * @return 结果
     */
    @Override
    public int deleteDeviceDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除设备数据信息
     *
     * @param id 设备数据主键
     * @return 结果
     */
    @Override
    public int deleteDeviceDataById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    /**
     * 处理设备上报的数据消息
     *
     * 步骤：1.解析消息内容，获取设备ID、上报属性和上报时间
     *      2.根据设备ID查询设备，设备不存在则直接返回
     *      3.将上报时间由世界时间(UTC)转为北京时间
     *      4.根据properties数据批量保存设备数据到数据库
     *
     * @param contentStr AMQP上报的消息内容
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleDeviceData(String contentStr) {
        //1.解析消息内容，获取设备ID、服务数据
        JSONObject jsonObject = JSONUtil.parseObj(contentStr);
        JSONObject notifyData = jsonObject.getJSONObject("notify_data");
        if (notifyData == null) {
            return;
        }
        JSONObject header = notifyData.getJSONObject("header");
        if (header == null) {
            return;
        }
        //1.1 获取设备ID
        String deviceId = header.getStr("device_id");
        if (StringUtils.isEmpty(deviceId)) {
            return;
        }
        //1.2 获取顶层上报时间，作为service无上报时间时的兜底
        String topEventTime = jsonObject.getStr("event_time");

        //2.根据设备ID查询设备，设备不存在则直接返回
        Device device = deviceService.getOne(new LambdaQueryWrapper<Device>().eq(Device::getIotId, deviceId));
        if (device == null) {
            log.info("设备【{}】不存在，忽略本次上报数据", deviceId);
            return;
        }

        //3.遍历每个服务上报的属性数据，组装设备数据列表
        List<DeviceData> deviceDataList = new ArrayList<>();
        JSONObject body = notifyData.getJSONObject("body");
        if (body != null) {
            JSONArray services = body.getJSONArray("services");
            if (services != null && !services.isEmpty()) {
                for (int i = 0; i < services.size(); i++) {
                    JSONObject service = services.getJSONObject(i);
                    JSONObject properties = service.getJSONObject("properties");
                    if (properties == null || properties.isEmpty()) {
                        continue;
                    }
                    //4.上报时间由世界时间(UTC)转为北京时间
                    String serviceEventTime = service.getStr("event_time");
                    LocalDateTime alarmTime = convertToBeijingTime(
                            StringUtils.isNotEmpty(serviceEventTime) ? serviceEventTime : topEventTime);

                    //5.遍历服务下的每个上报属性，一个属性生成一条设备数据
                    for (Map.Entry<String, Object> entry : properties.entrySet()) {
                        DeviceData deviceData = new DeviceData();
                        deviceData.setIotId(deviceId);
                        deviceData.setDeviceName(device.getDeviceName());
                        deviceData.setProductKey(device.getProductKey());
                        deviceData.setProductName(device.getProductName());
                        deviceData.setFunctionId(entry.getKey());
                        deviceData.setAccessLocation(device.getBindingLocation());
                        deviceData.setLocationType(device.getLocationType());
                        deviceData.setPhysicalLocationType(device.getPhysicalLocationType());
                        deviceData.setDeviceDescription(device.getDeviceDescription());
                        deviceData.setDataValue(String.valueOf(entry.getValue()));
                        deviceData.setAlarmTime(alarmTime);
                        deviceDataList.add(deviceData);
                    }
                }
            }
        }

        //6.批量保存设备数据
        if (!deviceDataList.isEmpty()) {
            saveBatch(deviceDataList);
            log.info("设备【{}】上报数据保存成功，共{}条", deviceId, deviceDataList.size());

            //7.存入Redis Hash，大key固定，小key为设备id，每次上报覆盖最新数据
            stringRedisTemplate.opsForHash().put(IOT_DEVICE_LATEST_DATA_KEY, deviceId, JSONUtil.toJsonStr(deviceDataList));
            log.info("设备【{}】最新数据已更新到Redis", deviceId);
        }
    }

    /**
     * 将华为云返回的上报时间（世界时间UTC）转为北京时间并格式化为LocalDateTime
     * 兼容两种格式：yyyyMMdd'T'HHmmss'Z'（如 20260606T111615Z）、ISO8601（如 2026-06-06T11:16:15.779Z）
     *
     * @param eventTime 华为云返回的世界时间字符串
     * @return 北京时间LocalDateTime
     */
    private LocalDateTime convertToBeijingTime(String eventTime) {
        if (StringUtils.isEmpty(eventTime)) {
            return null;
        }
        try {
            //1.将世界时间(UTC)解析为Date
            Date utcDate;
            if (eventTime.contains("-")) {
                //ISO8601格式，直接按Instant解析
                utcDate = Date.from(Instant.parse(eventTime));
            } else {
                SimpleDateFormat utcFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                utcDate = utcFormat.parse(eventTime);
            }

            //2.转换为北京时间并格式化为LocalDateTime
            SimpleDateFormat beijingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            beijingFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return LocalDateTime.parse(beijingFormat.format(utcDate),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            //时间格式解析失败时记录日志并返回null
            log.error("上报时间转换失败，原始时间：{}", eventTime, e);
            return null;
        }
    }
}
