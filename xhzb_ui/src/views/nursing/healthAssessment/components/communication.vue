<template>
  <div class="">
    <el-form
      ref="commmunFormRef"
      :model="localFormData"
      label-width="200px"
      :rules="cationRules"
    >
    <h3 class="title">感知觉与沟通</h3>
      <!-- 意识水平 -->
      <el-form-item label="意识水平" prop="consciousLevel">
        <el-radio-group v-model="localFormData.consciousLevel">
          <el-radio
            v-for="option in radioOptions.consciousLevel"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 视力 -->
      <el-form-item label="视力：若平日带老花镜或近视镜，应在佩戴眼镜的情况下评估" prop="vision">
        <el-radio-group v-model="localFormData.vision">
          <el-radio
            v-for="option in radioOptions.vision"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 听力 -->
      <el-form-item label="听力：若平时佩戴助听器，应在佩戴助听器的情况下评估" prop="hearing">
        <el-radio-group v-model="localFormData.hearing">
          <el-radio
            v-for="option in radioOptions.hearing"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 沟通交流 -->
      <el-form-item label="沟通交流:包括非语言沟通" prop="communication">
        <el-radio-group v-model="localFormData.communication">
          <el-radio
            v-for="option in radioOptions.communication"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 感知觉与沟通分级 -->
      <!-- <el-form-item label="感知觉与沟通分级" prop="abilityRating">
        <el-input
          v-model="abilityRating"
          placeholder="自动计算"
          disabled
        />
      </el-form-item> -->
    </el-form>
  </div>
</template>

<script setup>
import { reactive, watch, ref } from 'vue';
import { ElMessage } from 'element-plus';

// 定义 props 接收父组件传递的数据
const props = defineProps({
  formData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});
const radioOptions = {
  consciousLevel: [
    { label: '神志清醒，对周围环境警觉', value: 0 },
    {
      label:
        '嗜睡，表现为睡眠状态过度延长。当呼唤或推动其肢体时可唤醒，并进行正确的交谈或执行指令，停止刺激后又继续入睡',
      value: 1,
    },
    {
      label:
        '昏睡，一般的外界刺激不能使其觉醒，给予较强烈的刺激时可有短时的意识清醒，醒后可简短回答提问，当刺激减弱后又很快进入睡眠状态',
      value: 2,
    },
    {
      label:
        '昏迷，处于浅昏迷时对疼痛刺激有回避和痛苦表情；处于深昏迷时对刺激无反应（若评定为昏迷，直接评定为重度失能，可不进行以下项目的评估）',
      value: 3,
    },
  ],
  vision: [
    { label: '能看清楚报纸上的标准字体', value: 0 },
    { label: '能看清楚大字体，但看不清书报上的标准字体', value: 1 },
    { label: '视力有限，看不清报纸大标题，但能辨认物体', value: 2 },
    { label: '辨认物体有困难，但眼睛能跟随物体移动，只能看到光、颜色和形状', value: 3 },
    {
      label: '没有视力，眼睛不能跟随物体移动',
      value: 4,
    },
  ],
  hearing: [
    { label: '可正常交谈，能听到电视、电话、门铃的声音', value: 0 },
    {
      label: '在轻声说话或说话距离超过2米时听不清',
      value: 1,
    },
    { label: '正常交流有些困难，需在安静的环静或大声说话才能听到', value: 2 },
    { label: '讲话者大声说话或说话很慢，才能部分听见', value: 3 },
    { label: '完全听不见', value: 4 },
  ],
  communication: [
    { label: '无困难，能与他人正常沟通和交流', value: 0},
    { label: '能够表达自己的需要及理解别人的话，但需要增加时间或给予帮助', value: 1 },
    { label: '表达需要或理解有困难，需频繁重复或简化口头表达', value: 2 },
    {
      label: '不能表达需要或理解他人的话',
      value: 3,
    },
  ],
  perceptionAndCommunicationLevel: [
    {
      label: '意识清醒，且视力或听力评为0或1，沟通评为0',
      value: 0,
    },
    {
      label: '意识清醒，但视力或听力中至少一项评为2，或沟通评为1',
      value: 1,
    },
    {
      label:
        '意识清醒，但视力或听力中至少一项评为3，或沟通评为2；或嗜睡，视力或听力评定为3及以下，沟通评定为2及以下',
      value: 2,
    },
    {
      label:
        '意识清醒或嗜睡，但视力或听力中至少一项评为4，或沟通评为3；或昏睡/昏迷',
      value: 3,
    },
  ],
};
// 表单校验规则
const cationRules = {
  consciousLevel: [
    { required: true, message: '意识水平为空，请选择意识水平', trigger: 'change' },
  ],
  vision: [{ required: true, message: '视力情况为空，请选择视力情况', trigger: 'change' }],
  hearing: [{ required: true, message: '听力情况为空，请选择听力情况', trigger: 'change' }],
  communication: [
    { required: true, message: '沟通交流为空，请选择沟通交流情况', trigger: 'change' },
  ],
  // perceptionAndCommunicationLevel: [
  //   { required: true, message: '请选择感知觉与沟通分级', trigger: 'change' },
  // ],
};
const commmunFormRef = ref(null);
// const commmunFormData = reactive({})
// 定义 emit 向父组件传递事件
const emit = defineEmits(['update:formData']);
const submitData = ref({})
// 根据文本值查找数值值
const findValueByLabel = (options, label) => {
  if (label === undefined || label === null || label === '') return '';
  const option = options.find(item => item.label === label);
  return option ? option.value : '';
};
// 根据 value 查找 label
const getLabelByValue = (options, value) => {
  const option = options.find((item) => item.value === value);
  return option ? option.label : '';
};
// 处理回显数据
const processEchoData = (data) => {
  if (!data) return {};
  
  const processedData = {
    consciousLevel: findValueByLabel(radioOptions.consciousLevel, data.consciousLevel),
    vision: findValueByLabel(radioOptions.vision, data.vision),
    hearing: findValueByLabel(radioOptions.hearing, data.hearing),
    communication: findValueByLabel(radioOptions.communication, data.communication),
    abilityRating: data.abilityRating || '',
  };
  
  // 在回显时立即生成 submitData
  generateSubmitData(processedData);
  
  return processedData;
};
// 生成 submitData 的函数
const generateSubmitData = (formData) => {
  submitData.value = {
    consciousLevel: getLabelByValue(radioOptions.consciousLevel, formData.consciousLevel),
    vision: getLabelByValue(radioOptions.vision, formData.vision),
    hearing: getLabelByValue(radioOptions.hearing, formData.hearing),
    communication: getLabelByValue(radioOptions.communication, formData.communication),
    abilityRating: formData.abilityRating || '',
  };
};
// 使用本地响应式数据副本
const localFormData = reactive(processEchoData(props.formData));

