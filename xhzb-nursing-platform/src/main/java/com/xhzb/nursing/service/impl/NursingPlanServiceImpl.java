package com.xhzb.nursing.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.common.utils.DateUtils;
import com.xhzb.nursing.domain.NursingLevel;
import com.xhzb.nursing.domain.NursingProjectPlan;
import com.xhzb.nursing.domain.dto.NursingPlanDto;
import com.xhzb.nursing.mapper.NursingProjectPlanMapper;
import com.xhzb.nursing.domain.vo.NursingPlanVo;
import com.xhzb.nursing.domain.vo.NursingProjectPlanVo;
import com.xhzb.nursing.service.INursingLevelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xhzb.nursing.mapper.NursingPlanMapper;
import com.xhzb.nursing.domain.NursingPlan;
import com.xhzb.nursing.service.INursingPlanService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 护理计划Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Transactional
@Service
public class NursingPlanServiceImpl extends ServiceImpl<NursingPlanMapper, NursingPlan> implements INursingPlanService {
    @Autowired
    private NursingPlanMapper nursingPlanMapper;

    /**
     * 查询护理计划
     *
     * @param id 护理计划主键
     * @return 护理计划
     */
    @Override
    public NursingPlanVo selectNursingPlanById(Long id) {
        //查询护理计划
        NursingPlan nursingPlan = nursingPlanMapper.selectNursingPlanById(id);
        NursingPlanVo nursingPlanVo = new NursingPlanVo();
        BeanUtils.copyProperties(nursingPlan, nursingPlanVo);

        //根据护理计划的id查询对应的护理项目列表
        List<NursingProjectPlanVo> list = nursingProjectPlanMapper.selectByPlanId(id);
        nursingPlanVo.setProjectPlans(list);

        return nursingPlanVo;
    }

    /**
     * 查询护理计划列表
     *
     * @param nursingPlan 护理计划
     * @return 护理计划
     */
    @Override
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan) {
        return nursingPlanMapper.selectNursingPlanList(nursingPlan);
    }

    @Autowired
    private NursingProjectPlanMapper nursingProjectPlanMapper;

    /**
     * 新增护理计划
     *
     * @param dto 护理计划
     * @return 结果
     */

    @Override
    public int insertNursingPlan(NursingPlanDto dto) {
        // 保存护理计划
        NursingPlan nursingPlan = new NursingPlan();
        BeanUtils.copyProperties(dto, nursingPlan);
        nursingPlan.setCreateTime(DateUtils.getNowDate());
        int count = nursingPlanMapper.insertNursingPlan(nursingPlan);

        //保存护理项目计划中间关系数据
        if (dto.getProjectPlans() != null && !dto.getProjectPlans().isEmpty()) {
            dto.getProjectPlans().forEach(projectPlan -> {
                projectPlan.setPlanId(nursingPlan.getId());
                projectPlan.setCreateTime(DateUtils.getNowDate());
            });
            //批量保存
            return nursingProjectPlanMapper.batchInsert(dto.getProjectPlans());
        }
        return count;
    }


    @Autowired
    private INursingLevelService nursingLevelService;


    /**
     * 修改护理计划
     *
     * @param dto 护理计划
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateNursingPlan(NursingPlanDto dto) {

        long count = nursingLevelService.count(Wrappers.<NursingLevel>lambdaQuery().eq(NursingLevel::getLplanId, dto.getId()));
        if (count > 0) {
            throw new RuntimeException("该护理计划被引用，不能修改");
        }

        // 属性拷贝
        NursingPlan nursingPlan = new NursingPlan();
        BeanUtils.copyProperties(dto, nursingPlan);

        //判断dto的list（护理计划与护理项目的关联关系）是否为空，不为空，删除之前的关联关系，再重新批量添加
        if (dto.getProjectPlans() != null && !dto.getProjectPlans().isEmpty()) {
            //删除之前的关联关系  根据护理计划ID删除
            nursingProjectPlanMapper.deleteByPlanId(nursingPlan.getId());
            //批量添加
            dto.getProjectPlans().forEach(projectPlan -> {
                projectPlan.setPlanId(nursingPlan.getId());
                projectPlan.setCreateTime(DateUtils.getNowDate());
            });
            //批量保存
            return nursingProjectPlanMapper.batchInsert(dto.getProjectPlans());
        }

        //不管dto的list空不空，都要修改护理计划
        return nursingPlanMapper.updateNursingPlan(nursingPlan);

    }

    /**
     * 删除护理计划信息
     *
     * @param id 护理计划主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteNursingPlanById(Long id) {
        long count = nursingLevelService.count(Wrappers.<NursingLevel>lambdaQuery().eq(NursingLevel::getLplanId, id));
        if (count > 0) {
            throw new RuntimeException("该护理计划被引用，不能删除");
        }
        //删除之前的关联关系  根据护理计划ID删除
        nursingProjectPlanMapper.deleteByPlanId(id);
        return nursingPlanMapper.deleteNursingPlanById(id);
    }

    /**
     * 查询所有护理计划
     *
     * @return
     */
    @Override
    public List<NursingPlan> listAll() {
        return nursingPlanMapper.listAll();
    }
}
