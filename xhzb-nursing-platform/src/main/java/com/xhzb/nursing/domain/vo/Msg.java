package com.xhzb.nursing.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AI对话消息VO视图对象
 * 作用：包装SpringAI原生Message消息对象，额外增加消息创建时间字段
 * 提供双向转换能力：
 * 1. SpringAI原生Message → 当前Msg实体（用于接收AI对话消息、存储到数据库/传给前端）
 * 2. 当前Msg实体 → SpringAI原生Message（用于传给AI大模型接口调用）
 */
@NoArgsConstructor // lombok：无参构造函数，序列化/反序列化必备
@AllArgsConstructor // lombok：全参构造函数，方便对象快速赋值
@Data // lombok：自动生成get/set、toString、equals、hashCode等通用方法
public class Msg {
    /**
     * 消息类型枚举
     * SYSTEM：系统提示词（给AI设定角色、规则）
     * USER：用户提问消息
     * ASSISTANT：AI模型回复消息
     */
    MessageType messageType;

    /**
     * 消息文本内容
     * 用户输入文字 / AI回复文字 / 系统prompt文本
     */
    String text;

    /**
     * 消息元数据扩展信息
     * 存储自定义附加参数，例如对话ID、用户ID、额外业务标记等
     */
    Map<String, Object> metadata;

    /**
     * 消息创建时间
     * 转换消息时自动赋值为当前系统时间，记录这条消息生成的创建时间，用于排序
     */
    private LocalDateTime createTime;

    /**
     * 构造方法：获取SpringAI原生Message 转换为 当前业务Msg对象， 写入数据库
     * 自动填充消息类型、文本、元数据，同时记录当前创建时间
     * @param message SpringAI框架提供的标准对话消息对象
     */
    public Msg(Message message) {
        // 赋值消息类型（系统/用户/助手）
        this.messageType = message.getMessageType();
        // 赋值消息正文文本
        this.text = message.getText();
        // 赋值扩展元数据
        this.metadata = message.getMetadata();
        // 自动设置当前时间为消息创建时间
        createTime = LocalDateTime.now();
    }

    /**
     * 将当前业务Msg对象 转换为 SpringAI标准Message对象， 因为从数据库读取数据只能用Msg, SpringAI不认识MSG，然而我们要Message
         将历史消息给到SpringAI，这样SpringAI在Advisor前置拦截才可以获取Message历史消息，所以这里将Msg转换为Message
     * 供AI大模型接口调用使用，根据消息类型区分构建不同消息实例
     * @return SpringAI框架原生Message消息对象，可直接传入ChatClient调用大模型
     * @throws IllegalArgumentException 遇到不支持的消息类型时抛出异常
     *
     * 建造者设计模式创建对象：就是一种创建对象的方式，本周调用里面全参构造器，只不过赋值以一个一个的灵活赋值，没有赋值就为null
     * UserMessage.builder()
*                     .text(text)//给属性text赋值
*                     .media(List.of()) // 暂无图片/文件多媒体，传入空集合
*                     .metadata(metadata)
*                     .build();
     *
     *
     */
    public Message toMessage() {
        // 根据消息类型分支构建对应AI消息实体
        return switch (messageType) {
            // 系统提示消息：用于设定AI角色、对话规则、背景要求
            case SYSTEM -> new SystemMessage(text);
            // 用户提问消息：构建用户输入，传入文本、空多媒体资源、自定义元数据
            case USER -> UserMessage.builder()
                    .text(text)
                    .media(List.of()) // 暂无图片/文件多媒体，传入空集合
                    .metadata(metadata)
                    .build();
            // AI助手回复消息：构建模型返回内容，携带元数据属性
            case ASSISTANT -> AssistantMessage.builder()
                    .content(text)
                    .properties(metadata)
                    .media(List.of()) // 暂无多媒体内容，空集合
                    .build();
            // 未知/不支持的消息类型，抛出非法参数异常，阻断流程
            default -> throw new IllegalArgumentException("Unsupported message type: " + messageType);
        };
    }
}