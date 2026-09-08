package com.xhzb.nursing.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description SpringAIConfig
 * @Author songyu
 * @Date 2026-07-18
 */
@Configuration
public class SpringAIConfig {

//    @Autowired
//    private VectorStore vectorStore;

    /*
     * @param openAiChatModel springAI会读取你配置文件中大模型信息封装的大模型数据对象
     * @return
     */
//    @Bean
//    public ChatClient chatClient(OpenAiChatModel openAiChatModel, NursingProjectTool nursingProjectTool, RedisChatMemoryService redisChatMemoryService) {
//
////        //检索rag数据配置
////        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor
////                .builder(vectorStore)
////                .searchRequest(SearchRequest.builder()
////                        .similarityThreshold(0.7D)
////                        .topK(10)
////                        .build())
////                .build();
//
//
////        //创建调用大模型的客户端对象
////        return ChatClient.builder(openAiChatModel)
////                .defaultSystem(SystemConstants.prompt)
////                .defaultTools(nursingProjectTool)
////                .defaultAdvisors(
////                        new SimpleLoggerAdvisor(),
////                        MessageChatMemoryAdvisor.builder(redisChatMemoryService).build(),//创建ChatMemoryAdvisor给到大模型，去实现会话聊天记忆
////                        questionAnswerAdvisor
////                )
////                .build();
//        return null;
//    }

//    //这个springAI客户端专门用于评估分析操作
//    @Bean
//    public ChatClient chatClientByAssessment(OpenAiChatModel openAiChatModel) {
//
//        return ChatClient
//                .builder(openAiChatModel)
//                .defaultSystem("你是一个健康评估专家，专门用来评估老人的健康情况")
//                .defaultAdvisors(new SimpleLoggerAdvisor())
//                .build();
//    }

    // @Bean
    // public ChatClient chatClient(OllamaChatModel ollamaChatModel){
    //
    //     //创建调用大模型的客户端对象
    //     return ChatClient.builder(ollamaChatModel)
    //             //.defaultSystem("您是一家名为“星海智伴”的职业养老机构的客户聊天助手，你的名字叫小智。请以友好、乐于助人和愉快的方式解答用户的各种问题。")
    //             .build();
    // }

    /**
     * DeepSeek
     *
     * @param deepSeekChatModel
     * @return
     */
    @Bean
    public ChatClient chatClient(DeepSeekChatModel deepSeekChatModel) {
        //创建调用大模型的客户端对象
        return ChatClient.builder(deepSeekChatModel)
                .defaultSystem("您是一家名为“星海智伴deepseek”的职业养老机构的客户聊天助手，你的名字叫小智。请以友好、乐于助人和愉快的方式解答用户的各种问题。")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public TextSplitter textSplitter() {
        return TokenTextSplitter.builder()
                .withChunkSize(500)  //目标块大小  token数
                .withMinChunkSizeChars(200) // 最小块的字符数
                .withMinChunkLengthToEmbed(10) // 最小的文本字符长度
                .withMaxNumChunks(10000)  //文档最大块数
                .withKeepSeparator(false)   //不保留换行符
                .build();
    }
}
