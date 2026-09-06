package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.AlertRule;
import com.xhzb.nursing.domain.DeviceData;

import java.util.List;

/**
 * 报警规则Service接口
 *
 * @author ruoyi
 * @date 2026-08-16
 */
public interface IAlertRuleService extends IService<AlertRule> {
    /**
     * 查询报警规则
     *
     * @param id 报警规则主键
     * @return 报警规则
     */
    AlertRule selectAlertRuleById(Long id);

    /**
     * 查询报警规则列表
     *
     * @param alertRule 报警规则
     * @return 报警规则集合
     */
    List<AlertRule> selectAlertRuleList(AlertRule alertRule);

    /**
     * 新增报警规则
     *
     * @param alertRule 报警规则
     * @return 结果
     */
    int insertAlertRule(AlertRule alertRule);

    /**
     * 修改报警规则
     *
     * @param alertRule 报警规则
     * @return 结果
     */
    int updateAlertRule(AlertRule alertRule);

    /**
     * 批量删除报警规则
     *
     * @param ids 需要删除的报警规则主键集合
     * @return 结果
     */
    int deleteAlertRuleByIds(Long[] ids);

    /**
     * 删除报警规则信息
     *
     * @param id 报警规则主键
     * @return 结果
     */
    int deleteAlertRuleById(Long id);

    /**
     * 保存报警数据（批量保存报警通知数据）
     * <p>
     * 根据报警规则筛选出需要通知的全部相关人员，批量插入报警通知数据，每条数据的userId赋值为对应人员ID
     *
     * @param deviceData 设备上报数据
     * @param alertRule  报警规则
     */
    void saveAlertData(DeviceData deviceData, AlertRule alertRule);
}
