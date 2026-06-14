package com.xhzb.nursing.domain;

import com.xhzb.common.annotation.Excel;
import com.xhzb.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 房型对象 room_type
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "房型实体")
public class RoomType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 房型名称 */
    @Excel(name = "房型名称")
    @Schema(title = "房型名称")
    private String name;

    /** 床位数量 */
    @Excel(name = "床位数量")
    @Schema(title = "床位数量")
    private Long bedCount;

    /** 床位费用 */
    @Excel(name = "床位费用")
    @Schema(title = "床位费用")
    private BigDecimal price;

    /** 介绍 */
    @Excel(name = "介绍")
    @Schema(title = "介绍")
    private String introduction;

    /** 照片 */
    @Excel(name = "照片")
    @Schema(title = "照片")
    private String photo;

    /** 状态，0：禁用，1：启用 */
    @Excel(name = "状态，0：禁用，1：启用")
    @Schema(title = "状态，0：禁用，1：启用")
    private Long status;




}
