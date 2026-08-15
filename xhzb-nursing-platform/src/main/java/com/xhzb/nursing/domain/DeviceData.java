package com.xhzb.nursing.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 设备数据对象 device_data
 * 
 * @author ruoyi
 * @date 2026-08-15
 */
@Data
@Schema(description = "设备数据对象")
public class DeviceData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 告警规则ID，自增主键 */
    @Schema(title = "告警规则ID，自增主键")
    private Long id;

    /** 设备名称 */
    @Schema(title = "设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备ID */
    @Schema(title = "设备ID")
    @Excel(name = "设备ID")
    private String iotId;

    /** 所属产品的key */
    @Schema(title = "所属产品的key")
    @Excel(name = "所属产品的key")
    private String productKey;

    /** 产品名称 */
    @Schema(title = "产品名称")
    @Excel(name = "产品名称")
    private String productName;

    /** 功能名称 */
    @Schema(title = "功能名称")
    @Excel(name = "功能名称")
    private String functionId;

    /** 接入位置 */
    @Schema(title = "接入位置")
    @Excel(name = "接入位置")
    private String accessLocation;

    /** 位置类型 0：随身设备 1：固定设备 */
    @Schema(title = "位置类型 0：随身设备 1：固定设备")
    @Excel(name = "位置类型 0：随身设备 1：固定设备")
    private Integer locationType;

    /** 物理位置类型 0楼层 1房间 2床位 */
    @Schema(title = "物理位置类型 0楼层 1房间 2床位")
    @Excel(name = "物理位置类型 0楼层 1房间 2床位")
    private Integer physicalLocationType;

    /** 位置备注 */
    @Schema(title = "位置备注")
    @Excel(name = "位置备注")
    private String deviceDescription;

    /** 数据值 */
    @Schema(title = "数据值")
    @Excel(name = "数据值")
    private String dataValue;

    /** 数据上报时间 */
    @Schema(title = "数据上报时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "数据上报时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime alarmTime;

}
