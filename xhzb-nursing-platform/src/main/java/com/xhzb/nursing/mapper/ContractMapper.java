package com.xhzb.nursing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhzb.nursing.domain.Contract;

/**
 * 合同Mapper接口
 *
 * @author ruoyi
 * @date 2026-07-10
 */
@Mapper
public interface ContractMapper extends BaseMapper<Contract> {
    /**
     * 查询合同
     *
     * @param id 合同主键
     * @return 合同
     */
    Contract selectContractById(Long id);

    /**
     * 查询合同列表
     *
     * @param contract 合同
     * @return 合同集合
     */
    List<Contract> selectContractList(Contract contract);

    /**
     * 新增合同
     *
     * @param contract 合同
     * @return 结果
     */
    int insertContract(Contract contract);

    /**
     * 修改合同
     *
     * @param contract 合同
     * @return 结果
     */
    int updateContract(Contract contract);

    /**
     * 删除合同
     *
     * @param id 合同主键
     * @return 结果
     */
    int deleteContractById(Long id);

    /**
     * 批量删除合同
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteContractByIds(Long[] ids);
}
