package com.xhzb.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 预约信息Mapper接口
 *
 * @author ruoyi
 * @date 2026-08-21
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
    /**
     * 查询预约信息
     *
     * @param id 预约信息主键
     * @return 预约信息
     */
    Reservation selectReservationById(Long id);

    /**
     * 查询预约信息列表
     *
     * @param reservation 预约信息
     * @return 预约信息集合
     */
    List<Reservation> selectReservationList(Reservation reservation);

    /**
     * 新增预约信息
     *
     * @param reservation 预约信息
     * @return 结果
     */
    int insertReservation(Reservation reservation);

    /**
     * 修改预约信息
     *
     * @param reservation 预约信息
     * @return 结果
     */
    int updateReservation(Reservation reservation);

    /**
     * 删除预约信息
     *
     * @param id 预约信息主键
     * @return 结果
     */
    int deleteReservationById(Long id);

    /**
     * 批量删除预约信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteReservationByIds(Long[] ids);

    @Select("select * from reservation where date_format(time,'%Y-%m-%d') = #{now}")
    List<Reservation> selectByNow(LocalDate now);
}
