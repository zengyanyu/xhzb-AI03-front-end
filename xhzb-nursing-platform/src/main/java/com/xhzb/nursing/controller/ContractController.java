package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.nursing.domain.Contract;
import com.xhzb.nursing.service.IContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同Controller
 *
 * @author ruoyi
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/nursing/contract")
@Tag(name = "合同相关接口")
public class ContractController extends BaseController {
    @Autowired
    private IContractService contractService;

    /**
     * 查询合同列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:contract:list')")
    @GetMapping("/list")
    @Operation(summary = "查询合同列表")
    public TableDataInfo list(Contract contract) {
        startPage();
        List<Contract> list = contractService.selectContractList(contract);
        return getDataTable(list);
    }

    /**
     * 导出合同列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:contract:export')")
    @Log(title = "合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出合同列表")
    public void export(HttpServletResponse response, Contract contract) {
        List<Contract> list = contractService.selectContractList(contract);
        ExcelUtil<Contract> util = new ExcelUtil<Contract>(Contract.class);
        util.exportExcel(response, list, "合同数据");
    }

    /**
     * 获取合同详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:contract:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取合同详细信息")
    public AjaxResult getInfo(@Schema(name = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED)
                              @PathVariable("id") Long id) {
        return success(contractService.selectContractById(id));
    }

    /**
     * 新增合同
     */
    @PreAuthorize("@ss.hasPermi('nursing:contract:add')")
    @Log(title = "合同", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增合同")
    public AjaxResult add(@RequestBody Contract contract) {
        return toAjax(contractService.insertContract(contract));
    }

    /**
     * 修改合同
     */
    @PreAuthorize("@ss.hasPermi('nursing:contract:edit')")
    @Log(title = "合同", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改合同")
    public AjaxResult edit(@RequestBody Contract contract) {
        return toAjax(contractService.updateContract(contract));
    }

    /**
     * 删除合同
     */
    @PreAuthorize("@ss.hasPermi('nursing:contract:remove')")
    @Log(title = "合同", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除合同")
    public AjaxResult remove(@Schema(name = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids) {
        return toAjax(contractService.deleteContractByIds(ids));
    }
}
