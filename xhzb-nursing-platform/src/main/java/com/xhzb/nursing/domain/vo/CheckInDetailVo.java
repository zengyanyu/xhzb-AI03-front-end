package com.xhzb.nursing.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 入住详情VO
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "入住详情VO")
public class CheckInDetailVo {

    /** 老人信息 */
    @Schema(title = "老人信息")
    private CheckInElderVo checkInElderVo;

    /** 家属信息列表 */
    @Schema(title = "家属信息列表")
    private List<ElderFamilyVo> elderFamilyVoList;

    /** 入住配置 */
    @Schema(title = "入住配置")
    private CheckInConfigVo checkInConfigVo;

    /** 合同信息 */
    @Schema(title = "合同信息")
    private ContractVo contract;
}
