package com.xhzb.nursing.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.KnowledgeBase;

/**
 * 知识库主Mapper接口
 * 
 * @author ruoyi
 * @date 2026-07-21
 */
@Mapper
public interface KnowledgeBaseMapper extends BaseMapper<KnowledgeBase>
{
    /**
     * 查询知识库主
     * 
     * @param id 知识库主主键
     * @return 知识库主
     */
    public KnowledgeBase selectKnowledgeBaseById(Long id);

    /**
     * 查询知识库主列表
     * 
     * @param knowledgeBase 知识库主
     * @return 知识库主集合
     */
    public List<KnowledgeBase> selectKnowledgeBaseList(KnowledgeBase knowledgeBase);

    /**
     * 新增知识库主
     * 
     * @param knowledgeBase 知识库主
     * @return 结果
     */
    public int insertKnowledgeBase(KnowledgeBase knowledgeBase);

    /**
     * 修改知识库主
     * 
     * @param knowledgeBase 知识库主
     * @return 结果
     */
    public int updateKnowledgeBase(KnowledgeBase knowledgeBase);

    /**
     * 删除知识库主
     * 
     * @param id 知识库主主键
     * @return 结果
     */
    public int deleteKnowledgeBaseById(Long id);

    /**
     * 批量删除知识库主
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKnowledgeBaseByIds(Long[] ids);
}
