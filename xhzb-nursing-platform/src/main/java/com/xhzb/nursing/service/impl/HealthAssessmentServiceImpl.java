package com.xhzb.nursing.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhzb.common.exception.ServiceException;
import com.xhzb.common.exception.base.BaseException;
import com.xhzb.common.utils.DateUtils;
import com.xhzb.common.utils.PDFUtil;
import com.xhzb.common.utils.StringUtils;
import com.xhzb.nursing.constants.AssessmentPromptConstants;
import com.xhzb.nursing.domain.HealthAssessment;
import com.xhzb.nursing.domain.HealthAssessmentDataCollection;
import com.xhzb.nursing.domain.HealthAssessmentReport;
import com.xhzb.nursing.domain.dto.health.BasicInfo;
import com.xhzb.nursing.domain.dto.health.ElderAssessmentDto;
import com.xhzb.nursing.domain.dto.health.HealthAssessmentDto;
import com.xhzb.nursing.domain.vo.ElderInfoVo;
import com.xhzb.nursing.mapper.HealthAssessmentMapper;
import com.xhzb.nursing.service.IHealthAssessmentDataCollectionService;
import com.xhzb.nursing.service.IHealthAssessmentReportService;
import com.xhzb.nursing.service.IHealthAssessmentService;
import com.xhzb.oss.client.OSSAliyunFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 健康评估记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-07-10
 */
@Slf4j
@Service
public class HealthAssessmentServiceImpl extends ServiceImpl<HealthAssessmentMapper, HealthAssessment> implements IHealthAssessmentService
{
    @Autowired
    private HealthAssessmentMapper healthAssessmentMapper;

    @Autowired
    private IHealthAssessmentDataCollectionService healthAssessmentDataCollectionService;

    @Autowired
    private IHealthAssessmentReportService healthAssessmentReportService;

    @Autowired
    private OSSAliyunFileStorageService ossAliyunFileStorageService;

    @Autowired
    @Qualifier("chatClientByAssessment")
    private ChatClient assessmentChatClient;

    /**
     * 查询健康评估记录
     * 
     * @param id 健康评估记录主键
     * @return 健康评估记录
     */
    @Override
    public HealthAssessmentDataCollection selectHealthAssessmentById(Long id)
    {
        return healthAssessmentDataCollectionService.getById(id);
    }

    /**
     * 查询健康评估记录列表
     * 
     * @param healthAssessment 健康评估记录
     * @return 健康评估记录
     */
    @Override
    public List<HealthAssessment> selectHealthAssessmentList(HealthAssessment healthAssessment)
    {
        return healthAssessmentMapper.selectHealthAssessmentList(healthAssessment);
    }

