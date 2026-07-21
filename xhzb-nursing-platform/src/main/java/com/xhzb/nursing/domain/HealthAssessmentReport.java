package com.xhzb.nursing.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 健康评估报告对象 health_assessment_report
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Data
@Schema(description = "健康评估报告对象")
public class HealthAssessmentReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(title = "主键ID")
    private String id;

    /** 健康评估ID */
    @Schema(title = "健康评估ID")
    @Excel(name = "健康评估ID")
    private Long healthAssessmentId;

    /** 评估时间 */
    @Schema(title = "评估时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "评估时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime assessmentTime;

    /** 评估员姓名 */
    @Schema(title = "评估员姓名")
    @Excel(name = "评估员姓名")
    private String assessorName;

    /** 推荐入住房型（如：标准双人间） */
    @Schema(title = "推荐入住房型（如：标准双人间）")
    @Excel(name = "推荐入住房型", readConverterExp = "如=：标准双人间")
    private String recommendedRoomType;

    /** 推荐护理等级（如：一级护理等级） */
    @Schema(title = "推荐护理等级（如：一级护理等级）")
    @Excel(name = "推荐护理等级", readConverterExp = "如=：一级护理等级")
    private String recommendedCareLevel;

    /** 护理重点（多条用分号分隔，如：血压管控；肢体协助；认知干预） */
    @Schema(title = "护理重点（多条用分号分隔，如：血压管控；肢体协助；认知干预）")
    @Excel(name = "护理重点", readConverterExp = "多=条用分号分隔，如：血压管控；肢体协助；认知干预")
    private String careFocus;

    /** 建议说明（如：老人基础健康状况稳定，日常生活能部分自理） */
    @Schema(title = "建议说明（如：老人基础健康状况稳定，日常生活能部分自理）")
    @Excel(name = "建议说明", readConverterExp = "如=：老人基础健康状况稳定，日常生活能部分自理")
    private String suggestionDescription;

    /** 日常生活活动等级 */
    @Schema(title = "日常生活活动等级")
    @Excel(name = "日常生活活动等级")
    private String dailyActivityLevel;

    /** 精神状态等级 */
    @Schema(title = "精神状态等级")
    @Excel(name = "精神状态等级")
    private String mentalStatusLevel;

    /** 感知觉与沟通等级 */
    @Schema(title = "感知觉与沟通等级")
    @Excel(name = "感知觉与沟通等级")
    private String perceptionCommunicationLevel;

    /** 社会参与等级 */
    @Schema(title = "社会参与等级")
    @Excel(name = "社会参与等级")
    private String socialParticipationLevel;

    /** 老年人能力初步等级 */
    @Schema(title = "老年人能力初步等级")
    @Excel(name = "老年人能力初步等级")
    private String initialAbilityLevel;

    /** 老年人能力最终等级 */
    @Schema(title = "老年人能力最终等级")
    @Excel(name = "老年人能力最终等级")
    private String finalAbilityLevel;

    /** 等级变更依据说明 */
    @Schema(title = "等级变更依据说明")
    @Excel(name = "等级变更依据说明")
    private String levelChangeReason;

    /** 家属配合事项（多条用分号分隔） */
    @Schema(title = "家属配合事项（多条用分号分隔）")
    @Excel(name = "家属配合事项", readConverterExp = "多=条用分号分隔")
    private String familyCooperation;

    /** 饮食建议（如：低盐低脂饮食，控制主食摄入量，避免高糖食物） */
    @Schema(title = "饮食建议（如：低盐低脂饮食，控制主食摄入量，避免高糖食物）")
    @Excel(name = "饮食建议", readConverterExp = "如=：低盐低脂饮食，控制主食摄入量，避免高糖食物")
    private String dietSuggestion;

    /** 心理关怀建议（如：多关注老人情绪变化，每日进行不少于10分钟的一对一沟通） */
    @Schema(title = "心理关怀建议（如：多关注老人情绪变化，每日进行不少于10分钟的一对一沟通）")
    @Excel(name = "心理关怀建议", readConverterExp = "如=：多关注老人情绪变化，每日进行不少于10分钟的一对一沟通")
    private String psychologicalCare;

    /** 机构准备事项（多条用分号分隔，如：提前准备轮椅；房间配置；照护人员适配） */
    @Schema(title = "机构准备事项（多条用分号分隔，如：提前准备轮椅；房间配置；照护人员适配）")
    @Excel(name = "机构准备事项", readConverterExp = "多=条用分号分隔，如：提前准备轮椅；房间配置；照护人员适配")
    private String institutionPreparation;

    /** 健康评分 */
    @Schema(title = "健康评分")
    @Excel(name = "健康评分")
    private String healthScore;

    /** 严重危险(健康, 提示, 风险, 危险, 严重危险) */
    @Schema(title = "严重危险(健康, 提示, 风险, 危险, 严重危险)")
    @Excel(name = "严重危险(健康, 提示, 风险, 危险, 严重危险)")
    private String riskLevel;

    /** 报告总结 */
    @Schema(title = "报告总结")
    @Excel(name = "报告总结")
    private String reportSummary;

    /** 异常分析 */
    @Schema(title = "异常分析")
    @Excel(name = "异常分析")
    private String abnormalAnalysis;

    /** 健康系统分值 */
    @Schema(title = "健康系统分值")
    @Excel(name = "健康系统分值")
    private String systemScore;

    /** 核心建议，0-不建议入住，1-建议入住 */
    @Schema(title = "核心建议，0-不建议入住，1-建议入住")
    @Excel(name = "核心建议，0-不建议入住，1-建议入住")
    private Long coreSuggestion;

    /** 入住状态：0-未入住，1-已入住 */
    @Schema(title = "入住状态：0-未入住，1-已入住")
    @Excel(name = "入住状态：0-未入住，1-已入住")
    private Long checkInStatus;

}
