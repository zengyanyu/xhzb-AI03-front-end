package com.xhzb.nursing.service;

import java.util.List;
import com.xhzb.nursing.domain.HealthAssessmentDataCollection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 健康评估数据采集Service接口
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
public interface IHealthAssessmentDataCollectionService extends IService<HealthAssessmentDataCollection>
{
    /**
     * 查询健康评估数据采集
     * 
     * @param id 健康评估数据采集主键
     * @return 健康评估数据采集
     */
    public HealthAssessmentDataCollection selectHealthAssessmentDataCollectionById(Long id);

    /**
     * 查询健康评估数据采集列表
     * 
     * @param healthAssessmentDataCollection 健康评估数据采集
     * @return 健康评估数据采集集合
     */
    public List<HealthAssessmentDataCollection> selectHealthAssessmentDataCollectionList(HealthAssessmentDataCollection healthAssessmentDataCollection);

    /**
     * 新增健康评估数据采集
     * 
     * @param healthAssessmentDataCollection 健康评估数据采集
     * @return 结果
     */
    public int insertHealthAssessmentDataCollection(HealthAssessmentDataCollection healthAssessmentDataCollection);

    /**
     * 修改健康评估数据采集
     * 
     * @param healthAssessmentDataCollection 健康评估数据采集
     * @return 结果
     */
    public int updateHealthAssessmentDataCollection(HealthAssessmentDataCollection healthAssessmentDataCollection);

    /**
     * 批量删除健康评估数据采集
     * 
     * @param ids 需要删除的健康评估数据采集主键集合
     * @return 结果
     */
    public int deleteHealthAssessmentDataCollectionByIds(Long[] ids);

    /**
     * 删除健康评估数据采集信息
     * 
     * @param id 健康评估数据采集主键
     * @return 结果
     */
    public int deleteHealthAssessmentDataCollectionById(Long id);
}
