package com.xhzb.nursing.domain;

import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 报警规则对象 alert_rule
 * 
 * @author ruoyi
 * @date 2026-08-16
 */
@Data
@Schema(description = "报警规则对象")
public class AlertRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    private Long id;

    /** 所属产品的key */
    @Schema(title = "所属产品的key")
    @Excel(name = "所属产品的key")
    private String productKey;

    /** 产品名称 */
    @Schema(title = "产品名称")
    @Excel(name = "产品名称")
    private String productName;

    /** 模块的key */
    @Schema(title = "模块的key")
    @Excel(name = "模块的key")
    private String moduleId;

    /** 模块名称 */
    @Schema(title = "模块名称")
    @Excel(name = "模块名称")
    private String moduleName;

    /** 功能名称 */
    @Schema(title = "功能名称")
    @Excel(name = "功能名称")
    private String functionName;

    /** 功能标识 */
    @Schema(title = "功能标识")
    @Excel(name = "功能标识")
    private String functionId;

    /** 物联网设备id */
    @Schema(title = "物联网设备id")
    @Excel(name = "物联网设备id")
    private String iotId;

    /** 设备名称 */
    @Schema(title = "设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /** 报警数据类型，0：老人异常数据，1：设备异常数据 */
    @Schema(title = "报警数据类型，0：老人异常数据，1：设备异常数据")
    @Excel(name = "报警数据类型，0：老人异常数据，1：设备异常数据")
    private Integer alertDataType;

    /** 告警规则名称 */
    @Schema(title = "告警规则名称")
    @Excel(name = "告警规则名称")
    private String alertRuleName;

    /** 运算符 */
    @Schema(title = "运算符")
    @Excel(name = "运算符")
    private String operator;

    /** 阈值 */
    @Schema(title = "阈值")
    @Excel(name = "阈值")
    private Double value;

    /** 持续周期 */
    @Schema(title = "持续周期")
    @Excel(name = "持续周期")
    private Integer duration;

    /** 报警生效时段 */
    @Schema(title = "报警生效时段")
    @Excel(name = "报警生效时段")
    private String alertEffectivePeriod;

    /** 报警沉默周期 */
    @Schema(title = "报警沉默周期")
    @Excel(name = "报警沉默周期")
    private Integer alertSilentPeriod;

    /** 0 禁用 1启用 */
    @Schema(title = "0 禁用 1启用")
    @Excel(name = "0 禁用 1启用")
    private Integer status;

}
