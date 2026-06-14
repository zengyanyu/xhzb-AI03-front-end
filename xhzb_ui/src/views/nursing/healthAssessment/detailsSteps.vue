<template>
  <div class="detail-base details-steps bg-wt">
    <!-- 步骤条 -->
    <!-- <el-steps
      :active="currentStep"
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
    <!-- 步骤条 -->
    <Steps
      :active="currentStep"
      :completed-steps="completedSteps"
      mode="edit"
      @getActive="getActive"
    ></Steps>
    <!-- 表单内容 -->
    <div class="form-content" v-loading="loading">
      <template v-if="!loading && currentStep === 0">
        <BasicInfoForm
          :formData="baseData.basicInfo"
          @id-card-change="handleIdCardChange"
          @ethnicity-change="handleEthnicityChange"
          ref="basicFormRef"
        />
      </template>

      <template v-else-if="!loading && currentStep === 1">
        <HealthAssessmentForm
          ref="healthFormRef"
          :formData="baseData.healthAssessmentDto"
          :base-data="baseData.basicInfo"
        />
      </template>
      <template v-else-if="!loading && currentStep === 2">
        <Everyday
          :formData="baseData.dailyLivingActivities"
          ref="everydayFormRef"
        />
      </template>
      <template v-else-if="!loading && currentStep === 3">
        <SpiritStatus
          ref="statusFormRef"
          :formData="baseData.mentalState"
          @update:formData="updateMentalState"
        />
      </template>

      <template v-else-if="!loading && currentStep === 4">
        <Communication
          ref="commmunFormRef"
          :formData="baseData.perceptionAndCommunication"
        />
      </template>
      <template v-else-if="!loading && currentStep === 5">
        <Participation
          ref="participFormRef"
          :formData="baseData.socialParticipation"
        />
      </template>
      <template v-else>
        <!-- <AssessmentReport
          ref="participFormRef"
          :baseData="baseData"
          :reportData="reportData"
        /> -->
      </template>
    </div>

    <!-- 底部按钮 -->
    <div class="bg-wt boxBottom fx fx-ct" v-if="!loading">
      <el-button @click="goBack">返回</el-button>
      <el-button @click="saveAndBack" v-if="currentStep <= 5"
        >返回并保存</el-button
      >
      <el-button type="primary" @click="saveAndNext" v-if="currentStep < 5"
        >保存并下一步</el-button
      >
      <el-button type="primary" @click="handleAssess" v-if="currentStep === 5"
        >保存并评估</el-button
      >
    </div>
    <div class="update" v-if="updateLoading">
      <div class="updateLoad">
        <div class="bigBg"></div>
        <div class="smallBg"></div>
        <div class="textInfo">
          <p class="text">AI</p>
          <p>分析中...</p>
        </div>
      </div>
    </div>
    <!-- 评估失败弹层模态框  -->
    <el-dialog
      v-model="assessmentFailedDialog"
      title="评估失败"
      width="500px"
      class="assessError"
    >
      <div class="dialog-content">
        <img src="@/assets/images/duanwang.png" alt="评估失败" />
        <p>评估失败，点击「重新评估」即可再次尝试</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="assessmentFailedDialog = false">取消</el-button>
          <el-button type="primary" @click="handleAssessment"
            >重新评估</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted, onUnmounted } from 'vue';
import { ElMessage } from 'element-plus';
// import { stepsData } from '@/utils/baseData';
//步骤
import Steps from './components/Steps.vue';
import BasicInfoForm from './components/BasicInfoForm.vue';
import HealthAssessmentForm from './components/HealthAssessmentForm.vue';
import Everyday from './components/everyday.vue';
import SpiritStatus from './components/spiritStatus.vue';
import Communication from './components/communication.vue';
import Participation from './components/Participation.vue';
import AssessmentReport from './components/assessmentReport.vue';
import {
  addHealthAssessment,
  getHealthAssessment,
  updateHealthAssessment,
  assessmentData,
  getReportAssessment,
} from '@/api/nursing/healthAssessment';
import { stepsData } from '@/utils/baseData';
const { proxy } = getCurrentInstance();
// ------ 定义变量 ------
// ------vuex存储数据------
const route = useRoute();

// 当前步骤
const currentStep = ref(0);

