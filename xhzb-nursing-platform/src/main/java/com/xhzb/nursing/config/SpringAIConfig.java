package com.xhzb.nursing.config;

import org.springframework.ai.chat.client.ChatClient;
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
    public ChatClient chatClient(OpenAiChatModel openAiChatModel){

        //创建调用大模型的客户端对象
        return ChatClient.builder(openAiChatModel)
                .build();
    }
}
