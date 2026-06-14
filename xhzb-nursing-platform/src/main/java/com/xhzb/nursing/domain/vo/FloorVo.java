package com.xhzb.nursing.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "楼层VO")
public class FloorVo {

    @Schema(title = "楼层ID")
    private Long id;

    @Schema(title = "楼层名称")
    private String name;

    @Schema(title = "楼层编号")
    private Integer code;

    @Schema(title = "房间")
    private List<RoomVo> roomVoList;

}
