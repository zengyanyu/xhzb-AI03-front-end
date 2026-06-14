<template>
  <div ref="mainHeight" class="bg-wt baseStep">
    <el-card header="社会参与" v-if="hasSocialData">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-120">生活能力：</h1>
          <span>{{ detailData.livingAbility || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">工作能力：</h1>
          <span>{{ detailData.workAbility || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">时间/空间定向：</h1>
          <span>{{ detailData.timeSpaceOrientation || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">人物定向：</h1>
          <span>{{ detailData.personOrientation || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">社交交往能力：</h1>
          <span>{{ detailData.socialSkills || '--' }}</span>
        </div>
      </div>
    </el-card>
    
    <!-- 当没有数据时显示提示 -->
    <el-card header="社会参与" v-else>
      <div class="empty-message">
        <el-empty description="暂无社会参与数据" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { watch, ref, computed } from 'vue';

const props = defineProps({
  baseData: {
    type: Object,
    default: () => ({}),
  },
});

// 初始化 detailData 为完整的结构，确保所有属性都有默认值
const detailData = ref({
  livingAbility: '',
  workAbility: '',
  timeSpaceOrientation: '',
  personOrientation: '',
  socialSkills: ''
});

// 计算是否有社会参与数据
const hasSocialData = computed(() => {
  const data = detailData.value;
  return data.livingAbility || data.workAbility || data.timeSpaceOrientation || 
         data.personOrientation || data.socialSkills;
});

watch(() => props.baseData, (val) => { 
  console.log('收到 baseData:', val);
  
  // 安全地更新数据
  if (val?.socialParticipation) {
    // 合并数据，确保结构完整
    detailData.value = {
      livingAbility: '',
      workAbility: '',
      timeSpaceOrientation: '',
      personOrientation: '',
      socialSkills: '',
      ...val.socialParticipation // 用传入的数据覆盖默认值
    };
  } else {
    // 如果没有数据，重置为默认值
    detailData.value = {
      livingAbility: '',
      workAbility: '',
      timeSpaceOrientation: '',
      personOrientation: '',
      socialSkills: ''
    };
  }
  
  console.log('更新后的 detailData:', detailData.value);
}, { deep: true, immediate: true });
</script>

<style scoped>
.empty-message {
  text-align: center;
  padding: 40px 0;
}

.label-wt-120 {
  min-width: 120px;
  font-weight: bold;
}

.info-item {
  display: flex;
  margin-bottom: 16px;
  align-items: flex-start;
}

.info-item span {
  flex: 1;
  word-break: break-word;
}
</style>