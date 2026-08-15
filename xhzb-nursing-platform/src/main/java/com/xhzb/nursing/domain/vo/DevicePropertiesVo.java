package com.xhzb.nursing.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备上报的数据响应VO
 *
 * @author songyu
 * @date 2026-08-15
 */
@Data
@Schema(description = "设备上报的数据响应VO")
public class DevicePropertiesVo
{
    /** 功能ID（产品模型中的服务/属性标识） */
    @Schema(title = "功能ID")
    private String functionId;

    /** 上报时间（北京时间，格式 yyyy-MM-dd'T'HH:mm:ss） */
    @Schema(title = "上报时间")
    private String eventTime;

    /** 上报值 */
    @Schema(title = "上报值")
    private Object value;
}
