package com.xhzb.nursing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.nursing.domain.Elder;
import com.xhzb.nursing.mapper.ElderMapper;
import com.xhzb.nursing.service.IElderService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 老人Service业务层处理
 *
 * @author ruoyi
 * @date 2026-07-10
 */
@Service
public class ElderServiceImpl extends ServiceImpl<ElderMapper, Elder> implements IElderService {
    /**
     * 查询老人
     *
     * @param id 老人主键
     * @return 老人
     */
    @Override
    public Elder selectElderById(Long id) {
        return getById(id);
    }

    /**
     * 查询老人列表
     *
     * @param elder 老人
     * @return 老人
     */
    @Override
    public List<Elder> selectElderList(Elder elder) {
        return this.baseMapper.selectElderList(elder);
    }

    /**
     * 新增老人
     *
     * @param elder 老人
     * @return 结果
     */
    @Override
    public int insertElder(Elder elder) {
        return save(elder) ? 1 : 0;
    }

    /**
     * 修改老人
     *
     * @param elder 老人
     * @return 结果
     */
    @Override
    public int updateElder(Elder elder) {
        return updateById(elder) ? 1 : 0;
    }

    /**
     * 批量删除老人
     *
     * @param ids 需要删除的老人主键
     * @return 结果
     */
    @Override
    public int deleteElderByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除老人信息
     *
     * @param id 老人主键
     * @return 结果
     */
    @Override
    public int deleteElderById(Long id) {
        return removeById(id) ? 1 : 0;
    }
}
