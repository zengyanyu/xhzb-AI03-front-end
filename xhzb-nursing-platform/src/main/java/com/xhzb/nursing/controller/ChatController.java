package com.xhzb.nursing.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Description ChatController
 * @Author songyu
 * @Date 2026-07-18
 */
@RestController
@RequestMapping("/ai")
public class ChatController {


    @Autowired
    private ChatClient chatClient;

    /**
     * 处理大模型聊天请求
     * @return String
     *    注意：如果controller直接String，默认返回中文会乱码，因为默认返回编码是gbk
     *         返回对象（非字符串），@ResponseBody默认会将对象（非字符串）转换为json字符串并会设置码表utf-8
     *            由于这里是字符串不转了并且也不设置编码了
     *         解决方案：produces = "text/html;charset=utf-8" 手动设置响应数据类型与码表
     */
    @PostMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt){//prompt用户提示词
        //使用大模型客户端调用调用大模型聊天接口发送提示词并返回大模型输出的结果
        return chatClient.prompt()
                .user(prompt) //设置用户提示词
                // .call() //同步调用，非流式输出，等待大模型输出所有结果在返回
                .stream() //流式输出
                .content();//得到大模型输出内容
    }
}
