package com.xhzb.nursing.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.AddDevice;
import com.huaweicloud.sdk.iotda.v5.model.AddDeviceRequest;
import com.huaweicloud.sdk.iotda.v5.model.AddDeviceResponse;
import com.huaweicloud.sdk.iotda.v5.model.AuthInfo;
import com.huaweicloud.sdk.iotda.v5.model.DeviceShadowData;
import com.huaweicloud.sdk.iotda.v5.model.DeviceShadowProperties;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsRequest;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsResponse;
import com.huaweicloud.sdk.iotda.v5.model.ProductSummary;
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceRequest;
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceResponse;
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceShadowRequest;
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceShadowResponse;
import com.xhzb.common.constant.CacheConstants;
import com.xhzb.common.core.domain.entity.SysUser;
import com.xhzb.common.exception.ServiceException;
import com.xhzb.common.exception.base.BaseException;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.nursing.domain.Device;
import com.xhzb.nursing.domain.dto.RegisterDeviceDto;
import com.xhzb.nursing.domain.vo.DeviceDetailVo;
import com.xhzb.nursing.domain.vo.DevicePropertiesVo;
import com.xhzb.nursing.domain.vo.ProductVo;
import com.xhzb.nursing.mapper.DeviceMapper;
import com.xhzb.nursing.service.IDeviceService;
import com.xhzb.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 设备管理Service业务层处理
 *
 * @author ruoyi
 * @date 2026-07-18
 */
