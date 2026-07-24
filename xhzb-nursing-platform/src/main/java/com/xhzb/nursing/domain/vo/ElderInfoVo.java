package com.xhzb.nursing.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 老人信息详情VO（入住字段填充）
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "老人信息详情VO")
public class ElderInfoVo {

    /** 联系电话 */
    @Schema(title = "联系电话")
    private String phone;

    /** 医疗费用支付方式 */
    @Schema(title = "医疗费用支付方式")
    private String medicalPaymentMethod;

    /** 核心建议，0-不建议入住，1-建议入住 */
    @Schema(title = "核心建议，0-不建议入住，1-建议入住")
    private Integer coreSuggestion;

    /** 民族 */
    @Schema(title = "民族")
    private String nation;

    /** 文化程度 */
    @Schema(title = "文化程度")
    private String educationLevel;

    /** 身份证号 */
    @Schema(title = "身份证号")
    private String idCardNo;

    /** 老人姓名 */
    @Schema(title = "老人姓名")
    private String name;

    /** 社保卡号 */
    @Schema(title = "社保卡号")
    private String socialSecurityCard;

    /** 居住情况 */
    @Schema(title = "居住情况")
    private String livingSituation;

    /** 宗教信仰 */
    @Schema(title = "宗教信仰")
    private String religiousBelief;

    /** 经济来源 */
    @Schema(title = "经济来源")
    private String economicSource;

    /** 婚姻状况 */
    @Schema(title = "婚姻状况")
    private String maritalStatus;
}
