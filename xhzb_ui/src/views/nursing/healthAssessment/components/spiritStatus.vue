<template>
  <el-form
    ref="statusFormRef"
    :model="localFormData"
    label-width="200px"
    :rules="dynamicRules"
  >
  <h3 class="title">精神状态</h3>
    <!-- 回忆词语 -->
    <el-form-item label="回忆词语" prop="recallWords" class="spiriForm">
      <p>说三样东西"苹果、手表、国旗"，请老人重复一遍，一会儿再问老人</p>
      <p>老人回答：
        <el-input v-model="localFormData.recallWords1" maxlength="30" show-word-limit clearable placeholder="" />、
        <el-input v-model="localFormData.recallWords2" maxlength="30" show-word-limit clearable placeholder="" />、
        <el-input v-model="localFormData.recallWords3" maxlength="30" show-word-limit clearable placeholder="" />
      </p>
    </el-form-item>

    <!-- 画钟测验 -->
    <el-form-item label="画钟测验" prop="clockTest" class="spiriForm">
      <div class="updateUrl">
        <p>请老人在纸上画一个圆形的时钟，在时钟上标出 10点45分</p>
        <p>
          <image-upload
            v-model="localFormData.clockDrawingTest.url"
            :limit="1"
            :fileSize="10"
            :fileType="['png','jpg','jpeg']"
            @change="handleClockImageChange"
            :disabled="localFormData.clockDrawingTest.result === 2"
          />
        </p>
      </div>

      <el-radio-group
        v-model="localFormData.clockDrawingTest.result"
        @change="handleClockResultChange"
        class="clock-radio-group"
      >
        <el-radio
          v-for="option in ptionsTestData"
          :key="option.value"
          :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- 攻击行为 -->
    <el-form-item label="攻击行为" prop="aggressiveBehavior">
      <el-radio-group
        v-model="localFormData.aggressiveBehavior"
        @change="handleFieldChange"
      >
        <el-radio
          v-for="option in aggressData"
          :key="option.value"
          :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- 抑郁症状 -->
    <el-form-item label="抑郁症状" prop="depressiveSymptoms">
      <el-radio-group
        v-model="localFormData.depressiveSymptoms"
        @change="handleFieldChange"
      >
        <el-radio
          v-for="option in depressiveData"
          :key="option.value"
          :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- 精神状态总分 -->
    <!-- <el-form-item label="精神状态总分" prop="totalScore">
      <el-input
        v-model="localFormData.totalScore"
        placeholder="自动计算"
        disabled
      />
    </el-form-item> -->

    <!-- 精神状态分级 -->
    <!-- <el-form-item label="精神状态分级" prop="abilityRating">
      <el-radio-group v-model="localFormData.abilityRating" disabled>
        <el-radio label="0级">能力完好：总分0分</el-radio>
        <el-radio label="1级">轻度受损：总分1分</el-radio>
        <el-radio label="2级">中度受损：总分2-3分</el-radio>
        <el-radio label="3级">重度受损：总分4-6分</el-radio>
      </el-radio-group>
    </el-form-item> -->
  </el-form>
</template>

