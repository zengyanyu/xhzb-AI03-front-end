package com.xhzb.nursing.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 报警数据对象 alert_data
 * 
 * @author ruoyi
 * @date 2026-08-18
 */
@Data
@Schema(description = "报警数据对象")
public class AlertData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(title = "主键")
    private Long id;

    /** 物联网设备id */
    @Schema(title = "物联网设备id")
    @Excel(name = "物联网设备id")
    private String iotId;

    /** 设备名称 */
    @Schema(title = "设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /** 所属产品key */
    @Schema(title = "所属产品key")
    @Excel(name = "所属产品key")
    private String productKey;

    /** 产品名称 */
    @Schema(title = "产品名称")
    @Excel(name = "产品名称")
    private String productName;

    /** 功能标识符 */
    @Schema(title = "功能标识符")
    @Excel(name = "功能标识符")
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

    /** 报警规则id */
    @Schema(title = "报警规则id")
    @Excel(name = "报警规则id")
    private Long alertRuleId;

    /** 报警原因，格式：功能名称+运算符+阈值+持续周期+聚合周期 */
    @Schema(title = "报警原因，格式：功能名称+运算符+阈值+持续周期+聚合周期")
    @Excel(name = "报警原因，格式：功能名称+运算符+阈值+持续周期+聚合周期")
    private String alertReason;

    /** 处理结果 */
    @Schema(title = "处理结果")
    @Excel(name = "处理结果")
    private String processingResult;

    /** 处理人id */
    @Schema(title = "处理人id")
    @Excel(name = "处理人id")
    private Long processorId;

    /** 处理人名称 */
    @Schema(title = "处理人名称")
    @Excel(name = "处理人名称")
    private String processorName;

    /** 处理时间 */
    @Schema(title = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime processingTime;

    /** 报警数据类型，0：老人异常数据，1：设备异常数据 */
    @Schema(title = "报警数据类型，0：老人异常数据，1：设备异常数据")
    @Excel(name = "报警数据类型，0：老人异常数据，1：设备异常数据")
    private Integer type;

    /** 状态，0：待处理，1：已处理 */
    @Schema(title = "状态，0：待处理，1：已处理")
    @Excel(name = "状态，0：待处理，1：已处理")
    private Integer status;

    /** 接收人id */
    @Schema(title = "接收人id")
    @Excel(name = "接收人id")
    private Long userId;

}
