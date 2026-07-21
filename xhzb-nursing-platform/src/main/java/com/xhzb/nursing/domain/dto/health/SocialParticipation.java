package com.xhzb.nursing.domain.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *  社会参与
 */
@Data
@Schema(description = "社会参与")
public class SocialParticipation {

    /**
     * 生活能力
     */
    @JsonProperty("livingAbility")
    @Schema(title = "生活能力")
    private String livingAbility;

    /**
     * 工作能力
     */
    @JsonProperty("workAbility")
    @Schema(title = "工作能力")
    private String workAbility;

    /**
     * 时间/空间定向
     */
    @JsonProperty("timeSpaceOrientation")
    @Schema(title = "时间/空间定向")
    private String timeSpaceOrientation;

    /**
     * 人物定向
     */
    @JsonProperty("personOrientation")
    @Schema(title = "人物定向")
    private String personOrientation;

    /**
     * 社会技能
     */
    @JsonProperty("socialSkills")
    @Schema(title = "社会技能")
    private String socialSkills;

    /**
     * 总分
     */
    @JsonProperty("totalScore")
    @Schema(title = "总分")
    private Integer totalScore;

    /**
     * 能力等级
     */
    @JsonProperty("abilityRating")
    @Schema(title = "能力等级")
    private String abilityRating;
}