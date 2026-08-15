package com.xhzb.nursing.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 设备详细数据响应VO
 *
 * @author songyu
 * @date 2026-08-15
 */
@Data
@Schema(description = "设备详细数据响应VO")
public class DeviceDetailVo
{
    /** 主键ID（本地库） */
    @Schema(title = "主键ID")
    private Long id;

    /** 物联网设备ID */
    @Schema(title = "物联网设备ID")
    private String iotId;

    /** 设备名称 */
    @Schema(title = "设备名称")
    private String deviceName;

    /** 节点id */
    @Schema(title = "节点id")
    private String nodeId;

    /** 设备秘钥 */
    @Schema(title = "设备秘钥")
    private String secret;

    /** 产品key */
    @Schema(title = "产品key")
    private String productKey;

    /** 产品名称 */
    @Schema(title = "产品名称")
    private String productName;

    /** 位置类型 0：随身设备 1：固定设备 */
    @Schema(title = "位置类型 0：随身设备 1：固定设备")
    private Integer locationType;

    /** 绑定位置 */
    @Schema(title = "绑定位置")
    private String bindingLocation;

    /** 备注 */
    @Schema(title = "备注")
    private String remark;

    /** 设备状态（华为云） */
    @Schema(title = "设备状态")
    private String deviceStatus;

    /** 激活时间（华为云） */
    @Schema(title = "激活时间")
    private String activeTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "创建时间")
    private Date createTime;

    /** 创建人id */
    @Schema(title = "创建人id")
    private Long createBy;

    /** 创建人昵称 */
    @Schema(title = "创建人昵称")
    private String creator;
}
