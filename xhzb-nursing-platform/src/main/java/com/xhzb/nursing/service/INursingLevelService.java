package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.NursingLevel;
import com.xhzb.nursing.domain.vo.NursingLevelVo;

import java.util.List;

/**
 * 护理等级Service接口
 *
 * @author ruoyi
 * @date 2025-03-28
 */
public interface INursingLevelService extends IService<NursingLevel> {
    /**
     * 查询护理等级
     *
     * @param id 护理等级主键
     * @return 护理等级
     */
    NursingLevel selectNursingLevelById(Long id);

    /**
     * 查询护理等级列表
     *
     * @param nursingLevel 护理等级
     * @return 护理等级集合
     */
    List<NursingLevelVo> selectNursingLevelList(NursingLevel nursingLevel);

    /**
     * 新增护理等级
     *
     * @param nursingLevel 护理等级
     * @return 结果
     */
    int insertNursingLevel(NursingLevel nursingLevel);

    /**
     * 修改护理等级
     *
     * @param nursingLevel 护理等级
     * @return 结果
     */
    int updateNursingLevel(NursingLevel nursingLevel);

    /**
     * 批量删除护理等级
     *
     * @param ids 需要删除的护理等级主键集合
     * @return 结果
     */
    int deleteNursingLevelByIds(Long[] ids);

    /**
     * 删除护理等级信息
     *
     * @param id 护理等级主键
     * @return 结果
     */
    int deleteNursingLevelById(Long id);

    /**
     * 查询所有启用的护理等级
     *
     * @return 护理等级集合
     */
    List<NursingLevel> listAll();

}
