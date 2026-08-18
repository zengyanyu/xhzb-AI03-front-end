package com.xhzb.nursing.domain;

import com.xhzb.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 护理员老人关联对象 nursing_elder
 *
 * @author songyu
 * @date 2026-08-18
 */
@Data
@Schema(description = "护理员老人关联对象")
public class NursingElder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Schema(title = "id")
    private Long id;

    /** 护理员id */
    @Schema(title = "护理员id")
    private Long nursingId;

    /** 老人id */
    @Schema(title = "老人id")
    private Long elderId;

}
