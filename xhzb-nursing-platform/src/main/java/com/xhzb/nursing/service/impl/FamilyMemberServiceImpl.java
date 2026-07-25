package com.xhzb.nursing.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.xhzb.common.constant.Constants;
import com.xhzb.common.core.domain.entity.SysUser;
import com.xhzb.common.core.domain.model.LoginUser;
import com.xhzb.common.exception.ServiceException;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.framework.web.service.TokenService;
import com.xhzb.nursing.domain.FamilyMember;
import com.xhzb.nursing.domain.dto.WechatLoginDto;
import com.xhzb.nursing.domain.vo.WechatLoginVo;
import com.xhzb.nursing.mapper.FamilyMemberMapper;
import com.xhzb.nursing.service.IFamilyMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 老人家属Service业务层处理
 *
 * @author ruoyi
 * @date 2026-07-24
 */
@Service
public class FamilyMemberServiceImpl extends ServiceImpl<FamilyMemberMapper, FamilyMember> implements IFamilyMemberService {

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Autowired
    private TokenService tokenService;

    /** 微信小程序appid */
    @Value("${wechat.appid}")
    private String appid;

    /** 微信小程序secret */
    @Value("${wechat.secret}")
    private String secret;

    /** 微信code2Session接口地址 */
    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /** 微信获取access_token接口地址 */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /** 微信获取手机号接口地址 */
    private static final String GET_PHONE_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";

    /** 随机名称用中文字符起始Unicode */
    private static final int CHINESE_CHAR_START = 0x4E00;

    /** 随机名称用中文字符结束Unicode */
    private static final int CHINESE_CHAR_END = 0x9FA5;

    /** 随机名称字数 */
    private static final int RANDOM_NAME_LENGTH = 4;

    /**
     * 查询老人家属
     *
     * @param id 老人家属主键
     * @return 老人家属
     */
    @Override
    public FamilyMember selectFamilyMemberById(Long id) {
        return getById(id);
    }

    /**
     * 查询老人家属列表
     *
     * @param familyMember 老人家属
     * @return 老人家属
     */
    @Override
    public List<FamilyMember> selectFamilyMemberList(FamilyMember familyMember) {
        return familyMemberMapper.selectFamilyMemberList(familyMember);
    }

    /**
     * 新增老人家属
     *
     * @param familyMember 老人家属
     * @return 结果
     */
    @Override
    public int insertFamilyMember(FamilyMember familyMember) {
        return save(familyMember) ? 1 : 0;
    }

    /**
     * 修改老人家属
     *
     * @param familyMember 老人家属
     * @return 结果
     */
    @Override
    public int updateFamilyMember(FamilyMember familyMember) {
        return updateById(familyMember) ? 1 : 0;
    }

    /**
     * 批量删除老人家属
     *
     * @param ids 需要删除的老人家属主键
     * @return 结果
     */
    @Override
    public int deleteFamilyMemberByIds(Long[] ids) {
        return removeByIds(List.of(ids)) ? 1 : 0;
    }

