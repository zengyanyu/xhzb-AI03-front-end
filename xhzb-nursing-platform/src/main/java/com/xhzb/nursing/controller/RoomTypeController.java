package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.nursing.domain.RoomType;
import com.xhzb.nursing.service.IRoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 房型Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/elder/roomType")
@Tag(name = "房型相关接口")
public class RoomTypeController extends BaseController
{
    @Autowired
    private IRoomTypeService roomTypeService;

    /**
     * 查询房型列表
     */
    @PreAuthorize("@ss.hasPermi('elder:roomType:list')")
    @GetMapping("/list")
    @Operation(summary = "查询房型列表")
    public TableDataInfo list(RoomType roomType)
    {
        startPage();
        List<RoomType> list = roomTypeService.selectRoomTypeList(roomType);
        return getDataTable(list);
    }

    /**
     * 查询房型列表
     */
    @PreAuthorize("@ss.hasPermi('elder:roomType:list')")
    @GetMapping("/listAll")
    @Operation(summary = "查询所有房型列表")
    public AjaxResult list()
    {
        List<RoomType> list = roomTypeService.list();
        return success(list);
    }

    /**
     * 导出房型列表
     */
    @PreAuthorize("@ss.hasPermi('elder:roomType:export')")
    @Log(title = "房型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出房型列表")
    public void export(HttpServletResponse response, RoomType roomType)
    {
        List<RoomType> list = roomTypeService.selectRoomTypeList(roomType);
        ExcelUtil<RoomType> util = new ExcelUtil<RoomType>(RoomType.class);
        util.exportExcel(response, list, "房型数据");
    }

    /**
     * 获取房型详细信息
     */
    @PreAuthorize("@ss.hasPermi('elder:roomType:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取房型详细信息")
    public AjaxResult getInfo(@Schema(name = "房型ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id)
    {
        return success(roomTypeService.selectRoomTypeById(id));
    }

    /**
     * 新增房型
     */
    @PreAuthorize("@ss.hasPermi('elder:roomType:add')")
    @Log(title = "房型", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增房型")
    public AjaxResult add(@RequestBody RoomType roomType)
    {
        return toAjax(roomTypeService.insertRoomType(roomType));
    }

    /**
     * 修改房型
     */
    @Operation(summary = "修改房型")
    @PreAuthorize("@ss.hasPermi('elder:roomType:edit')")
    @Log(title = "房型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RoomType roomType)
    {
        return toAjax(roomTypeService.updateRoomType(roomType));
    }

    /**
     * 删除房型
     */
    @Operation(summary = "删除房型")
    @PreAuthorize("@ss.hasPermi('elder:roomType:remove')")
    @Log(title = "房型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "房型ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(roomTypeService.deleteRoomTypeByIds(ids));
    }
}
