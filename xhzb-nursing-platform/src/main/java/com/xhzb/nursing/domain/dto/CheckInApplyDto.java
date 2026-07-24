package com.xhzb.nursing.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 申请入住请求DTO
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "申请入住请求DTO")
public class CheckInApplyDto {

    /** 评估ID */
    @Schema(title = "评估ID")
    private Long healthAssessmentId;

    /** 老人信息 */
    @Schema(title = "老人信息")
    private CheckInElderDto checkInElderDto;

    /** 家属信息列表 */
    @Schema(title = "家属信息列表")
    private List<ElderFamilyDto> elderFamilyDtoList;

    /** 入住配置 */
    @Schema(title = "入住配置")
    private CheckInConfigDto checkInConfigDto;

    /** 签约合同 */
    @Schema(title = "签约合同")
    private CheckInContractDto checkInContractDto;
}
