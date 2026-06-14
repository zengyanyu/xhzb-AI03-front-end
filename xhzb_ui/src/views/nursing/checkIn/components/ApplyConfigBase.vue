<!-- 入住配置 -->
<template>
  <div>
    <!-- 基本信息 -->
    <el-card title="">
      <h3>
        <span class="header-title">入住配置<el-tooltip
        class="item"
        effect="dark"
        content="该费用为月账单，每月1号收当月费用"
        placement="top"
      >
        <el-icon><WarningFilled /></el-icon>
      </el-tooltip></span>
      </h3>
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-130">入住期限：</h1>
          <span
            >{{ getDateInfo(baseData.startDate) }}~{{
              getDateInfo(baseData.endDate)
            }}</span
          >
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">费用期限：</h1>
          <span
            >{{ getDateInfo(baseData.feeStartDate) }}~{{
              getDateInfo(baseData.feeEndDate)
            }}</span
          >
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">护理等级：</h1>
          <span>{{ baseData.nursingLevelName }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">入住床位：</h1>
          <span>{{ baseData.bedNumber }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">押金：</h1>
          <span>{{ decimalsReplenish(baseData.deposit) }} 元</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">护理费用：</h1>
          <span>{{ decimalsReplenish(baseData.nursingFee) }} 元/月</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">床位费用：</h1>
          <span>{{ decimalsReplenish(baseData.bedFee) }} 元/月</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">其他费用：</h1>
          <span>{{
            baseData.otherFees
              ? decimalsReplenish(baseData.otherFees) + ' 元/月'
              : '--'
          }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">医保费用：</h1>
          <span>{{
            baseData.insurancePayment
              ? decimalsReplenish(baseData.insurancePayment) + ' 元/月'
              : '--'
          }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">政府补贴：</h1>
          <span>{{
            baseData.governmentSubsidy
              ? decimalsReplenish(baseData.governmentSubsidy) + '元/月'
              : '--'
          }}</span>
        </div>
      </div>
    </el-card>
    <!-- end -->
  </div>
</template>

<script setup lang="ts">
import { getDateInfo } from '@/utils/date'
import { decimalsReplenish } from '@/utils/index'
// 获取父组件值、方法
defineProps({
  // 基本信息数据
  baseData: {
    type: Object,
    default: () => {
      return {}
    }
  }
})
</script>
