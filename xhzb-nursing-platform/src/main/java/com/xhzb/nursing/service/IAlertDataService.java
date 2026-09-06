package com.xhzb.nursing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhzb.nursing.domain.AlertData;

import java.util.List;

/**
 * 报警数据Service接口
 *
 * @author ruoyi
 * @date 2026-08-18
 */
public interface IAlertDataService extends IService<AlertData> {
    /**
     * 查询报警数据
     *
     * @param id 报警数据主键
     * @return 报警数据
     */
    AlertData selectAlertDataById(Long id);

    /**
     * 查询报警数据列表
     *
     * @param alertData 报警数据
     * @return 报警数据集合
     */
    List<AlertData> selectAlertDataList(AlertData alertData);

    /**
     * 新增报警数据
     *
     * @param alertData 报警数据
     * @return 结果
     */
    int insertAlertData(AlertData alertData);

    /**
     * 修改报警数据
     *
     * @param alertData 报警数据
     * @return 结果
     */
    int updateAlertData(AlertData alertData);

    /**
     * 批量删除报警数据
     *
     * @param ids 需要删除的报警数据主键集合
     * @return 结果
     */
    int deleteAlertDataByIds(Long[] ids);

    /**
     * 删除报警数据信息
     *
     * @param id 报警数据主键
     * @return 结果
     */
    int deleteAlertDataById(Long id);
}