// 存储已完成步骤的集合
const completedSteps = ref(new Set());
const loading = ref(false);
// 表单数据
const baseData = ref({
  basicInfo: {},
  healthAssessmentDto: {},
  dailyLivingActivities: {},
  mentalState: {},
  perceptionAndCommunication: {},
  socialParticipation: {},
});
const abnormalAnalysis = ref([]); // 异常分析数据
const systemScore = ref({}); // 健康指数分布
const reportData = ref({
  reportSummary: {},
  abnormalAnalysis: {},
  systemScore: {},
});
// 表单引用
const basicFormRef = ref(null);
const healthFormRef = ref(null);
const everydayFormRef = ref(null);
const statusFormRef = ref(null);
const commmunFormRef = ref(null);
const participFormRef = ref(null);
const updateLoading = ref(false); //图片上传中
const headlId = ref(null);
const headlAddId = ref(null);
// 对话框状态
const assessmentFailedDialog = ref(false);

onMounted(() => {
  headlId.value = route.query.id ? route.query.id : null;
  if (headlId.value) {
    // 编辑模式：先显示 loading，加载完数据再显示内容
    loading.value = true;
    getDetailsData(headlId.value).finally(() => {
      loading.value = false;
    });
  } else {
    // 新增模式：直接显示步骤 0
    loading.value = false;
    currentStep.value = 0;
  }
});
onUnmounted(() => {});
// // 修改判断步骤是否可点击的方法 - 确保已完成的步骤始终可点击
// const isStepClickable = (index) => {
//   const stepNumber = index;

//   // 已完成的步骤都可以点击
//   if (completedSteps.value.has(stepNumber)) {
//     return true;
//   }

//   // 当前步骤也可以点击
//   if (stepNumber === currentStep.value) {
//     return true;
//   }

//   // 当前步骤之前的步骤都可以点击
//   if (stepNumber < currentStep.value) {
//     return true;
//   }

//   return false;
// };

// // 处理步骤点击
// const handleStepClick = (index) => {
//   console.log('点击了步骤:', index);
//   const stepNumber = index;
//   if (
//     baseData.value.basicInfo.minorityEthnicity !== '' &&
//     baseData.value.basicInfo.minorityEthnicity !== undefined
//   ) {
//     baseData.value.basicInfo.nation = '少数民族';
//   } else {
//     baseData.value.basicInfo.nation = '汉族';
//   }
//   if (
//     baseData.value.basicInfo.religionDetail !== '' &&
//     baseData.value.basicInfo.religionDetail !== undefined
//   ) {
//     baseData.value.basicInfo.religiousBelief = '有';
//   } else {
//     baseData.value.basicInfo.religiousBelief = '无';
//   }
//   if (isStepClickable(index)) {
//     currentStep.value = stepNumber;
//   } else {
//     // ElMessage.warning('请先完成当前步骤');
//   }
// };

// 标记步骤为已完成
const markStepAsCompleted = (stepNumber) => {
  completedSteps.value.add(stepNumber);
};

// 处理民族选择变化
const handleEthnicityChange = (value) => {
  if (value !== '少数民族') {
    baseData.value.basicInfo.minorityEthnicity = '';
  }
};

