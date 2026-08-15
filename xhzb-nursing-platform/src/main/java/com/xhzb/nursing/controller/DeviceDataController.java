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
import com.xhzb.nursing.domain.DeviceData;
import com.xhzb.nursing.service.IDeviceDataService;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.common.core.page.TableDataInfo;

/**
 * 设备数据Controller
 * 
 * @author ruoyi
 * @date 2026-08-15
 */
@RestController
@RequestMapping("/nursing/data")
@Tag(name = "设备数据相关接口")
public class DeviceDataController extends BaseController
{
    @Autowired
    private IDeviceDataService deviceDataService;

    /**
     * 查询设备数据列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:data:list')")
    @GetMapping("/list")
    @Operation(summary = "查询设备数据列表")
    public TableDataInfo list(DeviceData deviceData)
    {
        startPage();
        List<DeviceData> list = deviceDataService.selectDeviceDataList(deviceData);
        return getDataTable(list);
    }

    /**
     * 导出设备数据列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:data:export')")
    @Log(title = "设备数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出设备数据列表")
    public void export(HttpServletResponse response, DeviceData deviceData)
    {
        List<DeviceData> list = deviceDataService.selectDeviceDataList(deviceData);
        ExcelUtil<DeviceData> util = new ExcelUtil<DeviceData>(DeviceData.class);
        util.exportExcel(response, list, "设备数据数据");
    }

    /**
     * 获取设备数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:data:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取设备数据详细信息")
    public AjaxResult getInfo(@Schema(name = "设备数据ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(deviceDataService.selectDeviceDataById(id));
    }

    /**
     * 新增设备数据
     */
    @PreAuthorize("@ss.hasPermi('nursing:data:add')")
    @Log(title = "设备数据", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增设备数据")
    public AjaxResult add(@RequestBody DeviceData deviceData)
    {
        return toAjax(deviceDataService.insertDeviceData(deviceData));
    }

    /**
     * 修改设备数据
     */
    @PreAuthorize("@ss.hasPermi('nursing:data:edit')")
    @Log(title = "设备数据", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改设备数据")
    public AjaxResult edit(@RequestBody DeviceData deviceData)
    {
        return toAjax(deviceDataService.updateDeviceData(deviceData));
    }

    /**
     * 删除设备数据
     */
    @PreAuthorize("@ss.hasPermi('nursing:data:remove')")
    @Log(title = "设备数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除设备数据")
    public AjaxResult remove(@Schema(name = "设备数据ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(deviceDataService.deleteDeviceDataByIds(ids));
    }
}
