package com.xhzb.nursing.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.xhzb.nursing.domain.Elder;
import com.xhzb.nursing.service.IElderService;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.common.core.page.TableDataInfo;

/**
 * 老人Controller
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/nursing/elder")
@Tag(name = "老人相关接口")
public class ElderController extends BaseController
{
    @Autowired
    private IElderService elderService;

    /**
     * 查询老人列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:elder:list')")
    @GetMapping("/list")
    @Operation(summary = "查询老人列表")
    public TableDataInfo list(Elder elder)
    {
        startPage();
        List<Elder> list = elderService.selectElderList(elder);
        return getDataTable(list);
    }

    /**
     * 导出老人列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:elder:export')")
    @Log(title = "老人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出老人列表")
    public void export(HttpServletResponse response, Elder elder)
    {
        List<Elder> list = elderService.selectElderList(elder);
        ExcelUtil<Elder> util = new ExcelUtil<Elder>(Elder.class);
        util.exportExcel(response, list, "老人数据");
    }

    /**
     * 获取老人详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:elder:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取老人详细信息")
    public AjaxResult getInfo(@Schema(name = "老人ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(elderService.selectElderById(id));
    }

    /**
     * 新增老人
     */
    @PreAuthorize("@ss.hasPermi('nursing:elder:add')")
    @Log(title = "老人", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增老人")
    public AjaxResult add(@RequestBody Elder elder)
    {
        return toAjax(elderService.insertElder(elder));
    }

    /**
     * 修改老人
     */
    @PreAuthorize("@ss.hasPermi('nursing:elder:edit')")
    @Log(title = "老人", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改老人")
    public AjaxResult edit(@RequestBody Elder elder)
    {
        return toAjax(elderService.updateElder(elder));
    }

    /**
     * 删除老人
     */
    @PreAuthorize("@ss.hasPermi('nursing:elder:remove')")
    @Log(title = "老人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除老人")
    public AjaxResult remove(@Schema(name = "老人ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(elderService.deleteElderByIds(ids));
    }
}
