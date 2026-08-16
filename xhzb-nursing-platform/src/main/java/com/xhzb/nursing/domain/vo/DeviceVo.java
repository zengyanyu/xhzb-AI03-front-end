package com.xhzb.nursing.domain.vo;

import com.xhzb.nursing.domain.DeviceData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 设备及数据VO（用于房间/床位的设备展示）
 *
 * @author ruoyi
 * @date 2026-06-14
 */
@Data
@Schema(description = "设备及数据VO")
public class DeviceVo {

    /** 设备ID */
    @Schema(title = "设备ID")
    private Long id;

    /** 物联网设备ID */
    @Schema(title = "物联网设备ID")
    private String iotId;

    /** 设备名称 */
    @Schema(title = "设备名称")
    private String deviceName;

    /** 产品key */
    @Schema(title = "产品key")
    private String productKey;

    /** 产品名称 */
    @Schema(title = "产品名称")
    private String productName;

    /** 设备上报数据列表（从Redis获取） */
    @Schema(title = "设备上报数据列表")
    private List<DeviceData> deviceDataVos;

}
