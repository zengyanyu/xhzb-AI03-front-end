package com.xhzb.nursing.domain;

import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 健康评估记录对象 health_assessment
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Data
@Schema(description = "健康评估记录对象")
public class HealthAssessment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(title = "主键")
    private Long id;

    /** 老人姓名 */
    @Schema(title = "老人姓名")
    @Excel(name = "老人姓名")
    private String elderName;

    /** 身份证号码 */
    @Schema(title = "身份证号码")
    @Excel(name = "身份证号码")
    private String idCard;

    /** 老人id */
    @Schema(title = "老人id")
    @Excel(name = "老人id")
    private Long elderId;

    /** 核心建议，0-不建议入住，1-建议入住 */
    @Schema(title = "核心建议，0-不建议入住，1-建议入住")
    @Excel(name = "核心建议，0-不建议入住，1-建议入住")
    private Integer coreSuggestion;

    /** 入住状态：0-未入住，1-已入住 */
    @Schema(title = "入住状态：0-未入住，1-已入住")
    @Excel(name = "入住状态：0-未入住，1-已入住")
    private Integer checkInStatus;

    /** 评估进度：0-评估中，1-已完成，2-已取消 */
    @Schema(title = "评估进度：0-评估中，1-已完成，2-已取消")
    @Excel(name = "评估进度：0-评估中，1-已完成，2-已取消")
    private Integer evaluationProgress;

}
