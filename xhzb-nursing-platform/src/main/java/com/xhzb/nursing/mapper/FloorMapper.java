package com.xhzb.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.Floor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 楼层Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface FloorMapper extends BaseMapper<Floor> {
    /**
     * 查询楼层
     *
     * @param id 楼层主键
     * @return 楼层
     */
    Floor selectFloorById(Long id);

    /**
     * 查询楼层列表
     *
     * @param floor 楼层
     * @return 楼层集合
     */
    List<Floor> selectFloorList(Floor floor);

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
     * 删除楼层
     *
     * @param id 楼层主键
     * @return 结果
     */
    int deleteFloorById(Long id);

    /**
     * 批量删除楼层
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteFloorByIds(Long[] ids);

    List<Floor> selectAllByNur();

    /**
     * 根据床位状态查询楼层-房间-床位树形数据
     *
     * @param bedStatus 床位状态（0:未入住 1:已入住）
     * @return 楼层列表（用于树形展示）
     */
    List<Floor> selectFloorsWithRoomAndBedByStatus(Integer bedStatus);

    /**
     * 查询拥有智能设备楼层列表
     *
     * @return
     */
    List<Floor> getAllFloorsWithDevice();
}
