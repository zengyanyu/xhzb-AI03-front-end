package com.xhzb.nursing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.DeviceData;

/**
 * 设备数据Mapper接口
 *
 * @author ruoyi
 * @date 2026-08-15
 */
@Mapper
public interface DeviceDataMapper extends BaseMapper<DeviceData> {
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
     * 删除设备数据
     *
     * @param id 设备数据主键
     * @return 结果
     */
    int deleteDeviceDataById(Long id);

    /**
     * 批量删除设备数据
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteDeviceDataByIds(Long[] ids);
}
