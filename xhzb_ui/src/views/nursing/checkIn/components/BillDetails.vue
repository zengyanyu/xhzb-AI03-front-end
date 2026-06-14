<!-- 账单预览 -->
<template>
  <div class="dialogBox billDialog scrollTable">
    <el-dialog
      v-model="formVisible"
      title="账单预览"
      :show-close="true"
      :before-close="onClickCloseBtn"
      width="600px"
      destroy-on-close
    >
      <div class="detailsList">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="账单月份">
            {{ costStartTime ? getYearMonthInfo(costStartTime) : '--' }}
          </el-descriptions-item>
          <el-descriptions-item label="账单周期">
            {{ billPeriod ? billPeriod : '--' }}
          </el-descriptions-item>
          <el-descriptions-item label="老人姓名">
            {{ billData.name ? billData.name : '--' }}
          </el-descriptions-item>
          
          <el-descriptions-item label="共计天数">
            {{ days ? days + '天' : '--' }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="details" style="margin-top: 20px">
          <div class="details-label" style="margin-bottom: 10px; font-weight: bold">费用结算：</div>
          <div class="billTable">
            <el-table :data="tableData" border style="width: 100%">
              <el-table-column prop="type" label="类型" width="100" />
              <el-table-column prop="item" label="费用项目" width="120" />
              <el-table-column prop="service" label="服务内容" min-width="120" />
              <el-table-column prop="amount" label="金额（元）" width="140">
                <template #default="{ row }">
                  <span :class="{ 'amount-strong': row.isStrong }">
                    {{ row.amount }}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import dayjs from 'dayjs'
import {
  getDays,
  getMonthInfo,
  getYearMonthInfo,
  getDateInfo
} from '@/utils/date'
import { decimalsReplenish } from '@/utils/index'

// 获取父组件值、方法
const props = defineProps({
  // 基本信息数据
  formData: {
    type: Object,
    default: () => ({})
  },
  baseData: {
    type: Object,
    default: () => ({})
  },
  applyFormInfo: {
    type: Object,
    default: () => ({})
  },
  // 弹层隐藏显示
  visible: {
    type: Boolean,
    default: false
  }
})

// 触发父级事件
const emit = defineEmits(['handleClose'])

// 表单数据
const costStartTime = ref(null)
const costEndTime = ref(null)
const formVisible = ref(false)
const days = ref(null)
const addSubplan = ref(null)
const deduction = ref(null)
const monthlyPayment = ref(null)
const currentPeriod = ref(null)
const billAmount = ref(null)
const billPeriod = ref(null)
const billData = ref({} as any)

// 表格数据
const tableData = computed(() => {
  if (!billData.value) return []
  console.log(billData.value)
  const data = [
    // 添加项
    {
      type: '添加项',
      item: '护理费用',
      service: '特级护理等级',
      amount: billData.value.nursingFee ? decimalsReplenish(billData.value.nursingFee) : '--',
      isStrong: false
    },
    {
      type: '',
      item: '床位费用',
      service: '特护房',
      amount: billData.value.bedFee ? decimalsReplenish(billData.value.bedFee) : '--',
      isStrong: false
    },
    {
      type: '',
      item: '其他费用',
      service: '其他',
      amount: billData.value.otherFees ? decimalsReplenish(billData.value.otherFees) : '--',
      isStrong: false
    },
    {
      type: '小计',
      item: '',
      service: '',
      amount: addSubplan.value ? decimalsReplenish(addSubplan.value) : '--',
      isStrong: true
    },
    // 扣减项
    {
      type: '扣减项',
      item: '医保支付',
      service: '--',
      amount: billData.value.insurancePayment 
        ? '- ' + decimalsReplenish(billData.value.insurancePayment) 
        : '--',
      isStrong: false
    },
    {
      type: '',
      item: '政府补贴',
      service: '--',
      amount: billData.value.governmentSubsidy 
        ? '- ' + decimalsReplenish(billData.value.governmentSubsidy) 
        : '--',
      isStrong: false
    },
    {
      type: '小计',
      item: '',
      service: '',
      amount: deduction.value ? '- ' + decimalsReplenish(deduction.value) : '--',
      isStrong: true
    },
    // 汇总行
    {
      type: '每月应付',
      item: '',
      service: '',
      amount: monthlyPayment.value === 0 || monthlyPayment.value 
        ? decimalsReplenish(monthlyPayment.value) 
        : '--',
      isStrong: true
    },
    {
      type: '本期应付',
      item: '',
      service: '',
      amount: currentPeriod.value === 0 || currentPeriod.value 
        ? currentPeriod.value 
        : '--',
      isStrong: false
    },
    {
      type: '押金',
      item: '',
      service: '',
      amount: decimalsReplenish(billData.value.deposit),
      isStrong: false
    },
    {
      type: '账单金额',
      item: '',
      service: '',
      amount: billAmount.value === 0 || billAmount.value 
        ? decimalsReplenish(billAmount.value) 
        : '--',
      isStrong: true
    }
  ]

  return data
})

// 获取当前月最后一天
const getCurrentMonthLast = (val) => {
  const date = new Date(val)
  const year = date.getFullYear()
  let month: any = date.getMonth() + 1
  month = month < 10 ? `0${month}` : month
  const day = new Date(year, month, 0)
  return `${year}-${month}-${day.getDate()}`
}

// 监听弹窗显示
watch(
  () => props.visible,
  (val) => {
    formVisible.value = props.visible
    if (val) {
      let data = {} as any
      if (props.baseData?.id) {
        data = props.baseData
        billData.value = {
          ...data,
          costTime: [
            getDateInfo(data.feeStartDate),
            getDateInfo(data.feeEndDate)
          ],
          name: props.applyFormInfo.name
        }
        console.log(billData.value)
      } else {
        data = props.formData
        billData.value = {
          ...data,
          costTime: data.feeTime
        }
      }

      // 账单周期、共计天数
      if (billData.value.costTime && billData.value.costTime.length > 0) {
        costStartTime.value = billData.value.costTime[0]
        costEndTime.value = billData.value.costTime[1]
        const startDate = dayjs(costStartTime.value)
        const startYear = startDate.year()
        const startMonth = startDate.month() + 1
        const endDate = dayjs(costEndTime.value)
        const endYear = endDate.year()
        const endMonth = endDate.month() + 1
        
        if (startMonth === endMonth && endMonth < 12 && startYear === endYear) {
          billPeriod.value = `${getDateInfo(costStartTime.value)}~${getDateInfo(costEndTime.value)}`
          days.value = getDays(costStartTime.value, costEndTime.value)
        } else {
          billPeriod.value = `${costStartTime.value}~${getCurrentMonthLast(
            costStartTime.value
          )}`
          days.value = getDays(
            costStartTime.value,
            getCurrentMonthLast(costStartTime.value)
          )
        }
      }
      console.log('billData', data)
      // 添加项
      addSubplan.value =
        Number(data.nursingFee || 0) +
        Number(data.bedFee || 0) +
        Number(data.otherFees || 0)

      // 扣减项
      deduction.value =
        Number(data.insurancePayment || 0) +
        Number(data.governmentSubsidy || 0)

      // 每月应付
      monthlyPayment.value = addSubplan.value - deduction.value
      if (monthlyPayment.value < 0) {
        monthlyPayment.value = '0.00'
      }

      // 本期应付
      if (monthlyPayment.value >= 0 && costStartTime.value) {
        const monthInfo = getMonthInfo(costStartTime.value)
        currentPeriod.value = (
          monthlyPayment.value === '0.00'
            ? 0
            : (monthlyPayment.value / monthInfo.days) * days.value
        ).toFixed(2)
      }

      // 账单金额
      if (currentPeriod.value) {
        billAmount.value =
          Number(currentPeriod.value) + Number(data.deposit || 0)
      }
    }
  }
)

// 关闭弹层
const onClickCloseBtn = () => {
  emit('handleClose')
}
</script>

<style scoped>
.detailsList {
  padding: 10px 0;
}

.amount-strong {
  font-weight: bold;
  color: #f56c6c;
}

:deep(.el-table .cell) {
  text-align: center;
}

:deep(.el-descriptions__cell) {
  padding: 12px 16px;
}
</style>