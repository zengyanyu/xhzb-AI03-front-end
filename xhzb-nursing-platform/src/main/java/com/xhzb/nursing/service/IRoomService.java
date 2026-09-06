package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.Room;
import com.xhzb.nursing.domain.vo.RoomVo;

import java.util.List;

/**
 * 房间Service接口
 *
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IRoomService extends IService<Room> {
    /**
     * 查询房间
     *
     * @param id 房间主键
     * @return 房间
     */
    Room selectRoomById(Long id);

    /**
     * 查询房间列表
     *
     * @param room 房间
     * @return 房间集合
     */
    List<Room> selectRoomList(Room room);

    /**
     * 新增房间
     *
     * @param room 房间
     * @return 结果
     */
    int insertRoom(Room room);

    /**
     * 修改房间
     *
     * @param room 房间
     * @return 结果
     */
    int updateRoom(Room room);

    /**
     * 批量删除房间
     *
     * @param ids 需要删除的房间主键集合
     * @return 结果
     */
    int deleteRoomByIds(Long[] ids);

    /**
     * 根据楼层 id 获取房间视图对象列表
     *
     * @param floorId
     * @return
     */
    List<RoomVo> getRoomsByFloorId(Long floorId);

    /**
     * 获取所有房间（负责老人）
     *
     * @param floorId
     * @return
     */
    List<RoomVo> getRoomsWithNurByFloorId(Long floorId);

    /**
     * 查询房间详情（楼层、房间、价格）
     *
     * @param id 房间ID
     * @return 房间VO
     */
    RoomVo getRoomDetailById(Long id);

    /**
     * 根据楼层ID查询房间及其智能设备、床位及入住老人、设备最新数据
     *
     * @param floorId 楼层ID
     * @return 房间VO列表（仅包含有设备的房间）
     */
    List<RoomVo> getRoomsWithDeviceByFloorId(Long floorId);
}
