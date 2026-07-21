package com.xhzb.nursing.domain.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 健康评估
 */

@Data
@Schema(description = "健康评估")
public class HealthAssessmentDto {
    /**
     * 疾病诊断
     */
    @JsonProperty("diseaseDiagnosis")
    @Schema(title = "疾病诊断")
    private DiseaseDiagnosis diseaseDiagnosis;

    /**
     * 最近30天意外事件和体检报告
     */
    @JsonProperty("recent30Days")
    @Schema(title = "最近30天意外事件和体检报告")
    private Recent30Days recent30Days;
    
    @Data
    public static class DiseaseDiagnosis {
        /**
         * 痴呆
         */
        @JsonProperty("dementia")
        @Schema(title = "痴呆")
        private String dementia;
        /**
         * 精神疾病
         */
        @JsonProperty("mentalIllness")
        @Schema(title = "精神疾病")
        private String mentalIllness;
        /**
         * 慢性病
         */
        @JsonProperty("chronicDiseases")
        @Schema(title = "慢性病")
        private String chronicDiseases;
    }
    
    @Data
    public static class Recent30Days {
        /**
         * 跌倒
         */
        @JsonProperty("fall")
        @Schema(title = "跌倒")
        private Integer fall;

        /**
         * 走失
         */
        @JsonProperty("lost")
        @Schema(title = "走失")
        private Integer lost;

        /**
         * 噎食
         */
        @JsonProperty("choking")
        @Schema(title = "噎食")
        private Integer choking;

        /**
         * 自杀
         */
        @JsonProperty("suicideAttempt")
        @Schema(title = "自杀")
        private Integer suicideAttempt;

        /**
         * 昏迷
         */
        @JsonProperty("coma")
        @Schema(title = "昏迷")
        private Integer coma;

        /**
         * 体检报告URL
         */
        @JsonProperty("medicalReport")
        @Schema(title = "体检报告URL")
        private String medicalReport;
    }
}