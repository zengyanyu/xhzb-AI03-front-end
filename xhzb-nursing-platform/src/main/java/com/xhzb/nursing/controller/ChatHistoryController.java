package com.xhzb.nursing.controller;

import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.nursing.domain.vo.MessageVo;
import com.xhzb.nursing.service.ChatHistoryService;
import com.xhzb.nursing.service.impl.RedisChatMemoryService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description ChatHistoryController
 * @Author songyu
 * @Date 2026-07-19
 */
@RestController
@RequestMapping("/ai")
public class ChatHistoryController extends BaseController {

    @Autowired
    private ChatHistoryService chatHistoryService;
    @Autowired
    private RedisChatMemoryService redisChatMemoryService;

    @GetMapping("/history")
    public AjaxResult history() {
        //调用业务层获取登录用户会话列表
        List<String> list = chatHistoryService.getHistory();

        //返回给前端
        return success(list);
    }

    /**
     * 处理获取聊天记录详情
     *
     * @param chatId
     * @return
     */
    @GetMapping("/history/{chatId}")
    public AjaxResult getHistoryDetail(@PathVariable String chatId) {

        //1.调用聊天历史业务对象获取指定会话id的历史聊天信息列表
        List<Message> messages = redisChatMemoryService.get(chatId);

        //2.判空处理
        if (CollectionUtils.isEmpty(messages)) {
            return success();
        }

        //3.将List<Message>转换为List<MessageVo>
        List<MessageVo> messageVos = messages.stream().map(message -> new MessageVo(message)).toList();
        // List<MessageVo> messageVos2 = messages.stream().map(MessageVo::new).toList();

        //3.有数据
        return success(messageVos);
    }

    /**
     * 处理删除会话及聊天记录
     *
     * @param chatId
     * @return
     */
    @DeleteMapping("/history/{chatId}")
    public AjaxResult delete(@PathVariable String chatId) {
        //调用业务处理
        chatHistoryService.delete(chatId);

        //返回成功
        return success();
    }
}
