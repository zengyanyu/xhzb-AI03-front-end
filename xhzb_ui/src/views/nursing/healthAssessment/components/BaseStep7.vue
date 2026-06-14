<template>
  <div ref="mainHeight" class="bg-wt">
    <!-- <el-card header="疾病诊断">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-160">痴呆：</h1>
          <span>{{ detailData.diseaseDiagnosis.dementia }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">精神疾病：</h1>
          <span>{{ detailData.diseaseDiagnosis.mentalIllness }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">慢性疾病：</h1>
          <span>{{ detailData.diseaseDiagnosis.chronicDiseases }}</span>
        </div>
      </div>
    </el-card> -->
  </div>
</template>
<script setup>
import { reactive, watch, ref, computed, onMounted } from 'vue';
// 组件
// 不同年龄人群健康指数分布
import AgeDistribution from './AgeDistribution.vue';
// 不同系统健康指数分布
import SystemDistribution from './SystemDistribution.vue';
const props = defineProps({
  baseData: {
    type: Object,
    default: () => {},
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
const detailData = ref({
  diseaseDiagnosis: {},
  recent30Days: {},
});
watch(() => props.baseData, (val) => { 
  detailData.value=val.healthAssessment
  console.log(detailData.value)
},
  { deep: true});
</script>