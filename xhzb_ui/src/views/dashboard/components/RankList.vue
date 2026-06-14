<template>
  <el-row :gutter="12">
    <el-col :lg="7" :xl="8">
      <el-card class="dashboard-rank-card">
        <template #header>
          <div class="timeTie">
            <div>老人等级分布</div>
            <div>
              数据更新:{{
                new Date().getFullYear() +
                '-' +
                Number(new Date().getMonth() + 1) +
                '-' +
                new Date().getDate()
              }}
            </div>
          </div>
        </template>
        <div ref="elderRankContainer" style="height: 240px"></div>
      </el-card>
    </el-col>
    <el-col :lg="7" :xl="8">
      <el-card class="dashboard-rank-card">
        <template  #header>
          <div class="timeTie">
            <div>老人年龄分布</div>
            <div>
              数据更新:{{
                new Date().getFullYear() +
                '-' +
                Number(new Date().getMonth() + 1) +
                '-' +
                new Date().getDate()
              }}
            </div>
          </div>
        </template>
        <div ref="elderAgeContainer" style="height: 220px"></div>
      </el-card>
    </el-col>
    <el-col :lg="10" :xl="8">
      <el-card class="dashboard-rank-card">
        <template #header>
          <div class="timeTie">
            <div>预约总览</div>
            <div>
              <span class="goToday" @click="goToday">回到今日</span
              >{{ selectTime }}
            </div>
          </div>
        </template>
        <div class="dateSelete">
          <span
            class="pre"
            :class="isToday ? 'forbidActive' : ''"
            @click="!isToday && getPreWeek()"
          ></span>
          <span class="next" @click="getNextWeek"></span>
          <ul>
            <li v-for="(item, index) in dataObj" :key="index">
              <p>
                <span>{{ item.week }}</span>
              </p>
              <p @click="handleDay(item, index)">
                <span :class="dayActive === index ? 'dayActive' : ''">{{
                  item.day
                }}</span>
              </p>
            </li>
          </ul>
        </div>
        <div class="subscribeCon">
          <ul>
            <li v-for="(item, index) in subscribeData" :key="index">
              <span
                class="typeIcon"
                :class="item.type === 1 ? 'icon3' : 'icon1'"
                >{{ item.type === 1 ? '参观' : '探访' }}</span
              >
              <span class="time">{{ item.time }}</span>
              <span>预约人：{{ item.name }}</span>
              <span>手机号：{{ item.phone }}</span>
            </li>
          </ul>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
// 导入样式
import {onMounted, ref} from 'vue'
import * as echarts from 'echarts/core'
import {getDateInfo, getMonthInfo, getWeekDate} from '@/utils/date'
import {
  BACKLOG_DATA_A,
  BACKLOG_DATA_B,
  BACKLOG_DATA_C,
  ELDER_AGE_DATA_A,
  ELDER_AGE_DATA_B,
  ELDER_AGE_DATA_C,
  ELDER_RANK_DATA_A,
  ELDER_RANK_DATA_B,
  ELDER_RANK_DATA_C,
  SUBSCRIBE_DATA
} from '../constants'
import {getElderAgeDistribution, getElderRankDistribution} from '../index1'

const dataObj = ref([])
const isToday = ref(false)
const selectTime = ref('')
const dayActive = ref(0)
const subscribeData = ref([]) // 触发每天的数据
const subDataArr = ref([]) // 一月的数据
const backlogData = ref(BACKLOG_DATA_A) // 待办事项
let myChart=null
const elderRankContainer = ref() // 老人等级
const elderAgeContainer = ref() // 老人年龄

const elderRankData = ref(ELDER_RANK_DATA_A)
const elderAgeData = ref(ELDER_AGE_DATA_A)
const dades = ref([])
onMounted(() => {
  const today = new Date()
  const weekData = []
  // 生成今日及后6天的数据
  for (let i = 0; i < 7; i++) {
    const currentDate = new Date(today)
    currentDate.setDate(today.getDate() + i)
    const weekNames = ['日', '一', '二', '三', '四', '五', '六']
    const week = weekNames[currentDate.getDay()]
    const day = currentDate.getDate()
    const dateStr = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    weekData.push({ week, day, dateStr })
  }
  dataObj.value = weekData
  // ========== 原有代码保持不变 ==========
  
  selectTime.value = time()
  subDataArr.value = [...SUBSCRIBE_DATA, ...SUBSCRIBE_DATA]
  subscribeData.value = subDataArr.value[0]
  isChick(dataObj.value)
  // 3套数据3天出现一次
  const date = getMonthInfo(new Date())
  const num = (date.surplusDay + 1) % 3
  if (num === 1) {
    backlogData.value = BACKLOG_DATA_A

    elderRankData.value = ELDER_RANK_DATA_A
    elderAgeData.value = ELDER_AGE_DATA_A
  } else if (num === 2) {
    backlogData.value = BACKLOG_DATA_B

    elderRankData.value = ELDER_RANK_DATA_B
    elderAgeData.value = ELDER_AGE_DATA_B
  } else {
    backlogData.value = BACKLOG_DATA_C

    elderRankData.value = ELDER_RANK_DATA_C
    elderAgeData.value = ELDER_AGE_DATA_C
  }

  window.addEventListener('resize', handleResize)
  dades.value = JSON.parse(JSON.stringify(subscribeData.value))
  elderRankChart()
  elderAgeChart()
})

const handleResize = () => {
  myChart.resize()
}

// 老人等级分布
const elderRankChart = () => {
  myChart = echarts.init(elderRankContainer.value)
  myChart.setOption(getElderRankDistribution(elderRankData.value))
}

// 老人年龄分布
const elderAgeChart = () => {
  myChart = echarts.init(elderAgeContainer.value)
  myChart.setOption(getElderAgeDistribution(elderAgeData.value))
}