// 验证当前步骤
const validateCurrentStep = () => {
  return new Promise((resolve, reject) => {
    if (currentStep.value === 0) {
      if (!basicFormRef.value.basicFormRef) {
        reject('基本信息表单引用不存在');
        return;
      }
      basicFormRef.value.basicFormRef.validate((valid) => {
        if (valid) {
          baseData.value.basicInfo = basicFormRef.value.localFormData;
          if (baseData.value.basicInfo.nation === '少数民族') {
            baseData.value.basicInfo.nation =
              baseData.value.basicInfo.minorityEthnicity;
          }
          if (baseData.value.basicInfo.religiousBelief === '有') {
            baseData.value.basicInfo.religiousBelief =
              baseData.value.basicInfo.religionDetail;
          }
          // 标记当前步骤为已完成
          markStepAsCompleted(currentStep.value);
          resolve(true);
        } else {
          // ElMessage.error('请完成基本信息的所有必填项');
          reject('基本信息验证失败');
        }
      });
    } else if (currentStep.value === 1) {
      if (!healthFormRef.value.healthFormRef) {
        reject('健康评估表单引用不存在');
        return;
      }

      healthFormRef.value.healthFormRef.validate((valid) => {
        if (!valid) {
          // ElMessage.error('请完成健康评估的所有必填项');
          reject('健康评估验证失败');
          return;
        }

        baseData.value.healthAssessmentDto = {
          ...healthFormRef.value.healthAssessment,
        };
        // 标记当前步骤为已完成
        markStepAsCompleted(currentStep.value);
        resolve(true);
      });
    } else if (currentStep.value === 2) {
      if (!everydayFormRef.value) {
        reject('日常生活表单引用不存在');
        return;
      }

      everydayFormRef.value.everydayFormRef.validate((valid) => {
        if (!valid) {
          // ElMessage.error('请完成健康评估的所有必填项');
          reject('健康评估验证失败');
          return;
        }
        baseData.value.dailyLivingActivities = {
          ...everydayFormRef.value.submitData,
        };
        // 标记当前步骤为已完成
        markStepAsCompleted(currentStep.value);
        resolve(true);
      });
    } else if (currentStep.value === 3) {
      if (!statusFormRef.value.statusFormRef) {
        reject('精神状态表单引用不存在');
        return;
      }

      statusFormRef.value.statusFormRef.validate((valid) => {
        if (!valid) {
          // ElMessage.error('请完成精神状态所有必填项');
          reject('健康评估验证失败');
          return;
        }
        const datas = statusFormRef.value.submitData;
        baseData.value.mentalState = {
          ...datas,
          recallWords:
            datas.recallWords1 +
            '、' +
            datas.recallWords2 +
            '、' +
            datas.recallWords3,
        };
        // 标记当前步骤为已完成
        markStepAsCompleted(currentStep.value);
        resolve(true);
      });
    } else if (currentStep.value === 4) {
      if (!commmunFormRef.value.commmunFormRef) {
        reject('感知觉与沟通表单引用不存在');
        return;
      }

      commmunFormRef.value.commmunFormRef.validate((valid) => {
        if (!valid) {
          // ElMessage.error('请完成精神状态所有必填项');
          reject('健康评估验证失败');
          return;
        }

        baseData.value.perceptionAndCommunication = {
          ...commmunFormRef.value.submitData,
        };
        // 标记当前步骤为已完成
        markStepAsCompleted(currentStep.value);
        resolve(true);
      });
    } else if (currentStep.value === 5) {
      if (!participFormRef.value.participFormRef) {
        reject('社会参与表单引用不存在');
        return;
      }

      participFormRef.value.participFormRef.validate((valid) => {
        if (!valid) {
          // ElMessage.error('请完成精神状态所有必填项');
          reject('健康评估验证失败');
          return;
        }
        baseData.value.socialParticipation = {
          ...participFormRef.value.submitData,
        };
        // 标记当前步骤为已完成
        markStepAsCompleted(currentStep.value);
        resolve(true);
      });
    }
  });
};

// 按钮事件
const goBack = () => {
  proxy.$router.push({
    path: '/enterQuit/healthAssessment',
    query: {
      _t: Date.now(), // 添加时间戳确保每次都不同
    },
  });
};