@Slf4j
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService
{
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ISysUserService sysUserService;

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

    /**
     * 查询设备详细数据
     *
     * 步骤：1.根据设备ID(iotId)从MySQL查询设备数据
     *       2.根据设备ID调用华为云查询设备详情，补全设备状态、激活时间等
     *       3.合并两种数据并返回
     *
     * @param iotId 设备ID
     * @return 设备详细数据
     */
    @Override
    public DeviceDetailVo getDeviceDetail(String iotId) {
        //1.根据设备ID(iotId)从MySQL查询设备数据
        Device device = getOne(new LambdaQueryWrapper<Device>().eq(Device::getIotId, iotId));
        if (device == null) {
            throw new ServiceException("设备【" + iotId + "】不存在");
        }

        //2.根据设备ID调用华为云查询设备详情，补全设备状态、激活时间等
        //2.1 构建查询设备详情请求对象
        ShowDeviceRequest request = new ShowDeviceRequest().withDeviceId(iotId);
        ShowDeviceResponse showDeviceResponse = null;
        try {
            //2.2 通过客户端调用华为云查询设备详情
            showDeviceResponse = ioTDAClient.showDevice(request);
        } catch (Exception e) {
            //华为云查询设备详情失败，不影响本地数据的返回，仅记录日志
            log.error("查询设备【{}】华为云详情失败", iotId, e);
        }

        //3.合并MySQL数据与华为云数据，组装响应VO
        //3.1 拷贝MySQL中设备数据
        DeviceDetailVo vo = new DeviceDetailVo();
        vo.setId(device.getId());
        vo.setIotId(device.getIotId());
        vo.setDeviceName(device.getDeviceName());
        vo.setNodeId(device.getNodeId());
        vo.setSecret(device.getSecret());
        vo.setProductKey(device.getProductKey());
        vo.setProductName(device.getProductName());
        vo.setLocationType(device.getLocationType());
        vo.setBindingLocation(device.getBindingLocation());
        vo.setRemark(device.getRemark());
        vo.setCreateTime(device.getCreateTime());
        if (StringUtils.isNotEmpty(device.getCreateBy())) {
            vo.setCreateBy(Long.valueOf(device.getCreateBy()));
        }

        //3.2 合并华为云数据：设备状态、激活时间
        if (showDeviceResponse != null) {
            vo.setDeviceStatus(showDeviceResponse.getStatus());
            vo.setActiveTime(formatIotTime(showDeviceResponse.getActiveTime()));
        }

        //3.3 根据创建人id查询创建人昵称
        if (StringUtils.isNotEmpty(device.getCreateBy())) {
            SysUser sysUser = sysUserService.selectUserById(Long.valueOf(device.getCreateBy()));
            if (sysUser != null) {
                vo.setCreator(sysUser.getNickName());
            }
        }
        return vo;
    }

    /**
     * 将华为云返回的时间字符串（如 20170306T185107Z）格式化为 yyyy-MM-dd HH:mm:ss
     *
     * @param iotTime 华为云返回的时间字符串
     * @return 格式化后的时间字符串
     */
    private String formatIotTime(String iotTime) {
        if (StringUtils.isEmpty(iotTime)) {
            return null;
        }
        try {
            Date date = DateUtil.parse(iotTime, "yyyyMMdd'T'HHmmss'Z'");
            return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            //时间格式解析失败时返回原始字符串
            return iotTime;
        }
    }

    /**
     * 查询设备上报的数据（服务属性）
     *
     * 步骤：1.根据设备ID(iotId)调用华为云查询设备影子数据
     *       2.遍历每个服务上报的属性数据，组装为接口文档需要的数据
     *       3.将上报时间由世界时间(UTC)转为北京时间
     *
     * @param iotId 设备ID
     * @return 设备上报的数据列表
     */
    @Override
    public List<DevicePropertiesVo> queryServiceProperties(String iotId) {
        //1.根据设备ID(iotId)从MySQL查询设备数据，确认设备已注册
        Device device = getOne(new LambdaQueryWrapper<Device>().eq(Device::getIotId, iotId));
        if (device == null) {
            throw new ServiceException("设备【" + iotId + "】不存在");
        }

        //2.根据设备ID调用华为云查询设备影子数据
        //2.1 构建查询设备影子请求对象
        ShowDeviceShadowRequest request = new ShowDeviceShadowRequest().withDeviceId(iotId);

        //2.2 通过客户端调用华为云查询设备影子
        ShowDeviceShadowResponse response = ioTDAClient.showDeviceShadow(request);
        if (response.getHttpStatusCode() != 200) {
            throw new BaseException("设备管理-查询设备上报的数据失败");
        }

        //3.遍历每个服务上报的属性数据，组装为接口文档需要的数据
        List<DevicePropertiesVo> result = new ArrayList<>();
        List<DeviceShadowData> shadowList = response.getShadow();
        if (shadowList == null || shadowList.isEmpty()) {
            return result;
        }
        for (DeviceShadowData shadowData : shadowList) {
            //3.1 获取服务上报的数据，无上报数据则跳过
            DeviceShadowProperties reported = shadowData.getReported();
            if (reported == null || reported.getProperties() == null) {
                continue;
            }

            //3.2 将上报时间由世界时间(UTC)转为北京时间
            String eventTime = formatEventTime(reported.getEventTime());

            //3.3 遍历服务下的每个上报属性，一个属性组装一条数据
            JSONObject propertiesJson = JSONUtil.parseObj(reported.getProperties());
            for (Map.Entry<String, Object> entry : propertiesJson.entrySet()) {
                DevicePropertiesVo vo = new DevicePropertiesVo();
                //功能ID取属性名
                vo.setFunctionId(entry.getKey());
                //上报时间（北京时间）
                vo.setEventTime(eventTime);
                //上报值
                vo.setValue(entry.getValue());
                result.add(vo);
            }
        }
        return result;
    }

    /**
     * 将华为云返回的上报时间（世界时间UTC，如 20250219T070424Z）转为北京时间并格式化
     * 格式：yyyy-MM-dd'T'HH:mm:ss
     *
     * @param eventTime 华为云返回的世界时间字符串
     * @return 格式化后的北京时间字符串
     */
    private String formatEventTime(String eventTime) {
        if (StringUtils.isEmpty(eventTime)) {
            return null;
        }
        try {
            //1.将世界时间(UTC)解析为Date
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date utcDate = utcFormat.parse(eventTime);

            //2.转换为北京时间并格式化输出
            SimpleDateFormat beijingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            beijingFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return beijingFormat.format(utcDate);
        } catch (Exception e) {
            //时间格式解析失败时返回原始字符串
            return eventTime;
        }
    }
}
