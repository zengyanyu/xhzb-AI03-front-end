package com.xhzb.nursing.controller;

import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.nursing.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/history")
    public AjaxResult history(){
        //调用业务层获取登录用户会话列表
        List<String> list = chatHistoryService.getHistory();

        //返回给前端
        return success(list);
    }
}
