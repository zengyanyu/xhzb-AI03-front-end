package com.xhzb.nursing.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
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
    // @Bean
    // public ChatClient chatClient(OpenAiChatModel openAiChatModel){
    //
    //     //创建调用大模型的客户端对象
    //     return ChatClient.builder(openAiChatModel)
    //             .defaultSystem("您是一家名为“星海智伴”的职业养老机构的客户聊天助手，你的名字叫小智。请以友好、乐于助人和愉快的方式解答用户的各种问题。")
    //             .build();
    // }

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel){

        //创建调用大模型的客户端对象
        return ChatClient.builder(ollamaChatModel)
                //.defaultSystem("您是一家名为“星海智伴”的职业养老机构的客户聊天助手，你的名字叫小智。请以友好、乐于助人和愉快的方式解答用户的各种问题。")
                .build();
    }
}
