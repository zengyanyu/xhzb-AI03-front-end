package com.xhzb.nursing.domain.vo;

import com.xhzb.common.core.domain.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "床位VO")
public class BedVo {


    @Schema(title = "床位ID")
    private Long id;

    /**
     * 床位号
     */
    @Schema(title = "床位号")
    private String bedNumber;

    /**
     * 床位状态: 未入住0, 已入住1 入住申请中2
     */
    @Schema(title = "床位状态: 未入住0, 已入住1 入住申请中2")
    private Integer bedStatus;

    /**
     * 房间ID
     */
    @Schema(title = "房间ID")
    private Long roomId;

    /**
     * 老人姓名
     */
    @Schema(title = "老人姓名")
    private String ename;

    /**
     * 老人id
     */
    @Schema(title = "老人id")
    private Long elderId;


    @Schema(title = "护理员")
    private List<SysUser> userVos;

}
