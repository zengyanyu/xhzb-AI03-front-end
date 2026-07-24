package com.xhzb.nursing.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 老人家属信息VO
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "老人家属信息VO")
public class ElderFamilyVo {

    /** 家属姓名 */
    @Schema(title = "家属姓名")
    private String name;

    /** 家属联系电话 */
    @Schema(title = "家属联系电话")
    private String phone;

    /** 家属关系 */
    @Schema(title = "家属关系")
    private String kinship;
}
