package com.xhzb.nursing.domain.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 感知与沟通
 */
@Data
@Schema(description = "感知与沟通")
public class PerceptionAndCommunication {

    /**
     * 意识水平
     */
    @JsonProperty("consciousLevel")
    @Schema(title = "意识水平")
    private String consciousLevel;

    /**
     * 视力
     */
    @JsonProperty("vision")
    @Schema(title = "视力")
    private String vision;

    /**
     * 听力
     */
    @JsonProperty("hearing")
    @Schema(title = "听力")
    private String hearing;

    /**
     * 沟通交流
     */
    @JsonProperty("communication")
    @Schema(title = "沟通交流")
    private String communication;

    /**
     * 感知觉与沟通分级
     */
    @JsonProperty("perceptionAndCommunicationLevel")
    @Schema(title = "感知觉与沟通分级")
    private String perceptionAndCommunicationLevel;

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