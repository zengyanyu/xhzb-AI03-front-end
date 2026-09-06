package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.Floor;
import com.xhzb.nursing.domain.vo.FloorRoomBedVo;

import java.util.List;

/**
 * 楼层Service接口
 *
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IFloorService extends IService<Floor> {
    /**
     * 查询楼层
     *
     * @param id 楼层主键
     * @return 楼层
     */
    Floor selectFloorById(Long id);

    /**
     * 新增楼层
     *
     * @param floor 楼层
     * @return 结果
     */
    int insertFloor(Floor floor);

    /**
     * 修改楼层
     *
     * @param floor 楼层
     * @return 结果
     */
    int updateFloor(Floor floor);

    /**
     * 批量删除楼层
     *
     * @param ids 需要删除的楼层主键集合
     * @return 结果
     */
    int deleteFloorByIds(Long[] ids);

    /**
     * 删除楼层信息
     *
     * @param id 楼层主键
     * @return 结果
     */
    int deleteFloorById(Long id);

    /**
     * 查询负责老人楼层
     *
     * @return
     */
    List<Floor> selectAllByNur();

    /**
     * 根据床位状态查询楼层-房间-床位树形数据
     *
     * @param bedStatus 床位状态（0:未入住 1:已入住）
     * @return 楼层树形VO列表
     */
    List<FloorRoomBedVo> getRoomAndBedByBedStatus(Integer bedStatus);

    /**
     * 获取所有智能设备的楼层列表
     *
     * @return
     */
    List<Floor> getAllFloorsWithDevice();
}
