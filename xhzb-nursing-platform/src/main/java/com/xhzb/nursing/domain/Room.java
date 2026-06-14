package com.xhzb.nursing.domain;

import com.xhzb.common.annotation.Excel;
import com.xhzb.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 房间对象 room
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "房间实体")
public class Room extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 房间编号 */
    @Schema(title = "房间编号")
    @Excel(name = "房间编号")
    private String code;

    /** 排序号 */
    @Schema(title = "排序号")
    @Excel(name = "排序号")
    private Long sort;

    /** 房间类型名称 */
    @Schema(title = "房间类型名称")
    @Excel(name = "房间类型名称")
    private String typeName;

    /** 楼层id */
    @Schema(title = "楼层id")
    @Excel(name = "楼层id")
    private Long floorId;

    /** 是否删除 */
    @Schema(title = "是否删除")
    @Excel(name = "是否删除")
    private Integer isDeleted;




}
