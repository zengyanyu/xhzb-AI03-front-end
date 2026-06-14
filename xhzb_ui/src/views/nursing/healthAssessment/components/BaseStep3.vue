<template>
  <div ref="mainHeight" class="bg-wt baseStep">
    <el-card header="日常生活活动">
      <div class="info-block day">
        <div class="info-item">
          <h1 class="label">进食：指用餐具将食物由容器送到口中，咀嚼、吞咽等过程：</h1>
          <span>{{ detailData.eating }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">洗澡：</h1>
          <span>{{ detailData.bathing }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">修饰：指洗脸、刷牙、梳头、刮脸等：</h1>
          <span>{{ detailData.grooming }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">穿衣：指穿脱衣服、系扣、拉拉链、穿脱鞋袜、系鞋带：</h1>
          <span>{{ detailData.grooming }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">大便控制：</h1>
          <span>{{ detailData.bowelControl }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">小便控制：</h1>
          <span>{{ detailData.bladderControl }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">如厕：包括去厕所、解开衣裤、擦净、整理衣裤、冲水：</h1>
          <span>{{ detailData.toileting }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">床椅转椅：</h1>
          <span>{{ detailData.chairTransfer }}</span>
        </div>

        <div class="info-item">
          <h1 class="label">平地行走：</h1>
          <span>{{ detailData.walkingOnFlatGround }}</span>
        </div>
        <div class="info-item">
          <h1 class="label">平地行走：</h1>
          <span>{{ detailData.upDownStairs }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script setup>
import { reactive, watch, ref, computed, onMounted } from 'vue';
const props = defineProps({
  baseData: {
    type: Object,
    default: () => {},
  },
});

const detailData = ref({
  diseaseDiagnosis: {},
  recent30Days: {},
});
watch(() => props.baseData, (val) => { 
  detailData.value=val.dailyLivingActivities
  console.log(val.dailyLivingActivities)
},
  { deep: true,immediate: true});
</script>