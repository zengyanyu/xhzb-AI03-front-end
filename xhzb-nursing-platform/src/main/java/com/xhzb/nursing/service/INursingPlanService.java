package com.xhzb.nursing.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.NursingPlan;
import com.xhzb.nursing.domain.dto.NursingPlanDto;
import com.xhzb.nursing.domain.vo.NursingPlanVo;

/**
 * 护理计划Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface INursingPlanService  extends IService<NursingPlan>
{
    /**
     * 查询护理计划
     * 
     * @param id 护理计划主键
     * @return 护理计划
     */
    public NursingPlanVo selectNursingPlanById(Long id);

    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划集合
     */
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan);

    /**
     * 新增护理计划
     * 
     * @param dto 护理计划
     * @return 结果
     */
    public int insertNursingPlan(NursingPlanDto dto);

    /**
     * 修改护理计划
     * 
     * @param dto 护理计划
     * @return 结果
     */
    public int updateNursingPlan(NursingPlanDto dto);

    /**
     * 删除护理计划信息
     * 
     * @param id 护理计划主键
     * @return 结果
     */
    public int deleteNursingPlanById(Long id);

    /**
     * 查询所有护理计划
     * @return
     */
    List<NursingPlan> listAll();
}
