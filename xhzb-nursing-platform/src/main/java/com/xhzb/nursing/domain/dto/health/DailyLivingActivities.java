package com.xhzb.nursing.domain.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 日常生活活动
 */
@Schema(description = "日常活动")
@Data
public class DailyLivingActivities {
    /**
     * 进食
     */
    @JsonProperty("eating")
    @Schema(title = "进食")
    private String eating;

    /**
     *  洗澡
     */
    @JsonProperty("bathing")
    @Schema(title = "洗澡")
    private String bathing;

    /**
     * 修饰
     */
    @JsonProperty("grooming")
    @Schema(title = "修饰")
    private String grooming;

    /**
     *  穿衣
     */
    @JsonProperty("dressing")
    @Schema(title = "穿衣")
    private String dressing;

    /**
     *  大便控制
     */
    @JsonProperty("bowelControl")
    @Schema(title = "大便控制")
    private String bowelControl;

    /**
     *  小便控制
     */
    @JsonProperty("bladderControl")
    @Schema(title = "小便控制")
    private String bladderControl;

    /**
     *   如厕
     */
    @JsonProperty("toileting")
    @Schema(title = "如厕")
    private String toileting;

    /**
     *   床椅转椅
     */
    @JsonProperty("chairTransfer")
    @Schema(title = "床椅转椅")
    private String chairTransfer;

    /**
     *   走动
     */
    @JsonProperty("walkingOnFlatGround")
    @Schema(title = "走动")
    private String walkingOnFlatGround;

    /**
     *   上下楼梯
     */
    @JsonProperty("upDownStairs")
    @Schema(title = "上下楼梯")
    private String upDownStairs;

    /**
     *   总分
     */
    @JsonProperty("totalScore")
    @Schema(title = "总分")
    private Integer totalScore;

    /**
     *   能力等级
     */
    @JsonProperty("abilityRating")
    @Schema(title = "能力等级")
    private String abilityRating;
}