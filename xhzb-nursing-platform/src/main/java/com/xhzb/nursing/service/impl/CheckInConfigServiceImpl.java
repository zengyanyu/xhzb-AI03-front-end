package com.xhzb.nursing.service.impl;

import java.util.List;
import com.xhzb.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xhzb.nursing.mapper.CheckInConfigMapper;
import com.xhzb.nursing.domain.CheckInConfig;
import com.xhzb.nursing.service.ICheckInConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 入住配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Service
public class CheckInConfigServiceImpl extends ServiceImpl<CheckInConfigMapper, CheckInConfig> implements ICheckInConfigService
{
    @Autowired
    private CheckInConfigMapper checkInConfigMapper;

    /**
     * 查询入住配置
     * 
     * @param id 入住配置主键
     * @return 入住配置
     */
    @Override
    public CheckInConfig selectCheckInConfigById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询入住配置列表
     * 
     * @param checkInConfig 入住配置
     * @return 入住配置
     */
    @Override
    public List<CheckInConfig> selectCheckInConfigList(CheckInConfig checkInConfig)
    {
        return checkInConfigMapper.selectCheckInConfigList(checkInConfig);
    }

    /**
     * 新增入住配置
     * 
     * @param checkInConfig 入住配置
     * @return 结果
     */
    @Override
    public int insertCheckInConfig(CheckInConfig checkInConfig)
    {
        return save(checkInConfig)? 1 : 0;
    }

    /**
     * 修改入住配置
     * 
     * @param checkInConfig 入住配置
     * @return 结果
     */
    @Override
    public int updateCheckInConfig(CheckInConfig checkInConfig)
    {
        return updateById(checkInConfig)? 1 : 0;
    }

    /**
     * 批量删除入住配置
     * 
     * @param ids 需要删除的入住配置主键
     * @return 结果
     */
    @Override
    public int deleteCheckInConfigByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除入住配置信息
     * 
     * @param id 入住配置主键
     * @return 结果
     */
    @Override
    public int deleteCheckInConfigById(Long id)
    {
        return removeById(id)? 1 : 0;
    }
}
