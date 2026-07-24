package com.xhzb.nursing.controller.member;

import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.nursing.domain.RoomType;
import com.xhzb.nursing.service.IRoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member/roomTypes")
@Tag(name = "客户房型管理")
public class MemberRoomTypeController extends BaseController {

    @Autowired
    private IRoomTypeService roomTypeService;

    @GetMapping
    @Operation(summary = "根据状态查询房型")
    public AjaxResult findRoomTypeListByStatus(Integer status) {

        List<RoomType> roomTypeVoList = roomTypeService.findRoomTypeListByStatus(status);
        return success(roomTypeVoList);
    }

}
