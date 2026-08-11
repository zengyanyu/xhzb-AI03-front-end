package com.xhzb.nursing.controller;

import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.nursing.domain.Device;
import com.xhzb.nursing.service.IDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备管理Controller
 * 
 * @author ruoyi
 * @date 2026-07-18
 */
@RestController
@RequestMapping("/nursing/device")
@Tag(name = "设备管理相关接口")
public class DeviceController extends BaseController
{
    @Autowired
    private IDeviceService deviceService;

    /**
     * 查询设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:device:list')")
    @GetMapping("/list")
    @Operation(summary = "查询设备管理列表")
    public TableDataInfo list(Device device)
    {
        startPage();
        List<Device> list = deviceService.selectDeviceList(device);
        return getDataTable(list);
    }


}
