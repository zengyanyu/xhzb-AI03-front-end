package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.nursing.domain.HealthAssessment;
import com.xhzb.nursing.domain.dto.health.ElderAssessmentDto;
import com.xhzb.nursing.service.IHealthAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 健康评估记录Controller
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/nursing/healthAssessment")
@Tag(name = "健康评估记录相关接口")
public class HealthAssessmentController extends BaseController
{
    @Autowired
    private IHealthAssessmentService healthAssessmentService;

    /**
     * 查询健康评估记录列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:healthAssessment:list')")
    @GetMapping("/list")
    @Operation(summary = "查询健康评估记录列表")
    public TableDataInfo list(HealthAssessment healthAssessment)
    {
        startPage();
        List<HealthAssessment> list = healthAssessmentService.selectHealthAssessmentList(healthAssessment);
        return getDataTable(list);
    }

    /**
     * 导出健康评估记录列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:healthAssessment:export')")
    @Log(title = "健康评估记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出健康评估记录列表")
    public void export(HttpServletResponse response, HealthAssessment healthAssessment)
    {
        List<HealthAssessment> list = healthAssessmentService.selectHealthAssessmentList(healthAssessment);
        ExcelUtil<HealthAssessment> util = new ExcelUtil<HealthAssessment>(HealthAssessment.class);
        util.exportExcel(response, list, "健康评估记录数据");
    }

    /**
     * 获取健康评估记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:healthAssessment:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取健康评估记录详细信息")
    public AjaxResult getInfo(@Schema(name = "健康评估记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(healthAssessmentService.selectHealthAssessmentById(id));
    }

    /**
     * 新增健康评估记录
     */
    @PreAuthorize("@ss.hasPermi('nursing:healthAssessment:add')")
    @Log(title = "健康评估记录", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增健康评估记录")
    public AjaxResult add(@RequestBody ElderAssessmentDto dto)
    {
        return success(healthAssessmentService.insertHealthAssessment(dto));
    }

    /**
     * 修改健康评估记录
     */
    @PreAuthorize("@ss.hasPermi('nursing:healthAssessment:edit')")
    @Log(title = "健康评估记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改健康评估记录")
    public AjaxResult edit(@RequestBody ElderAssessmentDto dto)
    {
        return success(healthAssessmentService.updateHealthAssessment(dto));
    }

    /**
     * 删除健康评估记录
     */
    @PreAuthorize("@ss.hasPermi('nursing:healthAssessment:remove')")
    @Log(title = "健康评估记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除健康评估记录")
    public AjaxResult remove(@Schema(name = "健康评估记录ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(healthAssessmentService.deleteHealthAssessmentByIds(ids));
    }
}
