package com.xhzb.nursing.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xhzb.nursing.config.DifyProperties;
import com.xhzb.nursing.service.DifyChatService;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Dify 智能体聊天服务实现
 * <p>
 * 使用 JDK 自带 HttpClient 调用 Dify open-api, 不引入额外依赖。
 * 流式聊天通过 SseEmitter 将 Dify 的 SSE 事件原样转发给前端。
 *
 * @Description DifyChatServiceImpl
 * @Author xhzb
 * @Date 2026-08-21
 */
@Service
public class DifyChatServiceImpl implements DifyChatService {

    private static final Logger log = LoggerFactory.getLogger(DifyChatServiceImpl.class);

    @Autowired
    private DifyProperties difyProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /** 流式转发线程池(IO密集型) */
    private final ExecutorService streamExecutor = Executors.newFixedThreadPool(8, r -> {
        Thread t = new Thread(r, "dify-sse-forward");
        t.setDaemon(true);
        return t;
    });

    /** 普通请求专用 HttpClient(带响应超时) */
    private volatile HttpClient normalClient;

    /** 流式请求专用 HttpClient(读取不超时) */
    private volatile HttpClient streamClient;

    private HttpClient normalClient() {
        if (normalClient == null) {
            synchronized (this) {
                if (normalClient == null) {
                    normalClient = HttpClient.newBuilder()
                            .connectTimeout(Duration.ofSeconds(difyProperties.getStreamConnectTimeoutSeconds()))
                            .build();
                    log.info("[Dify] 普通请求 HttpClient 初始化完成, 连接超时: {}s",
                            difyProperties.getStreamConnectTimeoutSeconds());
                }
            }
        }
        return normalClient;
    }

    private HttpClient streamClient() {
        if (streamClient == null) {
            synchronized (this) {
                if (streamClient == null) {
                    streamClient = HttpClient.newBuilder()
                            .connectTimeout(Duration.ofSeconds(difyProperties.getStreamConnectTimeoutSeconds()))
                            .build();
                    log.info("[Dify] 流式请求 HttpClient 初始化完成, 连接超时: {}s",
                            difyProperties.getStreamConnectTimeoutSeconds());
                }
            }
        }
        return streamClient;
    }

    @PreDestroy
    public void shutdown() {
        log.info("[Dify] 服务销毁, 关闭流式转发线程池");
        streamExecutor.shutdown();
    }

    // ==================== 1. 流式聊天 ====================

