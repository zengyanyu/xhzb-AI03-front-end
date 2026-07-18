package com.xhzb.nursing.tool;

import com.xhzb.nursing.domain.vo.NursingProjectVo;
import com.xhzb.nursing.service.INursingProjectService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description NursingProjectTool
 * @Author songyu
 * @Date 2026-07-18
 */
@Component//将工具类加入spring容器
public class NursingProjectTool {

    @Autowired
    private INursingProjectService nursingProjectService;

    /**
     * 定义工具（AI大模型可以调用工具）
     * @return
     */
    @Tool(name = "find_project_tool",description = "查询护理项目")
    public List<NursingProjectVo> findAllProjectVo(){
        return nursingProjectService.listAll();
    }
}