    /**
     * 删除老人家属信息
     *
     * @param id 老人家属主键
     * @return 结果
     */
    @Override
    public int deleteFamilyMemberById(Long id) {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 微信小程序登录
     * <p>
     * 业务逻辑：
     * 1. 根据前端传入的code调用微信code2Session接口获取openid
     * 2. 调用微信getPhoneNumber接口获取手机号
     * 3. 根据openid查询家属信息
     * 4. 如果家属为空，新增家属（随机四字昵称+手机号后四位）
     * 5. 如果家属不为空，判断手机号是否变更，变更则更新
     * 6. 生成JWT token返回
     *
     * @param dto 登录请求参数
     * @return 登录结果
     */
    @Override
    public WechatLoginVo login(WechatLoginDto dto) {
        // 1. 调用微信code2Session接口获取openid
        Map<String, Object> code2SessionParam = new HashMap<>();
        code2SessionParam.put("appid", appid);
        code2SessionParam.put("secret", secret);
        code2SessionParam.put("js_code", dto.getCode());
        code2SessionParam.put("grant_type", "authorization_code");

        String code2SessionResult = HttpUtil.get(CODE2SESSION_URL, code2SessionParam);
        JSONObject code2SessionJson = JSONUtil.parseObj(code2SessionResult);

        // 检查微信接口返回是否异常
        Integer errcode = code2SessionJson.getInt("errcode");
        if (errcode != null && errcode != 0) {
            throw new ServiceException("微信登录失败：" + code2SessionJson.getStr("errmsg", "未知错误"));
        }

        String openid = code2SessionJson.getStr("openid");
        if (StringUtils.isEmpty(openid)) {
            throw new ServiceException("获取微信OpenID失败");
        }

        // 2. 调用微信getPhoneNumber接口获取手机号
        String phone = getPhoneNumber(dto.getPhoneCode());

        // 3. 根据openid查询家属信息
        FamilyMember member = lambdaQuery().eq(FamilyMember::getOpenId, openid).one();

        // 4-5. 新增或更新家属信息
        if (member == null) {
            // 家属不存在，新增
            member = new FamilyMember();
            member.setOpenId(openid);
            member.setPhone(phone);
            // 随机起四个字的名字 + 手机号后四位
            String nickName = generateRandomName() + phone.substring(phone.length() - 4);
            member.setName(nickName);
            member.setCreateTime(new Date());
            save(member);
        } else {
            // 家属已存在，判断手机号是否变化
            if (!phone.equals(member.getPhone())) {
                member.setPhone(phone);
                member.setUpdateTime(new Date());
                updateById(member);
            }
        }

        // 6. 使用TokenService生成token
        String token = createLoginToken(member);

        WechatLoginVo vo = new WechatLoginVo();
        vo.setToken(token);
        vo.setNickName(member.getName());
        return vo;
    }

    /**
     * 调用微信getPhoneNumber接口获取手机号
     *
     * @param phoneCode 前端获取手机号的临时code
     * @return 手机号
     */
    private String getPhoneNumber(String phoneCode) {
        // 2.1 获取access_token
        Map<String, Object> tokenParam = new HashMap<>();
        tokenParam.put("grant_type", "client_credential");
        tokenParam.put("appid", appid);
        tokenParam.put("secret", secret);

        String tokenResult = HttpUtil.get(ACCESS_TOKEN_URL, tokenParam);
        JSONObject tokenJson = JSONUtil.parseObj(tokenResult);

        String accessToken = tokenJson.getStr("access_token");
        if (StringUtils.isEmpty(accessToken)) {
            throw new ServiceException("获取微信access_token失败：" + tokenJson.getStr("errmsg", "未知错误"));
        }

        // 2.2 使用access_token获取手机号
        String phoneUrl = GET_PHONE_URL + "?access_token=" + accessToken;
        Map<String, String> phoneBody = new HashMap<>();
        phoneBody.put("code", phoneCode);

        String phoneResult = HttpUtil.post(phoneUrl, JSONUtil.toJsonStr(phoneBody));
        JSONObject phoneJson = JSONUtil.parseObj(phoneResult);

        Integer phoneErrcode = phoneJson.getInt("errcode");
        if (phoneErrcode != null && phoneErrcode != 0) {
            throw new ServiceException("获取微信手机号失败：" + phoneJson.getStr("errmsg", "未知错误"));
        }

        // phone_info.phoneNumber 为不带区号的手机号
        JSONObject phoneInfo = phoneJson.getJSONObject("phone_info");
        if (phoneInfo == null) {
            throw new ServiceException("获取微信手机号失败：phone_info为空");
        }

        String phoneNumber = phoneInfo.getStr("phoneNumber");
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new ServiceException("获取微信手机号失败：手机号为空");
        }

        return phoneNumber;
    }

    /**
     * 生成随机四字中文昵称
     *
     * @return 随机四个中文字符
     */
    private String generateRandomName() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(RANDOM_NAME_LENGTH);
        for (int i = 0; i < RANDOM_NAME_LENGTH; i++) {
            int codePoint = CHINESE_CHAR_START + random.nextInt(CHINESE_CHAR_END - CHINESE_CHAR_START + 1);
            sb.append((char) codePoint);
        }
        return sb.toString();
    }

    /**
     * 为家属生成JWT登录token
     *
     * @param member 家属信息
     * @return JWT token字符串
     */
    private String createLoginToken(FamilyMember member) {
        // 构建一个最小SysUser用于LoginUser
        SysUser sysUser = new SysUser();
        sysUser.setUserId(member.getId());
        sysUser.setUserName(member.getName());

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(member.getId());
        loginUser.setUser(sysUser);

        return tokenService.createToken(loginUser);
    }
}
