<template>
  <div ref="mainHeight" class="bg-wt">
    <!-- 使用可选链操作符安全访问 -->
    <el-card header="疾病诊断" v-if="detailData?.diseaseDiagnosis">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-110">痴呆：</h1>
          <span>{{ detailData.diseaseDiagnosis.dementia || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">精神疾病：</h1>
          <span>{{ detailData.diseaseDiagnosis.mentalIllness || '--' }}</span>
        </div>
        <div class="info-item wlong">
          <h1 class="label-wt-110">慢性疾病：</h1>
          <span>{{ detailData.diseaseDiagnosis.chronicDiseases || '--' }}</span>
        </div>
      </div>
    </el-card>

    <el-card header="近30天内意外事件" v-if="detailData?.recent30Days">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-110">跌倒：</h1>
          <span>{{ getEventText(detailData.recent30Days.fall) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">走失：</h1>
          <span>{{ getEventText(detailData.recent30Days.lost) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">噎食：</h1>
          <span>{{ getEventText(detailData.recent30Days.choking) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">自杀：</h1>
          <span>{{ getEventText(detailData.recent30Days.suicideAttempt) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">昏迷：</h1>
          <span>{{ getEventText(detailData.recent30Days.coma) }}</span>
        </div>
      </div>
    </el-card>

    <el-card header="近30天体检报告" v-if="detailData?.recent30Days?.medicalReport">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-110">体检报告：</h1>
          <span>
            <i>
              {{elderName}}的体检报告.{{getFileType(detailData.recent30Days.medicalReport)}}
              <a class="font-bt" :href="detailData.recent30Days.medicalReport" target="_blank">查看</a>
            </i>
          </span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, watch, ref, computed, onMounted } from 'vue';
import {getFileType} from '@/utils';
const props = defineProps({
  baseData: {
    type: Object,
    default: () => ({}),
  },
  systemScore: {
    type: Array,
    default: () => [],
  },
  abnormalAnalysis: {
    type: Array,
    default: () => [],
  },
});
const elderName = ref('')
const ptionsData = [
  { label: '无', value: 0 },
  { label: '发生过1次', value: 1 },
  { label: '发生过2次', value: 2 },
  { label: '发生过3次以上', value: 3 },
];

// 初始化 detailData 为完整的结构，确保所有属性都有默认值
const detailData = ref({
  diseaseDiagnosis: {
    dementia: '',
    mentalIllness: '',
    chronicDiseases: ''
  },
  recent30Days: {
    fall: '',
    lost: '',
    choking: '',
    suicideAttempt: '',
    coma: '',
    medicalReport: ''
  }
});

// 将数字映射为文本
const getEventText = (value) => {
  if (value === undefined || value === null || value === '') {
    return '--';
  }
  const option = ptionsData.find(item => item.value === value);
  return option ? option.label : '--';
};

// 安全处理 props 数据
watch(() => props.baseData, (val) => { 
  console.log('baseData 变化:', val);
  
  if (val?.healthAssessment) {
    // 合并数据，确保结构完整
    elderName.value = val.basicInfo.elderName
    detailData.value = {
      diseaseDiagnosis: {
        ...detailData.value.diseaseDiagnosis,
        ...(val.healthAssessment.diseaseDiagnosis || {})
      },
      recent30Days: {
        ...detailData.value.recent30Days,
        ...(val.healthAssessment.recent30Days || {})
      }
    };
  } else {
    // 如果没有数据，重置为默认值
    detailData.value = {
      diseaseDiagnosis: {
        dementia: '',
        mentalIllness: '',
        chronicDiseases: ''
      },
      recent30Days: {
        fall: '',
        lost: '',
        choking: '',
        suicideAttempt: '',
        coma: '',
        medicalReport: ''
      }
    };
  }
  
  console.log('detailData 更新后:', detailData.value);
}, { deep: true, immediate: true });

// 或者使用 computed 属性，更简洁
// const detailData = computed(() => {
//   const healthData = props.baseData?.healthAssessment || {};
//   return {
//     diseaseDiagnosis: {
//       dementia: '',
//       mentalIllness: '',
//       chronicDiseases: '',
//       ...healthData.diseaseDiagnosis
//     },
//     recent30Days: {
//       fall: '',
//       lost: '',
//       choking: '',
//       suicideAttempt: '',
//       coma: '',
//       medicalReport: '',
//       ...healthData.recent30Days
//     }
//   };
// });
</script>

<style scoped>
/* 确保链接正常显示 */

.startTop {
  margin-top: 10px;
}
</style>