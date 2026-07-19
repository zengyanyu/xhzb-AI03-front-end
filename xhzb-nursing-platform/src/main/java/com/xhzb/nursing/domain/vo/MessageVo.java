package com.xhzb.nursing.domain.vo;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;

/**
 * @Description MessageVo
 * @Author songyu
 * @Date 2026-07-19
 */
@Data
public class MessageVo {

    private String role;

    private String content;

    public MessageVo(Message message){
        this.role = switch (message.getMessageType()){
            case USER->"user";
            case SYSTEM->"system";
            case ASSISTANT -> "assistant";
            default -> "";
        };
        this.content = message.getText();
    }
}
