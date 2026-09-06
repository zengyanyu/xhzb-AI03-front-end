package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.KnowledgeBase;

import java.util.List;

/**
 * 知识库主Service接口
 *
 * @author ruoyi
 * @date 2026-07-21
 */
public interface IKnowledgeBaseService extends IService<KnowledgeBase> {
    /**
     * 查询知识库主
     *
     * @param id 知识库主主键
     * @return 知识库主
     */
    KnowledgeBase selectKnowledgeBaseById(Long id);

    /**
     * 查询知识库主列表
     *
     * @param knowledgeBase 知识库主
     * @return 知识库主集合
     */
    List<KnowledgeBase> selectKnowledgeBaseList(KnowledgeBase knowledgeBase);

    /**
     * 新增知识库主
     *
     * @param knowledgeBase 知识库主
     * @return 结果
     */
    int insertKnowledgeBase(KnowledgeBase knowledgeBase);

    /**
     * 修改知识库主
     *
     * @param knowledgeBase 知识库主
     * @return 结果
     */
    int updateKnowledgeBase(KnowledgeBase knowledgeBase);

    /**
     * 批量删除知识库主
     *
     * @param ids 需要删除的知识库主主键集合
     * @return 结果
     */
    int deleteKnowledgeBaseByIds(Long[] ids);

    /**
     * 删除知识库主信息
     *
     * @param id 知识库主主键
     * @return 结果
     */
    int deleteKnowledgeBaseById(Long id);
}
