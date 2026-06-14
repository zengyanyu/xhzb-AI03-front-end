<template>
  <div class="everydayCon">
    <!-- 日常生活活动 -->
    <h3 class="title">日常生活活动</h3>
    <div class="formMain">
      <el-form
        ref="everydayFormRef"
        :model="localFormData"
        label-width="200px"
        :rules="everydayRules"
      >
        <!-- 动态渲染表单项 -->
        <el-form-item
          v-for="item in formItems"
          :key="item.prop"
          :label="item.label"
          :prop="item.prop"
        >
          <el-radio-group
            v-model="localFormData[item.prop]"
            @change="handleFieldChange"
          >
            <el-radio
              v-for="option in item.options"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            >
              {{ option.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 需求一：显示总分 -->
        <!-- <el-form-item label="日常生活活动总分" prop="totalScore">
          <el-input 
            v-model="localFormData.totalScore" 
            placeholder="自动计算" 
            disabled 
            style="width: 200px;"
          />
          <span class="score-text">分</span>
        </el-form-item> -->

        <!-- 需求二：显示分级 -->
        <!-- <el-form-item label="日常生活活动分级" prop="abilityRating">
          <el-radio-group v-model="localFormData.abilityRating" disabled>
            <el-radio label="能力完好">能力完好：总分100分</el-radio>
            <el-radio label="轻度受损">轻度受损：总分65-95分</el-radio>
            <el-radio label="中度受损">中度受损：总分45-60分</el-radio>
            <el-radio label="重度受损">重度受损：总分≤40分</el-radio>
          </el-radio-group>
        </el-form-item> -->
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch, ref, computed } from 'vue';

// 定义 props 接收父组件传递的数据
const props = defineProps({
  formData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});
const everydayFormRef = ref(null);
const submitData = ref({})

// 表单项目配置数据
const formItems = [
  {
    prop: 'eating',
    label: '进食：指用餐具将食物由容器送到口中、咀嚼、吞咽等过程',
    options: [
      { value: '10分', label: '可独立进食（在合理的时间内独立进食准备好的食物）' },
      { value: '5分', label: '需部分帮助（进食过程中需要一定帮助，如协助把持餐具）' },
      { value: '0分', label: '需极大帮助或完全依赖他人，或有留置营养管' }
    ]
  },
  {
    prop: 'bathing',
    label: '洗澡',
    options: [
      { value: '5分', label: '准备好洗澡水后，可自己独立完成洗澡过程' },
      { value: '0分', label: '在洗澡过程中需他人帮助' }
    ]
  },
  {
    prop: 'grooming',
    label: '修饰：指洗脸、刷牙、梳头、刮脸等',
    options: [
      { value: '5分', label: '可自己独立完成' },
      { value: '0分',  label: '需他人帮助' }
    ]
  },
  {
    prop: 'dressing',
    label: '穿衣：指穿脱衣服、系扣、拉拉链、穿脱鞋袜、系鞋带',
    options: [
      { value: '10分', label: '可独立完成' },
      { value: '5分', label: '需部分帮助（能自己穿脱，但需他人帮助整理衣物、系扣/鞋带、拉拉链）' },
      { value: '0分', label: '需极大帮助或完全依赖他人' }
    ]
  },
  {
    prop: 'bowelControl',
    label: '大便控制',
    options: [
      { value: '10分', label: '可控制大便' },
      { value: '5分', label: '偶尔失控（每周<1次），或需要他人提示' },
      { value: '0分', label: '完全失控' }
    ]
  },
  {
    prop: 'bladderControl',
    label: '小便控制',
    options: [
      { value: '10分', label: '可控制小便' },
      { value: '5分', label: '偶尔失控（每天<1次，但每周>1次），或需要他人提示' },
      { value: '0分', label: '完全失控，或留置导尿管' }
    ]
  },
  {
    prop: 'toileting',
    label: '如厕：包括去厕所、解开衣裤、擦净、整理衣裤、冲水',
    options: [
      { value: '10分', label: '可独立完成' },
      { value: '5分',  label: '需部分帮助（需他人搀扶去厕所、需他人帮忙冲水或整理衣裤等）' },
      { value: '0分', label: '需极大帮助或完全依赖他人' }
    ]
  },
  {
    prop: 'chairTransfer',
    label: '床椅转移',
    options: [
      { value: '15分', label: '可独立完成' },
      { value: '10分', label: '需部分帮助（需他人搀扶或使用拐杖）' },
      { value: '5分', label: '需极大帮助（较大程度上依赖他人搀扶和帮助）' },
      { value: '0分',  label: '完全依赖他人' }
    ]
  },
  {
    prop: 'walkingOnFlatGround',
    label: '平地行走',
    options: [
      { value: '15分', label: '可独立在平地上行走45m' },
      { value: '10分', label: '需部分帮助（因肢体残疾、平衡能力差、过度衰弱、视力等问题，在一定程度上需他人地搀扶或使用拐杖、助行器等辅助用具）' },
      { value: '5分',  label: '需极大帮助（因肢体残疾、平衡能力差、过度衰弱、视力等问题，在较大程度上依赖他人搀扶，或坐在轮椅上自行移动）' },
      { value: '0分', label: '完全依赖他人' }
    ]
  },
  {
    prop: 'upDownStairs',
    label: '上下楼梯',
    options: [
      { value: '10分', label: '可独立上下楼梯（连续上下10-15个台阶）' },
      { value: '5分', label: '需部分帮助（需他人搀扶，或扶着楼梯、使用拐杖等）' },
      { value: '0分', label: '需极大帮助或完全依赖他人' }
    ]
  }
];

// 动态生成校验规则（只对前10个项目生成校验规则）
const everydayRules = formItems.reduce((rules, item) => {
  rules[item.prop] = [
    { required: true, message: `请选择${item.label.split('：')[0]}情况`, trigger: 'change' }
  ];
  return rules;
}, {
  // 总分和分级不需要校验规则
  totalScore: [],
  abilityRating: []
});

// 定义 emit 向父组件传递事件
const emit = defineEmits(['update:formData']);

// 根据文本值查找数值值
const findValueByLabel = (options, label) => {
  if (label === undefined || label === null || label === '') return '';
  const option = options.find(item => item.label === label);
  return option ? option.label : '';
};

// 根据 value 查找 label
const getLabelByValue = (options, value) => {

  if (value === undefined || value === null || value === '') return '';
  const option = options.find((item) => item.label === value);
  return option ? option.label : '';
};

// 处理回显数据 - 修改为当前页面的参数
const processEchoData = (data) => {
  if (!data) return {};
  
  const processedData = {
    eating: findValueByLabel(formItems[0].options, data.eating),
    bathing: findValueByLabel(formItems[1].options, data.bathing),
    grooming: findValueByLabel(formItems[2].options, data.grooming),
    dressing: findValueByLabel(formItems[3].options, data.dressing),
    bowelControl: findValueByLabel(formItems[4].options, data.bowelControl),
    bladderControl: findValueByLabel(formItems[5].options, data.bladderControl),
    toileting: findValueByLabel(formItems[6].options, data.toileting),
    chairTransfer: findValueByLabel(formItems[7].options, data.chairTransfer),
    walkingOnFlatGround: findValueByLabel(formItems[8].options, data.walkingOnFlatGround),
    upDownStairs: findValueByLabel(formItems[9].options, data.upDownStairs),
    totalScore: data.totalScore,
    abilityRating: data.abilityRating
  };
  console.log('processedData:', processedData);
  // 在回显时立即生成 submitData
  generateSubmitData(processedData);
  
  return processedData;
};

// 生成 submitData 的函数 - 修改为当前页面的参数
const generateSubmitData = (formData) => {
  submitData.value = {
    eating: getLabelByValue(formItems[0].options, formData.eating),
    bathing: getLabelByValue(formItems[1].options, formData.bathing),
    grooming: getLabelByValue(formItems[2].options, formData.grooming),
    dressing: getLabelByValue(formItems[3].options, formData.dressing),
    bowelControl: getLabelByValue(formItems[4].options, formData.bowelControl),
    bladderControl: getLabelByValue(formItems[5].options, formData.bladderControl),
    toileting: getLabelByValue(formItems[6].options, formData.toileting),
    chairTransfer: getLabelByValue(formItems[7].options, formData.chairTransfer),
    walkingOnFlatGround: getLabelByValue(formItems[8].options, formData.walkingOnFlatGround),
    upDownStairs: getLabelByValue(formItems[9].options, formData.upDownStairs),
    totalScore: formData.totalScore,
    abilityRating: formData.abilityRating
  };
};

// 使用本地响应式数据副本
const localFormData = reactive(processEchoData(props.formData));

// 监听props变化，同步到本地数据
watch(() => props.formData, (newValue) => {
  Object.assign(localFormData, newValue);
}, { deep: true });

// 提取分数值（从"10分"中提取数字10）
const extractScore = (scoreStr) => {
  if (!scoreStr) return 0;
  const match = scoreStr.match(/^(\d+)分$/);
  return match ? parseInt(match[1]) : 0;
};
const getScoreByLabel = (options, selectedLabel) => {
  if (!selectedLabel) return 0;
  const option = options.find(opt => opt.label === selectedLabel);
  if (!option) return 0;
  const match = option.value.match(/^(\d+)分$/);
  return match ? parseInt(match[1]) : 0;
};
// 需求一：计算总分 - 10个项目得分之和
const calculateTotalScore = () => {
  return formItems.reduce((total, item) => {
    const selectedLabel = localFormData[item.prop]; // 用户选择的 label
    const score = getScoreByLabel(item.options, selectedLabel); // 从 options 中提取分数
    return total + score;
  }, 0);
};

// 需求二：根据总分计算分级
const abilityRating = (score) => {
  if (score === 100) return '能力完好';
  if (score >= 65 && score <= 95) return '轻度受损';
  if (score >= 45 && score <= 60) return '中度受损';
  if (score <= 40) return '重度受损';
  return '';
};

// 监听所有表单字段变化，重新计算总分和分级
watch(
  () => [
    localFormData.eating,
    localFormData.bathing,
    localFormData.grooming,
    localFormData.dressing,
    localFormData.bowelControl,
    localFormData.bladderControl,
    localFormData.toileting,
    localFormData.chairTransfer,
    localFormData.walkingOnFlatGround,
    localFormData.upDownStairs,
  ],
  () => {
    // 计算总分
    const totalScore = calculateTotalScore();
    localFormData.totalScore = totalScore;
    console.log('totalScore:', totalScore);

    // 根据总分计算分级
    localFormData.abilityRating = abilityRating(totalScore);

    // 更新 submitData
    submitData.value = {
      ...Object.fromEntries(
        formItems.map(item => [
          item.prop,
          getLabelByValue(item.options, localFormData[item.prop]),
        ])
      ),
      totalScore: localFormData.totalScore,
      abilityRating: localFormData.abilityRating,
    };

    console.log('submitData:', submitData.value);
    emit('update:formData', { ...localFormData });
  },
  { deep: true }
);

// 处理单个字段变化
const handleFieldChange = () => {
  // 字段变化时自动触发watch计算总分和分级
  emit('update:formData', { ...localFormData });
};

// 页面加载时计算一次总分和分级
const initScores = () => {
  const totalScore = calculateTotalScore();
  localFormData.totalScore = totalScore;
  localFormData.abilityRating = abilityRating(totalScore);
  emit('update:formData', { ...localFormData });
};

// 初始化计算
initScores();

defineExpose({
  everydayFormRef,
  localFormData,
  submitData
});
</script>

<style scoped>


.formMain {
  display: flex;
  flex-wrap: wrap;
}

.formMain .el-form-item {
  flex: 1 1 calc(50% - 7.5px); /* 计算间距 */
  min-width: 250px; /* 防止过小 */
  margin-bottom: 15px;
}

/* 总分和分级项单独样式 */
.formMain .el-form-item:last-child,
.formMain .el-form-item:nth-last-child(2) {
  flex: 1 1 100%;
  /* margin-top: 20px; */
  /* border-top: 1px solid #eee; */
  /* padding-top: 20px; */
}

.score-text {
  margin-left: 10px;
  color: #333;
  font-weight: bold;
}

/* 总分输入框样式 */
:deep(.el-input.is-disabled .el-input__inner) {
  background-color: #f5f7fa;
  color: #409eff;
  font-weight: bold;
  font-size: 16px;
}
</style>