package com.xhzb.nursing.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 老人对象 elder
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Data
@Schema(description = "老人对象")
public class Elder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Schema(title = "id")
    private Long id;

    /** 名称 */
    @Schema(title = "名称")
    @Excel(name = "名称")
    private String name;

    /** 头像 */
    @Schema(title = "头像")
    @Excel(name = "头像")
    private String image;

    /** 身份证号 */
    @Schema(title = "身份证号")
    @Excel(name = "身份证号")
    private String idCardNo;

    /** 性别（0:女  1:男） */
    @Schema(title = "性别（0:女  1:男）")
    @Excel(name = "性别", readConverterExp = "0=:女,1=:男")
    private Integer sex;

    /** 状态（0:禁用，1:已入住 2:请假 3:已退住） */
    @Schema(title = "状态（0:禁用，1:已入住 2:请假 3:已退住）")
    @Excel(name = "状态", readConverterExp = "0=:禁用，1:已入住,2=:请假,3=:已退住")
    private Integer status;

    /** 手机号 */
    @Schema(title = "手机号")
    @Excel(name = "手机号")
    private String phone;

    /** 出生日期 */
    @Schema(title = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime birthday;

    /** 家庭住址 */
    @Schema(title = "家庭住址")
    @Excel(name = "家庭住址")
    private String address;

    /** 身份证国徽面 */
    @Schema(title = "身份证国徽面")
    @Excel(name = "身份证国徽面")
    private String idCardNationalEmblemImg;

    /** 身份证人像面 */
    @Schema(title = "身份证人像面")
    @Excel(name = "身份证人像面")
    private String idCardPortraitImg;

    /** 床位编号 */
    @Schema(title = "床位编号")
    @Excel(name = "床位编号")
    private String bedNumber;

    /** 床位id */
    @Schema(title = "床位id")
    @Excel(name = "床位id")
    private Long bedId;

    /** 民族 */
    @Schema(title = "民族")
    @Excel(name = "民族")
    private String nation;

    /** 文化程度 */
    @Schema(title = "文化程度")
    @Excel(name = "文化程度")
    private String educationLevel;

    /** 社保卡号 */
    @Schema(title = "社保卡号")
    @Excel(name = "社保卡号")
    private String socialSecurityCard;

    /** 居住情况 */
    @Schema(title = "居住情况")
    @Excel(name = "居住情况")
    private String livingSituation;

    /** 宗教信仰 */
    @Schema(title = "宗教信仰")
    @Excel(name = "宗教信仰")
    private String religiousBelief;

    /** 经济来源 */
    @Schema(title = "经济来源")
    @Excel(name = "经济来源")
    private String economicSource;

    /** 婚姻状况 */
    @Schema(title = "婚姻状况")
    @Excel(name = "婚姻状况")
    private String maritalStatus;

    /** 医疗费用支付方式 */
    @Schema(title = "医疗费用支付方式")
    @Excel(name = "医疗费用支付方式")
    private String medicalPaymentMethod;

    /** 核心建议 */
    @Schema(title = "核心建议")
    @Excel(name = "核心建议")
    private String coreSuggestion;

}
