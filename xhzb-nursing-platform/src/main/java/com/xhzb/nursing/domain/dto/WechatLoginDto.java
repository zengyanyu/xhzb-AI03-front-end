package com.xhzb.nursing.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信小程序登录请求DTO
 *
 * @author songyu
 */
@Data
public class WechatLoginDto {

    /** 临时登录凭证code */
    @NotBlank(message = "微信登录code不能为空")
    private String code;

    /** 微信用户昵称 */
    private String nickName;

    /** 获取手机号的临时code */
    @NotBlank(message = "获取手机号的code不能为空")
    private String phoneCode;
}
