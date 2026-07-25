package com.xhzb.framework.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.xhzb.common.constant.Constants;
import com.xhzb.common.exception.base.BaseException;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.common.utils.UserThreadLocal;
import com.xhzb.framework.web.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class MemberInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取token
        String token = request.getHeader("authorization");
        if(StringUtils.isEmpty(token)){
            throw new BaseException("认证失败");
        }
        //解析token
        Map<String, Object> claims =  tokenService.parseToken(token);
        if(ObjectUtil.isEmpty(claims)){
            throw new BaseException("认证失败");
        }
        //注意这里要和生成Token时设置的userId要保持一致
        // Long userId = MapUtil.get(claims, Constants.JWT_USERID, Long.class);
        Long userId =  ((Integer)claims.get(Constants.JWT_USERID)).longValue();
        if(ObjectUtil.isEmpty(userId)){
            throw new BaseException("认证失败");
        }
        //把数据存储到线程中
        UserThreadLocal.set(userId);
        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}