const saveAndBack = async () => {
  try {
    await validateCurrentStep();

    if (headlId.value) {
      await updateHeal();
    } else {
      await addHeal();
    }
    proxy.$router.push({
      path: '/enterQuit/healthAssessment',
      query: {
        _t: Date.now(), // 添加时间戳确保每次都不同
      },
    });
  } catch (error) {
    console.log('验证失败:', error);
  }
};
// 获取当前的步骤值
const getActive = (val) => {
  currentStep.value = val;

  if (
    baseData.value.basicInfo.minorityEthnicity !== '' &&
    baseData.value.basicInfo.minorityEthnicity !== undefined
  ) {
    baseData.value.basicInfo.nation = '少数民族';
  } else {
    baseData.value.basicInfo.nation = '汉族';
  }
  if (
    baseData.value.basicInfo.religionDetail !== '' &&
    baseData.value.basicInfo.religionDetail !== undefined
  ) {
    baseData.value.basicInfo.religiousBelief = '有';
  } else {
    baseData.value.basicInfo.religiousBelief = '无';
  }
};
const getReportDetailsData = (id) => {
  getReportAssessment(id).then((res) => {
    if (res.data) {
      console.log('报告数据:', res.data);
      if (res.code === 200) {
        const resData = res.data;
        reportData.value = {
          ...resData,
          abnormalAnalysis: JSON.parse(resData.abnormalAnalysis),
          systemScore: JSON.parse(resData.systemScore),
        };

        // 更新到对应的响应式变量
        abnormalAnalysis.value = reportData.value.abnormalAnalysis || [];
        systemScore.value = reportData.value.systemScore || {};

        // 如果有报告数据，标记第7步为已完成
        if (resData.reportSummary || resData.abnormalAnalysis) {
          // completedSteps.value.add(7);
          proxy.$router.push({
            path: '/enterQuit/healthDetails',
            query: { id: headlId.value },
          });
        }
      } else {
        updateLoading.value = false;
      }
      console.log(reportData.value);
      console.log(baseData.value);
    }
  });
};
// 获取详情数据
const getDetailsData = (id) => {
  return getHealthAssessment(id).then((res) => {
    const datas = res.data;
    baseData.value = {
      id: datas.id,
      basicInfo: JSON.parse(datas.basicInfo),
      healthAssessmentDto: datas.healthAssessment
        ? JSON.parse(datas.healthAssessment)
        : {},
      dailyLivingActivities: datas.dailyLivingActivities
        ? JSON.parse(datas.dailyLivingActivities)
        : {},
      mentalState: datas.mentalState ? JSON.parse(datas.mentalState) : {},
      perceptionAndCommunication: datas.perceptionCommunication
        ? JSON.parse(datas.perceptionCommunication)
        : {},
      socialParticipation: datas.socialParticipation
        ? JSON.parse(datas.socialParticipation)
        : {},
    };

    // 清空已完成的步骤
    completedSteps.value.clear();

    // 判断对象是否有效（有内容）
    const hasData = (obj) => {
      if (!obj || typeof obj !== 'object') return false;
      return (
        Object.keys(obj).length > 0 &&
        !Object.values(obj).every(
          (val) => val === null || val === undefined || val === '',
        )
      );
    };

// 根据已有数据标记已完成的步骤（使用独立的 if 判断，不是 else if）
    // 1. 基本信息
    if (hasData(baseData.value.basicInfo)) {
      // 如果获取的不是汉族就把nation填充成从后端获取的数据
      if (baseData.value.basicInfo.nation !== '汉族') {
        baseData.value.basicInfo.nation = '少数民族';
        baseData.value.basicInfo.minorityEthnicity =
          baseData.value.basicInfo.nation;
      }
      if (baseData.value.basicInfo.religiousBelief !== '无') {
        baseData.value.basicInfo.religionDetail =
          baseData.value.basicInfo.religiousBelief;
        baseData.value.basicInfo.religiousBelief = '有';
      }
      completedSteps.value.add(0);
    }

    // 2. 健康评估
    if (hasData(baseData.value.healthAssessmentDto)) {
      completedSteps.value.add(1);
    }

    // 3. 日常生活活动
    if (hasData(baseData.value.dailyLivingActivities)) {
      completedSteps.value.add(2);
    }

    // 4. 精神状态
    if (hasData(baseData.value.mentalState)) {
      completedSteps.value.add(3);
      const words = baseData.value.mentalState.recallWords
        ? baseData.value.mentalState.recallWords.split('、')
        : [];
      baseData.value.mentalState.recallWords1 = words[0] || '';
      baseData.value.mentalState.recallWords2 = words[1] || '';
      baseData.value.mentalState.recallWords3 = words[2] || '';
    }

    // 5. 感知觉与沟通
    if (hasData(baseData.value.perceptionAndCommunication)) {
      completedSteps.value.add(4);
    }

    // 6. 社会参与
    if (hasData(baseData.value.socialParticipation)) {
      completedSteps.value.add(5);
    }
    console.log(completedSteps.value);
    if (completedSteps.value.size === 6) {
      getReportDetailsData(headlId.value);
    }
    // 获取报告数据

    // 修改这里：不要自动跳到下一步，而是停留在最后一个有数据的步骤
    // 如果所有步骤都有数据，才跳到第6步（报告页）
    let targetStep = 0;
    const stepsArray = Array.from(completedSteps.value);

    if (stepsArray.length > 0) {
      // 找到最大的步骤号（即最后完成的步骤）
      const maxStep = Math.max(...stepsArray);

      // 检查是否所有步骤（0-5）都已完成
      const allStepsCompleted = [0, 1, 2, 3, 4, 5].every((step) =>
        completedSteps.value.has(step),
      );
      targetStep = maxStep;
    }

    currentStep.value = targetStep;
    console.log('当前步骤设置为:', currentStep.value);
  });
};

