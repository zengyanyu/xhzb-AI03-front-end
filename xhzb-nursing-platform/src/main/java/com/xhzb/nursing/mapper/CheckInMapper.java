package com.xhzb.nursing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.CheckIn;

/**
 * 入住Mapper接口
 *
 * @author ruoyi
 * @date 2026-07-10
 */
@Mapper
public interface CheckInMapper extends BaseMapper<CheckIn> {
    /**
     * 查询入住
     *
     * @param id 入住主键
     * @return 入住
     */
    CheckIn selectCheckInById(Long id);

    /**
     * 查询入住列表
     *
     * @param checkIn 入住
     * @return 入住集合
     */
    List<CheckIn> selectCheckInList(CheckIn checkIn);

    /**
     * 新增入住
     *
     * @param checkIn 入住
     * @return 结果
     */
    int insertCheckIn(CheckIn checkIn);

    /**
     * 修改入住
     *
     * @param checkIn 入住
     * @return 结果
     */
    int updateCheckIn(CheckIn checkIn);

    /**
     * 删除入住
     *
     * @param id 入住主键
     * @return 结果
     */
    int deleteCheckInById(Long id);

    /**
     * 批量删除入住
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCheckInByIds(Long[] ids);
}
