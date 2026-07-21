package com.xhzb.nursing.service;

import java.util.List;
import com.xhzb.nursing.domain.HealthAssessmentReport;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 健康评估报告Service接口
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
public interface IHealthAssessmentReportService extends IService<HealthAssessmentReport>
{
    /**
     * 查询健康评估报告
     * 
     * @param id 健康评估报告主键
     * @return 健康评估报告
     */
    public HealthAssessmentReport selectHealthAssessmentReportById(String id);

    /**
     * 查询健康评估报告列表
     * 
     * @param healthAssessmentReport 健康评估报告
     * @return 健康评估报告集合
     */
    public List<HealthAssessmentReport> selectHealthAssessmentReportList(HealthAssessmentReport healthAssessmentReport);

    /**
     * 新增健康评估报告
     * 
     * @param healthAssessmentReport 健康评估报告
     * @return 结果
     */
    public int insertHealthAssessmentReport(HealthAssessmentReport healthAssessmentReport);

    /**
     * 修改健康评估报告
     * 
     * @param healthAssessmentReport 健康评估报告
     * @return 结果
     */
    public int updateHealthAssessmentReport(HealthAssessmentReport healthAssessmentReport);

    /**
     * 批量删除健康评估报告
     * 
     * @param ids 需要删除的健康评估报告主键集合
     * @return 结果
     */
    public int deleteHealthAssessmentReportByIds(String[] ids);

    /**
     * 删除健康评估报告信息
     * 
     * @param id 健康评估报告主键
     * @return 结果
     */
    public int deleteHealthAssessmentReportById(String id);
}
