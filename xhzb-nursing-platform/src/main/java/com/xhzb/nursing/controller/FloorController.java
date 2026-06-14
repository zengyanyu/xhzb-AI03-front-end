package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.domain.R;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.nursing.domain.Floor;
import com.xhzb.nursing.service.IFloorService;
import com.xhzb.nursing.domain.vo.FloorVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 楼层Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/elder/floor")
@Tag(name = "楼层相关接口")
public class FloorController extends BaseController
{
    @Autowired
    private IFloorService floorService;

    /**
     * 查询楼层列表
     */
    @PreAuthorize("@ss.hasPermi('elder:floor:list')")
    @GetMapping("/list")
    @Operation(summary = "查询所有楼层列表")
    public R<List<Floor>> list()
    {
        List<Floor> list = floorService.list();
        return R.ok(list);
    }

    /**
     * 获取楼层详细信息
     */
    @PreAuthorize("@ss.hasPermi('elder:floor:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取楼层详细信息")
    public R<Floor> getInfo(@Schema(name = "楼层ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id)
    {
        return R.ok(floorService.selectFloorById(id));
    }

    /**
     * 新增楼层
     */
    @PreAuthorize("@ss.hasPermi('elder:floor:add')")
    @Log(title = "楼层", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增楼层")
    public AjaxResult add(@RequestBody Floor floor)
    {
        return toAjax(floorService.insertFloor(floor));
    }

    /**
     * 修改楼层
     */
    @Operation(summary = "修改楼层")
    @PreAuthorize("@ss.hasPermi('elder:floor:edit')")
    @Log(title = "楼层", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Floor floor)
    {
        return toAjax(floorService.updateFloor(floor));
    }

    /**
     * 删除楼层
     */
    @Operation(summary = "删除楼层")
    @PreAuthorize("@ss.hasPermi('elder:floor:remove')")
    @Log(title = "楼层", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "楼层ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(floorService.deleteFloorByIds(ids));
    }

    @GetMapping("/getAllFloorsWithNur")
    @Operation(summary = "获取所有楼层 (负责老人)", description = "无需参数，获取所有楼层，返回楼层信息列表")
    public R<List<Floor>> getAllFloorsWithNur() {
        List<Floor> list = floorService.selectAllByNur();
        return R.ok(list);
    }
}
