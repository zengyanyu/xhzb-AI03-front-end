package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.FamilyMember;
import com.xhzb.nursing.domain.dto.WechatLoginDto;
import com.xhzb.nursing.domain.vo.WechatLoginVo;

import java.util.List;

/**
 * 老人家属Service接口
 *
 * @author ruoyi
 * @date 2026-07-24
 */
public interface IFamilyMemberService extends IService<FamilyMember> {
    /**
     * 查询老人家属
     *
     * @param id 老人家属主键
     * @return 老人家属
     */
    FamilyMember selectFamilyMemberById(Long id);

    /**
     * 查询老人家属列表
     *
     * @param familyMember 老人家属
     * @return 老人家属集合
     */
    List<FamilyMember> selectFamilyMemberList(FamilyMember familyMember);

    /**
     * 新增老人家属
     *
     * @param familyMember 老人家属
     * @return 结果
     */
    int insertFamilyMember(FamilyMember familyMember);

    /**
     * 修改老人家属
     *
     * @param familyMember 老人家属
     * @return 结果
     */
    int updateFamilyMember(FamilyMember familyMember);

    /**
     * 批量删除老人家属
     *
     * @param ids 需要删除的老人家属主键集合
     * @return 结果
     */
    int deleteFamilyMemberByIds(Long[] ids);

    /**
     * 删除老人家属信息
     *
     * @param id 老人家属主键
     * @return 结果
     */
    int deleteFamilyMemberById(Long id);

    /**
     * 微信小程序登录
     *
     * @param dto 登录请求参数
     * @return 登录结果（token和昵称）
     */
    WechatLoginVo login(WechatLoginDto dto);
}
