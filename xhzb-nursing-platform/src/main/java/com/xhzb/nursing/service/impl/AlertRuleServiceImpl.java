package com.xhzb.nursing.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.common.core.domain.entity.SysRole;
import com.xhzb.common.core.domain.entity.SysUser;
import com.xhzb.common.core.domain.model.LoginUser;
import com.xhzb.common.utils.SecurityUtils;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.nursing.domain.AlertData;
import com.xhzb.nursing.domain.AlertRule;
import com.xhzb.nursing.domain.DeviceData;
import com.xhzb.nursing.domain.Elder;
import com.xhzb.nursing.mapper.AlertRuleMapper;
import com.xhzb.nursing.service.IAlertDataService;
import com.xhzb.nursing.service.IAlertRuleService;
import com.xhzb.nursing.service.IElderService;
import com.xhzb.nursing.service.INursingElderService;
import com.xhzb.system.service.ISysRoleService;
import com.xhzb.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报警规则Service业务层处理
 *
 * @author ruoyi
 * @date 2026-08-16
 */
@Slf4j
@Service
public class AlertRuleServiceImpl extends ServiceImpl<AlertRuleMapper, AlertRule> implements IAlertRuleService {
    /**
     * 角色名称：行政（设备异常数据通知人员）
     */
    private static final String ROLE_NAME_ADMIN = "行政";

    /**
     * 角色名称：超级管理员（所有报警场景均通知人员）
     */
    private static final String ROLE_NAME_SUPER_ADMIN = "超级管理员";

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private IAlertDataService alertDataService;

    @Autowired
    private IElderService elderService;

    @Autowired
    private INursingElderService nursingElderService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询报警规则
     *
     * @param id 报警规则主键
     * @return 报警规则
     */
    @Override
    public AlertRule selectAlertRuleById(Long id) {
        return getById(id);
    }

    /**
     * 查询报警规则列表
     *
     * @param alertRule 报警规则
     * @return 报警规则
     */
    @Override
    public List<AlertRule> selectAlertRuleList(AlertRule alertRule) {
        return alertRuleMapper.selectAlertRuleList(alertRule);
    }

    /**
     * 新增报警规则
     *
     * @param alertRule 报警规则
     * @return 结果
     */
    @Override
    public int insertAlertRule(AlertRule alertRule) {
        return save(alertRule) ? 1 : 0;
    }

    /**
     * 修改报警规则
     *
     * @param alertRule 报警规则
     * @return 结果
     */
    @Override
    public int updateAlertRule(AlertRule alertRule) {
        return updateById(alertRule) ? 1 : 0;
    }

