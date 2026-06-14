package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.domain.R;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.nursing.domain.Room;
import com.xhzb.nursing.service.IRoomService;
import com.xhzb.nursing.domain.vo.RoomVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 房间Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/elder/room")
@Tag(name = "房间相关接口")
public class RoomController extends BaseController
{
    @Autowired
    private IRoomService roomService;


    @GetMapping("/getRoomsWithNurByFloorId/{floorId}")
    @Operation(summary = "获取所有房间（负责老人）")
    public R<List<RoomVo>> getRoomsWithNurByFloorId(@PathVariable("floorId") Long floorId) {
        List<RoomVo> list = roomService.getRoomsWithNurByFloorId(floorId);
        return R.ok(list);
    }

    @GetMapping("/getRoomsByFloorId/{floorId}")
    @Operation(summary = "获取所有房间（入住配置）")
    public R<List<RoomVo>> getRoomsByFloorId(@Schema(name = "楼层ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("floorId") Long floorId) {
        List<RoomVo> list = roomService.getRoomsByFloorId(floorId);
        return R.ok(list);
    }
    /**
     * 查询房间列表
     */
    @PreAuthorize("@ss.hasPermi('elder:room:list')")
    @GetMapping("/list")
    @Operation(summary = "查询房间列表")
    public TableDataInfo list(Room room)
    {
        startPage();
        List<Room> list = roomService.selectRoomList(room);
        return getDataTable(list);
    }

    /**
     * 获取房间详细信息
     */
    @PreAuthorize("@ss.hasPermi('elder:room:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取房间详细信息")
    public R<Room> getInfo(@Schema(name = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id)
    {
        return R.ok(roomService.selectRoomById(id));
    }

    /**
     * 新增房间
     */
    @PreAuthorize("@ss.hasPermi('elder:room:add')")
    @Log(title = "房间", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增房间")
    public AjaxResult add(@RequestBody Room room)
    {
        return toAjax(roomService.insertRoom(room));
    }

    /**
     * 修改房间
     */
    @Operation(summary = "修改房间")
    @PreAuthorize("@ss.hasPermi('elder:room:edit')")
    @Log(title = "房间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Room room)
    {
        return toAjax(roomService.updateRoom(room));
    }

    /**
     * 删除房间
     */
    @Operation(summary = "删除房间")
    @PreAuthorize("@ss.hasPermi('elder:room:remove')")
    @Log(title = "房间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("ids") Long[] ids)
    {
        return toAjax(roomService.deleteRoomByIds(ids));
    }

}
