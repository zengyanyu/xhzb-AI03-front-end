package com.xhzb.nursing.domain.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 基本信息
 */
@Data
@Schema(description = "基本信息")
public class BasicInfo {


    /**
     * 老人姓名
     */
    @JsonProperty("elderName")
    @Schema(title = "老人姓名")
    private String elderName;

    /**
     *  身份证号码
     */
    @JsonProperty("idCard")
    @Schema(title = "身份证号码")
    private String idCard;

    /**
     * 出生日期
     */
    @JsonProperty("birthDate")
    @Schema(title = "出生日期")
    private String birthDate;

    /**
     * 年龄
     */
    @JsonProperty("age")
    @Schema(title = "年龄")
    private Integer age;

    /**
     * 性别
     */
    @JsonProperty("gender")
    @Schema(title = "性别")
    private String gender;

    /**
     * 民族
     */
    @JsonProperty("nation")
    @Schema(title = "民族")
    private String nation;

    /**
     * 社保卡号
     */
    @JsonProperty("socialSecurityCard")
    @Schema(title = "社保卡号")
    private String socialSecurityCard;

    /**
     * 宗教信仰
     */
    @JsonProperty("religiousBelief")
    @Schema(title = "宗教信仰")
    private String religiousBelief;

    /**
     * 文化程度
     */
    @JsonProperty("educationLevel")
    @Schema(title = "文化程度")
    private String educationLevel;

    /**
     * 婚姻状况
     */
    @JsonProperty("maritalStatus")
    @Schema(title = "婚姻状况")
    private String maritalStatus;

    /**
     * 居住情况
     */
    @JsonProperty("livingSituation")
    @Schema(title = "居住情况")
    private String livingSituation;

    /**
     * 医疗付款方式
     */
    @JsonProperty("medicalPaymentMethod")
    @Schema(title = "医疗付款方式")
    private String medicalPaymentMethod;

    /**
     * 经济来源
     */
    @JsonProperty("economicSource")
    @Schema(title = "经济来源")
    private String economicSource;

    /**
     * 老人联系方式
     */
    @JsonProperty("elderContact")
    @Schema(title = "老人联系方式")
    private String elderContact;

    /**
     * 信息提供者姓名
     */
    @JsonProperty("providerName")
    @Schema(title = "信息提供者姓名")
    private String providerName;

    /**
     * 信息提供者联系方式
     */
    @JsonProperty("providerContact")
    @Schema(title = "信息提供者联系方式")
    private String providerContact;

    /**
     * 与老人关系
     */
    @JsonProperty("relationshipWithElder")
    @Schema(title = "与老人关系")
    private String relationshipWithElder;

    /**
     * 知情同意书URL
     */
    @JsonProperty("informedConsentUrl")
    @Schema(title = "知情同意书URL")
    private String informedConsentUrl;
}