    /**
     * 新增健康评估记录
     * 
     * @param dto 健康评估记录
     * @return 结果
     * 新增或修改请求参数都是ElderAssessmentDto封装，区别有id代表进行修改，没有id代表进行新增
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long insertHealthAssessment(ElderAssessmentDto dto)
    {
       return saveOrUpdateHealthAssessment(dto);
    }

    private Long saveOrUpdateHealthAssessment(ElderAssessmentDto dto) {
        //1.新增数据到健康评估记录表
        //1.1 新建健康评估记录表实体类对象
        HealthAssessment healthAssessment = new HealthAssessment();
        //1.2 判断前端是否传递有效健康评估id过来
        if(dto.getId()!=null) {
            //1.2.1 如果传递过来，根据id查询数据库的数据，在此基础上修改
            healthAssessment = getById(dto.getId());
            //判断是否评估完成，如果评估完成不可以修改
            if(healthAssessment!=null && healthAssessment.getEvaluationProgress()!=0){
                //来到这里是不可以修改评估数据，所以结束程序
                throw new BaseException("评估结束，不可以再次修改");
            }
        }
        //1.2 补全数据
        // 老人姓名
        healthAssessment.setElderName(dto.getBasicInfo().getElderName());
        // 身份证号
        healthAssessment.setIdCard(dto.getBasicInfo().getIdCard());
        // 入住状态：未入住 0
        healthAssessment.setCheckInStatus(0);
        // 评估状态：评估中 0
        healthAssessment.setEvaluationProgress(0);
        // 创建时间
        healthAssessment.setCreateTime(DateUtils.getNowDate());
        //1.3 插入或更新数据库
        saveOrUpdate(healthAssessment);

        //2.新增数据到健康数据采集表
        //2.0 实例采集数据实体类
        HealthAssessmentDataCollection hadc = new HealthAssessmentDataCollection();
        //2.1 封装id，与健康评估记录表id一直
        hadc.setId(healthAssessment.getId());
        //2.2 封装-基本信息，json字符串
        hadc.setBasicInfo(JSONUtil.toJsonStr(dto.getBasicInfo()));
        //2.3 封装-日常生活活动，json字符串
        hadc.setDailyLivingActivities(JSONUtil.toJsonStr(dto.getDailyLivingActivities()));
        //2.4 封装-精神状态，json字符串
        hadc.setMentalState(JSONUtil.toJsonStr(dto.getMentalState()));
        //2.5 封装-感知与沟通，json字符串
        hadc.setPerceptionCommunication(JSONUtil.toJsonStr(dto.getPerceptionAndCommunication()));
        //2.6 封装-社会参与，json字符串
        hadc.setSocialParticipation(JSONUtil.toJsonStr(dto.getSocialParticipation()));
        //2.7 封装-健康评估，json字符串
        hadc.setHealthAssessment(JSONUtil.toJsonStr(dto.getHealthAssessmentDto()));
        //2.8 封装-创建时间
        hadc.setCreateTime(DateUtils.getNowDate());
        //2.9 插入或更新数据库信息
        healthAssessmentDataCollectionService.saveOrUpdate(hadc);

        return healthAssessment.getId();
    }

    /**
     * 修改健康评估记录
     * 
     * @param dto 健康评估记录
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long updateHealthAssessment(ElderAssessmentDto dto)
    {
        return saveOrUpdateHealthAssessment(dto);
    }

    /**
     * 批量删除健康评估记录
     * 
     * @param ids 需要删除的健康评估记录主键
     * @return 结果
     */
    @Override
    public int deleteHealthAssessmentByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除健康评估记录信息
     *
     * @param id 健康评估记录主键
     * @return 结果
     */
    @Override
    public int deleteHealthAssessmentById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    /**
     * AI评估分析：保存评估数据并进行两阶段AI分析
     *
     * @param dto 评估数据
     * @return 评估ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long assessmentData(ElderAssessmentDto dto) {
        // 1. 更新保存评估基础数据
        Long assessmentId = saveOrUpdateHealthAssessment(dto);

        try {
            // 2. 提取各项能力等级
            String dailyActivityLevel = dto.getDailyLivingActivities().getAbilityRating();
            String mentalStatusLevel = dto.getMentalState().getAbilityRating();
            String perceptionLevel = dto.getPerceptionAndCommunication().getAbilityRating();
            String socialLevel = dto.getSocialParticipation().getAbilityRating();

            // 提取30天内意外事件数据
            HealthAssessmentDto.Recent30Days recent30Days = dto.getHealthAssessmentDto().getRecent30Days();
            int fallCount = recent30Days.getFall() != null ? recent30Days.getFall() : 0;
            int lostCount = recent30Days.getLost() != null ? recent30Days.getLost() : 0;
            int chokingCount = recent30Days.getChoking() != null ? recent30Days.getChoking() : 0;
            int suicideCount = recent30Days.getSuicideAttempt() != null ? recent30Days.getSuicideAttempt() : 0;
            int comaCount = recent30Days.getComa() != null ? recent30Days.getComa() : 0;

            // 提取疾病诊断信息
            HealthAssessmentDto.DiseaseDiagnosis diseaseDiagnosis = dto.getHealthAssessmentDto().getDiseaseDiagnosis();
            int dementia = convertToFlag(diseaseDiagnosis.getDementia());
            int mentalIllness = convertToFlag(diseaseDiagnosis.getMentalIllness());

            // 是否确诊认知障碍（clockDrawingTest.result == 2 表示确诊认知障碍）
            Integer clockResult = dto.getMentalState().getClockDrawingTest() != null
                    ? dto.getMentalState().getClockDrawingTest().getResult() : 0;
            int cognitiveImpairment = (clockResult != null && clockResult == 2) ? 1 : 0;

            // 3. 第一阶段AI分析：多维度能力评估
            log.info("开始第一阶段AI分析（能力评估），评估ID: {}", assessmentId);
            String abilityPrompt = AssessmentPromptConstants.buildAbilityAssessmentPrompt(
                    dailyActivityLevel, mentalStatusLevel, perceptionLevel, socialLevel,
                    fallCount, chokingCount, suicideCount, lostCount, comaCount,
                    dementia, mentalIllness, cognitiveImpairment);

            String abilityResult = assessmentChatClient.prompt().user(abilityPrompt).call().content();
            log.info("第一阶段AI分析完成，评估ID: {}, 返回内容: {}", assessmentId, abilityResult);

            // 解析能力评估JSON结果
            JSONObject abilityJson = parseJsonObject(abilityResult);
            String preLevel = abilityJson.getStr("preLevel");
            String finalLevel = abilityJson.getStr("finalLevel");
            String levelChangeReason = abilityJson.getStr("reason");

            // 4. 第二阶段AI分析：体检报告评估
            String medicalReportUrl = recent30Days.getMedicalReport();
            String healthScore = null;
            String riskLevel = null;
            String reportSummary = null;
            String abnormalAnalysis = null;
            String systemScore = null;

            if (StringUtils.isNotEmpty(medicalReportUrl)) {
                log.info("开始第二阶段AI分析（体检报告评估），评估ID: {}, 报告URL: {}", assessmentId, medicalReportUrl);

                // 从OSS下载体检报告PDF
                InputStream inputStream = ossAliyunFileStorageService.download(medicalReportUrl);
                if (inputStream == null) {
                    log.warn("体检报告PDF下载失败，URL: {}", medicalReportUrl);
                } else {
                    // 提取PDF文本内容
                    String pdfContent = extractPdfText(inputStream);
                    log.info("PDF文本提取完成，评估ID: {}, 文本长度: {}", assessmentId,
                            pdfContent != null ? pdfContent.length() : 0);

                    if (StringUtils.isNotEmpty(pdfContent)) {
                        // 组装老人健康评估Prompt
                        String healthPrompt = AssessmentPromptConstants.buildHealthAssessmentPrompt(pdfContent);

                        // AI分析体检报告
                        String healthResult = assessmentChatClient.prompt().user(healthPrompt).call().content();
                        log.info("第二阶段AI分析完成，评估ID: {}, 返回内容: {}", assessmentId, healthResult);

                        // 解析体检报告评估JSON结果
                        JSONObject healthJson = parseJsonObject(healthResult);
                        healthScore = healthJson.getStr("healthScore");
                        riskLevel = healthJson.getStr("riskLevel");
                        reportSummary = healthJson.getStr("summarize");

                        // 解析异常分析数组
                        JSONArray abnormalArray = healthJson.getJSONArray("abnormalData");
                        if (abnormalArray != null && !abnormalArray.isEmpty()) {
                            abnormalAnalysis = abnormalArray.toString();
                        }

                        // 解析健康系统分值
                        JSONObject systemScoreObj = healthJson.getJSONObject("systemScore");
                        if (systemScoreObj != null) {
                            systemScore = systemScoreObj.toString();
                        }
                    }
                }
            } else {
                log.info("未提供体检报告URL，跳过第二阶段AI分析，评估ID: {}", assessmentId);
            }

            // 5. 保存到评估结果表
            HealthAssessmentReport report = new HealthAssessmentReport();
            report.setHealthAssessmentId(assessmentId);
            report.setAssessmentTime(LocalDateTime.now());
            report.setDailyActivityLevel(dailyActivityLevel);
            report.setMentalStatusLevel(mentalStatusLevel);
            report.setPerceptionCommunicationLevel(perceptionLevel);
            report.setSocialParticipationLevel(socialLevel);
            report.setInitialAbilityLevel(preLevel);
            report.setFinalAbilityLevel(finalLevel);
            report.setLevelChangeReason(levelChangeReason);
            report.setHealthScore(healthScore);
            report.setRiskLevel(riskLevel);
            report.setReportSummary(reportSummary);
            report.setAbnormalAnalysis(abnormalAnalysis);
            report.setSystemScore(systemScore);
            report.setCreateTime(DateUtils.getNowDate());
            healthAssessmentReportService.save(report);
            log.info("评估报告保存完成，报告ID: {}", report.getId());

            // 6. 更新评估信息状态和入住建议
            HealthAssessment assessment = getById(assessmentId);
            if (assessment != null) {
                assessment.setEvaluationProgress(1); // 评估完成
                // healthScore超过60才建议入住
                if (StringUtils.isNotEmpty(healthScore)) {
                    try {
                        double score = Double.parseDouble(healthScore);
                        assessment.setCoreSuggestion(score > 60 ? 1 : 0);
                    } catch (NumberFormatException e) {
                        log.warn("健康评分解析失败: {}", healthScore);
                    }
                }
                assessment.setUpdateTime(DateUtils.getNowDate());
                updateById(assessment);
            }
            log.info("评估状态更新完成，评估ID: {}, evaluationProgress=1", assessmentId);

            // 7. 返回评估ID
            return assessmentId;

        } catch (Exception e) {
            log.error("AI评估分析失败，评估ID: {}", assessmentId, e);
            throw new ServiceException("AI分析失败");
        }
    }

    /**
     * 根据评估ID查询评估报告
     *
     * @param healthAssessmentId 健康评估ID
     * @return 评估报告
     */
    @Override
    public HealthAssessmentReport selectReportByAssessmentId(Long healthAssessmentId) {
        HealthAssessmentReport queryParam = new HealthAssessmentReport();
        queryParam.setHealthAssessmentId(healthAssessmentId);
        List<HealthAssessmentReport> list = healthAssessmentReportService.selectHealthAssessmentReportList(queryParam);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据评估ID获取老人基本信息（入住字段填充）
     *
     * @param assessmentId 评估ID
     * @return 老人信息VO
     */
    @Override
    public ElderInfoVo getElderInfoByAssessmentId(Long assessmentId) {
        // 1. 从健康评估记录表获取核心建议
        HealthAssessment assessment = getById(assessmentId);
        if (assessment == null) {
            throw new ServiceException("评估记录不存在");
        }

        // 2. 从健康评估数据采集表获取基本信息
        HealthAssessmentDataCollection dataCollection = healthAssessmentDataCollectionService.getById(assessmentId);
        if (dataCollection == null || StringUtils.isEmpty(dataCollection.getBasicInfo())) {
            throw new ServiceException("评估数据不存在");
        }

        // 3. 解析基本信息JSON
        BasicInfo basicInfo = JSONUtil.toBean(dataCollection.getBasicInfo(), BasicInfo.class);

        // 4. 组装返回VO
        ElderInfoVo vo = new ElderInfoVo();
        vo.setPhone(basicInfo.getElderContact());
        vo.setMedicalPaymentMethod(basicInfo.getMedicalPaymentMethod());
        vo.setCoreSuggestion(assessment.getCoreSuggestion());
        vo.setNation(basicInfo.getNation());
        vo.setEducationLevel(basicInfo.getEducationLevel());
        vo.setIdCardNo(basicInfo.getIdCard());
        vo.setName(basicInfo.getElderName());
        vo.setSocialSecurityCard(basicInfo.getSocialSecurityCard());
        vo.setLivingSituation(basicInfo.getLivingSituation());
        vo.setReligiousBelief(basicInfo.getReligiousBelief());
        vo.setEconomicSource(basicInfo.getEconomicSource());
        vo.setMaritalStatus(basicInfo.getMaritalStatus());

        return vo;
    }

    /**
     * 将字符串标志转换为整数标志（"无"→0，其他→1）
     */
    private int convertToFlag(String value) {
        if (StringUtils.isEmpty(value) || "无".equals(value.trim())) {
            return 0;
        }
        return 1;
    }

    /**
     * 从PDF输入流中提取文本内容
     */
    private String extractPdfText(InputStream inputStream) {
        try {
            String content = PDFUtil.pdfToString(inputStream);
            return content;
        } catch (Exception e) {
            log.error("PDF文本提取失败", e);
            throw new ServiceException("体检报告PDF解析失败");
        }
    }

    /**
     * 从AI返回内容中提取并解析JSON对象
     * 处理AI可能返回的markdown代码块包裹或其他额外文本
     */
    private JSONObject parseJsonObject(String aiResponse) {
        if (StringUtils.isEmpty(aiResponse)) {
            throw new ServiceException("AI返回内容为空");
        }
        String jsonStr = aiResponse.trim();
        // 去除markdown代码块包裹
        if (jsonStr.startsWith("```")) {
            int startIndex = jsonStr.indexOf("\n");
            if (startIndex > 0) {
                jsonStr = jsonStr.substring(startIndex + 1);
            }
            if (jsonStr.endsWith("```")) {
                jsonStr = jsonStr.substring(0, jsonStr.length() - 3);
            }
        }
        // 尝试找到JSON对象的起止位置
        int braceStart = jsonStr.indexOf("{");
        int braceEnd = jsonStr.lastIndexOf("}");
        if (braceStart >= 0 && braceEnd > braceStart) {
            jsonStr = jsonStr.substring(braceStart, braceEnd + 1);
        }
        try {
            return JSONUtil.parseObj(jsonStr);
        } catch (Exception e) {
            log.error("JSON解析失败，原始内容: {}", aiResponse, e);
            throw new ServiceException("AI返回结果解析失败");
        }
    }
}
