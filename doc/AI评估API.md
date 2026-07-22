 ### AI评估

**接口地址**:`/nursing/healthAssessment/assessmentData`

**请求方式**:`POST`

**请求参数**:

```JSON
{
  "id":46, //主键
  // 基本信息
  "basicInfo": {
    "elderName": "李天龙", //老人姓名
    "idCard": "130123194512181234",//身份证号码
    "birthDate": "1945-12-18",//出生日期
    "age": 81,//年龄
    "gender": "男",//性别
    "nation": "汉",//民族
    "socialSecurityCard": "1101101101",//社保卡号
    "religiousBelief": "无",//宗教信仰
    "educationLevel": "小学",//文化程度
    "maritalStatus": "已婚",//婚姻状况
    "livingSituation": "北京海淀区",//居住情况
    "medicalPaymentMethod": "医保",//医疗付款方式
    "economicSource": "退休金",//经济来源
    "elderContact": "13512346543",//老人联系方式
    "providerName": "李志豪",//信息提供者姓名
    "providerContact": "13211122233",//信息提供者联系方式
    "relationshipWithElder": "父子",//与老人关系
    "informedConsentUrl": "是一个url地址"//知情同意书URL
  },
  // 健康评估
  "healthAssessmentDto": {
    "diseaseDiagnosis": { //疾病诊断
      "dementia": "无",//痴呆
      "mentalIllness": "无",//精神疾病
      "chronicDiseases": ""//慢性病
    },
    "recent30Days": {//最近30天意外事件和体检报告
      "fall": 0,//跌倒
      "lost": 0,//走失
      "choking": 0,//噎食
      "suicideAttempt": 0,//自杀
      "coma": 0,//昏迷
      "medicalReport": "是一个url地址"//体检报告URL
    }
  },
  // 日常活动
  "dailyLivingActivities": {
    "eating": "可独立进食物",//进食
    "bathing": "准备好洗澡水后，可自己独立完成洗澡过程",//洗澡
    "grooming": "可自己独立完成",//修饰
    "dressing": "可自己独立完成",//穿衣
    "bowelControl": "可控制大便",//大便控制
    "bladderControl": "可控制小便",//小便控制
    "toileting": "可自己独立完成",//如厕
    "chairTransfer": "床椅转椅情况",//床椅转椅
    "walkingOnFlatGround": "可自己独立完成",//走动
    "upDownStairs":"上下楼梯"//上下楼梯
    "totalScore":9,//总分
    "abilityRating":"0"//能力等级
  },
  //精神状态
  "mentalState": {
    "recallWords": "苹果、手表、国旗",//回忆词语
    "clockDrawingTest": {//时钟绘制测试
      "url": "测试图片url",//图片地址
      "result": 0//画钟测验结果(0-正确，1-错误，2-确诊认知障碍)
    },
    "aggressiveBehavior": "无身体攻击行为",//攻击性行为
    "depressiveSymptoms": "无",//抑郁症状
    "totalScore":9,//选项汇总分数
    "abilityRating":"0"//技能等级
  },
  //感知觉及沟通
  "perceptionAndCommunication": {
    "consciousLevel": "神志清醒，对周围环境警觉",//意识水平
    "vision": "能看清报纸上的标准字体",//视力
    "hearing": "可正常交谈，能听到电视、电话、门铃的声音",//听力
    "communication": "无困难，能与他人正常沟通和交流",//沟通交流
    "perceptionAndCommunicationLevel": "意识清醒，且视力和听力评为0或1，沟通评为0",//感知觉与沟通分级
    "totalScore":9,//总分
    "abilityRating":"0"//能力等级
  },
  //社会参与
  "socialParticipation": {
    "livingAbility": "除个人生活自理外（如饮食、洗漱、穿戴、二便），能料理家务（如做饭、洗衣）或当家管理事务",//生活能力
    "workAbility": "原来熟练的脑力工作或体力技巧性工作可照常进行",//工作能力
    "timeSpaceOrientation": "时间观念（年、月、日、时）清楚，可单独出远门，能很快掌握新环境的方位",//时间/空间定向
    "personOrientation": "知道周围人们的关系",//人物定向
    "socialSkills":"社交能力",//社会技能
    "totalScore":9,//总分
    "abilityRating":"0"//能力等级
  }
}
```

