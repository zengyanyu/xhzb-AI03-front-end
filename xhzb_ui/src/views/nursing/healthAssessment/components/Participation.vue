<template>
  <el-form
    ref="participFormRef"
    :model="localFormData"
    label-width="200px"
    :rules="particiRules"
  >
  <h3 class="title">社会参与</h3>
    <!-- B.4.1 生活能力 -->
    <el-form-item label="生活能力：" prop="livingAbility">
      <el-radio-group v-model="localFormData.livingAbility">
        <el-radio
          v-for="option in radioOptions.livingAbility"
          :key="option.value"
          :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- B.4.2 工作能力 -->
    <el-form-item label="工作能力：" prop="workAbility">
      <el-radio-group v-model="localFormData.workAbility">
        <el-radio
          v-for="option in radioOptions.workAbility"
          :key="option.value"
          :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- B.4.3 时间/空间定向 -->
    <el-form-item label="时间/空间定向：" prop="timeSpaceOrientation">
      <el-radio-group v-model="localFormData.timeSpaceOrientation">
        <el-radio
          v-for="option in radioOptions.timeSpaceOrientation"
          :key="option.value"
          :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- B.4.4 人物定向 -->
    <el-form-item label="人物定向：" prop="personOrientation">
      <el-radio-group v-model="localFormData.personOrientation">
        <el-radio
          v-for="option in radioOptions.personOrientation"
          :key="option.value"
          :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- B.4.5 社交交往能力 -->
    <el-form-item label="社交交往能力：" prop="socialSkills">
      <el-radio-group v-model="localFormData.socialSkills">
        <el-radio
          v-for="option in radioOptions.socialSkills"
          :key="option.value"
          :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- B.4.6 社会参与总分 -->
    <!-- <el-form-item label="社会参与总分" prop="totalScore">
      <el-input
        v-model="localFormData.totalScore"
        placeholder="自动计算"
        disabled
      />
    </el-form-item> -->
    <!-- 社会参与分级 -->
    
  </el-form>
</template>

