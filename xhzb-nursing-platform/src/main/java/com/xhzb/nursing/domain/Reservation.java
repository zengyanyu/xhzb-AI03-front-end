package com.xhzb.nursing.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xhzb.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 预约信息对象 reservation
 * 
 * @author ruoyi
 * @date 2026-08-21
 */
@Data
@Schema(description = "预约信息对象")
public class Reservation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 预约人姓名 */
    @Schema(title = "预约人姓名")
    @Excel(name = "预约人姓名")
    private String name;

    /** 预约人手机号 */
    @Schema(title = "预约人手机号")
    @Excel(name = "预约人手机号")
    private String mobile;

    /** 预约时间 */
    @Schema(title = "预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime time;

    /** 探访人 */
    @Schema(title = "探访人")
    @Excel(name = "探访人")
    private String visitor;

    /** 预约类型，0：参观预约，1：探访预约 */
    @Schema(title = "预约类型，0：参观预约，1：探访预约")
    @Excel(name = "预约类型，0：参观预约，1：探访预约")
    private Integer type;

    /** 预约状态，0：待报道，1：已完成，2：取消，3：过期 */
    @Schema(title = "预约状态，0：待报道，1：已完成，2：取消，3：过期")
    @Excel(name = "预约状态，0：待报道，1：已完成，2：取消，3：过期")
    private Integer status;

}
