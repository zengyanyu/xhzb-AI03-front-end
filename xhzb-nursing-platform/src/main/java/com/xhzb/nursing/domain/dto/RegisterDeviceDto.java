package com.xhzb.nursing.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 注册设备请求DTO
 *
 * @author songyu
 * @date 2026-08-11
 */
@Data
@Schema(description = "注册设备请求DTO")
public class RegisterDeviceDto
{
    /** 设备名称 */
    @NotBlank(message = "设备名称不能为空")
    @Schema(title = "设备名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deviceName;

    /** 绑定位置（随身设备传老人id，固定设备传房间id或床位id） */
    @NotBlank(message = "绑定位置不能为空")
    @Schema(title = "绑定位置", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bindingLocation;

    /** 位置类型 0：随身设备 1：固定设备 */
    @NotNull(message = "位置类型不能为空")
    @Schema(title = "位置类型 0：随身设备 1：固定设备", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer locationType;

    /** 设备节点id */
    @NotBlank(message = "设备节点id不能为空")
    @Schema(title = "设备节点id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nodeId;

    /** 物理位置类型 0楼层 1房间 2床位，随身设备传-1 */
    @Schema(title = "物理位置类型 0楼层 1房间 2床位，随身设备传-1")
    private Integer physicalLocationType;

    /** 产品key */
    @NotBlank(message = "产品key不能为空")
    @Schema(title = "产品key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productKey;

    /** 产品名称 */
    @NotBlank(message = "产品名称不能为空")
    @Schema(title = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productName;

    /** 备注（随身设备传老人姓名，固定设备传位置描述） */
    @Schema(title = "备注")
    private String remark;

    /** 位置备注 */
    @Schema(title = "位置备注")
    private String deviceDescription;
}
