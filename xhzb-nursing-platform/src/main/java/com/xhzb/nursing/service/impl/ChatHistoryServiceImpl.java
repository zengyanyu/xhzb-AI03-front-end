package com.xhzb.nursing.service.impl;

import com.xhzb.common.utils.SecurityUtils;
import com.xhzb.nursing.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @Description ChatHistoryService
 * @Author songyu
 * @Date 2026-07-19
 */
@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String HISTORY_PREFIX = "chat:history:";

    /**
     * 将会话写入数据库
     *
     * @param chatId
     */
    @Override
    public void save(String chatId) {
        //存储当前登录用户会话id特点：不重复，所以redis采用set

        //获取登录用户id
        Long userId = SecurityUtils.getUserId();//原理：通过SpringSecurity权限认证框架获取（以后讲解）

        //写入redis
        redisTemplate.opsForSet().add(HISTORY_PREFIX + userId, chatId);

    }

    /**
     * 获取登录用户会话历史列表
     * ctrl+i 快速重写方法
     *
     * @return
     */
    @Override
    public List<String> getHistory() {

        //获取登录用户id
        Long userId = SecurityUtils.getUserId();

        //1.从redis中获取登录用户的会话set数据
        Set<String> members = redisTemplate.opsForSet().members(HISTORY_PREFIX + userId);

        //2.判空
        if (CollectionUtils.isEmpty(members)) {
            return List.of();
        }

        //3.不为空，将Set<String> 排序后转换成List<String>
        List<String> list = members.stream()
                .sorted()//默认字符串升序排序
                .toList();
        return list;
    }

    @Autowired
    private RedisChatMemoryService redisChatMemoryService;

    /**
     * 删除会话及聊天信息
     *
     * @param chatId
     */
    @Override
    public void delete(String chatId) {
        //删除会话id的聊天历史信息
        redisChatMemoryService.clear(chatId);

        //删除登录用户的会话id的set中对应的会话id
        redisTemplate.opsForSet().remove(HISTORY_PREFIX + SecurityUtils.getUserId(), chatId);
    }
}
