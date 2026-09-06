package com.xhzb.nursing.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.nursing.domain.DeviceData;
import com.xhzb.nursing.domain.Room;
import com.xhzb.nursing.mapper.RoomMapper;
import com.xhzb.nursing.service.IRoomService;
import com.xhzb.nursing.domain.vo.BedVo;
import com.xhzb.nursing.domain.vo.DeviceVo;
import com.xhzb.nursing.domain.vo.RoomVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 房间Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Slf4j
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {
    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * Redis Hash大key，存储设备最新上报数据
     */
    private static final String IOT_DEVICE_LATEST_DATA_KEY = "iot:device_latest_data";

    /**
     * 查询房间
     *
     * @param id 房间主键
     * @return 房间
     */
    @Override
    public Room selectRoomById(Long id) {
        return getById(id);
    }

    /**
     * 查询房间列表
     *
     * @param room 房间
     * @return 房间
     */
    @Override
    public List<Room> selectRoomList(Room room) {
        return roomMapper.selectRoomList(room);
    }

    /**
     * 新增房间
     *
     * @param room 房间
     * @return 结果
     */
    @Override
    public int insertRoom(Room room) {
        return save(room) ? 1 : 0;
    }

    /**
     * 修改房间
     *
     * @param room 房间
     * @return 结果
     */
    @Override
    public int updateRoom(Room room) {
        return updateById(room) ? 1 : 0;
    }

    /**
     * 批量删除房间
     *
     * @param ids 需要删除的房间主键
     * @return 结果
     */
    @Override
    public int deleteRoomByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 根据楼层 id 获取房间视图对象列表
     *
     * @param floorId
     * @return
     */
    @Override
    public List<RoomVo> getRoomsByFloorId(Long floorId) {
        return roomMapper.selectByFloorId(floorId);
    }


    /**
     * 获取所有房间（负责老人）
     *
     * @param floorId
     * @return
     */
    @Override
    public List<RoomVo> getRoomsWithNurByFloorId(Long floorId) {
        return roomMapper.selectByFloorIdWithNur(floorId);
    }

    /**
     * 查询房间详情（楼层、房间、价格）
     *
     * @param id 房间ID
     * @return 房间VO
     */
    @Override
    public RoomVo getRoomDetailById(Long id) {
        return roomMapper.selectRoomDetailById(id);
    }

    /**
     * 根据楼层ID查询房间及其智能设备、床位及入住老人、设备最新数据
     * <p>
     * 流程：1) 查询房间+房间级设备 2) 查询床位+床位级设备
     * 3) 组装 4) 从Redis获取设备数据 5) 过滤无设备的房间
     *
     * @param floorId 楼层ID
     * @return 房间VO列表（仅包含有设备的房间）
     */
    @Override
    public List<RoomVo> getRoomsWithDeviceByFloorId(Long floorId) {
        //1.查询房间及房间级设备
        List<RoomVo> rooms = roomMapper.selectRoomsWithDevicesByFloorId(floorId);
        if (rooms == null || rooms.isEmpty()) {
            return List.of();
        }

        //2.收集所有房间ID
        List<Long> roomIds = rooms.stream()
                .map(RoomVo::getId)
                .collect(Collectors.toList());

        //3.查询床位及床位级设备
        List<BedVo> allBeds = roomMapper.selectBedsWithDevicesByRoomIds(roomIds);

        //4.按房间ID分组床位
        Map<Long, List<BedVo>> bedsByRoomId = new LinkedHashMap<>();
        if (allBeds != null && !allBeds.isEmpty()) {
            for (BedVo bed : allBeds) {
                //清理空设备（LEFT JOIN无匹配时的产物）
                if (bed.getDeviceVos() != null) {
                    bed.setDeviceVos(bed.getDeviceVos().stream()
                            .filter(d -> d.getId() != null)
                            .collect(Collectors.toList()));
                }
                if (bed.getRoomId() != null) {
                    bedsByRoomId.computeIfAbsent(bed.getRoomId(), k -> new ArrayList<>()).add(bed);
                }
            }
        }

        //5.将床位分配到对应房间，收集有设备的房间ID
        Set<Long> roomIdsWithDevice = new HashSet<>();
        for (RoomVo room : rooms) {
            //清理房间级空设备
            if (room.getDeviceVos() != null) {
                room.setDeviceVos(room.getDeviceVos().stream()
                        .filter(d -> d.getId() != null)
                        .collect(Collectors.toList()));
                if (!room.getDeviceVos().isEmpty()) {
                    roomIdsWithDevice.add(room.getId());
                }
            } else {
                room.setDeviceVos(List.of());
            }

            //分配床位
            List<BedVo> roomBeds = bedsByRoomId.get(room.getId());
            if (roomBeds != null) {
                room.setBedVoList(roomBeds);
                //检查是否有床位绑定了设备
                for (BedVo bed : roomBeds) {
                    if (bed.getDeviceVos() != null && !bed.getDeviceVos().isEmpty()) {
                        roomIdsWithDevice.add(room.getId());
                    }
                }
            } else {
                room.setBedVoList(List.of());
            }
        }

        //6.过滤：没有设备的房间不显示
        List<RoomVo> filteredRooms = rooms.stream()
                .filter(r -> roomIdsWithDevice.contains(r.getId()))
                .collect(Collectors.toList());

        //7.从Redis获取设备数据并填充到deviceDataVos
        for (RoomVo room : filteredRooms) {
            fillDeviceData(room.getDeviceVos());
            if (room.getBedVoList() != null) {
                for (BedVo bed : room.getBedVoList()) {
                    fillDeviceData(bed.getDeviceVos());
                }
            }
        }

        return filteredRooms;
    }

    /**
     * 从Redis获取设备最新上报数据，填充到DeviceVo.deviceDataVos中
     *
     * @param devices 设备VO列表
     */
    private void fillDeviceData(List<DeviceVo> devices) {
        if (devices == null || devices.isEmpty()) {
            return;
        }
        for (DeviceVo device : devices) {
            if (device.getIotId() == null) {
                device.setDeviceDataVos(List.of());
                continue;
            }
            try {
                Object jsonObj = stringRedisTemplate.opsForHash()
                        .get(IOT_DEVICE_LATEST_DATA_KEY, device.getIotId());
                if (jsonObj != null) {
                    List<DeviceData> dataList = JSONUtil.toList(
                            JSONUtil.parseArray(jsonObj.toString()), DeviceData.class);
                    device.setDeviceDataVos(dataList != null ? dataList : List.of());
                } else {
                    device.setDeviceDataVos(List.of());
                }
            } catch (Exception e) {
                log.warn("从Redis获取设备数据失败，iotId={}", device.getIotId(), e);
                device.setDeviceDataVos(List.of());
            }
        }
    }

}
