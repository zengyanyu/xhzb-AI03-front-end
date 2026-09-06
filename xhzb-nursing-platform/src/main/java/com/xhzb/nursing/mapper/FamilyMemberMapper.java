package com.xhzb.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.FamilyMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 老人家属Mapper接口
 *
 * @author ruoyi
 * @date 2026-07-24
 */
@Mapper
public interface FamilyMemberMapper extends BaseMapper<FamilyMember> {
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
     * 删除老人家属
     *
     * @param id 老人家属主键
     * @return 结果
     */
    int deleteFamilyMemberById(Long id);

    /**
     * 批量删除老人家属
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteFamilyMemberByIds(Long[] ids);
}
