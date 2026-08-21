package com.xhzb.nursing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Dify 智能体配置
 * <p>
 * 可在 application.yml 中通过以下配置覆盖默认值:
 * <pre>
 * dify:
 *   base-url: http://192.168.100.128/v1
 *   api-key: app-xxxx
 *   timeout-seconds: 300
 * </pre>
 *
 * @Description DifyProperties
 * @Author xhzb
 * @Date 2026-08-21
 */
@Component
@ConfigurationProperties(prefix = "dify")
public class DifyProperties {

    /** Dify API 基础地址(带 /v1) */
    private String baseUrl = "http://192.168.100.128/v1";

    /** Dify 应用 API 秘钥 */
    private String apiKey = "app-TPBAg6vcHXbARqwUap0bnTwG";

    /** 普通接口(会话列表/历史消息/删除会话)超时时间(秒) */
    private Integer timeoutSeconds = 60;

    /** 流式聊天接口连接超时时间(秒), 读取不超时(由服务端控制) */
    private Integer streamConnectTimeoutSeconds = 15;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public Integer getStreamConnectTimeoutSeconds() {
        return streamConnectTimeoutSeconds;
    }

    public void setStreamConnectTimeoutSeconds(Integer streamConnectTimeoutSeconds) {
        this.streamConnectTimeoutSeconds = streamConnectTimeoutSeconds;
    }
}
