package com.xhzb.nursing.controller;

import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.nursing.domain.Device;
import com.xhzb.nursing.domain.dto.RegisterDeviceDto;
import com.xhzb.nursing.domain.vo.DevicePropertiesVo;
import com.xhzb.nursing.domain.vo.ProductVo;
import com.xhzb.nursing.service.IDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 同步华为IOT产品列表到redis中
     * @return
     */
    @PostMapping("/syncProductList")
    @Operation(summary = "从物联网平台同步产品列表")
    public AjaxResult syncProductList() {
        deviceService.syncProductList();
        return success();
    }

    /**
     * 同步华为IOT产品列表到redis中
     * @return
     */
    @GetMapping("/allProduct")
    @Operation(summary = "从物联网平台同步产品列表")
    public AjaxResult allProduct() {
        List<ProductVo> productVos =  deviceService.allProduct();
        return success(productVos);
    }

    /**
     * 注册设备
     *
     * @param dto 注册设备请求参数
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('nursing:device:add')")
    @PostMapping("/register")
    @Operation(summary = "注册设备")
    public AjaxResult register(@Validated @RequestBody RegisterDeviceDto dto) {
        deviceService.registerDevice(dto);
        return success();
    }

    /**
     * 查询设备详细数据
     *
     * @param iotId 设备ID
     * @return 设备详细数据
     */
    @PreAuthorize("@ss.hasPermi('nursing:device:query')")
    @GetMapping(value = "/{iotId}")
    @Operation(summary = "查询设备详细数据")
    public AjaxResult getInfo(@PathVariable("iotId") String iotId) {
        return success(deviceService.getDeviceDetail(iotId));
    }

    /**
     * 查询设备上报的数据
     *
     * @param iotId 设备ID
     * @return 设备上报的数据
     */
    @PreAuthorize("@ss.hasPermi('nursing:device:query')")
    @GetMapping(value = "/queryServiceProperties/{iotId}")
    @Operation(summary = "查询设备上报的数据")
    public AjaxResult queryServiceProperties(@PathVariable("iotId") String iotId) {
        return success(deviceService.queryServiceProperties(iotId));
    }

    @GetMapping("/queryProduct/{productKey}")
    public AjaxResult queryProduct(@PathVariable String productKey){
        return deviceService.queryProduct(productKey);
    }
}
