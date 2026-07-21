package com.xhzb.nursing.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.common.exception.base.BaseException;
import com.xhzb.common.utils.DateUtils;
import com.xhzb.nursing.domain.KnowledgeBase;
import com.xhzb.nursing.mapper.KnowledgeBaseMapper;
import com.xhzb.nursing.service.IKnowledgeBaseService;
import com.xhzb.oss.client.OSSAliyunFileStorageService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 知识库主Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-07-21
 */
@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements IKnowledgeBaseService
{
    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    /**
     * 查询知识库主
     * 
     * @param id 知识库主主键
     * @return 知识库主
     */
    @Override
    public KnowledgeBase selectKnowledgeBaseById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询知识库主列表
     * 
     * @param knowledgeBase 知识库主
     * @return 知识库主
     */
    @Override
    public List<KnowledgeBase> selectKnowledgeBaseList(KnowledgeBase knowledgeBase)
    {
        return knowledgeBaseMapper.selectKnowledgeBaseList(knowledgeBase);
    }

    //导入OSS存储业务类
    @Autowired
    private OSSAliyunFileStorageService ossAliyunFileStorageService;

    //导入文本分割器
    @Autowired
    private TextSplitter textSplitter;

    //导入向量存储对象
    @Autowired
    private VectorStore vectorStore;

    /**
     * 新增知识库主
     * 
     * @param knowledgeBase 知识库主
     * @return 结果
     */
    @Override
    public int insertKnowledgeBase(KnowledgeBase knowledgeBase)
    {

        //1.从oss下载文档获取输入流数据
        InputStream inputStream = ossAliyunFileStorageService.download(knowledgeBase.getDocumentUrl());
        if(inputStream == null){
            throw new BaseException("上传的文件知识库文档不存在！");
        }

        //2.创建pdf解析器对象,根据输入流得到文档列表
        // 读取文件
        InputStreamResource resource = new InputStreamResource(inputStream);
        ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0) // 设置页眉边距
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0) // 删除页眉顶部的文本行数
                                .build())
                        .withPagesPerDocument(1) // 每个文档的页数
                        .build());
        List<Document> documentBigList = pdfReader.read();

        //3.创建文本文档分割器对象，分隔文档得到分隔后的文档列表
        List<Document> documentSmallList = textSplitter.apply(documentBigList);

        //4.给每个小数据库块文档添加自定义扩展属性file=文档名字
        documentSmallList.forEach(document -> document.getMetadata().put("filename",knowledgeBase.getTitle()));

        //5.将小块文档列表抽取id集合，便于后面写入数据库
        List<String> idList = documentSmallList.stream().map(document -> document.getId()).toList();

        //6.批量添加到向量数据库
        int batchSize = 10;
        for (int i = 0; i < documentSmallList.size(); i+=batchSize) {
            //从文档集合中提取部分元素（10个元素）
            List<Document> subList = documentSmallList.subList(i, Math.min(i + 10, documentSmallList.size()));
            //批量添加到向量数据库
            vectorStore.add(subList);
            System.out.println("已添加批次: " + (i / batchSize+1) + ", 数量: " + subList.size());
        }

        //7封装补全实体类KnowledgeBase数据（创建时间，），插入到数据库
        //创建时间
        knowledgeBase.setCreateTime(DateUtils.getNowDate());
        //remark(小文档的id列表，json字符串格式)
        knowledgeBase.setRemark(JSONUtil.toJsonStr(idList));
        //插入数据库
        return save(knowledgeBase)? 1 : 0;
    }

    /**
     * 修改知识库主
     * 
     * @param knowledgeBase 知识库主
     * @return 结果
     */
    @Override
    public int updateKnowledgeBase(KnowledgeBase knowledgeBase)
    {
        return updateById(knowledgeBase)? 1 : 0;
    }

    /**
     * 批量删除知识库主
     * 
     * @param ids 需要删除的知识库主主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除知识库主信息
     * 
     * @param id 知识库主主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseById(Long id)
    {
        return removeById(id)? 1 : 0;
    }
}
