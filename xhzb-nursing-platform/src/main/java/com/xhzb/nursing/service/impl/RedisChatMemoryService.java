package com.xhzb.nursing.service.impl;

import cn.hutool.json.JSONUtil;
import com.xhzb.nursing.domain.vo.Msg;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * @Description RedisChatMemoryService
 * @Author songyu
 * @Date 2026-07-19
 */
@Component
public class RedisChatMemoryService implements ChatMemory {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String PREFIX = "chat:memory:";//redis的key前缀都使用“:”隔开

    //将最新用户问题与大模型回复写入数据库
    //conversationId , springAI会提供每个会话的id
    //springAI使用AOP前置拦截调用将用户问题写入数据库
    //springAI使用AOP后置拦截调用将大模型回复写入数据库
    @Override
    public void add(String conversationId, List<Message> messages) {

        //判断messages是否为空，为空直接返回
        if(messages==null || messages.size()==0){
            return;
        }
        //目标：List<Message> 转化为 List<Msg>
        List<Msg> msgList = messages.stream().map(message -> new Msg(message)).toList();
        // List<Msg> list2 = messages.stream().map(Msg::new).toList();  使用了构造器方法引用代替lambda表达式，使用前提：调用构造器，其次将lambda的参数传入构造器
        //将List<Msg> 转换为 List<String>, redis的list中每个元素是String, 将每个Msg转换为String
        List<String> strList = msgList.stream().map(msg -> JSONUtil.toJsonStr(msg)).toList();

        //写入redis
        redisTemplate.opsForList().leftPushAll(PREFIX+conversationId,strList);//向列表中左侧添加多个元素,每个元素是String(Msg转换的json字符串)
    }

    //获取历史消息
    //springAI使用AOP前置拦截调用
    @Override
    public List<Message> get(String conversationId) {
        
        //1.先从缓存获取对应的数据
        List<String> strList = redisTemplate.opsForList().range(PREFIX + conversationId, 0, -1);//获取所有元素
        //["一个msg的json字符串","一个msg的json字符串","一个msg的json字符串",...]

        if(strList==null || strList.size()==0){
            return List.of();
        }

        //2.将List<String> 转换为 List<Messag>
        List<Message> messageList = strList.stream()
                .map(str -> JSONUtil.toBean(str, Msg.class))//先将每个String加工为Msg
                .sorted(Comparator.comparing(msg -> msg.getCreateTime())) //指定Msg根据创建时间升序
                .map(msg -> msg.toMessage())//将每个Msg转化为Message
                .toList();

        return messageList;
    }

    @Override
    public void clear(String conversationId) {
        redisTemplate.delete(PREFIX+conversationId);
    }
}
