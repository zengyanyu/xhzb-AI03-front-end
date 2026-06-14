package com.xhzb.nursing.domain;

import java.math.BigDecimal;
import lombok.Data;
import com.xhzb.common.annotation.Excel;
import com.xhzb.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 护理项目对象 nursing_project
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "护理项目实体")
public class NursingProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Schema(title = "编号")
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    @Schema(title = "名称")
    private String name;

    /** 排序号 */
    @Excel(name = "排序号")
    @Schema(title = "排序号")
    private Integer orderNo;

    /** 单位 */
    @Excel(name = "单位")
    @Schema(title = "单位")
    private String unit;

    /** 价格 */
    @Excel(name = "价格")
    @Schema(title = "价格")
    private BigDecimal price;

    /** 图片 */
    @Excel(name = "图片")
    @Schema(title = "图片")
    private String image;

    /** 护理要求 */
    @Excel(name = "护理要求")
    @Schema(title = "护理要求")
    private String nursingRequirement;

    /** 状态（0：禁用，1：启用） */
    @Excel(name = "状态", readConverterExp = "0=：禁用，1：启用")
    @Schema(title = "状态（0：禁用，1：启用）")
    private Integer status;



}
