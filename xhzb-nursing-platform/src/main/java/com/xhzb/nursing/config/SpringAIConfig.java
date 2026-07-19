package com.xhzb.nursing.config;

import com.xhzb.nursing.constants.SystemConstants;
import com.xhzb.nursing.service.impl.RedisChatMemoryService;
import com.xhzb.nursing.tool.NursingProjectTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description SpringAIConfig
 * @Author songyu
 * @Date 2026-07-18
 */
@Configuration
public class SpringAIConfig {

    /**
     *
     * @param openAiChatModel springAI会读取你配置文件中大模型信息封装的大模型数据对象
     * @return
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel, NursingProjectTool nursingProjectTool, RedisChatMemoryService redisChatMemoryService){

        //创建调用大模型的客户端对象
        return ChatClient.builder(openAiChatModel)
                .defaultSystem(SystemConstants.prompt)
                .defaultTools(nursingProjectTool)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(redisChatMemoryService).build()//创建ChatMemoryAdvisor给到大模型，去实现会话聊天记忆
                )
                .build();
    }

    // @Bean
    // public ChatClient chatClient(OllamaChatModel ollamaChatModel){
    //
    //     //创建调用大模型的客户端对象
    //     return ChatClient.builder(ollamaChatModel)
    //             //.defaultSystem("您是一家名为“星海智伴”的职业养老机构的客户聊天助手，你的名字叫小智。请以友好、乐于助人和愉快的方式解答用户的各种问题。")
    //             .build();
    // }

    // @Bean
    // public ChatClient chatClient(DeepSeekChatModel deepSeekChatModel){
    //
    //     //创建调用大模型的客户端对象
    //     return ChatClient.builder(deepSeekChatModel)
    //             .defaultSystem("您是一家名为“星海智伴deepseek”的职业养老机构的客户聊天助手，你的名字叫小智。请以友好、乐于助人和愉快的方式解答用户的各种问题。")
    //             .build();
    // }
}
