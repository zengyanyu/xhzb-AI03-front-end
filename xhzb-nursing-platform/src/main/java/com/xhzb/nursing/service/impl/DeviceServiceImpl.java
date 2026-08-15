package com.xhzb.nursing.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.AddDevice;
import com.huaweicloud.sdk.iotda.v5.model.AddDeviceRequest;
import com.huaweicloud.sdk.iotda.v5.model.AddDeviceResponse;
import com.huaweicloud.sdk.iotda.v5.model.AuthInfo;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsRequest;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsResponse;
import com.huaweicloud.sdk.iotda.v5.model.ProductSummary;
import com.xhzb.common.constant.CacheConstants;
import com.xhzb.common.exception.ServiceException;
import com.xhzb.common.exception.base.BaseException;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.nursing.domain.Device;
import com.xhzb.nursing.domain.dto.RegisterDeviceDto;
import com.xhzb.nursing.domain.vo.ProductVo;
import com.xhzb.nursing.mapper.DeviceMapper;
import com.xhzb.nursing.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 查询所有产品列表
     *
     * @return
     */
    @Override
    public List<ProductVo> allProduct() {

        //1.到redis中查询数据得到json字符串，key=CacheConstants.IOT_ALL_PRODUCT_LIST
        String json = redisTemplate.opsForValue().get(CacheConstants.IOT_ALL_PRODUCT_LIST);
        if(StringUtils.isEmpty(json)){
            return null;
        }

        //2.将json数据封装为List<ProductVo>
        List<ProductVo> productVos = JSONUtil.toList(json, ProductVo.class);

        //3.返回数据
        return productVos;
    }

    /**
     * 注册设备
     *
     * 步骤：1.校验设备名称是否重复 2.校验设备标识符(nodeId)是否重复
     *      3.校验同一位置是否已绑定相同产品 4.调用华为云IoT注册设备 5.插入本地设备表
     *
     * @param dto 注册设备请求参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerDevice(RegisterDeviceDto dto) {
        // 随身设备没有物理位置，物理位置类型统一为-1
        if (dto.getLocationType() != null && dto.getLocationType() == 0 && dto.getPhysicalLocationType() == null) {
            dto.setPhysicalLocationType(-1);//绑定老人是-1，楼层是0，房间1，床位是2
        }

        //1.校验设备名称是否重复
        Long nameCount = count(new LambdaQueryWrapper<Device>().eq(Device::getDeviceName, dto.getDeviceName()));
        if (nameCount > 0) {
            throw new ServiceException("设备名称【" + dto.getDeviceName() + "】已存在，请勿重复注册");
        }

        //2.校验设备标识符(nodeId)是否重复
        Long nodeCount = count(new LambdaQueryWrapper<Device>().eq(Device::getNodeId, dto.getNodeId()));
        if (nodeCount > 0) {
            throw new ServiceException("设备标识符【" + dto.getNodeId() + "】已存在，请勿重复注册");
        }

        //3.校验同一位置是否已绑定相同的产品（绑定位置+位置类型+物理位置类型+产品key）
        LambdaQueryWrapper<Device> locationWrapper = new LambdaQueryWrapper<Device>()
                .eq(Device::getBindingLocation, dto.getBindingLocation())
                .eq(Device::getLocationType, dto.getLocationType())
                .eq(Device::getProductKey, dto.getProductKey());
        if (dto.getPhysicalLocationType() != null) {
            locationWrapper.eq(Device::getPhysicalLocationType, dto.getPhysicalLocationType());
        }
        Long locationCount = count(locationWrapper);
        if (locationCount > 0) {
            throw new ServiceException("该位置已绑定相同的产品，请勿重复注册");
        }

        //4.组装数据，调用华为云IoT注册设备
        //4.1 构建注册请求体（不指定秘钥，由平台自动生成）
        AuthInfo authInfo = new AuthInfo().withAuthType("SECRET");
        AddDevice addDeviceBody = new AddDevice()
                .withNodeId(dto.getNodeId())
                .withDeviceName(dto.getDeviceName())
                .withProductId(dto.getProductKey())
                .withAuthInfo(authInfo)
                .withDescription(dto.getDeviceDescription());
        AddDeviceRequest request = new AddDeviceRequest().withBody(addDeviceBody);

        //4.2 调用华为云接口注册设备
        AddDeviceResponse response = ioTDAClient.addDevice(request);
        if (response.getHttpStatusCode() != 201) {
            throw new BaseException("设备管理-注册设备失败");
        }

        //5.插入到本地的设备表
        Device device = new Device();
        device.setIotId(response.getDeviceId());
        if (response.getAuthInfo() != null) {
            device.setSecret(response.getAuthInfo().getSecret());
        }
        device.setDeviceName(dto.getDeviceName());
        device.setNodeId(dto.getNodeId());
        device.setBindingLocation(dto.getBindingLocation());
        device.setLocationType(dto.getLocationType());
        device.setPhysicalLocationType(dto.getPhysicalLocationType());
        device.setProductKey(dto.getProductKey());
        device.setProductName(dto.getProductName());
        device.setDeviceDescription(dto.getDeviceDescription());
        device.setRemark(dto.getRemark());
        device.setHaveEntranceGuard(0);
        save(device);
    }
}