// 是否可以触发上一周
// 是否可以触发上一周
const isChick = (date) => {
  const today = time()
  const selectDate = date[0].dateStr
  const todayStr = new Date(today).getTime()
  const selectStr = new Date(selectDate).getTime()
  
  // 1. 仅判断是否为今日，不修改dataObj
  if (selectStr <= todayStr) {
    isToday.value = true
  } else {
    isToday.value = false
  }
  
  // 2. 修复：仅匹配今日日期时更新，移除覆盖selectTime的错误逻辑
  let isFoundToday = false
  date.forEach((obj, i) => {
    if (obj.dateStr === today) { // 只匹配今日
      isFoundToday = true
      dayActive.value = i
      selectTime.value = today // 强制设为今日
      subscribeData.value = subDataArr.value[obj.day]
    }
  })
  
  // 3. 如果不是今日周，保留原有逻辑（但不修改dataObj）
  if (!isFoundToday && !isToday.value) {
    selectTime.value = date[0].dateStr
    subscribeData.value = subDataArr.value[date[0].day]
  }
}
// 上一周
// 上一周（核心：基准日期往前推7天，然后显示该基准+后6天）
const getPreWeek = () => {
  if (!isToday.value) {
    // 1. 获取当前显示的第一个日期（比如24号），往前推7天作为新基准（17号）
    const currentFirstDate = new Date(dataObj.value[0].dateStr)
    const newBaseDate = new Date(currentFirstDate)
    newBaseDate.setDate(currentFirstDate.getDate() - 7)
    
    // 2. 生成新基准+后6天（比如17,18,19,20,21,22,23）
    const newWeekData = []
    for (let i = 0; i < 7; i++) {
      const currentDate = new Date(newBaseDate)
      currentDate.setDate(newBaseDate.getDate() + i)
      const weekNames = ['日', '一', '二', '三', '四', '五', '六']
      const week = weekNames[currentDate.getDay()]
      const day = currentDate.getDate()
      const dateStr = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      newWeekData.push({ week, day, dateStr })
    }
    
    // 3. 判断新基准是否是今日（17号）
    const today = new Date()
    const todayStr = time()
    const isCurrentBaseToday = newBaseDate.toDateString() === today.toDateString()
    
    if (isCurrentBaseToday) {
      // 新基准是今日：选中第一个（17号），左按钮置灰
      dayActive.value = 0
      selectTime.value = todayStr
      isToday.value = true
    } else {
      // 新基准不是今日：选中第一个，左按钮可点击
      dayActive.value = 0
      selectTime.value = newWeekData[0].dateStr
      isToday.value = false
    }
    
    // 4. 强制赋值7天数据（17-23号）
    dataObj.value = newWeekData
    
    // 5. 更新预约数据
    subscribeData.value = subDataArr.value[newWeekData[0].day] || []
  }
}

// 下一周（核心：基准日期往后推7天，然后显示该基准+后6天）
const getNextWeek = () => {
  // 1. 获取当前显示的第一个日期（比如17号），往后推7天作为新基准（24号）
  const currentFirstDate = new Date(dataObj.value[0].dateStr)
  const newBaseDate = new Date(currentFirstDate)
  newBaseDate.setDate(currentFirstDate.getDate() + 7)
  
  // 2. 生成新基准+后6天（比如24,25,26,27,28,29,30）
  const newWeekData = []
  for (let i = 0; i < 7; i++) {
    const currentDate = new Date(newBaseDate)
    currentDate.setDate(newBaseDate.getDate() + i)
    const weekNames = ['日', '一', '二', '三', '四', '五', '六']
    const week = weekNames[currentDate.getDay()]
    const day = currentDate.getDate()
    const dateStr = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    newWeekData.push({ week, day, dateStr })
  }
  
  // 3. 状态更新
  dayActive.value = 0
  selectTime.value = newWeekData[0].dateStr
  isToday.value = false // 下一周肯定不是今日，左按钮可点击
  
  // 4. 强制赋值7天数据
  dataObj.value = newWeekData
  
  // 5. 更新预约数据
  subscribeData.value = subDataArr.value[newWeekData[0].day] || []
}
// 触发当天显示的数据
const handleDay = (item, i) => {
  const newDate = getDateInfo(new Date())
  dayActive.value = i
  selectTime.value = item.dateStr
  const newDateArr = dataObj.value.filter((n) => n.dateStr === newDate)
  if (newDateArr.length > 0 && newDateArr[0].dateStr === item.dateStr) {
    subscribeData.value = dades.value
  } else {
    subscribeData.value = subDataArr.value[item.day]
  }
}
// 回到今天
// 回到今天
const goToday = () => {
  const today = new Date()
  const weekData = []
  // 1. 重新生成今日+后6天的正确数据
  for (let i = 0; i < 7; i++) {
    const currentDate = new Date(today)
    currentDate.setDate(today.getDate() + i)
    const weekNames = ['日', '一', '二', '三', '四', '五', '六']
    const week = weekNames[currentDate.getDay()]
    const day = currentDate.getDate()
    const dateStr = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    weekData.push({ week, day, dateStr })
  }
  
  // 2. 强制重置状态（关键：先把isToday设为true，避免isChick反向修改）
  isToday.value = true 
  dayActive.value = 0
  selectTime.value = time() // 强制设为今日日期
  
  // 3. 赋值正确的今日+后6天数据
  dataObj.value = weekData
  
  // 4. 最后执行检查（此时状态已重置，不会被覆盖）
  isChick(dataObj.value)
  
  // 5. 强制更新今日预约数据（避免数据错位）
  subscribeData.value = subDataArr.value[0]
}
// 当前时间
const time = () => {
  return getDateInfo(new Date())
}
</script>