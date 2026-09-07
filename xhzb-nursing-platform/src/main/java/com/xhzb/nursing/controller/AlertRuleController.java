package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.nursing.domain.AlertRule;
import com.xhzb.nursing.service.IAlertRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报警规则Controller
 *
 * @author ruoyi
 * @date 2026-08-16
 */
@RestController
@RequestMapping("/nursing/alertRule")
@Tag(name = "报警规则相关接口")
public class AlertRuleController extends BaseController {
    @Autowired
    private IAlertRuleService alertRuleService;

    /**
     * 查询报警规则列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:alertRule:list')")
    @GetMapping("/list")
    @Operation(summary = "查询报警规则列表")
    public TableDataInfo list(AlertRule alertRule) {
        startPage();
        List<AlertRule> list = alertRuleService.selectAlertRuleList(alertRule);
        return getDataTable(list);
    }

    /**
     * 导出报警规则列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:alertRule:export')")
    @Log(title = "报警规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出报警规则列表")
    public void export(HttpServletResponse response, AlertRule alertRule) {
        List<AlertRule> list = alertRuleService.selectAlertRuleList(alertRule);
        ExcelUtil<AlertRule> util = new ExcelUtil<AlertRule>(AlertRule.class);
        util.exportExcel(response, list, "报警规则数据");
    }

    /**
     * 获取报警规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:alertRule:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取报警规则详细信息")
    public AjaxResult getInfo(@Schema(name = "报警规则ID", requiredMode = Schema.RequiredMode.REQUIRED)
                              @PathVariable("id") Long id) {
        return success(alertRuleService.selectAlertRuleById(id));
    }

    /**
     * 新增报警规则
     */
    @PreAuthorize("@ss.hasPermi('nursing:alertRule:add')")
    @Log(title = "报警规则", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增报警规则")
    public AjaxResult add(@RequestBody AlertRule alertRule) {
        return toAjax(alertRuleService.insertAlertRule(alertRule));
    }

    /**
     * 修改报警规则
     */
    @PreAuthorize("@ss.hasPermi('nursing:alertRule:edit')")
    @Log(title = "报警规则", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改报警规则")
    public AjaxResult edit(@RequestBody AlertRule alertRule) {
        return toAjax(alertRuleService.updateAlertRule(alertRule));
    }

    /**
     * 删除报警规则
     */
    @PreAuthorize("@ss.hasPermi('nursing:alertRule:remove')")
    @Log(title = "报警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除报警规则")
    public AjaxResult remove(@Schema(name = "报警规则ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids) {
        return toAjax(alertRuleService.deleteAlertRuleByIds(ids));
    }
}
