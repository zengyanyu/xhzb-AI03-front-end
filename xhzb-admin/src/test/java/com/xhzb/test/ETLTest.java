package com.xhzb.test;


import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
@SpringBootTest
public class ETLTest {


    @Test
    public void testLoadTex() throws FileNotFoundException {
        // 读取文件
        InputStreamResource resource = new InputStreamResource(new FileInputStream("D:\\abc.txt"));
        // 创建TextReader
        TextReader textReader = new TextReader(resource);

        //追加一点东西(自定义)
        textReader.getCustomMetadata().put("filename", "abc.txt");

        // 读取文件内容
        List<Document> documentList = textReader.read();
        for (Document document : documentList) {
            System.out.println(document.getFormattedContent());
        }
    }

    //读取PDF - 按页读取，一页抽取一个Document
    @Test
    public void testPDFByPage() throws FileNotFoundException {
        // 读取文件
        InputStreamResource resource = new InputStreamResource(new FileInputStream("D:\\护理员工工作手册.pdf"));

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0) // 设置页眉边距
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0) // 删除页眉顶部的文本行数
                                /*
                                报表、合同、扫描 PDF 每页固定页眉：
                                    每页最上面 2 行是公司名称、页码、日期，属于无用干扰文本，会干扰向量化、检索。
                                    这时设置 .withNumberOfTopTextLinesToDelete(2)，自动丢掉每页前 2 行页眉。
                                    书本文档每页顶部是章节标题，想保留：传 0，顶部文字全部保留。
                                * */
                                .build())
                        .withPagesPerDocument(1) // 每个文档的页数
                        .build());

        System.out.println(pdfReader.read());
    }


    //读取PDF - 按段落读取，一个段落抽取一个Document
    @Test
    public void testPDFByParagraph() throws FileNotFoundException {
        // 读取文件
        InputStreamResource resource = new InputStreamResource(new FileInputStream("D:\\护理员工工作手册.pdf"));

        ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0) // 设置页眉边距
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0) // 删除页眉顶部的文本行数
                                .build())
                        /*
                        withPagesPerDocument(1)：段落跨页合并阈值，和分页读取器含义完全不同
                            // 含义：单个逻辑段落最多覆盖连续多少页，超过就拆分新段落Document
                            // 设为1：在一个1页面，一个段落一个文档
                            // 设为2：一个段落在2页内，一个段落一个文档
                            // 设为3：一个锻炼在3页内，合并为同一个Document；
                            // 不是“一页一个Document”，是限制段落跨页范围
                        * */
                        .withPagesPerDocument(1) // 每个文档的页数
                        .build());

        System.out.println(pdfReader.read());
    }


    @Test
    public void testTestSplitter() throws FileNotFoundException {

        // 读取文件
        InputStreamResource resource = new InputStreamResource(new FileInputStream("D:\\护理员工工作手册.pdf"));

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0) // 设置页眉边距
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0) // 删除页眉顶部的文本行数
                                .build())
                        .withPagesPerDocument(1) // 每个文档的页数
                        .build());


        // 创建TextSplitter
        TextSplitter textSplitter = new TokenTextSplitter();
    /*
        new TokenTextSplitter()  在构造函数中，默认给了一些分隔策略
        public TokenTextSplitter() {
            this(800, 350, 5, 10000, true);
        }
        自定义分隔策略
        TokenTextSplitter textSplitter = TokenTextSplitter.builder()
            .withChunkSize(1000) //chunkSize：每个文本块的目标大小（单位：token）（默认值：800）
            .withMinChunkSizeChars(400)//- minChunkSizeChars：每个文本块的最小长度（以字符为单位）（默认值：350）。
            .withMinChunkLengthToEmbed(10) //- minChunkLengthToEmbed：要包含的数据块的最小长度（默认值：5，单位：字符）。
            .withMaxNumChunks(5000)//- maxNumChunks：从文本生成的最大块数（默认值：10000，单位：文档个数或块数）。
            .withKeepSeparator(true)//keepSeparator是否在数据块中保留分隔符（如换行符）（默认值：true）
            .build();

        minChunkSizeChars(400)：分割阶段就合并短文本，尽量不让小块产生；
        minChunkLengthToEmbed(10)：分割完成后兜底过滤，再短的碎片直接抛弃；
        使用场景
        400 是 “尽量合并，不要产出小块”；
        10 是 “哪怕合并失败，低于 10 字符的垃圾碎片直接删掉”； 比如只剩一句单独的 “好的”（2 字符），会直接丢弃，不会存入知识库。

    * */
        System.out.println("分隔之前的文档数："+pdfReader.read().size());
        List<Document> documents = textSplitter.apply(pdfReader.read());
        System.out.println("分隔之后的文档数："+documents.size());
        System.out.println(documents);

    }


    @Autowired
    private VectorStore vectorStore;

    @Test
    public void testTestSplitter2() throws FileNotFoundException {

        // 读取文件
        InputStreamResource resource = new InputStreamResource(new FileInputStream("D:\\护理员工工作手册.pdf"));

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0) // 设置页眉边距
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0) // 删除页眉顶部的文本行数
                                .build())
                        .withPagesPerDocument(1) // 每个文档的页数
                        .build());

        // 创建TextSplitter
        TextSplitter textSplitter = new TokenTextSplitter();
        System.out.println("分隔之前的文档数："+pdfReader.read().size());
        List<Document> documents = textSplitter.apply(pdfReader.read());
        System.out.println("分隔之后的文档数："+documents.size());
        //给每个文档Document添加自定义数据filename
        documents.forEach(document -> document.getMetadata().put("filename","护理员工工作手册.pdf"));
        //存储到向量数据库中，分批添加，不然会报错
        for (int i = 0; i < documents.size(); i += 10) {
            //documents.subList(0,10)  提取0~9的元素返回一个List<Document>
            //Math.min(i + 10, documents.size())  取两个数据的最小值
            vectorStore.add(documents.subList(i, Math.min(i + 10, documents.size())));
        }
    }

    @Test
    public void testRetriever() {
        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.5) // 设置相似度阈值，在0.5以上
                .topK(5) // 设置返回的匹配度最高的前5个文档数量
                .build();
        List<Document> documents = retriever.retrieve(new Query("护理服务宗旨与核心价值是什么"));
        System.out.println(documents);
    }

}