**响应示例：**

```JSON
// 成功
{
    "msg": "操作成功",
    "code": 200,
    "data": 65
}
//失败
{
    "msg": "AI分析失败",
    "code": 500
}
```

 ### 查看评估详情(评估结果)

**请求方式**：`GET`

**请求路径**：`/nursing/healthAssessment/report/{id}`

**请求参数**：路径参数，评估id

**响应示例**：

```JSON
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "createBy": "1",  
        "createTime": "2026-02-06 15:57:21", //创建时间
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "id": 3,
        "healthAssessmentId": 26,
        "coreSuggestion":0 ,// 核心建议，0-不建议入住，1-建议入住
        "checkInStatus":0, //入住情况：0-未入住，1-已入住
        "assessmentTime": "2026-02-06T15:56:20", //评估时间
        "assessorName": "admin",  //评估员姓名
        "recommendedRoomType": "标准双人间", //推荐入住房型
        "recommendedCareLevel": "一级护理等级",//推荐护理等级
        "careFocus": "护理重点，测试数据",//护理重点
        "suggestionDescription": "建议说明",//建议说明
        "dailyActivityLevel": "0",//日常生活活动等级
        "mentalStatusLevel": "0",//精神状态等级
        "perceptionCommunicationLevel": "0",//感知觉与沟通等级
        "socialParticipationLevel": "0",//社会参与等级
        "initialAbilityLevel": "能力完好",//老年人能力初步等级
        "finalAbilityLevel": "能力完好",//老年人能力最终等级
        "levelChangeReason": "",//等级变更依据说明
        "familyCooperation": "家属配合事项",//家属配合事项
        "dietSuggestion": "饮食建议",//饮食建议
        "psychologicalCare": "心理关怀建议",//心理关怀建议
        "institutionPreparation": "机构准备事",//机构准备事项
        "healthScore": "72.35",//健康评分
        "riskLevel": "风险",//风险等级
        //报告总结
        "reportSummary": "体检报告中尿蛋白、收缩压、舒张压、血红蛋白、总胆固醇、甘油三酯、低密度脂蛋白胆固醇、双下肢水肿、脾大小、右耳听力共10项指标提示异常，尿液常规、血压、血常规、生化、免疫、B超等共43项指标提示正常，综合这些临床指标和数据分析：心脑血管、肾脏、听觉系统存在隐患，其中心脑血管有“高危”风险；肾脏部位有“中危”风险；听觉系统有“中危”风险。",
        //异常分析
        "abnormalAnalysis": "[{\"conclusion\":\"高血压\",\"examinationItem\":\"收缩压\",\"result\":145,\"referenceValue\":\"< 140\",\"unit\":\"mmHg\",\"interpret\":\"收缩压高于正常上限，提示可能存在原发性高血压或继发性高血压。老年人血管弹性下降、动脉硬化常见，加之可能存在的盐敏感性、肾素-血管紧张素系统激活等因素，易导致血压升高。长期未控制可增加心脑肾靶器官损害风险。\",\"advice\":\"每日家庭自测血压并记录；限盐（<5g/天），减少腌制食品及加工食品；增加富含钾的蔬果（如香蕉、菠菜）；规律有氧运动（如快走30分钟/天，每周5次）；避免情绪激动和熬夜；若连续2周家庭血压≥140/90 mmHg，建议心内科就诊评估是否启动药物治疗。\"},{\"conclusion\":\"高血压\",\"examinationItem\":\"舒张压\",\"result\":90,\"referenceValue\":\"< 90\",\"unit\":\"mmHg\",\"interpret\":\"舒张压达临界高值，与收缩压共同构成1级高血压（140-159/90-99 mmHg）。反映外周血管阻力增高，常见于动脉硬化、交感神经张力增高或肾血流灌注异常，需警惕左心室肥厚及微循环障碍。\",\"advice\":\"同收缩压管理；特别注意避免久坐、戒烟限酒；监测立位血压排除体位性因素；建议完善心电图（已正常）、心脏超声（未做）评估心脏结构功能。\"},{\"conclusion\":\"贫血（轻度）\",\"examinationItem\":\"血红蛋白\",\"result\":110,\"referenceValue\":\"120-160\",\"unit\":\"g/L\",\"interpret\":\"女性>65岁参考下限通常为115 g/L，本例110 g/L属轻度贫血，结合红细胞计数（4.0×10¹²/L）、MCV（88 fL）正常，提示正细胞性贫血。老年女性常见原因包括慢性病性贫血（如隐匿炎症、肿瘤）、铁利用障碍、维生素B12/叶酸相对缺乏或隐匿性失血（如消化道）。需排除消化道肿瘤或慢性肾病所致EPO不足。\",\"advice\":\"查血清铁、铁蛋白、维生素B12、叶酸、粪便潜血；增加红肉、动物肝脏、深绿色蔬菜摄入；必要时补充铁剂（需明确缺铁后）；若持续不纠正，建议胃肠镜检查。\"},{\"conclusion\":\"高脂血症（混合型）\",\"examinationItem\":\"总胆固醇\",\"result\":6.2,\"referenceValue\":\"2.3-5.7\",\"unit\":\"mmol/L\",\"interpret\":\"总胆固醇及低密度脂蛋白胆固醇（LDL-C）均升高，提示动脉粥样硬化风险显著增加。LDL-C达4.0 mmol/L属高危水平（>3.4 mmol/L即需干预），尤其在72岁女性合并高血压背景下，10年心血管事件风险大幅上升。可能与高饱和脂肪饮食、代谢减缓、遗传性高脂血症相关。\",\"advice\":\"严格低胆固醇饮食（禁动物内脏、蛋黄>3个/周、奶油糕点）；增加膳食纤维（燕麦、豆类、苹果）；起始中等强度他汀类药物（如阿托伐他汀10mg qn）需心内科评估；3个月后复查血脂。\"},{\"conclusion\":\"高脂血症（混合型）\",\"examinationItem\":\"甘油三酯\",\"result\":2,\"referenceValue\":\"0.5-7\",\"unit\":\"mmol/L\",\"interpret\":\"甘油三酯轻度升高，常与高碳水化合物/精制糖摄入、饮酒、胰岛素抵抗或甲状腺功能减退相关。TG升高协同LDL-C升高加剧动脉粥样硬化及急性胰腺炎风险（虽本例未达危险阈值2.3 mmol/L，但需警惕趋势）。\",\"advice\":\"戒酒；限制白米饭、面条、甜饮料等升糖指数食物；每餐主食控制在半碗以内；增加Omega-3脂肪酸（深海鱼、亚麻籽）；监测空腹血糖及糖化血红蛋白。\"},{\"conclusion\":\"高脂血症（混合型）\",\"examinationItem\":\"低密度脂蛋白胆固醇\",\"result\":4,\"referenceValue\":\"2.1-3.1\",\"unit\":\"mmol/L\",\"interpret\":\"LDL-C是动脉粥样硬化的核心致病因子，本例显著超标，直接驱动冠状动脉、颈动脉斑块形成。在老年女性中，LDL-C目标值应<2.6 mmol/L（极高危人群），当前水平使心梗、卒中风险倍增。\",\"advice\":\"必须启动降脂治疗：首选高强度他汀（如瑞舒伐他汀10mg或阿托伐他汀20mg）联合依折麦布；同步生活方式干预；3个月复查LDL-C目标<2.6 mmol/L。\"},{\"conclusion\":\"微量蛋白尿\",\"examinationItem\":\"尿蛋白（PRO）\",\"result\":\"微量\",\"referenceValue\":\"阴性\",\"unit\":\"-\",\"interpret\":\"尿蛋白微量提示肾小球滤过屏障受损，是早期肾脏损伤标志。在高血压、高脂血症、老年患者中，常见于高血压肾病、糖尿病肾病（虽空腹血糖正常，但需排查餐后血糖及HbA1c）或慢性肾小球肾炎。需动态观察以鉴别一过性（如发热、运动）或持续性损伤。\",\"advice\":\"复查晨尿微量白蛋白/肌酐比值（ACR）；检测血肌酐估算eGFR；排查24小时尿蛋白定量；控制血压目标<130/80 mmHg；避免NSAIDs类止痛药；若ACR>30 mg/g，转肾内科。\"},{\"conclusion\":\"双下肢凹陷性水肿\",\"examinationItem\":\"内科体格检查\",\"result\":\"双下肢轻微凹陷性水肿\",\"referenceValue\":\"无水肿\",\"unit\":\"-\",\"interpret\":\"非对称性或对称性下肢水肿需鉴别心源性（右心衰）、肾源性（低蛋白血症、水钠潴留）、肝源性（门脉高压）、静脉回流障碍（深静脉血栓）或药物性（钙拮抗剂）。本例伴高血压、微量蛋白尿，优先考虑心肾交互作用，如左心室舒张功能不全致肺循环淤血继发肾灌注不足，或慢性肾病致水钠潴留。\",\"advice\":\"测量双侧踝围并记录；行BNP（脑钠肽）及心脏超声（重点评估左室舒张功能、EF值）；下肢静脉超声排除血栓；限盐限水（<5L/天）；避免长时间站立或坐位；若BNP升高或超声示舒张功能障碍，需心内科介入。\"},{\"conclusion\":\"脾轻度增大\",\"examinationItem\":\"B超\",\"result\":\"脾轻度增大，回声均匀\",\"referenceValue\":\"正常大小（长径<12cm）\",\"unit\":\"-\",\"interpret\":\"脾大常见于门脉高压（肝硬化代偿期）、慢性感染（EBV、结核）、血液系统疾病（淋巴瘤、骨髓增殖性肿瘤）或自身免疫病。本例B超仅提示轻度增大且回声均匀，暂无占位或结构异常，但需排除隐匿性慢性肝病（如非酒精性脂肪性肝病进展）或低度恶性血液病。\",\"advice\":\"查肝功能全套、乙肝五项、丙肝抗体、自身免疫抗体谱（ANA、ENA）、外周血涂片；腹部增强CT或MRI进一步评估脾实质及门静脉系统；若持续存在，血液科会诊。\"},{\"conclusion\":\"右耳听力下降\",\"examinationItem\":\"耳鼻喉科\",\"result\":\"右耳听力下降\",\"referenceValue\":\"双耳听力正常\",\"unit\":\"-\",\"interpret\":\"老年性耳聋（Presbycusis）多为双侧对称性高频听力下降，单侧下降需警惕听神经瘤、突发性耳聋、中耳炎后遗症或血管性事件（如小脑前下动脉供血区缺血）。本例未提及其他神经系统症状，但单侧病变不可忽视。\",\"advice\":\"纯音测听+声导抗+耳声发射+ABR（听觉脑干诱发电位）明确性质；头颅MRI内听道薄层扫描排除听神经瘤；若为突发性聋，72小时内激素治疗关键；日常避免噪音暴露，使用助听器评估。\"}]",
        //健康系统分值
        "systemScore": "{\"breathingSystem\":95,\"digestiveSystem\":88,\"endocrineSystem\":82,\"immuneSystem\":85,\"circulatorySystem\":65,\"urinarySystem\":70,\"motionSystem\":90,\"senseSystem\":78}"
    }
}
```

