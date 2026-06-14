<template>
  <div ref="mainHeight" class="bg-wt baseStep">
    <el-card header="精神状态">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-110">回忆词语：</h1>
          <span>{{ detailData.recallWords }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">画钟测验：</h1>
          <div v-if="detailData.clockDrawingTest">
            <div class="imgInfo">
              <!-- <img :src="detailData.clockDrawingTest.url" @click="handlePictureCardPreview"/> -->
              <ImagePreview :src="detailData.clockDrawingTest.url" :width="'100px'" :height="'100px'"></ImagePreview>
              </div>
          <div>{{getEventText(detailData.clockDrawingTest.result)}}</div>
          </div>
          
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">攻击行为：</h1>
          <span>{{ detailData.aggressiveBehavior }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">抑郁症状：</h1>
          <span>{{ detailData.depressiveSymptoms }}</span>
        </div>
      </div>
    </el-card>
    <el-dialog
      v-model="dialogVisible"
      title="预览"
      width="800px"
      append-to-body
    >
      <img
        :src="detailData.clockDrawingTest.url"
        style="display: block; max-width: 100%; margin: 0 auto"
      />
    </el-dialog>
  </div>
</template>
<script setup>
import { reactive, watch, ref, computed, onMounted } from 'vue';
import {ptionsTestData} from '@/utils/baseData'
const props = defineProps({
  baseData: {
    type: Object,
    default: () => {},
  },
});
const detailData = ref({
  clockDrawingTest: {}
});
const dialogImageUrl = ref("");
const dialogVisible = ref(false);
// 将数字映射为文本
const getEventText = (value) => {
  const option = ptionsTestData.find(item => item.value === value);
  return option ? option.label : '未知';
};
function handlePictureCardPreview() {
  dialogVisible.value = true;
}
watch(() => props.baseData, (val) => { 
  detailData.value=val.mentalState
  console.log(detailData.value)
},
  { deep: true,immediate: true});
</script>