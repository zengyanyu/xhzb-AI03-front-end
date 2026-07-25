package com.xhzb.nursing.controller.member;

import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.nursing.domain.dto.WechatLoginDto;
import com.xhzb.nursing.domain.vo.WechatLoginVo;
import com.xhzb.nursing.service.IFamilyMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序用户Controller
 * 处理微信小程序登录相关接口
 *
 * @author songyu
 * @date 2026-07-25
 */
@RestController
@RequestMapping("/member/user")
@Tag(name = "小程序用户接口")
public class MemberUserController extends BaseController {

    @Autowired
    private IFamilyMemberService familyMemberService;

    /**
     * 微信小程序登录
     *
     * @param dto 登录请求参数（code、nickName、phoneCode）
     * @return 登录结果（token、nickName）
     */
    @PostMapping("/login")
    @Operation(summary = "微信小程序登录")
    public AjaxResult login(@Validated @RequestBody WechatLoginDto dto) {
        WechatLoginVo vo = familyMemberService.login(dto);
        return success(vo);
    }
}
