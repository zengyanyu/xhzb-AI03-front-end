package com.xhzb.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.NursingPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 护理计划Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface NursingPlanMapper extends BaseMapper<NursingPlan> {
    /**
     * 查询护理计划
     *
     * @param id 护理计划主键
     * @return 护理计划
     */
    NursingPlan selectNursingPlanById(Long id);

    /**
     * 查询护理计划列表
     *
     * @param nursingPlan 护理计划
     * @return 护理计划集合
     */
    List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan);

    /**
     * 新增护理计划
     *
     * @param nursingPlan 护理计划
     * @return 结果
     */
    int insertNursingPlan(NursingPlan nursingPlan);

    /**
     * 修改护理计划
     *
     * @param nursingPlan 护理计划
     * @return 结果
     */
    int updateNursingPlan(NursingPlan nursingPlan);

    /**
     * 删除护理计划
     *
     * @param id 护理计划主键
     * @return 结果
     */
    int deleteNursingPlanById(Long id);

    /**
     * 批量删除护理计划
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteNursingPlanByIds(Long[] ids);

    @Select("select id,plan_name planName from nursing_plan where status = 1")
    List<NursingPlan> listAll();
}
