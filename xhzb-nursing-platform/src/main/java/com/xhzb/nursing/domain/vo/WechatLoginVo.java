package com.xhzb.nursing.domain.vo;

import lombok.Data;

/**
 * 微信小程序登录响应VO
 *
 * @author songyu
 */
@Data
public class WechatLoginVo {

    /** 认证token */
    private String token;

    /** 用户昵称 */
    private String nickName;
}
