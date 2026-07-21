package com.xhzb.nursing.domain.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 汇总数据
 */
@Data
@Schema(description = "老人健康评估数据")
public class ElderAssessmentDto {

    /**
     * 主键
     */
    @JsonProperty("id")
    @Schema(title = "主键")
    private Long id;

    /**
     * 基本信息
     */
    @JsonProperty("basicInfo")
    @Schema(title = "基本信息")
    private BasicInfo basicInfo;

    /**
     * 健康评估
     */
    @JsonProperty("healthAssessmentDto")
    @Schema(title = "健康评估")
    private HealthAssessmentDto healthAssessmentDto;

    /**
     * 日常活动
     */
    @JsonProperty("dailyLivingActivities")
    @Schema(title = "日常活动")
    private DailyLivingActivities dailyLivingActivities;

    /**
     * 精神状态
     */
    @JsonProperty("mentalState")
    @Schema(title = "精神状态")
    private MentalState mentalState;

    /**
     * 感知觉及沟通
     */
    @JsonProperty("perceptionAndCommunication")
    @Schema(title = "感知觉及沟通")
    private PerceptionAndCommunication perceptionAndCommunication;

    /**
     * 社会参与
     */
    @JsonProperty("socialParticipation")
    @Schema(title = "社会参与")
    private SocialParticipation socialParticipation;
}