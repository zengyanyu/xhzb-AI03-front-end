package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.nursing.domain.NursingLevel;
import com.xhzb.nursing.domain.vo.NursingLevelVo;
import com.xhzb.nursing.service.INursingLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 护理等级Controller
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/nursing/nursingLevel")
@Tag(name = "护理等级相关接口")
public class NursingLevelController extends BaseController {
    @Autowired
    private INursingLevelService nursingLevelService;

    /**
     * 查询护理等级列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:nursingLevel:list')")
    @GetMapping("/list")
    @Operation(summary = "查询护理等级列表")
    public TableDataInfo list(NursingLevel nursingLevel) {
        startPage();
        List<NursingLevelVo> list = nursingLevelService.selectNursingLevelList(nursingLevel);
        return getDataTable(list);
    }

    /**
     * 获取护理等级详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:nursingLevel:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取护理等级详细信息")
    public AjaxResult getInfo(@Schema(name = "护理等级ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id) {
        return success(nursingLevelService.selectNursingLevelById(id));
    }

    /**
     * 新增护理等级
     */
    @PreAuthorize("@ss.hasPermi('nursing:nursingLevel:add')")
    @Log(title = "护理等级", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增护理等级")
    public AjaxResult add(@RequestBody NursingLevel nursingLevel) {
        return toAjax(nursingLevelService.insertNursingLevel(nursingLevel));
    }

    /**
     * 修改护理等级
     */
    @PreAuthorize("@ss.hasPermi('nursing:nursingLevel:edit')")
    @Log(title = "护理等级", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改护理等级")
    public AjaxResult edit(@RequestBody NursingLevel nursingLevel) {
        return toAjax(nursingLevelService.updateNursingLevel(nursingLevel));
    }

    /**
     * 删除护理等级
     */
    @PreAuthorize("@ss.hasPermi('nursing:nursingLevel:remove')")
    @Log(title = "护理等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除护理等级")
    public AjaxResult remove(@Schema(name = "护理等级ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids) {
        return toAjax(nursingLevelService.deleteNursingLevelByIds(ids));
    }

    /**
     * 查询所有启用的护理等级（不分页）
     */
    @GetMapping("/listAll")
    @Operation(summary = "查询所有护理等级信息")
    public AjaxResult listAll() {
        return success(nursingLevelService.listAll());
    }
}
