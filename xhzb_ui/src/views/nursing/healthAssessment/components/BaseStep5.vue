<template>
  <div ref="mainHeight" class="bg-wt baseStep">
    <el-card header="感知觉与沟通">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label">意识水平：</h1>
          <span>{{ detailData.consciousLevel }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">视力：若平台带老花镜或近视镜，应在佩戴眼镜的情况下评估：</h1>
          <span>{{ detailData.vision }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">听力：若平时佩戴助听器，应在佩戴助听器的情况下评估：</h1>
          <span>{{ detailData.hearing }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">沟通交流：包括非语言沟通：</h1>
          <span>{{ detailData.communication }}</span>
        </div>
      </div>
    </el-card>
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
});
const detailData = ref({

});
watch(() => props.baseData, (val) => { 
  detailData.value=val.perceptionCommunication
  console.log(val.perceptionCommunication)
},
  { deep: true,immediate: true});
</script>