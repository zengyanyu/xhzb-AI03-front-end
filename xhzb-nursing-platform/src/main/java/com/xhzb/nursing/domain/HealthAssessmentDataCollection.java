package com.xhzb.nursing.domain;

import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 健康评估数据采集对象 health_assessment_data_collection
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Data
@Schema(description = "健康评估数据采集对象")
public class HealthAssessmentDataCollection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(title = "主键")
    private Long id;

    /** 基本信息 */
    @Schema(title = "基本信息")
    @Excel(name = "基本信息")
    private String basicInfo;

    /** 健康评估 */
    @Schema(title = "健康评估")
    @Excel(name = "健康评估")
    private String healthAssessment;

    /** 日常生活活动 */
    @Schema(title = "日常生活活动")
    @Excel(name = "日常生活活动")
    private String dailyLivingActivities;

    /** 精神状态 */
    @Schema(title = "精神状态")
    @Excel(name = "精神状态")
    private String mentalState;

    /** 感知与沟通 */
    @Schema(title = "感知与沟通")
    @Excel(name = "感知与沟通")
    private String perceptionCommunication;

    /** 社会参与 */
    @Schema(title = "社会参与")
    @Excel(name = "社会参与")
    private String socialParticipation;

    /** 详细评估报告内容 */
    @Schema(title = "详细评估报告内容")
    @Excel(name = "详细评估报告内容")
    private String assessmentDetails;

}
