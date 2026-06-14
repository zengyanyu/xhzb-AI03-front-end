package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.domain.R;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.nursing.domain.Bed;
import com.xhzb.nursing.service.IBedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 床位Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/elder/bed")
@Tag(name = "床位相关接口")
public class BedController extends BaseController
{
    @Autowired
    private IBedService bedService;

    /**
     * 查询床位列表
     */
    @PreAuthorize("@ss.hasPermi('elder:bed:list')")
    @GetMapping("/list")
    @Operation(summary = "查询床位列表")
    public TableDataInfo list(Bed bed)
    {
        startPage();
        List<Bed> list = bedService.selectBedList(bed);
        return getDataTable(list);
    }

    /**
     * 获取床位详细信息
     */
    @PreAuthorize("@ss.hasPermi('elder:bed:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取床位详细信息")
    public R<Bed> getInfo(@Schema(name = "床位ID", requiredMode = Schema.RequiredMode.REQUIRED)  @PathVariable("id") Long id)
    {
        return R.ok(bedService.selectBedById(id));
    }

    /**
     * 新增床位
     */
    @PreAuthorize("@ss.hasPermi('elder:bed:add')")
    @Log(title = "床位", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增床位")
    public AjaxResult add(@RequestBody Bed bed)
    {
        return toAjax(bedService.insertBed(bed));
    }

    /**
     * 修改床位
     */
    @Operation(summary = "修改床位")
    @PreAuthorize("@ss.hasPermi('elder:bed:edit')")
    @Log(title = "床位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Bed bed)
    {
        return toAjax(bedService.updateBed(bed));
    }

    /**
     * 删除床位
     */
    @Operation(summary = "删除床位")
    @PreAuthorize("@ss.hasPermi('elder:bed:remove')")
    @Log(title = "床位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "床位ID", requiredMode = Schema.RequiredMode.REQUIRED)  @PathVariable Long[] ids)
    {
        return toAjax(bedService.deleteBedByIds(ids));
    }
}
