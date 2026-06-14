package com.xhzb.nursing.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "护理计划VO")
public class NursingPlanVo {

    /**
     * 护理计划id
     */
    @Schema(title = "护理计划id")
    private Long id;

    /**
     * 排序号
     */
    @Schema(title = "护理计划排序号")
    private Integer sortNo;

    @Schema(title = "护理计划名称")
    private String planName;


    @Schema(title = "状态（0：禁用，1：启用）")
    private Integer status;

    @Schema(title = "护理计划项目计划列表")
    List<NursingProjectPlanVo> projectPlans;

}