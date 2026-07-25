package com.xhzb.nursing.controller.member;

import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.R;
import com.xhzb.common.utils.UserThreadLocal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 预约信息Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/member/reservation")
@Tag(name =  "预约信息相关接口")
public class MemberReservationController extends BaseController
{

    @GetMapping("/cancelled-count")
    @Operation(summary = "查询取消预约数量")
    public R<Integer> getCancelledReservationCount() {
        Long userId = UserThreadLocal.getUserId();
        System.out.println("------"+userId);
        return R.ok(1);
    }

}