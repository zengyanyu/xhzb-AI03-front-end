package com.xhzb.nursing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.nursing.domain.HealthAssessmentReport;
import com.xhzb.nursing.mapper.HealthAssessmentReportMapper;
import com.xhzb.nursing.service.IHealthAssessmentReportService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 健康评估报告Service业务层处理
 *
 * @author ruoyi
 * @date 2026-07-10
 */
@Service
public class HealthAssessmentReportServiceImpl extends ServiceImpl<HealthAssessmentReportMapper, HealthAssessmentReport> implements IHealthAssessmentReportService {

    /**
     * 查询健康评估报告
     *
     * @param id 健康评估报告主键
     * @return 健康评估报告
     */
    @Override
    public HealthAssessmentReport selectHealthAssessmentReportById(String id) {
        return getById(id);
    }

    /**
     * 查询健康评估报告列表
     *
     * @param healthAssessmentReport 健康评估报告
     * @return 健康评估报告
     */
    @Override
    public List<HealthAssessmentReport> selectHealthAssessmentReportList(HealthAssessmentReport healthAssessmentReport) {
        return this.baseMapper.selectHealthAssessmentReportList(healthAssessmentReport);
    }

    /**
     * 新增健康评估报告
     *
     * @param healthAssessmentReport 健康评估报告
     * @return 结果
     */
    @Override
    public int insertHealthAssessmentReport(HealthAssessmentReport healthAssessmentReport) {
        return save(healthAssessmentReport) ? 1 : 0;
    }

    /**
     * 修改健康评估报告
     *
     * @param healthAssessmentReport 健康评估报告
     * @return 结果
     */
    @Override
    public int updateHealthAssessmentReport(HealthAssessmentReport healthAssessmentReport) {
        return updateById(healthAssessmentReport) ? 1 : 0;
    }

    /**
     * 批量删除健康评估报告
     *
     * @param ids 需要删除的健康评估报告主键
     * @return 结果
     */
    @Override
    public int deleteHealthAssessmentReportByIds(String[] ids) {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除健康评估报告信息
     *
     * @param id 健康评估报告主键
     * @return 结果
     */
    @Override
    public int deleteHealthAssessmentReportById(String id) {
        return removeById(id) ? 1 : 0;
    }
}
