package com.xhzb.nursing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.AlertRule;

/**
 * 报警规则Mapper接口
 * 
 * @author ruoyi
 * @date 2026-08-16
 */
@Mapper
public interface AlertRuleMapper extends BaseMapper<AlertRule>
{
    /**
     * 查询报警规则
     * 
     * @param id 报警规则主键
     * @return 报警规则
     */
    public AlertRule selectAlertRuleById(Long id);

    /**
     * 查询报警规则列表
     * 
     * @param alertRule 报警规则
     * @return 报警规则集合
     */
    public List<AlertRule> selectAlertRuleList(AlertRule alertRule);

    /**
     * 新增报警规则
     * 
     * @param alertRule 报警规则
     * @return 结果
     */
    public int insertAlertRule(AlertRule alertRule);

    /**
     * 修改报警规则
     * 
     * @param alertRule 报警规则
     * @return 结果
     */
    public int updateAlertRule(AlertRule alertRule);

    /**
     * 删除报警规则
     * 
     * @param id 报警规则主键
     * @return 结果
     */
    public int deleteAlertRuleById(Long id);

    /**
     * 批量删除报警规则
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlertRuleByIds(Long[] ids);
}
