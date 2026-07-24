package com.xhzb.nursing.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhzb.common.exception.ServiceException;
import com.xhzb.common.utils.DateUtils;
import com.xhzb.common.utils.SecurityUtils;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.nursing.domain.*;
import com.xhzb.nursing.domain.dto.CheckInApplyDto;
import com.xhzb.nursing.domain.dto.CheckInConfigDto;
import com.xhzb.nursing.domain.dto.CheckInContractDto;
import com.xhzb.nursing.domain.dto.CheckInElderDto;
import com.xhzb.nursing.domain.dto.ElderFamilyDto;
import com.xhzb.nursing.domain.vo.*;
import com.xhzb.nursing.mapper.CheckInConfigMapper;
import com.xhzb.nursing.mapper.CheckInMapper;
import com.xhzb.nursing.mapper.ContractMapper;
import com.xhzb.nursing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入住Service业务层处理
 *
 * @author ruoyi
 * @date 2026-07-10
 */
@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements ICheckInService
{
    @Autowired
    private CheckInMapper checkInMapper;

    @Autowired
    private CheckInConfigMapper checkInConfigMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private IElderService elderService;

    @Autowired
    private IContractService contractService;

    @Autowired
    private ICheckInConfigService checkInConfigService;

    @Autowired
    private IHealthAssessmentService healthAssessmentService;

    @Autowired
    private IHealthAssessmentReportService healthAssessmentReportService;

    @Autowired
    private IBedService bedService;

    /**
     * 查询入住
     *
     * @param id 入住主键
     * @return 入住
     */
    @Override
    public CheckIn selectCheckInById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询入住列表
     *
     * @param checkIn 入住
     * @return 入住
     */
    @Override
    public List<CheckIn> selectCheckInList(CheckIn checkIn)
    {
        return checkInMapper.selectCheckInList(checkIn);
    }

    /**
     * 新增入住
     *
     * @param checkIn 入住
     * @return 结果
     */
    @Override
    public int insertCheckIn(CheckIn checkIn)
    {
        return save(checkIn)? 1 : 0;
    }

    /**
     * 修改入住
     *
     * @param checkIn 入住
     * @return 结果
     */
    @Override
    public int updateCheckIn(CheckIn checkIn)
    {
        return updateById(checkIn)? 1 : 0;
    }

    /**
     * 批量删除入住
     *
     * @param ids 需要删除的入住主键
     * @return 结果
     */
    @Override
    public int deleteCheckInByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除入住信息
     *
     * @param id 入住主键
     * @return 结果
     */
    @Override
    public int deleteCheckInById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    /**
     * 申请入住（核心业务逻辑）
     *
     * @param dto 申请入住请求DTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void apply(CheckInApplyDto dto) {
        CheckInElderDto elderDto = dto.getCheckInElderDto();
        CheckInConfigDto configDto = dto.getCheckInConfigDto();
        CheckInContractDto contractDto = dto.getCheckInContractDto();

        // 1. 判断评估是否完成，未完成不能入住
        HealthAssessment assessment = healthAssessmentService.getById(dto.getHealthAssessmentId());
        if (assessment == null) {
            throw new ServiceException("评估记录不存在");
        }
        if (assessment.getEvaluationProgress() == null || assessment.getEvaluationProgress() != 1) {
            throw new ServiceException("未完成评估不能入住");
        }

        // 2. 校验老人是否已入住（根据身份证号查询老人表，判断status是否为已入住）
        Elder existElder = elderService.getOne(new LambdaQueryWrapper<Elder>()
                .eq(Elder::getIdCardNo, elderDto.getIdCardNo()));
        if (existElder != null && existElder.getStatus() != null && existElder.getStatus() == 1) {
            throw new ServiceException("老人已入住");
        }

        // 3. 更新床位状态为已入住
        Bed bed = bedService.getById(Long.valueOf(configDto.getBedId()));
        if (bed == null) {
            throw new ServiceException("床位不存在");
        }
        bed.setBedStatus(1);
        bed.setUpdateTime(DateUtils.getNowDate());
        bedService.updateById(bed);

        // 4. 新增或更新老人
        Elder elder;
        if (existElder != null) {
            // 更新已有老人信息
            elder = existElder;
        } else {
            elder = new Elder();
        }
        elder.setName(elderDto.getName());
        elder.setImage(elderDto.getImage());
        elder.setIdCardNo(elderDto.getIdCardNo());
        elder.setSex(elderDto.getSex());
        elder.setStatus(1);  // 已入住
        elder.setPhone(elderDto.getPhone());
        if (StringUtils.isNotEmpty(elderDto.getBirthday())) {
            LocalDateTime birthday = parseDateTime(elderDto.getBirthday());
            if (birthday != null) {
                elder.setBirthday(birthday);
            }
        }
        elder.setAddress(elderDto.getAddress());
        elder.setIdCardNationalEmblemImg(elderDto.getIdCardNationalEmblemImg());
        elder.setIdCardPortraitImg(elderDto.getIdCardPortraitImg());
        elder.setBedNumber(configDto.getCode());
        elder.setBedId(Long.valueOf(configDto.getBedId()));
        elder.setNation(elderDto.getNation());
        elder.setEducationLevel(elderDto.getEducationLevel());
        elder.setSocialSecurityCard(elderDto.getSocialSecurityCard());
        elder.setLivingSituation(elderDto.getLivingSituation());
        elder.setReligiousBelief(elderDto.getReligiousBelief());
        elder.setEconomicSource(elderDto.getEconomicSource());
        elder.setMaritalStatus(elderDto.getMaritalStatus());
        elder.setMedicalPaymentMethod(elderDto.getMedicalPaymentMethod());
        elder.setCoreSuggestion(elderDto.getCoreSuggestion());
        elder.setUpdateTime(DateUtils.getNowDate());

        if (existElder != null) {
            elderService.updateById(elder);
        } else {
            elder.setCreateTime(DateUtils.getNowDate());
            elderService.save(elder);
        }

        // 5. 新增签约合同（合同号：HT + yyyyMMddHHmmss + 4位递增值）
        Contract contract = new Contract();
        contract.setElderId(elder.getId());
        contract.setContractName(contractDto.getContractName());
        // 生成合同编号
        String contractNumber = generateContractNumber();
        contract.setContractNumber(contractNumber);
        contract.setAgreementPath(contractDto.getAgreementPath());
        contract.setThirdPartyPhone(contractDto.getThirdPartyPhone());
        contract.setThirdPartyName(contractDto.getThirdPartyName());
        contract.setElderName(elderDto.getName());
        // 解析入住开始和结束时间
        LocalDateTime startDateTime = parseDateTime(configDto.getStartDate());
        LocalDateTime endDateTime = parseDateTime(configDto.getEndDate());
        contract.setStartDate(startDateTime);
        contract.setEndDate(endDateTime);
        // 根据签约日期判断合同状态：签约日期 <= 当前时间则为已生效，否则未生效
        LocalDateTime signDate = parseDateTime(contractDto.getSignDate());
        contract.setSignDate(signDate);
        if (signDate != null && !signDate.isAfter(LocalDateTime.now())) {
            contract.setStatus(1);  // 已生效
        } else {
            contract.setStatus(0);  // 未生效
        }
        contract.setSortOrder(0);
        contract.setCreateTime(DateUtils.getNowDate());
        contract.setCreateBy(SecurityUtils.getUsername());
        contractService.save(contract);

        // 6. 新增入住（家属信息转JSON保存到remark字段）
        CheckIn checkIn = new CheckIn();
        checkIn.setElderName(elderDto.getName());
        checkIn.setElderId(elder.getId());
        checkIn.setIdCardNo(elderDto.getIdCardNo());
        checkIn.setStartDate(startDateTime);
        checkIn.setEndDate(endDateTime);
        checkIn.setNursingLevelName(configDto.getNursingLevelName());
        checkIn.setBedNumber(configDto.getCode());
        checkIn.setStatus(0);  // 已入住
        checkIn.setSortOrder(0);
        checkIn.setCreateTime(DateUtils.getNowDate());
        checkIn.setCreateBy(SecurityUtils.getUsername());
        // 家属信息转JSON保存到remark
        if (dto.getElderFamilyDtoList() != null && !dto.getElderFamilyDtoList().isEmpty()) {
            checkIn.setRemark(JSONUtil.toJsonStr(dto.getElderFamilyDtoList()));
        }
        save(checkIn);

        // 7. 新增入住配置
        CheckInConfig config = new CheckInConfig();
        config.setCheckInId(checkIn.getId());
        config.setNursingLevelId(configDto.getNursingLevelId());
        config.setNursingLevelName(configDto.getNursingLevelName());
        // 费用日期
        if (StringUtils.isNotEmpty(configDto.getFeeStartDate())) {
            config.setFeeStartDate(parseDateTime(configDto.getFeeStartDate()));
        }
        if (StringUtils.isNotEmpty(configDto.getFeeEndDate())) {
            config.setFeeEndDate(parseDateTime(configDto.getFeeEndDate()));
        }
        config.setDeposit(configDto.getDeposit() != null ? configDto.getDeposit() : BigDecimal.ZERO);
        config.setNursingFee(configDto.getNursingFee() != null ? configDto.getNursingFee() : BigDecimal.ZERO);
        config.setBedFee(configDto.getBedFee() != null ? new BigDecimal(configDto.getBedFee()) : BigDecimal.ZERO);
        config.setInsurancePayment(configDto.getInsurancePayment() != null ? configDto.getInsurancePayment() : BigDecimal.ZERO);
        config.setGovernmentSubsidy(configDto.getGovernmentSubsidy() != null ? configDto.getGovernmentSubsidy() : BigDecimal.ZERO);
        config.setOtherFees(configDto.getOtherFees() != null ? configDto.getOtherFees() : BigDecimal.ZERO);
        config.setSortOrder(0);
        // 费用起止日期
        if (StringUtils.isNotEmpty(configDto.getFeeStartDate())) {
            config.setFeeStartDate(parseDateTime(configDto.getFeeStartDate()));
        }
        if (StringUtils.isNotEmpty(configDto.getFeeEndDate())) {
            config.setFeeEndDate(parseDateTime(configDto.getFeeEndDate()));
        }
        config.setCreateTime(DateUtils.getNowDate());
        config.setCreateBy(SecurityUtils.getUsername());
        // 通过Mapper插入（支持CheckInConfig的insert语句）
        checkInConfigMapper.insertCheckInConfig(config);

        // 8. 修改入住评估表，做老人关联
        assessment.setElderId(elder.getId());
        assessment.setCheckInStatus(1);  // 已入住
        assessment.setUpdateTime(DateUtils.getNowDate());
        healthAssessmentService.updateById(assessment);

        // 9. 修改入住评估结果表，修改为已入住
        HealthAssessmentReport report = new HealthAssessmentReport();
        report.setHealthAssessmentId(dto.getHealthAssessmentId());
        List<HealthAssessmentReport> reportList = healthAssessmentReportService.selectHealthAssessmentReportList(report);
        if (reportList != null && !reportList.isEmpty()) {
            HealthAssessmentReport existReport = reportList.get(0);
            existReport.setCheckInStatus(1L);  // 已入住
            existReport.setUpdateTime(DateUtils.getNowDate());
            healthAssessmentReportService.updateById(existReport);
        }
    }

    /**
     * 查询入住详情
     *
     * @param id 入住ID
     * @return 入住详情VO
     */
    @Override
    public CheckInDetailVo getCheckInDetail(Long id) {
        // 1. 查询入住记录
        CheckIn checkIn = getById(id);
        if (checkIn == null) {
            throw new ServiceException("入住记录不存在");
        }

        // 2. 查询老人信息
        Elder elder = elderService.getById(checkIn.getElderId());
        if (elder == null) {
            throw new ServiceException("老人信息不存在");
        }

        // 3. 查询入住配置
        CheckInConfig queryConfig = new CheckInConfig();
        queryConfig.setCheckInId(id);
        List<CheckInConfig> configList = checkInConfigService.selectCheckInConfigList(queryConfig);
        CheckInConfig config = (configList != null && !configList.isEmpty()) ? configList.get(0) : null;

        // 4. 查询合同
        Contract queryContract = new Contract();
        queryContract.setElderId(elder.getId());
        List<Contract> contractList = contractService.selectContractList(queryContract);
        Contract contract = (contractList != null && !contractList.isEmpty()) ? contractList.get(0) : null;

        // 5. 组装返回VO
        CheckInDetailVo vo = new CheckInDetailVo();

        // 老人信息VO
        CheckInElderVo elderVo = new CheckInElderVo();
        elderVo.setId(elder.getId());
        elderVo.setName(elder.getName());
        elderVo.setIdCardNo(elder.getIdCardNo());
        elderVo.setBirthday(elder.getBirthday() != null ?
                elder.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null);
        elderVo.setSex(elder.getSex());
        elderVo.setPhone(elder.getPhone());
        elderVo.setAddress(elder.getAddress());
        elderVo.setImage(elder.getImage());
        elderVo.setIdCardNationalEmblemImg(elder.getIdCardNationalEmblemImg());
        elderVo.setIdCardPortraitImg(elder.getIdCardPortraitImg());
        if (elder.getBirthday() != null) {
            elderVo.setAge(LocalDate.now().getYear() - elder.getBirthday().getYear());
        }
        elderVo.setNation(elder.getNation());
        elderVo.setEducationLevel(elder.getEducationLevel());
        elderVo.setSocialSecurityCard(elder.getSocialSecurityCard());
        elderVo.setLivingSituation(elder.getLivingSituation());
        elderVo.setReligiousBelief(elder.getReligiousBelief());
        elderVo.setEconomicSource(elder.getEconomicSource());
        elderVo.setMaritalStatus(elder.getMaritalStatus());
        elderVo.setMedicalPaymentMethod(elder.getMedicalPaymentMethod());
        elderVo.setCoreSuggestion(elder.getCoreSuggestion());
        vo.setCheckInElderVo(elderVo);

        // 家属信息列表（从remark字段解析JSON）
        List<ElderFamilyVo> familyVoList = new ArrayList<>();
        if (StringUtils.isNotEmpty(checkIn.getRemark())) {
            try {
                List<ElderFamilyDto> familyDtoList = JSONUtil.toList(checkIn.getRemark(), ElderFamilyDto.class);
                for (ElderFamilyDto familyDto : familyDtoList) {
                    ElderFamilyVo familyVo = new ElderFamilyVo();
                    familyVo.setName(familyDto.getName());
                    familyVo.setPhone(familyDto.getPhone());
                    familyVo.setKinship(familyDto.getKinship());
                    familyVoList.add(familyVo);
                }
            } catch (Exception e) {
                // ignore parse error
            }
        }
        vo.setElderFamilyVoList(familyVoList);

        // 入住配置VO
        if (config != null) {
            CheckInConfigVo configVo = new CheckInConfigVo();
            configVo.setCreateBy(config.getCreateBy());
            configVo.setCreateTime(config.getCreateTime());
            configVo.setUpdateTime(config.getUpdateTime());
            configVo.setId(config.getId());
            configVo.setCheckInId(config.getCheckInId());
            configVo.setNursingLevelId(config.getNursingLevelId());
            configVo.setNursingLevelName(config.getNursingLevelName());
            configVo.setFeeStartDate(config.getFeeStartDate());
            configVo.setFeeEndDate(config.getFeeEndDate());
            configVo.setDeposit(config.getDeposit());
            configVo.setNursingFee(config.getNursingFee());
            configVo.setBedFee(config.getBedFee());
            configVo.setInsurancePayment(config.getInsurancePayment());
            configVo.setGovernmentSubsidy(config.getGovernmentSubsidy());
            configVo.setOtherFees(config.getOtherFees());
            configVo.setSortOrder(config.getSortOrder());
            configVo.setStartDate(checkIn.getStartDate());
            configVo.setEndDate(checkIn.getEndDate());
            configVo.setBedNumber(checkIn.getBedNumber());
            vo.setCheckInConfigVo(configVo);
        }

        // 合同VO
        if (contract != null) {
            ContractVo contractVo = new ContractVo();
            contractVo.setCreateBy(contract.getCreateBy());
            contractVo.setCreateTime(contract.getCreateTime());
            contractVo.setUpdateTime(contract.getUpdateTime());
            contractVo.setId(contract.getId());
            contractVo.setElderId(contract.getElderId());
            contractVo.setContractName(contract.getContractName());
            contractVo.setContractNumber(contract.getContractNumber());
            contractVo.setAgreementPath(contract.getAgreementPath());
            contractVo.setThirdPartyPhone(contract.getThirdPartyPhone());
            contractVo.setThirdPartyName(contract.getThirdPartyName());
            contractVo.setElderName(contract.getElderName());
            contractVo.setStartDate(contract.getStartDate());
            contractVo.setEndDate(contract.getEndDate());
            contractVo.setStatus(contract.getStatus());
            contractVo.setSignDate(contract.getSignDate());
            contractVo.setSortOrder(contract.getSortOrder());
            vo.setContract(contractVo);
        }

        return vo;
    }

    /**
     * 生成合同编号（HT + yyyyMMddHHmmss + 4位递增）
     */
    private String generateContractNumber() {
        String prefix = "HT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // 查询今天已生成的最大合同编号，获取后4位递增值
        Contract queryParam = new Contract();
        List<Contract> list = contractService.selectContractList(queryParam);
        int maxSeq = 0;
        String todayPrefix = "HT" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        for (Contract c : list) {
            if (c.getContractNumber() != null && c.getContractNumber().startsWith(todayPrefix)) {
                try {
                    String seqStr = c.getContractNumber().substring(c.getContractNumber().length() - 4);
                    int seq = Integer.parseInt(seqStr);
                    if (seq > maxSeq) {
                        maxSeq = seq;
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        int nextSeq = maxSeq + 1;
        return prefix + String.format("%04d", nextSeq);
    }

    /**
     * 解析日期时间字符串
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        try {
            // 尝试 yyyy-MM-dd HH:mm:ss 格式
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            try {
                // 尝试 yyyy-MM-dd 格式
                return LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            } catch (Exception e2) {
                return null;
            }
        }
    }
}
