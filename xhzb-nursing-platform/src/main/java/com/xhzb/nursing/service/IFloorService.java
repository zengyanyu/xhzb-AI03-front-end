package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.Floor;
import com.xhzb.nursing.domain.vo.FloorVo;

import java.util.List;

/**
 * 楼层Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IFloorService extends IService<Floor>
{
    /**
     * 查询楼层
     * 
     * @param id 楼层主键
     * @return 楼层
     */
    public Floor selectFloorById(Long id);

    /**
     * 新增楼层
     * 
     * @param floor 楼层
     * @return 结果
     */
    public int insertFloor(Floor floor);

    /**
     * 修改楼层
     * 
     * @param floor 楼层
     * @return 结果
     */
    public int updateFloor(Floor floor);

    /**
     * 批量删除楼层
     * 
     * @param ids 需要删除的楼层主键集合
     * @return 结果
     */
    public int deleteFloorByIds(Long[] ids);

    /**
     * 删除楼层信息
     * 
     * @param id 楼层主键
     * @return 结果
     */
    public int deleteFloorById(Long id);


    /**
     * 查询负责老人楼层
     * @return
     */
    List<Floor> selectAllByNur();
}
