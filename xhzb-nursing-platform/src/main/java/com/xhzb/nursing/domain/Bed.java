package com.xhzb.nursing.domain;

import com.xhzb.common.annotation.Excel;
import com.xhzb.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 床位对象 bed
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "床位实体")
public class Bed extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 床位ID */
    @Schema(title = "床位ID")
    private Long id;

    /** 床位编号 */
    @Excel(name = "床位编号")
    @Schema(title = "床位编号")
    private String bedNumber;

    /** 床位状态: 未入住0, 已入住1 入住申请中2 */
    @Excel(name = "床位状态: 未入住0, 已入住1 入住申请中2")
    @Schema(title = "床位状态: 未入住0, 已入住1 入住申请中2")
    private Integer bedStatus;

    /** 床位号 */
    @Excel(name = "床位号")
    @Schema(title = "床位号")
    private Long sort;

    /** 房间ID */
    @Excel(name = "房间ID")
    @Schema(title = "房间ID")
    private Long roomId;

}