<script setup>
import { reactive, watch, ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

// 定义 props 接收父组件传递的数据
const props = defineProps({
  formData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});

// 定义单选选项对象
const radioOptions = {
  livingAbility: [
    { 
      label: '除个人生活自理外（如饮食、洗漱、穿戴、二便），能料理家务（如做饭、洗衣）或当家管理事务', 
      value: '0' 
    },
    { 
      label: '除个人生活自理外，能做家务，但欠好，家庭事务安排欠条理', 
      value: '1' 
    },
    { 
      label: '个人生活能自理；只有在他人帮助下才能做些家务，但质量不好', 
      value: '2' 
    },
    { 
      label: '个人基本生活事务能自理（如饮食、二便），在督促下可洗漱', 
      value: '3' 
    },
    { 
      label: '个人基本生活事务（如饮食、二便）需要部分帮助或完全依赖他人帮助', 
      value: '4' 
    }
  ],
  workAbility: [
    { 
      label: '原来熟练的脑力工作或体力技巧性工作可照常进行', 
      value: '0' 
    },
    { 
      label: '原来熟练的脑力工作或体力技巧性工作能力有所下降', 
      value: '1' 
    },
    { 
      label: '原来熟练的脑力工作或体力技巧性工作明显不如以往，部分遗忘', 
      value: '2' 
    },
    { 
      label: '对熟练工作只有一些片段保留，技能全部遗忘', 
      value: '3' 
    },
    { 
      label: '对以往的知识或技能全部磨灭', 
      value: '4' 
    }
  ],
  timeSpaceOrientation: [
    { 
      label: '时间观念（年、月、日、时）清楚；可单独出远门，能很快掌握新环境的方位', 
      value: '0' 
    },
    { 
      label: '时间观念有些下降，年、月、日清楚，但有时相差几天；可单独来往于近街，知道现住地的名称和方位，但不知回家路线', 
      value: '1' 
    },
    { 
      label: '时间观念较差，年、月、日不清楚，可知上半年或下半年；只能单独在家附近行动，对现住地只知名称，不知道方位', 
      value: '2' 
    },
    { 
      label: '时间观念很差，年、月、日不清楚，可知上午或下午；只能在左邻右舍间串门，对现住地不知名称和方位', 
      value: '3' 
    },
    { 
      label: '无时间观念；不能单独外出', 
      value: '4' 
    }
  ],
  personOrientation: [
    { 
      label: '知道周围人们的关系，知道祖孙、叔伯、姑姨、侄子侄女等称谓的意义；可分辨陌生人的大致年龄和身份，可用适当称呼', 
      value: '0' 
    },
    { 
      label: '只知家中亲密近亲的关系，不会分辨陌生人的大致年龄，不能称呼陌生人', 
      value: '1' 
    },
    { 
      label: '只能称呼家中人，或只能照样称呼，不知其关系，不辨辈分', 
      value: '2' 
    },
    { 
      label: '只认识常同住的亲人，可称呼子女或孙子女，可辨熟人和生人', 
      value: '3' 
    },
    { 
      label: '只认识保护人，不辨熟人和生人', 
      value: '4' 
    }
  ],
  socialSkills: [
    { 
      label: '参与社会，在社会环境有一定的适应能力，待人接物恰当', 
      value: '0' 
    },
    { 
      label: '能适应单纯环境，主动接触人，初见面时难让人发现智力问题，不能理解隐喻语', 
      value: '1' 
    },
    { 
      label: '脱离社会，可被动接触，不会主动待人，谈话中很多不适词句，容易上当受骗', 
      value: '2' 
    },
    { 
      label: '勉强可与人交往，谈吐内容不清楚，表情不恰当', 
      value: '3' 
    },
    { 
      label: '难以与人接触', 
      value: '4' 
    }
  ],
  abilityRating: [
    { label: '0级', value: '0级' },
    { label: '1级', value: '1级' },
    { label: '2级', value: '2级' },
    { label: '3级', value: '3级' }
  ]
};
const submitData = ref({})
// 表单校验规则
const particiRules = {
  livingAbility: [{ required: true, message: '生活能力为空，请选择生活能力', trigger: 'change' }],
  workAbility: [{ required: true, message: '工作能力为空，请选择工作能力', trigger: 'change' }],
  timeSpaceOrientation: [{ required: true, message: '时间/空间定向为空，请选择时间/空间定向', trigger: 'change' }],
  personOrientation: [{ required: true, message: '人物定向为空，请选择人物定向', trigger: 'change' }],
  socialSkills: [{ required: true, message: '社交交往能力为空，请选择社交交往能力', trigger: 'change' }],
};

const participFormRef = ref(null);

// 定义 emit 向父组件传递事件
const emit = defineEmits(['update:formData']);

// 根据文本值查找数值值
const findValueByLabel = (options, label) => {
  if (label === undefined || label === null || label === '') return '';
  const option = options.find(item => item.label === label);
  return option ? option.value : '';
};

// 根据 value 查找 label
const getLabelByValue = (options, value) => {
  if (value === undefined || value === null || value === '') return '';
  const option = options.find((item) => item.value === value);
  return option ? option.label : '';
};

// 处理回显数据 - 适配当前页面的参数
const processEchoData = (data) => {
  if (!data) return {};
  
  const processedData = {
    livingAbility: findValueByLabel(radioOptions.livingAbility, data.livingAbility),
    workAbility: findValueByLabel(radioOptions.workAbility, data.workAbility),
    timeSpaceOrientation: findValueByLabel(radioOptions.timeSpaceOrientation, data.timeSpaceOrientation),
    personOrientation: findValueByLabel(radioOptions.personOrientation, data.personOrientation),
    socialSkills: findValueByLabel(radioOptions.socialSkills, data.socialSkills),
    totalScore: data.totalScore,
    abilityRating: data.abilityRating
  };
  
  // 在回显时立即生成 submitData
  generateSubmitData(processedData);
  
  return processedData;
};

// 生成 submitData 的函数 - 适配当前页面的参数
const generateSubmitData = (formData) => {
  submitData.value = {
    livingAbility: getLabelByValue(radioOptions.livingAbility, formData.livingAbility),
    workAbility: getLabelByValue(radioOptions.workAbility, formData.workAbility),
    timeSpaceOrientation: getLabelByValue(radioOptions.timeSpaceOrientation, formData.timeSpaceOrientation),
    personOrientation: getLabelByValue(radioOptions.personOrientation, formData.personOrientation),
    socialSkills: getLabelByValue(radioOptions.socialSkills, formData.socialSkills),
    totalScore: formData.totalScore,
    abilityRating: formData.abilityRating
  };
};

// 使用本地响应式数据副本
const localFormData = reactive(processEchoData(props.formData));

// 使用 computed 实时计算 submitData
// const submitData = computed(() => {
//   return {
//     livingAbility: getLabelByValue(radioOptions.livingAbility, localFormData.livingAbility),
//     workAbility: getLabelByValue(radioOptions.workAbility, localFormData.workAbility),
//     timeSpaceOrientation: getLabelByValue(radioOptions.timeSpaceOrientation, localFormData.timeSpaceOrientation),
//     personOrientation: getLabelByValue(radioOptions.personOrientation, localFormData.personOrientation),
//     socialSkills: getLabelByValue(radioOptions.socialSkills, localFormData.socialSkills),
//     totalScore: localFormData.totalScore || '',
//     abilityRating: localFormData.abilityRating || ''
//   };
// });

// 监听 props 变化，同步到本地数据
watch(
  () => props.formData,
  (newValue) => {
    const processedData = processEchoData(newValue);
    Object.keys(processedData).forEach(key => {
      localFormData[key] = processedData[key];
    });
  },
  { deep: true, immediate: true }
);

// 计算总分
const totalScore = computed(() => {
  const scores = [
    parseInt(localFormData.livingAbility || 0),
    parseInt(localFormData.workAbility || 0),
    parseInt(localFormData.timeSpaceOrientation || 0),
    parseInt(localFormData.personOrientation || 0),
    parseInt(localFormData.socialSkills || 0),
  ];
  return scores.reduce((sum, score) => sum + score, 0);
});

// 监听总分变化，自动更新 totalScore 字段
watch(totalScore, (newValue) => {
  localFormData.totalScore = newValue;
});

// 自动设置分级
const abilityRating = computed(() => {
  const score = totalScore.value;
  console.log('score', score)
  if (score >= 0 && score <= 2) return '能力完好';
  if (score >= 3 && score <= 7) return '轻度受损';
  if (score >= 8 && score <= 13) return '中度受损';
  console.log('score', score)
  return '重度受损';
});

// 监听分级变化，自动更新 abilityRating 字段
watch(abilityRating, (newValue) => {
  localFormData.abilityRating = newValue;
});

// 监听所有字段变化，更新到父组件
watch(
  () => [
    localFormData.livingAbility,
    localFormData.workAbility,
    localFormData.timeSpaceOrientation,
    localFormData.personOrientation,
    localFormData.socialSkills,
    localFormData.totalScore,
    localFormData.abilityRating
  ],
  () => {
    submitData.value = {
    livingAbility: getLabelByValue(radioOptions.livingAbility, localFormData.livingAbility),
    workAbility: getLabelByValue(radioOptions.workAbility, localFormData.workAbility),
    timeSpaceOrientation: getLabelByValue(radioOptions.timeSpaceOrientation, localFormData.timeSpaceOrientation),
    personOrientation: getLabelByValue(radioOptions.personOrientation, localFormData.personOrientation),
    socialSkills: getLabelByValue(radioOptions.socialSkills, localFormData.socialSkills),
    totalScore: localFormData.totalScore,
    abilityRating: abilityRating.value,
  };
  },
  { deep: true }
);

// 暴露给父组件的方法和属性
defineExpose({
  participFormRef,
  localFormData,
  submitData
});
</script>

<style scoped>
.participation-form {
  padding: 20px;
  font-size: 14px;
}

.form-actions {
  margin-top: 20px;
  text-align: center;
}
</style>