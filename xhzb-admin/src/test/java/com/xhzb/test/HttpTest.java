package com.xhzb.test;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpTest {


    @Test
    public void testGet(){
        String result = HttpUtil.get("https://www.baidu.com");
        System.out.println(result);
    }

    @Test
    public void testGetParam(){
        // 访问地址
        String url = "http://localhost:8080/nursing/project/list";
        //参数构建
        Map<String,Object> param = new HashMap<>();
        param.put("pageNum",1);
        param.put("pageSize",10);


        //分页查询护理项目
        String result = HttpUtil.get(url, param);
        System.out.println(result);

    }

    @Test
    public void testGetByRequest(){
        // 访问地址
        String url = "http://localhost:8080/nursing/project/list";
        //参数构建
        Map<String,Object> param = new HashMap<>();
        param.put("pageNum",1);
        param.put("pageSize",10);


        //分页查询护理项目
        HttpResponse response = HttpUtil.createRequest(Method.GET, url)
                .header("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImxvZ2luX3VzZXJfa2V5IjoiYmRmZTEyNmEtNTRmOC00MDlkLWE5ZmUtMTEzMjMwYzc0YWI4In0.MVCqJ7dWXEOULTH7g5NkZxQzf0K6p7-BC831I3lXYhNXwXm5xlH-mfurHNkjA4m4Q3ovkV_f9IodJgrp4jN9RQ")
                .form(param).execute();
        if(response.isOk()){
            System.out.println(response.body());
        }
    }

    @Test
    public void testCreatePost(){

        // 访问地址
        String url = "http://localhost:8080/nursing/project";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "护理项目测试2");
        paramMap.put("orderNo", 1);
        paramMap.put("unit", "次");
        paramMap.put("price", 10.00);
        paramMap.put("image", "https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/ae7cf766-fb7b-49ff-a73c-c86c25f280e1.png");
        paramMap.put("nursingRequirement", "无特殊要求");
        paramMap.put("status", 1);

        //发送请求
        HttpResponse response = HttpUtil.createPost(url)
                .header("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImxvZ2luX3VzZXJfa2V5IjoiYmRmZTEyNmEtNTRmOC00MDlkLWE5ZmUtMTEzMjMwYzc0YWI4In0.MVCqJ7dWXEOULTH7g5NkZxQzf0K6p7-BC831I3lXYhNXwXm5xlH-mfurHNkjA4m4Q3ovkV_f9IodJgrp4jN9RQ")
                .body(JSONUtil.toJsonStr(paramMap))
                .execute();
        if(response.isOk()){
            System.out.println(response.body());
        }

    }

    
}