    @Override
    public SseEmitter chatStream(String query, String conversationId, String user) {
        long start = System.currentTimeMillis();
        log.info("[Dify][流式聊天] 收到请求 >>> user={}, conversationId={}, query长度={}, query内容={}",
                user, conversationId, query == null ? 0 : query.length(), query);

        // SSE 超时时间 0 = 不超时, 由客户端断开或 Dify 流结束控制
        SseEmitter emitter = new SseEmitter(0L);

        String finalConversationId = (conversationId == null ? "" : conversationId.trim());
        String finalUser = (user == null || user.isBlank()) ? "anonymous" : user.trim();

        // 客户端断开/超时/完成 回调日志
        emitter.onCompletion(() -> log.info("[Dify][流式聊天] SSE 已完成(complete), user={}, 耗时={}ms",
                finalUser, System.currentTimeMillis() - start));
        emitter.onTimeout(() -> log.warn("[Dify][流式聊天] SSE 超时, user={}, 耗时={}ms",
                finalUser, System.currentTimeMillis() - start));
        emitter.onError(t -> log.error("[Dify][流式聊天] SSE 异常, user={}, 错误: {}", finalUser, t.getMessage(), t));

        // 组装请求体
        ObjectNode body = objectMapper.createObjectNode();
        body.set("inputs", objectMapper.valueToTree(buildInputs()));
        body.put("query", query == null ? "" : query);
        body.put("response_mode", "streaming");
        body.put("conversation_id", finalConversationId);
        body.put("user", finalUser);
        String bodyJson = body.toString();
        log.info("[Dify][流式聊天] 请求 Dify 接口: POST {}, 请求体={}",
                difyProperties.getBaseUrl() + "/chat-messages", bodyJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(difyProperties.getBaseUrl() + "/chat-messages"))
                .header("Authorization", "Bearer " + difyProperties.getApiKey())
                .header("Content-Type", "application/json")
                .header("Accept", "text/event-stream")
                .timeout(Duration.ofSeconds(difyProperties.getStreamConnectTimeoutSeconds()))
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson, StandardCharsets.UTF_8))
                .build();

        // 异步转发, 避免阻塞 Tomcat 线程
        streamExecutor.execute(() -> {
            int eventCount = 0;
            try {
                HttpResponse<java.io.InputStream> response =
                        streamClient().send(request, HttpResponse.BodyHandlers.ofInputStream());

                int status = response.statusCode();
                log.info("[Dify][流式聊天] Dify 响应状态码: {}", status);
                if (status != 200) {
                    // 非200: 读取错误信息并转发给前端
                    String errBody = readAll(response.body());
                    log.error("[Dify][流式聊天] Dify 返回错误, status={}, body={}", status, errBody);
                    sendErrorEvent(emitter, "DIFY_HTTP_ERROR", "Dify 接口返回错误: HTTP " + status + ", " + truncate(errBody));
                    emitter.complete();
                    return;
                }

                // 解析 Dify 的 SSE 流: 每帧由若干行组成, "event: xxx" + "data: {...json...}", 帧之间空行分隔
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
                    String line;
                    String eventName = "message";
                    while ((line = reader.readLine()) != null) {
                        if (line.isBlank()) {
                            eventName = "message"; // 帧结束, 重置事件名
                            continue;
                        }
                        if (line.startsWith("event:")) {
                            eventName = line.substring("event:".length()).trim();
                            log.debug("[Dify][流式聊天] 收到事件类型: {}", eventName);
                            continue;
                        }
                        if (line.startsWith("data:")) {
                            String data = line.substring("data:".length()).trim();
                            eventCount++;
                            if ("error".equals(eventName)) {
                                log.error("[Dify][流式聊天] Dify 返回 error 事件: {}", data);
                            } else if ("message".equals(eventName)) {
                                // 增量文本, 只打 debug 日志避免刷屏, 截断展示
                                log.debug("[Dify][流式聊天] message增量(第{}个事件): {}", eventCount, truncate(data));
                            } else {
                                log.info("[Dify][流式聊天] 事件(第{}个): {} -> {}", eventCount, eventName, truncate(data));
                            }
                            // 原样转发给前端(事件名 + 数据)
                            emitter.send(SseEmitter.event().name(eventName).data(data));
                        }
                    }
                }
                log.info("[Dify][流式聊天] Dify 流读取完毕, 共转发 {} 个事件, user={}, 总耗时={}ms",
                        eventCount, finalUser, System.currentTimeMillis() - start);
                emitter.complete();
            } catch (IOException e) {
                // 客户端主动断开也会走到这里, 属常见情况
                log.warn("[Dify][流式聊天] IO 异常(可能为客户端断开或网络中断), user={}, 已转发事件数={}, 错误: {}",
                        finalUser, eventCount, e.getMessage());
                try {
                    sendErrorEvent(emitter, "DIFY_STREAM_ERROR", "流式连接中断: " + e.getMessage());
                    emitter.complete();
                } catch (Exception ignore) {
                    // emitter 已关闭则忽略
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("[Dify][流式聊天] 线程被中断, user={}", finalUser, e);
                emitter.completeWithError(e);
            } catch (Exception e) {
                log.error("[Dify][流式聊天] 未知异常, user={}, 已转发事件数={}", finalUser, eventCount, e);
                try {
                    sendErrorEvent(emitter, "DIFY_ERROR", "系统异常: " + e.getMessage());
                    emitter.complete();
                } catch (Exception ignore) {
                    emitter.completeWithError(e);
                }
            }
        });

        return emitter;
    }

    // ==================== 2. 会话列表 ====================

    @Override
    public JsonNode getConversations(String user, Integer limit, String lastId) {
        long start = System.currentTimeMillis();
        StringBuilder url = new StringBuilder(difyProperties.getBaseUrl())
                .append("/conversations?user=").append(urlEncode(user))
                .append("&limit=").append(limit == null ? 20 : limit);
        if (lastId != null && !lastId.isBlank()) {
            url.append("&last_id=").append(urlEncode(lastId));
        }
        log.info("[Dify][会话列表] 请求: GET {}, user={}", url, user);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url.toString()))
                    .header("Authorization", "Bearer " + difyProperties.getApiKey())
                    .timeout(Duration.ofSeconds(difyProperties.getTimeoutSeconds()))
                    .GET()
                    .build();
            HttpResponse<String> response = normalClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info("[Dify][会话列表] 响应状态码: {}, 耗时={}ms, 响应体: {}",
                    response.statusCode(), System.currentTimeMillis() - start, truncate(response.body()));
            return parseAndCheck(response);
        } catch (Exception e) {
            log.error("[Dify][会话列表] 请求异常, user={}, url={}", user, url, e);
            throw new RuntimeException("查询Dify会话列表失败: " + e.getMessage(), e);
        }
    }

    // ==================== 3. 会话历史消息 ====================

    @Override
    public JsonNode getMessages(String user, String conversationId, Integer limit) {
        long start = System.currentTimeMillis();
        String url = difyProperties.getBaseUrl() + "/messages?user=" + urlEncode(user)
                + "&conversation_id=" + urlEncode(conversationId)
                + "&limit=" + (limit == null ? 20 : limit);
        log.info("[Dify][历史消息] 请求: GET {}, user={}, conversationId={}", url, user, conversationId);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + difyProperties.getApiKey())
                    .timeout(Duration.ofSeconds(difyProperties.getTimeoutSeconds()))
                    .GET()
                    .build();
            HttpResponse<String> response = normalClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info("[Dify][历史消息] 响应状态码: {}, 耗时={}ms, 响应体: {}",
                    response.statusCode(), System.currentTimeMillis() - start, truncate(response.body()));
            return parseAndCheck(response);
        } catch (Exception e) {
            log.error("[Dify][历史消息] 请求异常, user={}, conversationId={}", user, conversationId, e);
            throw new RuntimeException("查询Dify历史消息失败: " + e.getMessage(), e);
        }
    }

    // ==================== 4. 删除会话 ====================

    @Override
    public JsonNode deleteConversation(String user, String conversationId) {
        long start = System.currentTimeMillis();
        String url = difyProperties.getBaseUrl() + "/conversations/" + urlEncode(conversationId);
        String bodyJson = objectMapper.createObjectNode().put("user", user).toString();
        log.info("[Dify][删除会话] 请求: DELETE {}, user={}, conversationId={}, 请求体={}",
                url, user, conversationId, bodyJson);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + difyProperties.getApiKey())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(difyProperties.getTimeoutSeconds()))
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(bodyJson, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = normalClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info("[Dify][删除会话] 响应状态码: {}, 耗时={}ms, 响应体: {}",
                    response.statusCode(), System.currentTimeMillis() - start, response.body());
            return parseAndCheck(response);
        } catch (Exception e) {
            log.error("[Dify][删除会话] 请求异常, user={}, conversationId={}", user, conversationId, e);
            throw new RuntimeException("删除Dify会话失败: " + e.getMessage(), e);
        }
    }

    // ==================== 公共方法 ====================

    @Override
    public Map<String, Object> buildInputs() {
        // 预留: 如需向 Dify 应用传递变量(如当前用户姓名), 在此追加
        return new HashMap<>();
    }

    /**
     * 发送统一的 error 事件给前端
     */
    private void sendErrorEvent(SseEmitter emitter, String code, String message) throws IOException {
        ObjectNode err = objectMapper.createObjectNode();
        err.put("code", code);
        err.put("message", message);
        log.warn("[Dify][流式聊天] 向前端发送 error 事件: {}", err);
        emitter.send(SseEmitter.event().name("error").data(err.toString()));
    }

    /**
     * 解析响应并校验状态码, 非2xx抛出异常
     */
    private JsonNode parseAndCheck(HttpResponse<String> response) {
        try {
            JsonNode node = objectMapper.readTree(response.body() == null ? "{}" : response.body());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("[Dify] 接口返回非2xx状态码: {}, 响应体: {}", response.statusCode(), response.body());
                throw new RuntimeException("Dify 接口错误: HTTP " + response.statusCode()
                        + ", " + (node.hasNonNull("message") ? node.get("message").asText() : truncate(response.body())));
            }
            return node;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("[Dify] 响应体JSON解析失败: {}", truncate(response.body()), e);
            throw new RuntimeException("Dify 响应解析失败: " + e.getMessage(), e);
        }
    }

    private String readAll(java.io.InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        try (in) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /** 日志截断, 避免超长响应刷爆日志 */
    private String truncate(String s) {
        if (s == null) {
            return null;
        }
        return s.length() > 500 ? s.substring(0, 500) + "...(共" + s.length() + "字符)" : s;
    }

    private String urlEncode(String s) {
        if (s == null) {
            return "";
        }
        return java.net.URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
