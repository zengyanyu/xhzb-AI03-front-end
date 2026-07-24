package com.xhzb.nursing.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 签约合同DTO
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "签约合同DTO")
public class CheckInContractDto {

    /** 协议地址（文件路径或URL） */
    @Schema(title = "协议地址")
    private String agreementPath;

    /** 合同名称 */
    @Schema(title = "合同名称")
    private String contractName;

    /** 签约日期 */
    @Schema(title = "签约日期")
    private String signDate;

    /** 丙方姓名（第三方） */
    @Schema(title = "丙方姓名")
    private String thirdPartyName;

    /** 丙方手机号（第三方） */
    @Schema(title = "丙方手机号")
    private String thirdPartyPhone;
}
