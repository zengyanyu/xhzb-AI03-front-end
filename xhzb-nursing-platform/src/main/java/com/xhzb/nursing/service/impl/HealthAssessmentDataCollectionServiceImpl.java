package com.xhzb.nursing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.nursing.domain.HealthAssessmentDataCollection;
import com.xhzb.nursing.mapper.HealthAssessmentDataCollectionMapper;
import com.xhzb.nursing.service.IHealthAssessmentDataCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 健康评估数据采集Service业务层处理
 *
 * @author ruoyi
 * @date 2026-07-10
 */
@Service
public class HealthAssessmentDataCollectionServiceImpl extends ServiceImpl<HealthAssessmentDataCollectionMapper, HealthAssessmentDataCollection> implements IHealthAssessmentDataCollectionService {
    @Autowired
    private HealthAssessmentDataCollectionMapper healthAssessmentDataCollectionMapper;

    /**
     * 查询健康评估数据采集
     *
     * @param id 健康评估数据采集主键
     * @return 健康评估数据采集
     */
    @Override
    public HealthAssessmentDataCollection selectHealthAssessmentDataCollectionById(Long id) {
        return getById(id);
    }

    /**
     * 查询健康评估数据采集列表
     *
     * @param healthAssessmentDataCollection 健康评估数据采集
     * @return 健康评估数据采集
     */
    @Override
    public List<HealthAssessmentDataCollection> selectHealthAssessmentDataCollectionList(HealthAssessmentDataCollection healthAssessmentDataCollection) {
        return healthAssessmentDataCollectionMapper.selectHealthAssessmentDataCollectionList(healthAssessmentDataCollection);
    }

    /**
     * 新增健康评估数据采集
     *
     * @param healthAssessmentDataCollection 健康评估数据采集
     * @return 结果
     */
    @Override
    public int insertHealthAssessmentDataCollection(HealthAssessmentDataCollection healthAssessmentDataCollection) {
        return save(healthAssessmentDataCollection) ? 1 : 0;
    }

    /**
     * 修改健康评估数据采集
     *
     * @param healthAssessmentDataCollection 健康评估数据采集
     * @return 结果
     */
    @Override
    public int updateHealthAssessmentDataCollection(HealthAssessmentDataCollection healthAssessmentDataCollection) {
        return updateById(healthAssessmentDataCollection) ? 1 : 0;
    }

    /**
     * 批量删除健康评估数据采集
     *
     * @param ids 需要删除的健康评估数据采集主键
     * @return 结果
     */
    @Override
    public int deleteHealthAssessmentDataCollectionByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除健康评估数据采集信息
     *
     * @param id 健康评估数据采集主键
     * @return 结果
     */
    @Override
    public int deleteHealthAssessmentDataCollectionById(Long id) {
        return removeById(id) ? 1 : 0;
    }
}
