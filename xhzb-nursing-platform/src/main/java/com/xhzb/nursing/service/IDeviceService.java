package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.nursing.domain.Device;
import com.xhzb.nursing.domain.dto.RegisterDeviceDto;
import com.xhzb.nursing.domain.vo.DeviceDetailVo;
import com.xhzb.nursing.domain.vo.DevicePropertiesVo;
import com.xhzb.nursing.domain.vo.ProductVo;

import java.util.List;

/**
 * 设备管理Service接口
 *
 * @author ruoyi
 * @date 2026-07-18
 */
public interface IDeviceService extends IService<Device> {
    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    Device selectDeviceById(Long id);

    /**
     * 查询设备管理列表
     *
     * @param device 设备管理
     * @return 设备管理集合
     */
    List<Device> selectDeviceList(Device device);

    /**
     * 新增设备管理
     *
     * @param device 设备管理
     * @return 结果
     */
    int insertDevice(Device device);

    /**
     * 修改设备管理
     *
     * @param device 设备管理
     * @return 结果
     */
    int updateDevice(Device device);

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理主键集合
     * @return 结果
     */
    int deleteDeviceByIds(Long[] ids);

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    int deleteDeviceById(Long id);

    /**
     * 同步产品列表
     */
    void syncProductList();

    /**
     * 查询所有产品列表
     *
     * @return
     */
    List<ProductVo> allProduct();

    /**
     * 注册设备
     *
     * @param dto 注册设备请求参数
     */
    void registerDevice(RegisterDeviceDto dto);

    /**
     * 查询设备详细数据
     * <p>
     * 步骤：1.根据设备ID(iotId)从MySQL查询设备数据
     * 2.根据设备ID调用华为云查询设备详情，补全设备状态、激活时间等
     * 3.合并两种数据并返回
     *
     * @param iotId 设备ID
     * @return 设备详细数据
     */
    DeviceDetailVo getDeviceDetail(String iotId);

    /**
     * 查询设备上报的数据（服务属性）
     * <p>
     * 步骤：1.根据设备ID(iotId)调用华为云查询设备影子数据
     * 2.遍历上报的属性数据，组装为接口文档需要的数据
     * 3.将上报时间由世界时间(UTC)转为北京时间
     *
     * @param iotId 设备ID
     * @return 设备上报的数据列表
     */
    List<DevicePropertiesVo> queryServiceProperties(String iotId);

    /**
     * 查询产品详情
     *
     * @param productKey
     * @return
     */
    AjaxResult queryProduct(String productKey);
}
