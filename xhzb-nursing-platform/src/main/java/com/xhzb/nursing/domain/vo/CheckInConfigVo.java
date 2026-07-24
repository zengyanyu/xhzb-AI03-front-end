package com.xhzb.nursing.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 入住配置VO
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "入住配置VO")
public class CheckInConfigVo {

    /** 创建者 */
    @Schema(title = "创建者")
    private String createBy;

    /** 创建时间 */
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @Schema(title = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 入住表ID */
    @Schema(title = "入住表ID")
    private Long checkInId;

    /** 护理等级ID */
    @Schema(title = "护理等级ID")
    private Long nursingLevelId;

    /** 护理等级名称 */
    @Schema(title = "护理等级名称")
    private String nursingLevelName;

    /** 费用开始时间 */
    @Schema(title = "费用开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime feeStartDate;

    /** 费用结束时间 */
    @Schema(title = "费用结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime feeEndDate;

    /** 押金（元） */
    @Schema(title = "押金")
    private BigDecimal deposit;

    /** 护理费用（元/月） */
    @Schema(title = "护理费用")
    private BigDecimal nursingFee;

    /** 床位费用（元/月） */
    @Schema(title = "床位费用")
    private BigDecimal bedFee;

    /** 医保支付（元/月） */
    @Schema(title = "医保支付")
    private BigDecimal insurancePayment;

    /** 政府补贴（元/月） */
    @Schema(title = "政府补贴")
    private BigDecimal governmentSubsidy;

    /** 其他费用（元/月） */
    @Schema(title = "其他费用")
    private BigDecimal otherFees;

    /** 排序编号 */
    @Schema(title = "排序编号")
    private Integer sortOrder;

    /** 入住开始时间 */
    @Schema(title = "入住开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    /** 入住结束时间 */
    @Schema(title = "入住结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    /** 床位编号 */
    @Schema(title = "床位编号")
    private String bedNumber;
}
