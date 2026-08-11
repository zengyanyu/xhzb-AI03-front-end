package com.xhzb.nursing.domain;

import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 设备管理对象 device
 * 
 * @author ruoyi
 * @date 2026-07-18
 */
@Data
@Schema(description = "设备管理对象")
public class Device extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    private Long id;

    /** 物联网设备ID */
    @Schema(title = "物联网设备ID")
    @Excel(name = "物联网设备ID")
    private String iotId;

    /** 设备秘钥 */
    @Schema(title = "设备秘钥")
    @Excel(name = "设备秘钥")
    private String secret;

    /** 绑定位置 */
    @Schema(title = "绑定位置")
    @Excel(name = "绑定位置")
    private String bindingLocation;

    /** 位置类型 0：随身设备 1：固定设备 */
    @Schema(title = "位置类型 0：随身设备 1：固定设备")
    @Excel(name = "位置类型 0：随身设备 1：固定设备")
    private Integer locationType;

    /** 物理位置类型 0楼层 1房间 2床位 */
    @Schema(title = "物理位置类型 0楼层 1房间 2床位")
    @Excel(name = "物理位置类型 0楼层 1房间 2床位")
    private Integer physicalLocationType;

    /** 设备名称 */
    @Schema(title = "设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /** 产品key */
    @Schema(title = "产品key")
    @Excel(name = "产品key")
    private String productKey;

    /** 产品名称 */
    @Schema(title = "产品名称")
    @Excel(name = "产品名称")
    private String productName;

    /** 位置备注 */
    @Schema(title = "位置备注")
    @Excel(name = "位置备注")
    private String deviceDescription;

    /** 产品是否包含门禁，0：否，1：是 */
    @Schema(title = "产品是否包含门禁，0：否，1：是")
    @Excel(name = "产品是否包含门禁，0：否，1：是")
    private Integer haveEntranceGuard;

    /** 节点id */
    @Schema(title = "节点id")
    @Excel(name = "节点id")
    private String nodeId;

}