const saveAndNext = async () => {
  try {
    await validateCurrentStep();
    if (currentStep.value >= 0 && currentStep.value <= 4) {
      // 保存数据
      if (headlId.value) {
        await updateHeal();
      } else {
        await addHeal();
      }
    }
    // 进入下一步
    if (currentStep.value < stepsData.length) {
      currentStep.value++;
      console.log('保存并下一步，当前步骤:', currentStep.value);
    }
  } catch (error) {
    console.log('验证失败:', error);
  }
};
// 保存并评估
const handleAssess = async () => {
  participFormRef.value.participFormRef.validate((valid) => {
    baseData.value.socialParticipation = {
      ...participFormRef.value.submitData,
    };
    if (valid) {
      proxy.$modal
        .confirm('评估开始后所有答题内容将无法修改，是否继续？', '确认评估',{
          
      // 关键配置：关闭弹窗动画，消除延时
      closeOnClickModal: false,
      showClose: true,
      // 禁用动画过渡（核心）
      customClass: 'no-modal-animation'
    
        })
        .then(function () {
          return handleAssessment();
        })
        .then(() => {})
        .catch(() => {});
    }
  });
};
// 数据筛选
const filterData = (data) => {
  const datas = Object.fromEntries(
    Object.entries(baseData.value).filter(([key, value]) => {
      // 如果值是对象且不为空，则保留；否则过滤掉
      return (
        value !== null &&
        value !== undefined &&
        typeof value === 'object' &&
        Object.keys(value).length > 0
      );
    }),
  );
  return datas;
};
// 新增
const addHeal = async () => {
  // 过滤掉空对象，只保留有数据的对象
  const filteredData = filterData(baseData.value);
  // 更新 baseData.value 为过滤后的数据
  baseData.value = {
    id: headlAddId.value,
    ...filteredData,
  };
  const res = await addHealthAssessment(baseData.value);
  if (res.code === 200) {
    if (res.data) {
      headlAddId.value = res.data;
    }
    proxy.$modal.msgSuccess(`操作成功`);
  }
};

// 编辑
const updateHeal = async () => {
  // 过滤掉空对象，只保留有数据的对象
  const filteredData = filterData(baseData.value);

  // 更新 baseData.value 为过滤后的数据
  baseData.value = {
    id: baseData.value.id,
    ...filteredData,
  };
  console.log('baseData.value:', baseData.value);
  const res = await updateHealthAssessment(baseData.value);
  if (res.code === 200) {
    proxy.$modal.msgSuccess(`操作成功`);
  }
};
// 评估数据
const handleAssessment = async () => {
  updateLoading.value = true;
  assessmentFailedDialog.value = false;
  console.log('baseData.value:', currentStep.value);
  // 先保存
  if (headlId.value) {
    await updateHeal();
  } else {
    await addHeal();
  }
  currentStep.value++
  // 再评估
  const res = await assessmentData(baseData.value);
  console.log('res:', res);
  if (res.code === 200) {
    updateLoading.value = false;
    headlId.value = res.data;
    const ids = headlId.value ? headlId.value : headlAddId.value;
    getReportDetailsData(ids);
    // 进入下一步
    if (currentStep.value < stepsData.length) {
      currentStep.value++;
    }
  } else if (res.code === 10098) {
    // 评估失败，打开弹窗
    updateLoading.value = false;
    assessmentFailedDialog.value = true;
    currentStep.value--;
  }
  markStepAsCompleted(currentStep.value);
};
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
  background-color: #f9f9f9;
}

.form-content {
  // margin-top: 20px;
  padding: 0 20px 20px;
  background: white;
  border-radius: 8px;
  // box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 40px;
}
// :deep(.el-steps) {
//   .el-step__title:not(.is-success),.el-step__head:not(.is-success) {
//     color: #ccc !important;
//   }
//   .el-step__head:not(.is-success){
//     .el-step__icon.is-text{
//       border-color:#ccc !important ;
//     }
//   }
// }
.assessError {
  .dialog-content {
    text-align: center;
    img {
      width: 50%;
    }
  }
}
/* 关闭弹窗动画 */
.no-modal-animation {
  --el-modal-transition: none !important;
  --el-message-box-transition: none !important;
}
/* 兜底：强制关闭所有弹窗动画 */
.el-message-box {
  transition: none !important;
}
.el-modal {
  transition: none !important;
}
</style>
