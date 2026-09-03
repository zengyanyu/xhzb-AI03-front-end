package com.xhzb.nursing.config;

import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

/**
 * SpringAI Redis向量存储配置类
 * 功能：配置Redis向量数据库客户端与向量存储Bean，用于文档向量存储、相似度检索
 * 底层依赖Redis Stack（Redis扩展向量检索模块），结合OpenAI Embedding模型实现文本向量化
 */
@Configuration
public class RedisVectorConfig {

    /**
     * 创建Jedis连接池客户端实例
     * JedisPooled：线程安全的Jedis客户端，内置连接池，适配Redis Stack向量服务
     *
     * @return 封装好连接地址与端口的Redis客户端
     */
    @Bean
    public JedisPooled jedisPooled() {
        // 参数1：Redis服务IP地址；参数2：Redis自定义端口6378（默认6379）
        return new JedisPooled("localhost", 6379);
    }

    /**
     * 构建Redis向量存储核心Bean-VectorStore，全局唯一向量操作仓库，用它实现向量数据增删改查
     * 依赖注入：
     * 1. jedisPooled：Redis连接客户端，负责与Redis Stack通信
     * 2. openAiEmbeddingModel：OpenAI向量化模型，将文本转为浮点向量
     *
     * @return VectorStore 统一向量存储标准接口，提供增删改查、相似度搜索API
     */
//    @Bean
//    public VectorStore vectorStore(JedisPooled jedisPooled, OpenAiEmbeddingModel openAiEmbeddingModel) {
//        // RedisVectorStore构建器，链式配置向量库参数
//        return RedisVectorStore.builder(jedisPooled, openAiEmbeddingModel)
//                // 向量索引名称：Redis内向量检索的索引标识，区分不同业务向量库
//                .indexName("spring-ai-index")
//                // Redis Key前缀：所有向量数据存储时自动拼接该前缀，隔离业务数据
//                .prefix("doc:")
//                // 定义可用于过滤查询的元数据字段（检索时可按标签/数值筛选文档）
//                .metadataFields(
//                        // tag类型：标签字符串字段，支持等值筛选（如country=China）
//                        RedisVectorStore.MetadataField.tag("country"),
//                        // numeric类型：数字字段，支持区间筛选（如year > 2020）
//                        RedisVectorStore.MetadataField.numeric("year")
//                )
//                // 自动初始化索引结构：true=项目启动时自动创建向量索引；false=手动创建
//                .initializeSchema(true)
//                // 批量向量化策略：基于Token数量自动拆分文档批量向量化，避免单次请求超限
//                .batchingStrategy(new TokenCountBatchingStrategy())
//                .build();
//    }

}