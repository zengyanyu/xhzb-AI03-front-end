package com.xhzb.nursing.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 合同VO
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "合同VO")
public class ContractVo {

    /** 创建者 */
    @Schema(title = "创建者")
    private String createBy;

    /** 创建时间 */
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @Schema(title = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 老人ID */
    @Schema(title = "老人ID")
    private Long elderId;

    /** 合同名称 */
    @Schema(title = "合同名称")
    private String contractName;

    /** 合同编号 */
    @Schema(title = "合同编号")
    private String contractNumber;

    /** 协议地址（文件路径或URL） */
    @Schema(title = "协议地址")
    private String agreementPath;

    /** 丙方手机号 */
    @Schema(title = "丙方手机号")
    private String thirdPartyPhone;

    /** 丙方姓名 */
    @Schema(title = "丙方姓名")
    private String thirdPartyName;

    /** 老人姓名 */
    @Schema(title = "老人姓名")
    private String elderName;

    /** 开始时间 */
    @Schema(title = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    /** 结束时间 */
    @Schema(title = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    /** 状态 (0: 未生效, 1: 已生效, 2: 已过期, 3: 已失效) */
    @Schema(title = "状态")
    private Integer status;

    /** 签约日期 */
    @Schema(title = "签约日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signDate;

    /** 排序编号 */
    @Schema(title = "排序编号")
    private Integer sortOrder;
}
