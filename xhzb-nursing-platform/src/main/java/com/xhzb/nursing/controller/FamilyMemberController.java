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
import com.xhzb.nursing.domain.FamilyMember;
import com.xhzb.nursing.service.IFamilyMemberService;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.common.core.page.TableDataInfo;

/**
 * 老人家属Controller
 *
 * @author ruoyi
 * @date 2026-07-24
 */
@RestController
@RequestMapping("/nursing/member")
@Tag(name = "老人家属相关接口")
public class FamilyMemberController extends BaseController {
    @Autowired
    private IFamilyMemberService familyMemberService;

    /**
     * 查询老人家属列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:member:list')")
    @GetMapping("/list")
    @Operation(summary = "查询老人家属列表")
    public TableDataInfo list(FamilyMember familyMember) {
        startPage();
        List<FamilyMember> list = familyMemberService.selectFamilyMemberList(familyMember);
        return getDataTable(list);
    }

    /**
     * 导出老人家属列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:member:export')")
    @Log(title = "老人家属", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出老人家属列表")
    public void export(HttpServletResponse response, FamilyMember familyMember) {
        List<FamilyMember> list = familyMemberService.selectFamilyMemberList(familyMember);
        ExcelUtil<FamilyMember> util = new ExcelUtil<FamilyMember>(FamilyMember.class);
        util.exportExcel(response, list, "老人家属数据");
    }

    /**
     * 获取老人家属详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:member:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取老人家属详细信息")
    public AjaxResult getInfo(@Schema(name = "老人家属ID", requiredMode = Schema.RequiredMode.REQUIRED)
                              @PathVariable("id") Long id) {
        return success(familyMemberService.selectFamilyMemberById(id));
    }

    /**
     * 新增老人家属
     */
    @PreAuthorize("@ss.hasPermi('nursing:member:add')")
    @Log(title = "老人家属", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增老人家属")
    public AjaxResult add(@RequestBody FamilyMember familyMember) {
        return toAjax(familyMemberService.insertFamilyMember(familyMember));
    }

    /**
     * 修改老人家属
     */
    @PreAuthorize("@ss.hasPermi('nursing:member:edit')")
    @Log(title = "老人家属", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改老人家属")
    public AjaxResult edit(@RequestBody FamilyMember familyMember) {
        return toAjax(familyMemberService.updateFamilyMember(familyMember));
    }

    /**
     * 删除老人家属
     */
    @PreAuthorize("@ss.hasPermi('nursing:member:remove')")
    @Log(title = "老人家属", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除老人家属")
    public AjaxResult remove(@Schema(name = "老人家属ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids) {
        return toAjax(familyMemberService.deleteFamilyMemberByIds(ids));
    }
}
