package com.xhzb.nursing.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.utils.SecurityUtils;
import com.xhzb.nursing.service.DifyChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * Dify 智能体聊天控制器
 * <p>
 * 提供:
 * 1. POST   /dify/chat                     流式聊天(SSE)
 * 2. GET    /dify/conversations            会话列表
 * 3. GET    /dify/messages                 会话历史消息
 * 4. DELETE /dify/conversations/{id}       删除会话
 * <p>
 * 注意: 不修改已有 /ai/** 接口, 本控制器全部走 /dify/** 前缀。
 *
 * @Description DifyChatController
 * @Author xhzb
 * @Date 2026-08-21
 */
@RestController
@RequestMapping("/dify")
public class DifyChatController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(DifyChatController.class);

    @Autowired
    private DifyChatService difyChatService;

    /**
     * 流式聊天
     * 请求体: { "query": "用户问题", "conversationId": "会话ID(新会话可传空)" }
     * 响应: SSE 流, 事件名与 Dify 保持一致(message/message_end/error/ping等), data 为 Dify 原始事件JSON
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestBody Map<String, String> body) {
        String query = body.get("query");
        String conversationId = body.get("conversationId");
        String user = currentUser();
        log.info("[Dify][Controller] /dify/chat 收到请求: user={}, conversationId={}, query={}",
                user, conversationId, query);
        return difyChatService.chatStream(query, conversationId, user);
    }

    /**
     * 会话列表
     * 可选参数: limit(默认20), lastId(分页游标)
     */
    @GetMapping("/conversations")
    public AjaxResult conversations(@RequestParam(required = false) Integer limit,
                                    @RequestParam(required = false) String lastId) {
        String user = currentUser();
        log.info("[Dify][Controller] /dify/conversations 收到请求: user={}, limit={}, lastId={}", user, limit, lastId);
        try {
            JsonNode result = difyChatService.getConversations(user, limit, lastId);
            log.info("[Dify][Controller] /dify/conversations 查询成功, 会话数量: {}",
                    result.has("data") && result.get("data").isArray() ? result.get("data").size() : 0);
            return success(result);
        } catch (Exception e) {
            log.error("[Dify][Controller] /dify/conversations 查询失败, user={}", user, e);
            return error("获取会话列表失败: " + e.getMessage());
        }
    }

    /**
     * 会话历史消息
     * 参数: conversationId(必填), limit(默认20)
     */
    @GetMapping("/messages")
    public AjaxResult messages(@RequestParam String conversationId,
                               @RequestParam(required = false) Integer limit) {
        String user = currentUser();
        log.info("[Dify][Controller] /dify/messages 收到请求: user={}, conversationId={}, limit={}",
                user, conversationId, limit);
        if (conversationId == null || conversationId.isBlank()) {
            log.warn("[Dify][Controller] /dify/messages 缺少 conversationId 参数");
            return error("conversationId 不能为空");
        }
        try {
            JsonNode result = difyChatService.getMessages(user, conversationId, limit);
            log.info("[Dify][Controller] /dify/messages 查询成功, 消息数量: {}",
                    result.has("data") && result.get("data").isArray() ? result.get("data").size() : 0);
            return success(result);
        } catch (Exception e) {
            log.error("[Dify][Controller] /dify/messages 查询失败, user={}, conversationId={}", user, conversationId, e);
            return error("获取历史消息失败: " + e.getMessage());
        }
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/conversations/{conversationId}")
    public AjaxResult deleteConversation(@PathVariable String conversationId) {
        String user = currentUser();
        log.info("[Dify][Controller] /dify/conversations/{} DELETE 收到请求: user={}", conversationId, user);
        if (conversationId == null || conversationId.isBlank()) {
            log.warn("[Dify][Controller] 删除会话缺少 conversationId");
            return error("conversationId 不能为空");
        }
        try {
            JsonNode result = difyChatService.deleteConversation(user, conversationId);
            log.info("[Dify][Controller] 删除会话成功: user={}, conversationId={}, result={}", user, conversationId, result);
            return success(result);
        } catch (Exception e) {
            log.error("[Dify][Controller] 删除会话失败, user={}, conversationId={}", user, conversationId, e);
            return error("删除会话失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户标识(作为 Dify 的 user 参数, 用于区分不同终端用户)
     */
    private String currentUser() {
        try {
            String username = SecurityUtils.getUsername();
            if (username != null && !username.isBlank()) {
                return username;
            }
        } catch (Exception e) {
            log.warn("[Dify] 获取当前登录用户失败, 使用默认用户标识 default-user, 原因: {}", e.getMessage());
        }
        return "default-user";
    }
}