<script setup>
import { reactive, watch, ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import {ptionsTestData} from '@/utils/baseData'
// 定义 props 接收父组件传递的数据
const props = defineProps({
  formData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});

const statusFormRef = ref(null);
// const ptionsData = [
//   { label: '画钟正确（画成一个闭锁圆，指针位置准确）', value: 0 },
//   { label: '画钟错误（画的圆不闭锁，或指针位置不准确）', value: 1 },
//   { label: '已确诊为认知障碍，如老年痴呆', value: 2 }
// ];
const aggressData = [
  { label: '无身体攻击行为（如打/踢/推/咬/抓/摔东西）和语言攻击行为（如骂人、语言威胁、尖叫）', value: 0 },
  { label: '每月有几次身体攻击行为，或每周有几次语言攻击行为', value: 1 },
  { label: '每周有几次身体攻击行为，或每日有语言攻击行为', value: 2 }
];
const depressiveData = [
  { label: '无', value: 0 },
  { label: '情绪低落、不爱说话、不爱梳洗、不爱活动', value: 1 },
  { label: '有自杀念头或自杀行为', value: 2 },
];

// 定义 emit 向父组件传递事件
const emit = defineEmits(['update:formData']);
const submitData = ref({});

// 根据文本值查找数值值
const findValueByLabel = (options, label) => {
  console.log('findValueByLabel', options, label);
  if (label === undefined || label === null || label === '') return '';
  const option = options.find(item => item.label === label);
  console.log('option', option ? option.value : '');
  return option ? option.value : '';
};

// 根据 value 查找 label
const getLabelByValue = (options, value) => {
  if (value === undefined || value === null || value === '') return '';
  const option = options.find((item) => item.value === value);
  console.log('getLabelByValue', option ? option.label : '');
  return option ? option.label : '';
};

// 处理回显数据 - 修改为当前页面的参数
const processEchoData = (data) => {
  if (!data) return {};
  
  const processedData = {
    // 回忆词语
    recallWords1: data.recallWords1 || '',
    recallWords2: data.recallWords2 || '',
    recallWords3: data.recallWords3 || '',
    
    // 画钟测验
    clockDrawingTest: {
      url: data.clockDrawingTest?.url,
      result: data.clockDrawingTest?.result
    },
    
    // 攻击行为
    aggressiveBehavior: findValueByLabel(aggressData, data.aggressiveBehavior),
    
    // 抑郁症状
    depressiveSymptoms: findValueByLabel(depressiveData, data.depressiveSymptoms),
    
    // 总分和分级
    totalScore: data.totalScore || 0,
    abilityRating: data.abilityRating
  };
  
  // 如果后端传递的是合并的 recallWords 字符串，则拆分
  if (data.recallWords && typeof data.recallWords === 'string') {
    const words = data.recallWords.split('、');
    processedData.recallWords1 = words[0] || '';
    processedData.recallWords2 = words[1] || '';
    processedData.recallWords3 = words[2] || '';
  }
  console.log('processedData', processedData);
  // 在回显时立即生成 submitData
  generateSubmitData(processedData);
  
  return processedData;
};

// 生成 submitData 的函数 - 修改为当前页面的参数
const generateSubmitData = (formData) => {
  submitData.value = {
    // 回忆词语 - 合并为一个字段
    recallWords: `${formData.recallWords1 || ''}、${formData.recallWords2 || ''}、${formData.recallWords3 || ''}`,
    recallWords1: formData.recallWords1 || '',
    recallWords2: formData.recallWords2 || '',
    recallWords3: formData.recallWords3 || '',
    
    // 画钟测验
    clockDrawingTest: {
      url: formData.clockDrawingTest?.url || '',
      result: formData.clockDrawingTest?.result
    },
    
    // 攻击行为
    aggressiveBehavior: getLabelByValue(aggressData, formData.aggressiveBehavior),
    
    // 抑郁症状
    depressiveSymptoms: getLabelByValue(depressiveData, formData.depressiveSymptoms),
    
    // 总分和分级
    totalScore: formData.totalScore,
    abilityRating: formData.abilityRating
  };
  console.log('submitData', submitData.value);
};

// 使用本地响应式数据副本
const localFormData = reactive(processEchoData(props.formData));

// 动态计算校验规则
const dynamicRules = computed(() => {
  const rules = {
    recallWords: [
      {
        validator: (rule, value, callback) => {
          if (
            !localFormData.recallWords1.trim() ||
            !localFormData.recallWords2.trim() ||
            !localFormData.recallWords3.trim()
          ) {
            callback(new Error('请输入老人的回忆词语'));
          } else {
            callback();
          }
        },
        trigger: 'blur',
      },
    ],
    recallWords1: [
      { required: true, message: '请输入第一个词语', trigger: 'blur' },
      {
        validator: (rule, value, callback) => {
          if (!value.trim()) {
            callback(new Error('请输入第一个词语'));
          } else {
            callback();
          }
        },
        trigger: 'blur',
      },
    ],
    recallWords2: [
      { required: true, message: '请输入第二个词语', trigger: 'blur' },
      {
        validator: (rule, value, callback) => {
          if (!value.trim()) {
            callback(new Error('请输入第二个词语'));
          } else {
            callback();
          }
        },
        trigger: 'blur',
      },
    ],
    recallWords3: [
      { required: true, message: '请输入第三个词语', trigger: 'blur' },
      {
        validator: (rule, value, callback) => {
          if (!value.trim()) {
            callback(new Error('请输入第三个词语'));
          } else {
            callback();
        }
        },
        trigger: 'blur',
      },
    ],
    aggressiveBehavior: [
      { required: true, message: '攻击行为为空，请选择攻击行为', trigger: 'change' },
    ],
    depressiveSymptoms: [
      { required: true, message: '抑郁症状为空，请选择抑郁症状', trigger: 'change' },
    ],
    totalScore: [
      { required: true, message: '总分为空，请选择总分', trigger: 'blur' },
    ],
    abilityRating: [
      { required: true, message: '分级为空，请选择分级', trigger: 'change' },
    ],
  };

  // 动态生成画钟测验的校验规则
  const clockTestRule = [
    {
      required: true,
      validator: (rule, value, callback) => {
        const result = localFormData.clockDrawingTest.result;
        const url = localFormData.clockDrawingTest.url;
        
        // 验证单选按钮选择
        if (result === undefined || result === null || result === '') {
          callback(new Error('请选择画钟结果'));
          return;
        }

        // 如果选择了"已确诊为认知障碍"，不需要上传图片
        if (result === 2) {
          callback();
          return;
        }

        // 其他情况需要上传图片
        if (!url) {
          callback(new Error('请上传画钟图片'));
          return;
        }

        callback();
      },
      trigger: 'change',
    },
  ];

  rules.clockTest = clockTestRule;
  
  return rules;
});

// 监听props变化，同步到本地数据
watch(() => props.formData, (newValue) => {
  const processedData = processEchoData(newValue);
  Object.assign(localFormData, processedData);
}, { deep: true });

// 监听所有字段变化，更新到父组件和submitData
watch(
  () => [
    localFormData.recallWords1,
    localFormData.recallWords2,
    localFormData.recallWords3,
    localFormData.clockDrawingTest.url,
    localFormData.clockDrawingTest.result,
    localFormData.aggressiveBehavior,
    localFormData.depressiveSymptoms
  ],
  () => {
    // 计算总分和分级
    calculateScores();
    
    // 生成提交数据
    generateSubmitData(localFormData);
    
    // 触发父组件更新
    emit('update:formData', { 
      ...localFormData, 
      ...submitData.value 
    });
  },
  { deep: true }
);

// 处理画钟图片上传变化
const handleClockImageChange = (file) => {
  const rawFile = file.raw;
  if (!rawFile) return;

  // 限制文件大小（10MB）
  if (rawFile.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过10MB');
    return;
  }

  // 显示预览图
  localFormData.clockDrawingTest.url = URL.createObjectURL(rawFile);

  // 触发校验
  triggerClockTestValidation();
};

// 处理画钟结果变化
const handleClockResultChange = (value) => {
  // 只是处理结果变化，不影响已上传的图片
  // 计算总分和分级
  calculateScores();
  
  // 触发校验
  triggerClockTestValidation();
};

// 触发画钟测验的验证
const triggerClockTestValidation = () => {
  if (statusFormRef.value) {
    statusFormRef.value.validateField('clockTest', (error) => {
      if (error) {
        console.log('画钟测验验证失败:', error);
      } else {
        console.log('画钟测验验证成功');
      }
    });
  }
};

// 计算总分和分级
const calculateScores = () => {
  // 计算画钟测验得分
  let clockScore = 0;
  if (localFormData.clockDrawingTest.result === 0) {
    clockScore = 0;
  } else if (localFormData.clockDrawingTest.result === 1) {
    clockScore = 1;
  } else if (localFormData.clockDrawingTest.result === 2) {
    clockScore = 2;
  }

  // 计算攻击行为得分
  let aggressiveScore = 0;
  if (localFormData.aggressiveBehavior === 0) {
    aggressiveScore = 0;
  } else if (localFormData.aggressiveBehavior === 1) {
    aggressiveScore = 1;
  } else if (localFormData.aggressiveBehavior === 2) {
    aggressiveScore = 2;
  }

  // 计算抑郁症状得分
  let depressionScore = 0;
  if (localFormData.depressiveSymptoms === 0) {
    depressionScore = 0;
  } else if (localFormData.depressiveSymptoms === 1) {
    depressionScore = 1;
  } else if (localFormData.depressiveSymptoms === 2) {
    depressionScore = 2;
  }

  // 计算总分
  const totalScore = clockScore + aggressiveScore + depressionScore;
  localFormData.totalScore = totalScore;

  // 根据总分计算分级
  let abilityRating = '';
  if (totalScore === 0) {
    abilityRating = '能力完好';
  } else if (totalScore === 1) {
    abilityRating = '轻度受损';
  } else if (totalScore >= 2 && totalScore <= 3) {
    abilityRating = '中度受损';
  } else if (totalScore >= 4 && totalScore <= 6) {
    abilityRating = '重度受损';
  }
  localFormData.abilityRating = abilityRating;
};

// 处理字段变化
const handleFieldChange = () => {
  // 计算总分和分级
  calculateScores();
  
  // 生成提交数据
  generateSubmitData(localFormData);
  
  // 触发父组件更新
  emit('update:formData', { 
    ...localFormData, 
    ...submitData.value 
  });
};

// 页面加载时计算一次总分和分级
const initScores = () => {
  calculateScores();
  
  // 生成提交数据
  generateSubmitData(localFormData);
  
  // 触发父组件更新
  emit('update:formData', { 
    ...localFormData, 
    ...submitData.value 
  });
};

// 初始化计算
initScores();

// 添加验证方法
const validate = () => {
  return new Promise((resolve, reject) => {
    if (!statusFormRef.value) {
      resolve(true);
      return;
    }

    statusFormRef.value.validate((valid) => {
      if (valid) {
        resolve(true);
      } else {
        reject(new Error('表单校验失败'));
      }
    });
  });
};

// 添加重置方法
const resetFields = () => {
  if (statusFormRef.value) {
    statusFormRef.value.resetFields();
  }
};

defineExpose({
  statusFormRef,
  localFormData,
  submitData,
  validate,
  resetFields,
  calculateScores,
});
</script>

<style scoped lang="scss">
:deep(.el-form-item) {
  &.spiriForm {
    .el-input {
      width: 200PX;
      margin-right: 8px;
      .el-input__wrapper {
        border: 0 none;
        border-radius: 0;
        border-bottom: 1px solid var(--el-input-hover-border-color);
        box-shadow: none;
      }
    }
    .el-form-item__content {
      display: block;
    }
  }
  &.is-error {
    .el-input__wrapper {
      &.is-focus {
        border-bottom: 1px solid var(--el-color-danger);
        box-shadow: none !important;
      }
    }
  }
}

.updateUrl {
  display: block;
  margin-bottom: 16px;
}

.clock-radio-group {
  width: 100%;
  :deep(.el-radio) {
    display: block;
    margin-bottom: 8px;
    &:last-child {
      margin-bottom: 0;
    }
  }
}
:deep(.el-form-item) {
  &.spiriForm {
    .el-form-item__label::before {
      content: '*';
      color: var(--el-color-danger);
      margin-right: 4px;
    }
  }
}
</style>