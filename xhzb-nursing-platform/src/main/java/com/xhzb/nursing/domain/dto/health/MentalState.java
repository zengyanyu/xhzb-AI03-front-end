package com.xhzb.nursing.domain.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 精神状态
 */

@Data
@Schema(description = "精神状态")
public class MentalState {

    /**
     * 回忆词语
     */
    @JsonProperty("recallWords")
    @Schema(title = "回忆词语")
    private String recallWords;

    /**
     * 时钟绘制测试
     */
    @JsonProperty("clockDrawingTest")
    @Schema(title = "时钟绘制测试")
    private ClockDrawingTest clockDrawingTest;

    /**
     * 攻击性行为
     */
    @JsonProperty("aggressiveBehavior")
    @Schema(title = "攻击性行为")
    private String aggressiveBehavior;

    /**
     * 抑郁症状
     */
    @JsonProperty("depressiveSymptoms")
    @Schema(title = "抑郁症状")
    private String depressiveSymptoms;
    
    @Data
    public static class ClockDrawingTest {
        /**
         * 图片地址
         */
        @JsonProperty("url")
        @Schema(title = "图片地址")
        private String url;

        /**
         * 画钟测验结果
         */
        @JsonProperty("result")
        @Schema(title = "画钟测验结果")
        private Integer result;
    }

    /**
     * 选项汇总分数
     */
    @JsonProperty("totalScore")
    @Schema(title = "选项汇总分数")
    private Integer totalScore;

    /**
     * 技能等级
     */
    @JsonProperty("abilityRating")
    @Schema(title = "技能等级")
    private String abilityRating;
}