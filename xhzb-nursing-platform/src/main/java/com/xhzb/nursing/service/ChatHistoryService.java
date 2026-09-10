package com.xhzb.nursing.service;

import java.util.List;

/**
 * @Description ChatHistoryService
 * @Author songyu
 * @Date 2026-07-19
 */
public interface ChatHistoryService {

    /**
     * 将会话写入数据库
     *
     * @param chatId
     */
    void save(String chatId);

    /**
     * 获取登录用户会话历史列表
     *
     * @return
     */
    List<String> getHistory();

    /**
     * 删除会话及聊天信息
     *
     * @param chatId
     */
    void delete(String chatId);
}
