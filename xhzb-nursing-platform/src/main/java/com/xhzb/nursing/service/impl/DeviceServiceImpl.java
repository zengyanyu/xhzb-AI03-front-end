package com.xhzb.nursing.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsRequest;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsResponse;
import com.huaweicloud.sdk.iotda.v5.model.ProductSummary;
import com.xhzb.common.constant.CacheConstants;
import com.xhzb.common.exception.base.BaseException;
import com.xhzb.nursing.domain.Device;
import com.xhzb.nursing.mapper.DeviceMapper;
import com.xhzb.nursing.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 设备管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-07-18
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService
{
    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 查询设备管理
     * 
     * @param id 设备管理主键
     * @return 设备管理
     */
    @Override
    public Device selectDeviceById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询设备管理列表
     * 
     * @param device 设备管理
     * @return 设备管理
     */
    @Override
    public List<Device> selectDeviceList(Device device)
    {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 新增设备管理
     * 
     * @param device 设备管理
     * @return 结果
     */
    @Override
    public int insertDevice(Device device)
    {
        return save(device)? 1 : 0;
    }

    /**
     * 修改设备管理
     * 
     * @param device 设备管理
     * @return 结果
     */
    @Override
    public int updateDevice(Device device)
    {
        return updateById(device)? 1 : 0;
    }

    /**
     * 批量删除设备管理
     * 
     * @param ids 需要删除的设备管理主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除设备管理信息
     * 
     * @param id 设备管理主键
     * @return 结果
     */
    @Override
    public int deleteDeviceById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    @Autowired
    private IoTDAClient ioTDAClient;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 同步产品列表
     */
    @Override
    public void syncProductList() {
        //调用华为云查询产品列表
        //1.构建产品列表查询请求对象
        ListProductsRequest request = new ListProductsRequest();
        request.setLimit(50);

        //2.通过客户端调用并返回结果
        ListProductsResponse response = ioTDAClient.listProducts(request);

        if(response.getHttpStatusCode()!=200){
            throw new BaseException("设备管理-同步产品列表失败");
        }

        //3.打印结果
        List<ProductSummary> products = response.getProducts();
        //写入redis
        redisTemplate.opsForValue().set(CacheConstants.IOT_ALL_PRODUCT_LIST, JSONUtil.toJsonStr(products));

    }
}
