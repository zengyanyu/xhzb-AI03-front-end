package com.xhzb.nursing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.HealthAssessmentDataCollection;

/**
 * 健康评估数据采集Mapper接口
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Mapper
public interface HealthAssessmentDataCollectionMapper extends BaseMapper<HealthAssessmentDataCollection>
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
     * 删除健康评估数据采集
     * 
     * @param id 健康评估数据采集主键
     * @return 结果
     */
    public int deleteHealthAssessmentDataCollectionById(Long id);

    /**
     * 批量删除健康评估数据采集
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHealthAssessmentDataCollectionByIds(Long[] ids);
}
