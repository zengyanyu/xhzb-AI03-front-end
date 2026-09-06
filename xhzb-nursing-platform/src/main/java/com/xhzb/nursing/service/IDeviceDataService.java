package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.DeviceData;

import java.util.List;

/**
 * 设备数据Service接口
 *
 * @author ruoyi
 * @date 2026-08-15
 */
public interface IDeviceDataService extends IService<DeviceData> {
    /**
     * 查询设备数据
     *
     * @param id 设备数据主键
     * @return 设备数据
     */
    DeviceData selectDeviceDataById(Long id);

    /**
     * 查询设备数据列表
     *
     * @param deviceData 设备数据
     * @return 设备数据集合
     */
    List<DeviceData> selectDeviceDataList(DeviceData deviceData);

    /**
     * 新增设备数据
     *
     * @param deviceData 设备数据
     * @return 结果
     */
    int insertDeviceData(DeviceData deviceData);

    /**
     * 修改设备数据
     *
     * @param deviceData 设备数据
     * @return 结果
     */
    int updateDeviceData(DeviceData deviceData);

    /**
     * 批量删除设备数据
     *
     * @param ids 需要删除的设备数据主键集合
     * @return 结果
     */
    int deleteDeviceDataByIds(Long[] ids);

    /**
     * 删除设备数据信息
     *
     * @param id 设备数据主键
     * @return 结果
     */
    int deleteDeviceDataById(Long id);

    /**
     * 处理设备上报的数据消息
     * <p>
     * 步骤：1.解析消息内容，获取设备ID、上报属性和上报时间
     * 2.根据设备ID查询设备，设备不存在则直接返回
     * 3.将上报时间由世界时间(UTC)转为北京时间
     * 4.根据properties数据批量保存设备数据到数据库
     *
     * @param contentStr AMQP上报的消息内容
     */
    void handleDeviceData(String contentStr);
}
