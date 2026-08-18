package com.xhzb.nursing.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.nursing.domain.NursingElder;
import com.xhzb.nursing.mapper.NursingElderMapper;
import com.xhzb.nursing.service.INursingElderService;
import org.springframework.stereotype.Service;

/**
 * 护理员老人关联Service业务层处理
 *
 * @author songyu
 * @date 2026-08-18
 */
@Service
public class NursingElderServiceImpl extends ServiceImpl<NursingElderMapper, NursingElder> implements INursingElderService
{
    /**
     * 根据老人id查询关联的护理人员id集合
     *
     * @param elderId 老人id
     * @return 护理人员id集合
     */
    @Override
    public List<Long> selectNursingIdsByElderId(Long elderId)
    {
        if (elderId == null)
        {
            return new ArrayList<>();
        }
        //查询该老人关联的全部护理人员，并去重返回护理人员id集合
        return list(new LambdaQueryWrapper<NursingElder>().eq(NursingElder::getElderId, elderId))
                .stream().map(NursingElder::getNursingId).distinct().collect(Collectors.toList());
    }
}
