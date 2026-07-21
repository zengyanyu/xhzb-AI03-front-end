package com.xhzb.nursing.controller;

import com.xhzb.common.annotation.Log;
import com.xhzb.common.core.controller.BaseController;
import com.xhzb.common.core.domain.AjaxResult;
import com.xhzb.common.core.page.TableDataInfo;
import com.xhzb.common.enums.BusinessType;
import com.xhzb.common.utils.poi.ExcelUtil;
import com.xhzb.nursing.domain.KnowledgeBase;
import com.xhzb.nursing.service.IKnowledgeBaseService;
import com.xhzb.oss.client.OSSAliyunFileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * 知识库主Controller
 * 
 * @author ruoyi
 * @date 2026-07-21
 */
@RestController
@RequestMapping("/nursing/knowledgeBase")
@Tag(name = "知识库主相关接口")
public class KnowledgeBaseController extends BaseController
{
    @Autowired
    private IKnowledgeBaseService knowledgeBaseService;

    /**
     * 查询知识库主列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:knowledgeBase:list')")
    @GetMapping("/list")
    @Operation(summary = "查询知识库主列表")
    public TableDataInfo list(KnowledgeBase knowledgeBase)
    {
        startPage();
        List<KnowledgeBase> list = knowledgeBaseService.selectKnowledgeBaseList(knowledgeBase);
        return getDataTable(list);
    }

    /**
     * 导出知识库主列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:knowledgeBase:export')")
    @Log(title = "知识库主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出知识库主列表")
    public void export(HttpServletResponse response, KnowledgeBase knowledgeBase)
    {
        List<KnowledgeBase> list = knowledgeBaseService.selectKnowledgeBaseList(knowledgeBase);
        ExcelUtil<KnowledgeBase> util = new ExcelUtil<KnowledgeBase>(KnowledgeBase.class);
        util.exportExcel(response, list, "知识库主数据");
    }

    /**
     * 获取知识库主详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:knowledgeBase:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取知识库主详细信息")
    public AjaxResult getInfo(@Schema(name = "知识库主ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(knowledgeBaseService.selectKnowledgeBaseById(id));
    }

    /**
     * 新增知识库主
     */
    @PreAuthorize("@ss.hasPermi('nursing:knowledgeBase:add')")
    @Log(title = "知识库主", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增知识库主")
    public AjaxResult add(@RequestBody KnowledgeBase knowledgeBase)
    {
        return toAjax(knowledgeBaseService.insertKnowledgeBase(knowledgeBase));
    }

    /**
     * 修改知识库主
     */
    @PreAuthorize("@ss.hasPermi('nursing:knowledgeBase:edit')")
    @Log(title = "知识库主", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改知识库主")
    public AjaxResult edit(@RequestBody KnowledgeBase knowledgeBase)
    {
        return toAjax(knowledgeBaseService.updateKnowledgeBase(knowledgeBase));
    }

    /**
     * 删除知识库主
     */
    @PreAuthorize("@ss.hasPermi('nursing:knowledgeBase:remove')")
    @Log(title = "知识库主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除知识库主")
    public AjaxResult remove(@Schema(name = "知识库主ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(knowledgeBaseService.deleteKnowledgeBaseByIds(ids));
    }

    @Autowired
    private OSSAliyunFileStorageService fileStorageService;

    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            //文件名--->UUID.后缀
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            //把文件上传到oss中
            String url = fileStorageService.store(filename, file.getInputStream());

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", url);
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
