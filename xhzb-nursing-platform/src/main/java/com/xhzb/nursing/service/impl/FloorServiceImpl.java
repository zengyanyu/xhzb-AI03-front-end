package com.xhzb.nursing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.nursing.domain.Floor;
import com.xhzb.nursing.domain.vo.BedVo;
import com.xhzb.nursing.domain.vo.FloorRoomBedVo;
import com.xhzb.nursing.domain.vo.FloorVo;
import com.xhzb.nursing.domain.vo.RoomVo;
import com.xhzb.nursing.mapper.FloorMapper;
import com.xhzb.nursing.mapper.RoomMapper;
import com.xhzb.nursing.service.IFloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 楼层Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements IFloorService
{
    @Autowired
    private FloorMapper floorMapper;

    @Autowired
    private RoomMapper roomMapper;

    /**
     * 查询楼层
     * 
     * @param id 楼层主键
     * @return 楼层
     */
    @Override
    public Floor selectFloorById(Long id)
    {
        return getById(id);
    }

    /**
     * 新增楼层
     * 
     * @param floor 楼层
     * @return 结果
     */
    @Override
    public int insertFloor(Floor floor)
    {
        return save(floor) ? 1 : 0;
    }

    /**
     * 修改楼层
     * 
     * @param floor 楼层
     * @return 结果
     */
    @Override
    public int updateFloor(Floor floor)
    {
        return updateById(floor) ? 1 : 0;
    }

    /**
     * 批量删除楼层
     * 
     * @param ids 需要删除的楼层主键
     * @return 结果
     */
    @Override
    public int deleteFloorByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除楼层信息
     * 
     * @param id 楼层主键
     * @return 结果
     */
    @Override
    public int deleteFloorById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }


    /**
     * 查询负责老人楼层
     * @return
     */
    @Override
    public List<Floor> selectAllByNur() {
        return floorMapper.selectAllByNur();
    }

    /**
     * 根据床位状态查询楼层-房间-床位树形数据
     *
     * @param bedStatus 床位状态（0:未入住 1:已入住）
     * @return 楼层树形VO列表
     */
    @Override
    public List<FloorRoomBedVo> getRoomAndBedByBedStatus(Integer bedStatus) {
        // 1. 查询有符合条件床位的楼层
        List<Floor> floors = floorMapper.selectFloorsWithRoomAndBedByStatus(bedStatus);

        // 2. 组装树形结构
        List<FloorRoomBedVo> result = new ArrayList<>();
        for (Floor floor : floors) {
            FloorRoomBedVo floorVo = new FloorRoomBedVo();
            floorVo.setValue(String.valueOf(floor.getId()));
            floorVo.setLabel(floor.getName());

            // 查询该楼层下符合条件的房间
            List<RoomVo> rooms = roomMapper.selectByFloorIdAndBedStatus(floor.getId(), bedStatus);
            List<FloorRoomBedVo> roomChildren = new ArrayList<>();
            for (RoomVo room : rooms) {
                FloorRoomBedVo roomVo = new FloorRoomBedVo();
                roomVo.setValue(String.valueOf(room.getId()));
                roomVo.setLabel(room.getCode());

                // 组装床位子节点
                List<FloorRoomBedVo> bedChildren = new ArrayList<>();
                if (room.getBedVoList() != null) {
                    for (BedVo bed : room.getBedVoList()) {
                        FloorRoomBedVo bedVo = new FloorRoomBedVo();
                        bedVo.setValue(String.valueOf(bed.getId()));
                        bedVo.setLabel(bed.getBedNumber());
                        bedChildren.add(bedVo);
                    }
                }
                roomVo.setChildren(bedChildren);
                roomChildren.add(roomVo);
            }
            floorVo.setChildren(roomChildren);
            result.add(floorVo);
        }

        return result;
    }
}
