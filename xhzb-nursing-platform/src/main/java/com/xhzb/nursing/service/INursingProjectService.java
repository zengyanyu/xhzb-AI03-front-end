package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.NursingProject;
import com.xhzb.nursing.domain.vo.NursingProjectVo;

import java.util.List;

/**
 * 护理项目Service接口
 *
 * @author ruoyi
 * @date 2025-03-28
 */
public interface INursingProjectService extends IService<NursingProject> {
    /**
     * 查询护理项目
     *
     * @param id 护理项目主键
     * @return 护理项目
     */
    NursingProject selectNursingProjectById(Long id);

    /**
     * 查询护理项目列表
     *
     * @param nursingProject 护理项目
     * @return 护理项目集合
     */
    List<NursingProject> selectNursingProjectList(NursingProject nursingProject);

    /**
     * 新增护理项目
     *
     * @param nursingProject 护理项目
     * @return 结果
     */
    int insertNursingProject(NursingProject nursingProject);

    /**
     * 修改护理项目
     *
     * @param nursingProject 护理项目
     * @return 结果
     */
    int updateNursingProject(NursingProject nursingProject);

    /**
     * 批量删除护理项目
     *
     * @param ids 需要删除的护理项目主键集合
     * @return 结果
     */
    int deleteNursingProjectByIds(Long[] ids);

    /**
     * 删除护理项目信息
     *
     * @param id 护理项目主键
     * @return 结果
     */
    int deleteNursingProjectById(Long id);

    /**
     * 查询所有的护理项目
     *
     * @return
     */
    List<NursingProjectVo> listAll();
}
