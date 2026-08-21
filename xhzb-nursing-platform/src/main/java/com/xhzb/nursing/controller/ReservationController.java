package com.xhzb.nursing.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.xhzb.nursing.domain.Reservation;
import com.xhzb.nursing.service.IReservationService;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.common.core.page.TableDataInfo;

/**
 * 预约信息Controller
 * 
 * @author ruoyi
 * @date 2026-08-21
 */
@RestController
@RequestMapping("/nursing/reservation")
@Tag(name = "预约信息相关接口")
public class ReservationController extends BaseController
{
    @Autowired
    private IReservationService reservationService;

    /**
     * 查询预约信息列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:reservation:list')")
    @GetMapping("/list")
    @Operation(summary = "查询预约信息列表")
    public TableDataInfo list(Reservation reservation)
    {
        startPage();
        List<Reservation> list = reservationService.selectReservationList(reservation);
        return getDataTable(list);
    }

    /**
     * 导出预约信息列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:reservation:export')")
    @Log(title = "预约信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出预约信息列表")
    public void export(HttpServletResponse response, Reservation reservation)
    {
        List<Reservation> list = reservationService.selectReservationList(reservation);
        ExcelUtil<Reservation> util = new ExcelUtil<Reservation>(Reservation.class);
        util.exportExcel(response, list, "预约信息数据");
    }

    /**
     * 获取预约信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:reservation:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取预约信息详细信息")
    public AjaxResult getInfo(@Schema(name = "预约信息ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(reservationService.selectReservationById(id));
    }

    /**
     * 新增预约信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:reservation:add')")
    @Log(title = "预约信息", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增预约信息")
    public AjaxResult add(@RequestBody Reservation reservation)
    {
        return toAjax(reservationService.insertReservation(reservation));
    }

    /**
     * 修改预约信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:reservation:edit')")
    @Log(title = "预约信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改预约信息")
    public AjaxResult edit(@RequestBody Reservation reservation)
    {
        return toAjax(reservationService.updateReservation(reservation));
    }

    /**
     * 删除预约信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:reservation:remove')")
    @Log(title = "预约信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除预约信息")
    public AjaxResult remove(@Schema(name = "预约信息ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(reservationService.deleteReservationByIds(ids));
    }
}