const mergeCommmunRules = computed(() => {
  return {
    ...cationRules,
  };
});
// 计算总分
// 计算感知觉与沟通能力级别
const abilityRating = computed(() => {
  const { consciousLevel, vision, hearing, communication } = localFormData;

  const visionValue = parseInt(vision, 10);
  const hearingValue = parseInt(hearing, 10);
  const communicationValue = parseInt(communication, 10);
  const consciousLevelValue = parseInt(consciousLevel, 10);

  if (
    consciousLevelValue === 0 &&
    (visionValue === 0 || visionValue === 1) &&
    (hearingValue === 0 || hearingValue === 1) &&
    communicationValue === 0
  ) {
    return '能力完好';
  }

  if (
    consciousLevelValue === 0 &&
    ((visionValue === 2 || hearingValue === 2) || communicationValue === 1)
  ) {
    return '轻度受损';
  }

  if (
    (consciousLevelValue === 0 &&
      ((visionValue === 3 || hearingValue === 3) || communicationValue === 2)) ||
    (consciousLevelValue === 1 &&
      (visionValue <= 3 && hearingValue <= 3) &&
      communicationValue <= 2)
  ) {
    return '中度受损';
  }

  if (
    ((consciousLevelValue === 0 || consciousLevelValue === 1) &&
      ((visionValue === 4 || hearingValue === 4) || communicationValue === 3)) ||
    (consciousLevelValue === 2 || consciousLevelValue === 3)
  ) {
    return '重度受损';
  }
});



// 监听字段变化
watch(
  () => [
    localFormData.consciousLevel,
    localFormData.vision,
    localFormData.hearing,
    localFormData.communication,
  ],
  () => {
    localFormData.abilityRating=abilityRating.value
    submitData.value = {
    consciousLevel: getLabelByValue(radioOptions.consciousLevel, localFormData.consciousLevel),
    vision: getLabelByValue(radioOptions.vision, localFormData.vision),
    hearing: getLabelByValue(radioOptions.hearing, localFormData.hearing),
    communication: getLabelByValue(radioOptions.communication, localFormData.communication),
    abilityRating: localFormData.abilityRating,
  };
  },
  { deep: true }
);
// 处理字段变化
defineExpose({
  commmunFormRef,
  localFormData,submitData
});
</script>

<style scoped>
.communication-form {
  padding: 20px;
  font-size: 14px;
}

.form-actions {
  margin-top: 20px;
  text-align: center;
}
</style>
