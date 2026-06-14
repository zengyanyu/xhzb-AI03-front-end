package com.xhzb.nursing.domain.vo;

import com.xhzb.nursing.domain.NursingLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "护理等级VO")
public class NursingLevelVo extends NursingLevel {

    /**
     * 护理计划名称
     */
    @Schema(title = "护理计划名称")
    private String planName;
}
