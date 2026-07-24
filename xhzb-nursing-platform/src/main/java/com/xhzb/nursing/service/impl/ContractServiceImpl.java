package com.xhzb.nursing.service.impl;

import java.util.List;
import com.xhzb.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xhzb.nursing.mapper.ContractMapper;
import com.xhzb.nursing.domain.Contract;
import com.xhzb.nursing.service.IContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 合同Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService
{
    @Autowired
    private ContractMapper contractMapper;

    /**
     * 查询合同
     * 
     * @param id 合同主键
     * @return 合同
     */
    @Override
    public Contract selectContractById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询合同列表
     * 
     * @param contract 合同
     * @return 合同
     */
    @Override
    public List<Contract> selectContractList(Contract contract)
    {
        return contractMapper.selectContractList(contract);
    }

    /**
     * 新增合同
     * 
     * @param contract 合同
     * @return 结果
     */
    @Override
    public int insertContract(Contract contract)
    {
        return save(contract)? 1 : 0;
    }

    /**
     * 修改合同
     * 
     * @param contract 合同
     * @return 结果
     */
    @Override
    public int updateContract(Contract contract)
    {
        return updateById(contract)? 1 : 0;
    }

    /**
     * 批量删除合同
     * 
     * @param ids 需要删除的合同主键
     * @return 结果
     */
    @Override
    public int deleteContractByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除合同信息
     * 
     * @param id 合同主键
     * @return 结果
     */
    @Override
    public int deleteContractById(Long id)
    {
        return removeById(id)? 1 : 0;
    }
}
