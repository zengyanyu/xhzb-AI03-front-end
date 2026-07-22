package com.xhzb.nursing.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.common.exception.base.BaseException;
import com.xhzb.common.utils.DateUtils;
import com.xhzb.nursing.domain.HealthAssessment;
import com.xhzb.nursing.domain.HealthAssessmentDataCollection;
import com.xhzb.nursing.domain.dto.health.ElderAssessmentDto;
import com.xhzb.nursing.mapper.HealthAssessmentMapper;
import com.xhzb.nursing.service.IHealthAssessmentDataCollectionService;
import com.xhzb.nursing.service.IHealthAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 健康评估记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Service
public class HealthAssessmentServiceImpl extends ServiceImpl<HealthAssessmentMapper, HealthAssessment> implements IHealthAssessmentService
{
    @Autowired
    private HealthAssessmentMapper healthAssessmentMapper;

    /**
     * 查询健康评估记录
     * 
     * @param id 健康评估记录主键
     * @return 健康评估记录
     */
    @Override
    public HealthAssessmentDataCollection selectHealthAssessmentById(Long id)
    {
        return healthAssessmentDataCollectionService.getById(id);
    }

    /**
     * 查询健康评估记录列表
     * 
     * @param healthAssessment 健康评估记录
     * @return 健康评估记录
     */
    @Override
    public List<HealthAssessment> selectHealthAssessmentList(HealthAssessment healthAssessment)
    {
        return healthAssessmentMapper.selectHealthAssessmentList(healthAssessment);
    }

    @Autowired
    private IHealthAssessmentDataCollectionService healthAssessmentDataCollectionService;

    /**
     * 新增健康评估记录
     * 
     * @param dto 健康评估记录
     * @return 结果
     * 新增或修改请求参数都是ElderAssessmentDto封装，区别有id代表进行修改，没有id代表进行新增
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long insertHealthAssessment(ElderAssessmentDto dto)
    {
        //1.新增数据到健康评估记录表
        //1.1 新建健康评估记录表实体类对象
        HealthAssessment healthAssessment = new HealthAssessment();
        //1.2 判断前端是否传递有效健康评估id过来
        if(dto.getId()!=null) {
            //1.2.1 如果传递过来，根据id查询数据库的数据，在此基础上修改
            healthAssessment = getById(dto.getId());
            //判断是否评估完成，如果评估完成不可以修改
            if(healthAssessment!=null && healthAssessment.getEvaluationProgress()!=0){
                //来到这里是不可以修改评估数据，所以结束程序
                throw new BaseException("评估结束，不可以再次修改");
            }
        }
        //1.2 补全数据
        // 老人姓名
        healthAssessment.setElderName(dto.getBasicInfo().getElderName());
        // 身份证号
        healthAssessment.setIdCard(dto.getBasicInfo().getIdCard());
        // 入住状态：未入住 0
        healthAssessment.setCheckInStatus(0);
        // 评估状态：评估中 0
        healthAssessment.setEvaluationProgress(0);
        // 创建时间
        healthAssessment.setCreateTime(DateUtils.getNowDate());
        //1.3 插入或更新数据库
        saveOrUpdate(healthAssessment);

        //2.新增数据到健康数据采集表
        //2.0 实例采集数据实体类
        HealthAssessmentDataCollection hadc = new HealthAssessmentDataCollection();
        //2.1 封装id，与健康评估记录表id一直
        hadc.setId(healthAssessment.getId());
        //2.2 封装-基本信息，json字符串
        hadc.setBasicInfo(JSONUtil.toJsonStr(dto.getBasicInfo()));
        //2.3 封装-日常生活活动，json字符串
        hadc.setDailyLivingActivities(JSONUtil.toJsonStr(dto.getDailyLivingActivities()));
        //2.4 封装-精神状态，json字符串
        hadc.setMentalState(JSONUtil.toJsonStr(dto.getMentalState()));
        //2.5 封装-感知与沟通，json字符串
        hadc.setPerceptionCommunication(JSONUtil.toJsonStr(dto.getPerceptionAndCommunication()));
        //2.6 封装-社会参与，json字符串
        hadc.setSocialParticipation(JSONUtil.toJsonStr(dto.getSocialParticipation()));
        //2.7 封装-健康评估，json字符串
        hadc.setHealthAssessment(JSONUtil.toJsonStr(dto.getHealthAssessmentDto()));
        //2.8 封装-创建时间
        hadc.setCreateTime(DateUtils.getNowDate());
        //2.9 插入或更新数据库信息
        healthAssessmentDataCollectionService.saveOrUpdate(hadc);

        return healthAssessment.getId();
    }

    /**
     * 修改健康评估记录
     * 
     * @param healthAssessment 健康评估记录
     * @return 结果
     */
    @Override
    public int updateHealthAssessment(HealthAssessment healthAssessment)
    {
        return updateById(healthAssessment)? 1 : 0;
    }

    /**
     * 批量删除健康评估记录
     * 
     * @param ids 需要删除的健康评估记录主键
     * @return 结果
     */
    @Override
    public int deleteHealthAssessmentByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除健康评估记录信息
     * 
     * @param id 健康评估记录主键
     * @return 结果
     */
    @Override
    public int deleteHealthAssessmentById(Long id)
    {
        return removeById(id)? 1 : 0;
    }
}
