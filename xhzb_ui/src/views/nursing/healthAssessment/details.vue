<template>
  <div class="detail-base details-steps bg-wt">
    <!-- 步骤条 -->
    <!-- <el-steps
      :active="stepsActive"
      finish-status="success"
      align-center
      class="custom-steps"
    >
      <el-step
        v-for="(step, index) in stepsData"
        :key="index"
        :title="step.title"
        @click="handleStepClick(index)"
        :class="{
          'clickable-step': isStepClickable(index),
          'custom-current-step': index + 1 === currentStep,
        }"
      ></el-step>
    </el-steps> -->
<Steps :active="currentStep" :completed-steps="completedSteps" @getActive="getActive" mode="view"></Steps>
    <!-- 步骤内容 -->
    <template v-if="!loading && currentStep === 0">
      <BaseStep1 :baseData="baseData"></BaseStep1>
    </template>
    <template v-if="!loading && currentStep === 1">
      <BaseStep2 :baseData="baseData"></BaseStep2>
    </template>
    <template v-if="!loading && currentStep === 2">
      <BaseStep3 :baseData="baseData"></BaseStep3>
    </template>
    <template v-if="!loading && currentStep === 3">
      <BaseStep4 :baseData="baseData"></BaseStep4>
    </template>
    <template v-if="!loading && currentStep === 4">
      <BaseStep5 :baseData="baseData"></BaseStep5>
    </template>
    <template v-if="!loading && currentStep === 5">
      <BaseStep6 :baseData="baseData"></BaseStep6>
    </template>
    <template v-if="!loading && currentStep === 6">
      <AssessmentReport
          ref="participFormRef"
          :baseData="baseData"
          :reportData="reportData"
          :abnormalAnalysis="abnormalAnalysis"
          :systemScore="systemScore"
        />
    </template>

    <!-- 底部按钮 -->
    <div class="bg-wt boxBottom fx fx-ct" v-if="!loading">
      <el-button @click="goBack">返回</el-button>
      
      <!-- 上一步按钮：当前不是第一步时显示 -->
      <el-button 
        v-if="currentStep > 0" 
        type="primary" 
        @click="handlePre"
      >
        上一步
      </el-button>
      
      <!-- 下一步按钮：当前不是最大完成步骤时显示 -->
      <el-button 
        v-if="currentStep < maxCompletedStep" 
        type="primary" 
        @click="handleNext"
      >
        下一步
      </el-button>
      
      <!-- 完成按钮：当处于最大完成步骤但不是最后一步（第7步）时显示 -->
      <!-- <el-button 
        v-if="currentStep === maxCompletedStep && currentStep < 7" 
        type="primary" 
        @click="handleComplete"
      >
        完成
      </el-button> -->
      
      <!-- 查看报告按钮：当有报告数据且当前在第7步时显示 -->
      <!-- <el-button 
        v-if="currentStep === 7" 
        type="primary" 
      >
        查看报告
      </el-button> -->
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed ,onUnmounted} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { stepsData } from '@/utils/baseData';
import Steps from "./components/Steps.vue";
const { proxy } = getCurrentInstance();
// 接口
import { getHealthAssessment, getReportAssessment } from '@/api/nursing/healthAssessment';
import BaseStep1 from './components/BaseStep1.vue';
import BaseStep2 from './components/BaseStep2.vue';
import BaseStep3 from './components/BaseStep3.vue';
import BaseStep4 from './components/BaseStep4.vue';
import BaseStep5 from './components/BaseStep5.vue';
import BaseStep6 from './components/BaseStep6.vue';
import AssessmentReport from './components/assessmentReport.vue';
import useUserStore from '@/store/modules/user'
// ------ 定义变量 ------
// ------vuex存储数据------
const store = useUserStore();
const route = useRoute();
const router = useRouter();
const baseData = ref({
  basicInfo: {},
  healthAssessment: {},
  dailyLivingActivities: {},
  mentalState: {},
  perceptionCommunication: {},
  socialParticipation: {},
}); // 详情数据
const abnormalAnalysis = ref([]); // 异常分析数据
const systemScore = ref({}); // 健康指数分布
const currentStep = ref(0);
const headlId = ref(null);
// 存储已完成步骤的集合
const completedSteps = ref(new Set());
const loading = ref(true);
const reportData = ref({
  reportSummary: {},
  abnormalAnalysis: {},
  systemScore: {}
});

// // 计算属性：el-steps 的 active 属性值
// const stepsActive = computed(() => {
//   // 如果所有6步都已完成，返回一个很大的数字（比如999），这样所有步骤都会显示为完成状态（对勾）
//   if (allStepsCompleted.value) {
//     return 999; // 返回一个大于步骤总数的值，让所有步骤都显示为完成
//   }
  
//   // 否则返回当前步骤-1（因为el-steps的active是从0开始的）
//   return currentStep.value - 1;
// });

// 计算属性：获取最大已完成的步骤（1-7）
const maxCompletedStep = computed(() => {
  // 如果没有已完成的步骤，返回1（第一步）
  if (completedSteps.value.size === 0) return 0;
  
  let max = 0;
  completedSteps.value.forEach(step => {
    if (step > max) max = step;
  });
  return max;
});

