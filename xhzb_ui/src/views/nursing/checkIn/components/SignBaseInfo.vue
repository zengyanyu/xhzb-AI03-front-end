<!-- 签约办理 -->
<template>
  <div>
    <!-- 签约办理 -->
    <el-card title="">
      <h3>
        <span class="header-title">签约办理
      <el-tooltip
        :popper-style="{ maxWidth: 300 + 'px' }"
        class="item"
        effect="dark"
        content="入住成功后，系统将会生成首期账单。生成账单后，入住配置和签约办理信息将无法修改，若修改信息，需办理退住流程。"
        placement="top"
      >
        <el-icon><WarningFilled /></el-icon>
      </el-tooltip></span>
      </h3>
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-130">合同名称：</h1>
          <span> {{ baseData.contractName }}</span>
        </div>
        <div class="info-item">
          <h1  class="label-wt-130">签约日期：</h1>
          <span>{{ getDateInfo(baseData.signDate) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">丙方姓名：</h1>
          <span> {{ baseData.thirdPartyName ? baseData.thirdPartyName : '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">丙方联系方式：</h1>
          <span> {{ baseData.thirdPartyPhone ? baseData.thirdPartyPhone : '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">入住合同：</h1>
          <span
            >{{ elderVo.name}}的入住合同.pdf
            <i
              ><a class="font-bt" :href="baseData.agreementPath" target="black"
                >查看</a
              ></i
            >
          </span>
        </div>
      </div>
    </el-card>
    <!-- end -->
  </div>
</template>

<script setup lang="ts">
import { getDateInfo } from '@/utils/date'
// 获取父组件值、方法
defineProps({
  // 基本信息数据
  baseData: {
    type: Object,
    default: () => {
      return {}
    }
  },
  // 老人信息
  elderVo: {
    type: Object,
    default: () => {
      return {}
    }
  }
})
</script>
