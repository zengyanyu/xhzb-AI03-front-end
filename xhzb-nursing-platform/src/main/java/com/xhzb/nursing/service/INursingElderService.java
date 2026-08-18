package com.xhzb.nursing.service;

import java.util.List;
import com.xhzb.nursing.domain.NursingElder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 护理员老人关联Service接口
 *
 * @author songyu
 * @date 2026-08-18
 */
public interface INursingElderService extends IService<NursingElder>
{
    /**
     * 根据老人id查询关联的护理人员id集合
     *
     * @param elderId 老人id
     * @return 护理人员id集合
     */
    public List<Long> selectNursingIdsByElderId(Long elderId);
}
