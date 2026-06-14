package com.xhzb.nursing.domain;

import com.xhzb.common.annotation.Excel;
import com.xhzb.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 楼层对象 floor
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "楼层实体")
public class Floor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Schema(title = "id")
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    @Schema(title = "名称")
    private String name;

    /** 编号 */
    @Excel(name = "编号")
    @Schema(title = "编号")
    private Long code;

}
