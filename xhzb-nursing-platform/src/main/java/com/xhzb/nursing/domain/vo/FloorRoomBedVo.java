package com.xhzb.nursing.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 楼层-房间-床位树形VO
 *
 * @author songyu
 * @date 2026-07-24
 */
@Data
@Schema(description = "楼层-房间-床位树形VO")
public class FloorRoomBedVo {

    /** 节点值（ID） */
    @Schema(title = "节点值")
    private String value;

    /** 节点显示名称 */
    @Schema(title = "节点显示名称")
    private String label;

    /** 子节点列表 */
    @Schema(title = "子节点列表")
    private List<FloorRoomBedVo> children;
}
