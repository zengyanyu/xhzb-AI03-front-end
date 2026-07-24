package com.xhzb.nursing.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 入住配置DTO
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "入住配置DTO")
public class CheckInConfigDto {

    /** 床位费用（元/月） */
    @Schema(title = "床位费用")
    private String bedFee;

    /** 床位ID */
    @Schema(title = "床位ID")
    private String bedId;

    /** 房间编号 */
    @Schema(title = "房间编号")
    private String code;

    /** 押金（元） */
    @Schema(title = "押金")
    private BigDecimal deposit;

    /** 入住结束时间 */
    @Schema(title = "入住结束时间")
    private String endDate;

    /** 费用结束时间 */
    @Schema(title = "费用结束时间")
    private String feeEndDate;

    /** 费用开始时间 */
    @Schema(title = "费用开始时间")
    private String feeStartDate;

    /** 楼层ID */
    @Schema(title = "楼层ID")
    private String floorId;

    /** 楼层名称 */
    @Schema(title = "楼层名称")
    private String floorName;

    /** 政府补贴（元/月） */
    @Schema(title = "政府补贴")
    private BigDecimal governmentSubsidy;

    /** 医保支付（元/月） */
    @Schema(title = "医保支付")
    private BigDecimal insurancePayment;

    /** 护理费用（元/月） */
    @Schema(title = "护理费用")
    private BigDecimal nursingFee;

    /** 护理等级ID */
    @Schema(title = "护理等级ID")
    private Long nursingLevelId;

    /** 护理等级名称 */
    @Schema(title = "护理等级名称")
    private String nursingLevelName;

    /** 其他费用（元/月） */
    @Schema(title = "其他费用")
    private BigDecimal otherFees;

    /** 房间ID */
    @Schema(title = "房间ID")
    private String roomId;

    /** 入住开始时间 */
    @Schema(title = "入住开始时间")
    private String startDate;
}
