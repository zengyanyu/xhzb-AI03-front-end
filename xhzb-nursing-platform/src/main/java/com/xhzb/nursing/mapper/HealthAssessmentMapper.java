package com.xhzb.nursing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.HealthAssessment;

/**
 * 健康评估记录Mapper接口
 *
 * @author ruoyi
 * @date 2026-07-10
 */
@Mapper
public interface HealthAssessmentMapper extends BaseMapper<HealthAssessment> {
    /**
     * 查询健康评估记录
     *
     * @param id 健康评估记录主键
     * @return 健康评估记录
     */
    HealthAssessment selectHealthAssessmentById(Long id);

    /**
     * 查询健康评估记录列表
     *
     * @param healthAssessment 健康评估记录
     * @return 健康评估记录集合
     */
    List<HealthAssessment> selectHealthAssessmentList(HealthAssessment healthAssessment);

    /**
     * 新增健康评估记录
     *
     * @param healthAssessment 健康评估记录
     * @return 结果
     */
    int insertHealthAssessment(HealthAssessment healthAssessment);

    /**
     * 修改健康评估记录
     *
     * @param healthAssessment 健康评估记录
     * @return 结果
     */
    int updateHealthAssessment(HealthAssessment healthAssessment);

    /**
     * 删除健康评估记录
     *
     * @param id 健康评估记录主键
     * @return 结果
     */
    int deleteHealthAssessmentById(Long id);

    /**
     * 批量删除健康评估记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteHealthAssessmentByIds(Long[] ids);
}
