package com.xhzb.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.Room;
import com.xhzb.nursing.domain.vo.BedVo;
import com.xhzb.nursing.domain.vo.RoomVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房间Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
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
     * 删除房间
     *
     * @param id 房间主键
     * @return 结果
     */
    int deleteRoomById(Long id);

    /**
     * 批量删除房间
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRoomByIds(Long[] ids);

    List<RoomVo> selectByFloorId(Long floorId);

    List<RoomVo> selectByFloorIdWithNur(Long floorId);

    /**
     * 查询房间详情（楼层、房间、价格）
     *
     * @param id 房间ID
     * @return 房间VO
     */
    RoomVo selectRoomDetailById(Long id);

    /**
     * 根据楼层ID和床位状态查询房间列表（含床位）
     *
     * @param floorId   楼层ID
     * @param bedStatus 床位状态
     * @return 房间VO列表
     */
    List<RoomVo> selectByFloorIdAndBedStatus(@Param("floorId") Long floorId, @Param("bedStatus") Integer bedStatus);

    /**
     * 查询指定楼层下所有有设备的房间及其房间级设备
     *
     * @param floorId 楼层ID
     * @return 房间VO列表（含房间绑定的设备）
     */
    List<RoomVo> selectRoomsWithDevicesByFloorId(Long floorId);

    /**
     * 查询指定房间下的床位、入住老人、床位级设备
     *
     * @param roomIds 房间ID集合
     * @return 床位VO列表（含床位绑定的设备）
     */
    List<BedVo> selectBedsWithDevicesByRoomIds(@Param("roomIds") List<Long> roomIds);
}