// 计算属性：是否所有步骤都已完成（1-6步）
const allStepsCompleted = computed(() => {
  // 检查前6步是否都已完成
  for (let i = 1; i <= 6; i++) {
    if (!completedSteps.value.has(i)) return false;
  }
  return true;
});

onMounted(() => {
  // 获取路由参数,某一条入住的id
  headlId.value = route.query.id;
  if (headlId.value) {
    getDetailsData(headlId.value);
  }
  if (localStorage.getItem("active")) {
    currentStep.value = Number(localStorage.getItem("active"));
  }
});
onUnmounted(() => {
  store.setStepActive(0);
  store.setStepNum(0);
});
// 获取详情数据
const getDetailsData = (id) => {
  return getHealthAssessment(id).then((res) => {
    const data = res.data;
    baseData.value = {
      ...data,
      basicInfo: JSON.parse(data.basicInfo),
      healthAssessment: JSON.parse(data.healthAssessment),
      dailyLivingActivities: JSON.parse(data.dailyLivingActivities),
      mentalState: JSON.parse(data.mentalState),
      perceptionCommunication: JSON.parse(data.perceptionCommunication),
      socialParticipation: JSON.parse(data.socialParticipation),
    };
    console.log('基础数据:', baseData.value);

    // 清空已完成的步骤
    completedSteps.value.clear();

    // 根据已有数据标记已完成的步骤
    const stepsConfig = [
      { key: 'basicInfo', step: 0 },
      { key: 'healthAssessment', step: 1 },
      { key: 'dailyLivingActivities', step: 2 },
      { key: 'mentalState', step: 3 },
      { key: 'perceptionCommunication', step: 4 },
      { key: 'socialParticipation', step: 5 }
    ];

    stepsConfig.forEach(({ key, step }) => {
      if (baseData.value[key] && Object.keys(baseData.value[key]).length > 0) {
        completedSteps.value.add(step);
        store.setStepNum(step);
        console.log(`标记步骤 ${step} 为已完成 (${key})`);
      }
    });

    // 获取报告数据
    getReportDetailsData(headlId.value);
  });
};

const getReportDetailsData = (id) => {
  getReportAssessment(id).then((res) => {
    console.log('报告数据:', res.data);
    if (res.code === 200 && res.data) {
      if (res.data) {
        const resData = res.data;
        reportData.value = {
          ...resData,
          abnormalAnalysis: JSON.parse(resData.abnormalAnalysis),
          systemScore: JSON.parse(resData.systemScore)
        };
        
        // 更新到对应的响应式变量
        abnormalAnalysis.value = reportData.value.abnormalAnalysis || [];
        systemScore.value = reportData.value.systemScore || {};
        
        // 如果有报告数据，标记第7步为已完成
        if (resData.reportSummary || resData.abnormalAnalysis) {
          completedSteps.value.add(6);
          store.setStepNum(6);
          console.log('标记步骤 7 为已完成 (报告数据)');
        }
        
        
      }
    }
    console.log(store.getStepNum);
    // 数据加载完成后设置当前步骤
    updateCurrentStep();
    loading.value = false;
  }).catch((error) => {
    // ✅ 出错也要关闭 loading
    loading.value = false;
    console.error('数据加载失败:', error);
  });;
};

// 更新当前步骤
const updateCurrentStep = () => {
  // 等待数据加载完成
    // 如果有报告数据（第7步已完成），显示第7步
    if (completedSteps.value.has(6)) {
      currentStep.value = 6;
      console.log('有报告数据，当前步骤设置为 7');
    } else {
      // 否则显示最大已完成步骤
      currentStep.value = maxCompletedStep.value;
      console.log(`当前步骤设置为最大完成步骤: ${currentStep.value}`);
    }
    
    console.log('已完成步骤:', Array.from(completedSteps.value));
    console.log('最大完成步骤:', maxCompletedStep.value);
    console.log('所有步骤是否完成（1-6）:', allStepsCompleted.value);
    // console.log('el-steps active 值:', stepsActive.value);
};

// 返回
const goBack = () => {
  proxy.$router.push({
    path: '/enterQuit/healthAssessment',
    query: {
      _t: Date.now(), // 添加时间戳确保每次都不同
    },
  });
};

// 上一步
const handlePre = () => {
  if (currentStep.value > 0) {
    currentStep.value--;
    store.setStepActive(currentStep.value);
    console.log('上一步，当前步骤:', currentStep.value);
  }
};

// 下一步
const handleNext = () => {
  if (currentStep.value < maxCompletedStep.value) {
    currentStep.value++;
    console.log('下一步，当前步骤:', currentStep.value);
  }
};
// 获取当前的步骤值
const getActive = (val) => {

  console.log(val)
  store.setStepActive(val);
  currentStep.value = val;
};

</script>

<style lang="scss" scoped>
.detail-base {
  padding: 20px 20px 80px;
}

.footer-buttons {
  .el-button {
    margin-right: 10px;
  }
}

/* 自定义步骤条样式 */
.custom-steps {
  margin-bottom: 30px;
  
  .clickable-step {
    cursor: pointer;
    
    :deep(.el-step__head) {
      &:hover {
        .el-step__icon {
          border-color: #409eff;
          color: #409eff;
        }
      }
    }
  }
  
  .custom-current-step {
    :deep(.el-step__head) {
      .el-step__icon {
        border-color: #409eff;
        background-color: #409eff;
        color: white;
      }
    }
  }
}
</style>