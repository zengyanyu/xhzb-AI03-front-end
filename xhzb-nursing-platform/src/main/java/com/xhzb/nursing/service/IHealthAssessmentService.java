package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.HealthAssessment;
import com.xhzb.nursing.domain.HealthAssessmentDataCollection;
import com.xhzb.nursing.domain.HealthAssessmentReport;
import com.xhzb.nursing.domain.dto.health.ElderAssessmentDto;

import java.util.List;

/**
 * 健康评估记录Service接口
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
public interface IHealthAssessmentService extends IService<HealthAssessment>
{
    /**
     * 查询健康评估记录
     * 
     * @param id 健康评估记录主键
     * @return 健康评估记录
     */
    public HealthAssessmentDataCollection selectHealthAssessmentById(Long id);

    /**
     * 查询健康评估记录列表
     * 
     * @param healthAssessment 健康评估记录
     * @return 健康评估记录集合
     */
    public List<HealthAssessment> selectHealthAssessmentList(HealthAssessment healthAssessment);

    /**
     * 新增健康评估记录
     * 
     * @param dto 健康评估记录
     * @return 结果
     */
    public Long insertHealthAssessment(ElderAssessmentDto dto);

    /**
     * 修改健康评估记录
     * 
     * @param dto 健康评估记录
     * @return 结果
     */
    public Long updateHealthAssessment(ElderAssessmentDto dto);

    /**
     * 批量删除健康评估记录
     * 
     * @param ids 需要删除的健康评估记录主键集合
     * @return 结果
     */
    public int deleteHealthAssessmentByIds(Long[] ids);

    /**
     * 删除健康评估记录信息
     *
     * @param id 健康评估记录主键
     * @return 结果
     */
    public int deleteHealthAssessmentById(Long id);

    /**
     * AI评估分析：保存评估数据并进行两阶段AI分析
     *
     * @param dto 评估数据
     * @return 评估ID
     */
    public Long assessmentData(ElderAssessmentDto dto);

    /**
     * 根据评估ID查询评估报告
     *
     * @param healthAssessmentId 健康评估ID
     * @return 评估报告
     */
    public HealthAssessmentReport selectReportByAssessmentId(Long healthAssessmentId);
}
