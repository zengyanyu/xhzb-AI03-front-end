package com.xhzb.nursing.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xhzb.common.annotation.Excel;
import com.xhzb.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 护理计划对象 nursing_plan
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "护理计划实体")
public class NursingPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Schema(title = "编号")
    private Long id;

    /** 排序号 */
    @Excel(name = "排序号")
    @Schema(title = "排序号")
    private Integer sortNo;

    /** 名称 */
    @Excel(name = "名称")
    @Schema(title = "名称")
    private String planName;

    /** 状态 0禁用 1启用 */
    @Excel(name = "状态 0禁用 1启用")
    @Schema(title = "状态 0禁用 1启用")
    private Integer status;

}
