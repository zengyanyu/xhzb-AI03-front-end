package com.xhzb.nursing.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * Dify 智能体聊天服务
 * <p>
 * 封装 Dify open-api:
 * 1. 流式聊天         POST /chat-messages (response_mode=streaming)
 * 2. 会话列表         GET  /conversations
 * 3. 会话历史消息     GET  /messages
 * 4. 删除会话         DELETE /conversations/{conversation_id}
 *
 * @Description DifyChatService
 * @Author xhzb
 * @Date 2026-08-21
 */
public interface DifyChatService {

    /**
     * 流式聊天: 调用 Dify /chat-messages 接口, 将 SSE 事件逐条转发给前端
     *
     * @param query           用户输入内容
     * @param conversationId  会话ID, 新会话传空字符串或null
     * @param user            用户标识(用于Dify区分终端用户)
     * @return SseEmitter SSE发射器
     */
    SseEmitter chatStream(String query, String conversationId, String user);

    /**
     * 查询会话列表
     *
     * @param user   用户标识
     * @param limit  返回数量上限, 传null默认20
     * @param lastId 分页游标(上一页最后一条的id), 首页传null
     * @return Dify 原始响应JSON
     */
    JsonNode getConversations(String user, Integer limit, String lastId);

    /**
     * 查询指定会话的历史消息
     *
     * @param user           用户标识
     * @param conversationId 会话ID
     * @param limit          返回数量上限, 传null默认20
     * @return Dify 原始响应JSON
     */
    JsonNode getMessages(String user, String conversationId, Integer limit);

    /**
     * 删除指定会话
     *
     * @param user           用户标识
     * @param conversationId 会话ID
     * @return Dify 原始响应JSON
     */
    JsonNode deleteConversation(String user, String conversationId);

    /**
     * 拼装请求 inputs 参数(预留扩展, 如需要携带业务上下文可在此追加)
     *
     * @return inputs Map
     */
    Map<String, Object> buildInputs();
}
