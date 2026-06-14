<template>
  <div class="custom-steps-container">
    <el-steps
      :active="active"
      finish-status="success"
      class="custom-steps"
    >
      <el-step
        v-for="(item, index) in stepsData"
        :key="index"
        :title="item.label"
        @click="handleClick(index)"
        :class="[
          isCompletedStep(index) ? 'completed-step' : '',
          index === active ? 'current-active-step' : ''
        ]"
      />
    </el-steps>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { stepsData } from '@/utils/baseData';

const props = defineProps({
  active: {
    type: Number,
    default: 0,
  },
  mode: {
    type: String,
    default: 'edit',
  },
  completedSteps: {
    type: Set,
    default: () => new Set(),
  },
});

const emit = defineEmits(['getActive']);

// 判断是否是已完成步骤
const isCompletedStep = (index) => {
  return props.completedSteps.has(index);
};

// 判断步骤是否可点击（仅已完成步骤可点击）
const isClickable = (index) => {
  return isCompletedStep(index);
};

// 点击步骤：仅触发父组件切换
const handleClick = (val) => {
  if (isClickable(val)) {
    emit("getActive", val);
  }
};
</script>

<style scoped lang="scss">
.custom-steps-container {
  padding:15px 20px;
}

:deep(.custom-steps) {
  // 通用重置：所有步骤头背景为白色
  .el-step__head {
    height: 24px;
    .el-step__icon {
      background: #fff !important;
      &.is-text{
        border-width: 1px;
      }
    }
  }
  .el-step__line-inner{
    border-color: #00b8a0 !important;
  }
.el-step__icon-inner{font-weight: normal;}
  // 1. 未完成步骤：灰框 + 灰色数字
  .el-step__head:not(.is-success):not(.is-process) {
    .el-step__icon.is-text {
      border-color: var(--el-text-color-placeholder) !important;
      color: var(--el-text-color-placeholder) !important;
      
      .el-step__icon-inner{
        font-weight: 400 !important;
      }
    }
  }
  .el-step__title:not(.is-success):not(.is-process) {
    color: var(--el-text-color-placeholder) !important;
    
  }

  // 2. 已完成步骤（非当前激活）：绿框 + 绿对勾 + 绿色标题
  .completed-step:not(.current-active-step) {
    .el-step__head.is-success {
      color: #00b8a0 !important; // 绿色
      .el-step__icon {
        background: url(@/assets/images/icon-tianwan.png) no-repeat !important;
        background-size: contain !important; // 绿框
        border: 0 none;
      }
      .el-step__icon-inner { // 绿对勾
        color: #00b8a0 !important;
        display: none;
      }
    }
    .el-step__title.is-success {
      color: #00b8a0 !important; // 绿色标题
    }
  }

  // 3. 核心：已完成且当前激活 → 红框 + 红对勾 + 红色标题
  .completed-step.current-active-step {
    .el-step__head.is-process {
      color: #f56c6c !important; // 红色（Element Plus 危险色）
      .el-step__icon {
        background: url(@/assets/images/tianwanActive.png) no-repeat !important;
        background-size: contain !important;
        &.is-text{
          border: 0 none;
        }
      }
      .el-step__icon-inner { // 红对勾
        color: #191919 !important;
        font-weight: bold !important;
        display: none;
      }
    }
    .el-step__title.is-process {
      color: #191919 !important; // 红色标题
    }
  }
  .completed-step{
    .el-step__head.is-wait {
      color: #00b8a0 !important; // 绿色
      .el-step__icon {
        background: url(@/assets/images/icon-tianwan.png) no-repeat !important;
        background-size: contain !important; // 绿框
        border: 0 none;
      }
      .el-step__icon-inner { // 绿对勾
        color: #00b8a0 !important;
        display: none;
      }
    }
    .el-step__title.is-wait {
      color: #00b8a0 !important; // 绿色标题
    }
  }

  // 4. 未完成但当前激活（第一步默认）：黑框 + 黑色数字
  .el-step__head.is-process {
    .el-step__icon.is-text {
      border-color: #191919 !important;
      color: #191919 !important;
    }
  }
  .el-step__title.is-process {
    color: #332929 !important;
  }

  // 可点击步骤手型
  .completed-step {
    cursor: pointer;
  }
}
</style>