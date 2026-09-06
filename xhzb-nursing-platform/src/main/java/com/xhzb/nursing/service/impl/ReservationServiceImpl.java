package com.xhzb.nursing.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.nursing.domain.Reservation;
import com.xhzb.nursing.domain.vo.ElderVisitInfoVO;
import com.xhzb.nursing.mapper.ReservationMapper;
import com.xhzb.nursing.service.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 预约信息Service业务层处理
 *
 * @author ruoyi
 * @date 2026-08-21
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    /**
     * 查询预约信息
     *
     * @param id 预约信息主键
     * @return 预约信息
     */
    @Override
    public Reservation selectReservationById(Long id) {
        return getById(id);
    }

    /**
     * 查询预约信息列表
     *
     * @param reservation 预约信息
     * @return 预约信息
     */
    @Override
    public List<Reservation> selectReservationList(Reservation reservation) {
        return reservationMapper.selectReservationList(reservation);
    }

    /**
     * 新增预约信息
     *
     * @param reservation 预约信息
     * @return 结果
     */
    @Override
    public int insertReservation(Reservation reservation) {
        return save(reservation) ? 1 : 0;
    }

    /**
     * 修改预约信息
     *
     * @param reservation 预约信息
     * @return 结果
     */
    @Override
    public int updateReservation(Reservation reservation) {
        return updateById(reservation) ? 1 : 0;
    }

    /**
     * 批量删除预约信息
     *
     * @param ids 需要删除的预约信息主键
     * @return 结果
     */
    @Override
    public int deleteReservationByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除预约信息信息
     *
     * @param id 预约信息主键
     * @return 结果
     */
    @Override
    public int deleteReservationById(Long id) {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 根据指定日期查询当前预约信息。
     *
     * @param dateTime
     * @return
     */
    @Override
    public String getReservationByDay(LocalDate dateTime) {

        if (ObjectUtil.isEmpty(dateTime)) {
            dateTime = LocalDate.now();
        }

        List<Reservation> list = reservationMapper.selectByNow(dateTime);
        if (null != list && !list.isEmpty()) {

            List<ElderVisitInfoVO> resultList = new ArrayList<>();
            for (Reservation reservation : list) {
                ElderVisitInfoVO vo = new ElderVisitInfoVO();
                String dateStr = reservation.getTime().toString();
                vo.setVisitTime(dateStr);
                vo.setVisitor(reservation.getName());
                vo.setName(reservation.getVisitor());
                resultList.add(vo);
            }
            return JSONUtil.toJsonStr(resultList);
        }

        return null;
    }
}