    /**
     * 批量删除报警规则
     *
     * @param ids 需要删除的报警规则主键
     * @return 结果
     */
    @Override
    public int deleteAlertRuleByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除报警规则信息
     *
     * @param id 报警规则主键
     * @return 结果
     */
    @Override
    public int deleteAlertRuleById(Long id) {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 保存报警数据（批量保存报警通知数据）
     * <p>
     * 步骤：1.根据报警规则筛选出需要通知的全部相关人员ID
     * 2.批量插入报警通知数据，每条数据的userId赋值为对应人员ID
     *
     * @param deviceData 设备上报数据
     * @param alertRule  报警规则
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAlertData(DeviceData deviceData, AlertRule alertRule) {
        //一、筛选出需要通知的全部相关人员ID
        List<Long> userIds = queryNotifyUserIds(deviceData, alertRule);
        if (CollUtil.isEmpty(userIds)) {
            log.warn("报警规则【{}】未查询到需要通知的人员，跳过报警数据保存", alertRule.getAlertRuleName());
            return;
        }

        //二、批量插入报警通知数据，每条数据的userId赋值为对应人员ID
        List<AlertData> alertDataList = new ArrayList<>();
        for (Long userId : userIds) {
            AlertData alertData = buildAlertData(deviceData, alertRule);
            alertData.setUserId(userId);
            alertDataList.add(alertData);
        }
        alertDataService.saveBatch(alertDataList);
        log.info("设备【{}】功能【{}】上报数据【{}】触发报警规则【{}】，批量保存{}条报警通知数据",
                deviceData.getIotId(), deviceData.getFunctionId(), deviceData.getDataValue(),
                alertRule.getAlertRuleName(), alertDataList.size());
    }

    /**
     * 查询需要通知的全部相关人员ID
     * <p>
     * 步骤：1.根据报警规则中的alertDataType判定当前报警设备是否关联老人
     * 0：老人异常数据（设备关联老人），1：设备异常数据（设备不关联老人）
     * 2.设备不关联老人：根据角色名称「行政」查询人员ID
     * 3.设备关联老人：根据locationType区分固定设备和随身设备
     * 固定设备physicalLocationType=2时accessLocation为床位id，通过床位--老人--护理人员关系找到护理人员ID
     * 随身设备accessLocation为老人id，直接通过老人和护理人员的关系找到护理人员ID
     * 4.所有场景下，根据角色名称「超级管理员」查询人员ID
     * 5.整合全部人员ID并去重
     *
     * @param deviceData 设备上报数据
     * @param alertRule  报警规则
     * @return 需要通知的人员ID集合
     */
    private List<Long> queryNotifyUserIds(DeviceData deviceData, AlertRule alertRule) {
        try {
            List<Long> userIds = new ArrayList<>();

            //1.根据报警规则中的alertDataType判定当前报警设备是否关联老人
            boolean isElderAlert = alertRule.getAlertDataType() != null && alertRule.getAlertDataType() == 0;
            if (isElderAlert) {
                //设备关联老人：根据位置类型区分固定设备和随身设备
                Integer locationType = deviceData.getLocationType();
                if (locationType != null && locationType == 0) {
                    //3.1 随身设备：accessLocation中的值就是老人id，直接老人和护理人员的关系找到对应护理人员ID
                    Long elderId = Convert.toLong(deviceData.getAccessLocation());
                    if (elderId != null) {
                        userIds.addAll(nursingElderService.selectNursingIdsByElderId(elderId));
                    }
                } else if (deviceData.getPhysicalLocationType() != null && deviceData.getPhysicalLocationType() == 2) {
                    //3.2 固定设备且物理位置类型为床位（0楼层 1房间 2床位）：accessLocation中的值就是床位id
                    Long bedId = Convert.toLong(deviceData.getAccessLocation());
                    if (bedId != null) {
                        //3.3 床位--老人
                        Elder elder = elderService.getOne(new LambdaQueryWrapper<Elder>().eq(Elder::getBedId, bedId).last("limit 1"));
                        if (elder != null) {
                            //3.4 老人--护理人员
                            userIds.addAll(nursingElderService.selectNursingIdsByElderId(elder.getId()));
                        }
                    }
                }
            } else {
                //2.设备不关联老人：根据角色名称「行政」查询人员ID
                userIds.addAll(queryUserIdsByRoleName(ROLE_NAME_ADMIN));
            }

            //4.所有场景下，根据角色名称「超级管理员」查询人员ID
            userIds.addAll(queryUserIdsByRoleName(ROLE_NAME_SUPER_ADMIN));

            //5.整合全部人员ID并去重
            return userIds.stream().distinct().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据角色名称查询该角色下的全部人员ID
     *
     * @param roleName 角色名称
     * @return 人员ID集合
     */
    private List<Long> queryUserIdsByRoleName(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            return new ArrayList<>();
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(1L);
        loginUser.setDeptId(103L);
        SecurityUtils.setLoginUser(loginUser);//给定时器模拟登录用户，用户为超级管理员

        //1.根据角色名称查询角色（selectRoleList使用模糊匹配，这里再精确过滤一次避免匹配到相似名称的角色）
        SysRole queryRole = new SysRole();
        queryRole.setRoleName(roleName);

        List<SysRole> roles = sysRoleService.selectRoleList(queryRole);
        List<Long> roleIds = roles.stream()
                .filter(role -> roleName.equals(role.getRoleName()))
                .map(SysRole::getRoleId)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }

        //2.根据角色ID查询该角色下的全部人员ID
        List<Long> userIds = new ArrayList<>();
        for (Long roleId : roleIds) {
            SysUser queryUser = new SysUser();
            queryUser.setRoleId(roleId);
            List<SysUser> users = sysUserService.selectAllocatedList(queryUser);
            if (CollUtil.isNotEmpty(users)) {
                userIds.addAll(users.stream().map(SysUser::getUserId).collect(Collectors.toList()));
            }
        }
        SecurityUtils.setLoginUser(null);//处理完将登录用户删掉
        return userIds;
    }

    /**
     * 组装报警数据
     *
     * @param deviceData 设备上报数据
     * @param alertRule  报警规则
     * @return 报警数据（userId未设置，由调用方赋值）
     */
    private AlertData buildAlertData(DeviceData deviceData, AlertRule alertRule) {
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
        return alertData;
    }
}
