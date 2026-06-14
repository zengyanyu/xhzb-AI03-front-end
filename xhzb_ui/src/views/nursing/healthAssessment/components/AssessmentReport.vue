<template>
  <div class="reportBox">
  <div ref="mainHeight">
    <el-card header="评估结果">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt">核心建议：</h1>
          <span :class="reportData.coreSuggestion===0?'tagCol2':'tagCol1'">{{
            getCoreSuggestionText()
          }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-long">核心建议说明：</h1>
          <span>{{ reportData.suggestionDescription || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt">推荐护理等级：</h1>
          <span>{{ reportData.recommendedCareLevel || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-long">推荐入住房型：</h1>
          <span>{{ reportData.recommendedRoomType || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt">护理重点：</h1>
          <span>{{ reportData.careFocus || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-long">饮食建议：</h1>
          <span>{{ reportData.dietSuggestion || '--' }}</span>
        </div>
        <!-- <div class="info-item">
          <h1 class="label-wt">用药注意事项：</h1>
          <span>{{ detailData.totalCheckDate }}</span>
        </div> -->
        <div class="info-item">
          <h1 class="label-wt">心理关怀建议：</h1>
          <span>{{ reportData.psychologicalCare || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt">家属配合事项：</h1>
          <span>{{ reportData.familyCooperation || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt">机构准备事项：</h1>
          <span>{{ reportData.institutionPreparation || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt">入住情况：</h1>
          <span v-if="reportData.checkInStatus === 0"
            ><span class="tagBg2"></span>未入住</span
          >
          <span v-else><span class="tagBg2"></span>已入住</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt">评估员姓名：</h1>
          <span>{{ reportData.assessorName || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt">创建时间：</h1>
          <span>{{ baseData.createTime || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt">评估时间：</h1>
          <span>{{ getTateDt(reportData.assessmentTime) || '--' }}</span>
        </div>
      </div>
    </el-card>
  </div>
  <!-- end -->
  <!-- 能力评估分析 -->
  <div ref="mainHeight">
    <el-card header="能力评估分析">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-130">各指标分数（分）：</h1>
          <div class="textInfo">
            <p>
              <span>日常生活活动：{{ reportData?.dailyActivityLevel || '--' }}</span>|
              <span>精神状态：{{ reportData?.mentalStatusLevel || '--' }}</span>
            </p>
            <p>
              <span>感知觉与沟通：{{ reportData?.perceptionCommunicationLevel || '--' }}</span>|
              <span>社会参与：{{ reportData?.socialParticipationLevel || '--' }}</span>
            </p>
          </div>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">老年人能力初步等级：</h1>
          <span>{{ reportData?.initialAbilityLevel || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">等级变更依据：</h1>
          <span>{{ reportData?.levelChangeReason || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">老年人能力最终等级：</h1>
          <span>{{ reportData?.finalAbilityLevel || '--' }}</span>
        </div>
      </div>
    </el-card>
  </div>
  <!-- end -->
  <!-- 体检报告分析 -->
  <div ref="mainHeight">
    <el-card header="体检报告分析">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt">健康评分：</h1>
          <span>{{ reportData?.healthScore || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-long">风险等级：</h1>
          <span>{{ reportData?.riskLevel || '--' }}</span>
        </div>
        <div class="info-item info-itemBlock">
          <h1 class="label-wt">报告总结：</h1>
          <div class="text">{{ reportData?.reportSummary || '--' }}</div>
        </div>
      </div>
      <div class="echartsBox">
        <!-- 不同年龄人群健康指数分布 -->
        <AgeDistribution :baseData="baseData || {}" :reportData="reportData"></AgeDistribution>
        <!-- end -->
        <!-- 不同系统健康指数分布 -->
        <SystemDistribution :systemScore="reportData.systemScore || []" :reportData="reportData"></SystemDistribution>
        <!-- end -->
      </div>
    </el-card>
  </div>
  <!-- end -->
  <!-- 异常分析 -->
  <div ref="mainHeight" v-if="reportData.abnormalAnalysis && reportData.abnormalAnalysis.length > 0">
    <el-card header="异常分析">
      <el-table :data="reportData.abnormalAnalysis" style="width: 100%">
        <el-table-column type="expand">
          <template #default="props">
            <div class="tableCon">
              <div class="item">
                <span>异常解读：</span>{{ props.row.interpret || '--' }}
              </div>
              <div class="item">
                <span>AI建议：</span>{{ props.row.advice || '--' }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="结论" prop="conclusion" />
        <el-table-column label="项目名称" prop="examinationItem" width="200" />
        <el-table-column label="检查结果" prop="result" />
        <el-table-column label="参考值" prop="referenceValue" />
        <el-table-column label="单位" prop="unit" />
      </el-table>
    </el-card>
  </div>
  </div>
</template>

<script setup>
import { reactive, watch, ref, computed, onMounted } from 'vue';
// 组件
// 不同年龄人群健康指数分布
import AgeDistribution from './AgeDistribution.vue';
// 不同系统健康指数分布
import SystemDistribution from './SystemDistribution.vue';
import { formatTime } from 'element-plus/es/components/countdown/src/utils.mjs';
import { getTateDt } from '@/utils/date.js';
const props = defineProps({
  baseData: {
    type: Object,
    default: () => ({}),
  },
  reportData: {
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

// 初始化 detailData 为响应式引用
const detailData = ref({
  coreSuggestion: '',
  suggestionDescription: '',
  recommendedCareLevel: '',
  recommendedRoomType: '',
  careFocus: '',
  dietSuggestion: '',
  psychologicalCare: '',
  familyCooperation: '',
  institutionPreparation: '',
  totalCheckDate: '',
  assessorName: '',
  createTime: ''
});

// 获取核心建议文本的方法
const getCoreSuggestionText = () => {
  if (detailData.value?.coreSuggestion === 0) {
    return '不建议入住';
  } else if (detailData.value?.coreSuggestion === 1) {
    return '建议入住';
  } else {
    return '--';
  }
};

watch(
  () => props.reportData,
  (val) => {
    // 确保 val 存在且合并到 detailData
    if (val) {
      detailData.value = {
        ...detailData.value,
        ...val
      };
      console.log(detailData.value);
    }
  },
  { deep: true, immediate: true }
);

// 监听 baseData 的变化
watch(
  () => props.baseData,
  (val) => {
    // 确保 baseData 不会为空
    console.log('baseData updated:', val);
  },
  { deep: true }
);

// 监听 systemScore 的变化
watch(
  () => props.systemScore,
  (val) => {
    console.log('systemScore updated:', val);
  },
  { deep: true }
);

// 监听 abnormalAnalysis 的变化
watch(
  () => props.abnormalAnalysis,
  (val) => {
    console.log('abnormalAnalysis updated:', val);
  },
  { deep: true }
);
</script>

<style scoped lang="scss">
:deep(.monitorContainer) {
  width: 100%;
  height: 400px;

  & > div {
    &:first-child {
      width: 100% !important;

      & > canvas {
        width: 100% !important;
      }
    }
  }
}

.echartsBox {
  padding: 20px 0 10px;
  display: flex;
}

.mt-24 {
  margin-top: 24px;
}

.info-block {
  .info-item {
    display: flex;
    margin-bottom: 16px;
    
    .label-wt {
      min-width: 120px;
    }
    
    .label-wt-long {
      min-width: 140px;
      // font-weight: bold;
    }
    
    .label-wt-130 {
      min-width: 130px;
      // font-weight: bold;
    }
    
    .label-wt-140 {
      min-width: 140px;
      // font-weight: bold;
    }
    
    .textInfo {
      display: flex;
      flex-direction: column;
      
      p {
        margin: 4px 0;
        
        span {
          margin: 0 8px;
        }
      }
    }
    
    .text {
      flex: 1;
      word-break: break-word;
    }
  }
  
  .info-itemBlock {
    // display: block;
    
    .label-wt {
      margin-bottom: 8px;
    }
  }
}

.tableCon {
  padding: 16px;
  
  .item {
    margin-bottom: 8px;
    
    span {
      font-weight: bold;
      margin-right: 8px;
    }
  }
}
</style>