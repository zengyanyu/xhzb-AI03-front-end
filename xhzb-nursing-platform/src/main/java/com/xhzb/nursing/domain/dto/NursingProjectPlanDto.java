package com.xhzb.nursing.domain.dto;

import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "护理计划项目关联DTO")
public class NursingProjectPlanDto {

    /** $column.columnComment */
    @Schema(title = "id")
    private Long id;

    /** 计划id */
    @Schema(title = "计划id")
    private Long planId;

    /** 项目id */
    @Schema(title = "项目id")
    private String projectId;

    /** 计划执行时间 */
    @Schema(title = "计划执行时间")
    private String executeTime;

    /** 执行周期 0 天 1 周 2月 */
    @Schema(title = "执行周期 0 天 1 周 2月")
    private Long executeCycle;

    /** 执行频次 */
    @Schema(title = "执行频次")
    private Long executeFrequency;


    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private Date createTime;
}
