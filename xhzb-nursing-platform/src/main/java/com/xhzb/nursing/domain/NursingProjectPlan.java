package com.xhzb.nursing.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xhzb.common.annotation.Excel;
import com.xhzb.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 护理计划和项目关联对象 nursing_project_plan
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Schema(description = "护理计划和项目关联实体")
public class NursingProjectPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Schema(title = "id")
    private Long id;

    /** 计划id */
    @Excel(name = "计划id")
    @Schema(title = "计划id")
    private Long planId;

    /** 项目id */
    @Excel(name = "项目id")
    @Schema(title = "项目id")
    private Long projectId;

    /** 计划执行时间 */
    @Excel(name = "计划执行时间")
    @Schema(title = "计划执行时间")
    private String executeTime;

    /** 执行周期 0 天 1 周 2月 */
    @Excel(name = "执行周期 0 天 1 周 2月")
    @Schema(title = "执行周期 0 天 1 周 2月")
    private Long executeCycle;

    /** 执行频次 */
    @Excel(name = "执行频次")
    @Schema(title = "执行频次")
    private Long executeFrequency;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlanId(Long planId) 
    {
        this.planId = planId;
    }

    public Long getPlanId() 
    {
        return planId;
    }
    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setExecuteTime(String executeTime) 
    {
        this.executeTime = executeTime;
    }

    public String getExecuteTime() 
    {
        return executeTime;
    }
    public void setExecuteCycle(Long executeCycle) 
    {
        this.executeCycle = executeCycle;
    }

    public Long getExecuteCycle() 
    {
        return executeCycle;
    }
    public void setExecuteFrequency(Long executeFrequency) 
    {
        this.executeFrequency = executeFrequency;
    }

    public Long getExecuteFrequency() 
    {
        return executeFrequency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planId", getPlanId())
            .append("projectId", getProjectId())
            .append("executeTime", getExecuteTime())
            .append("executeCycle", getExecuteCycle())
            .append("executeFrequency", getExecuteFrequency())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
