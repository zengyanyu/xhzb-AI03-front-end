<template>
  <div id="main" ref="EcharRef" class="monitorContainer"></div>
</template>

<script setup>
import { ref, watch, nextTick,onMounted,onUnmounted } from 'vue';
import * as echarts from 'echarts';
import { number } from 'echarts/core';

const props = defineProps({
  allDateArr: {
    type: Array,
    default: () => [],
  },
  allTimeArr: {
    type: Array,
    default: () => [],
  },
  tabActive: {
    type: Number,
    default: 0,
  },
});
// 创建一个erchart线图
const EcharRef = ref(null);
const baseData = ref([]);
const baseTime = ref([]);
const myChart = ref(null)
// 自适应窗口大小
const resizeChart = () => {
  if (myChart.value) {
    // resize方法会自动根据容器新尺寸重绘图表
    myChart.value.resize()
  }
}
// 监听器，监听父级传递的visible值，控制弹窗显示隐藏

watch(props, (val) => {
  baseData.value = val.allDateArr;
  baseTime.value = val.allTimeArr;
  setOption();
});
onMounted(()=>{
  baseData.value = props.allDateArr;
  baseTime.value = props.allTimeArr;
  setOption();
  if (!EcharRef.value) return
  // 销毁已有实例，避免内存泄漏
  if (myChart.value) {
    myChart.value.dispose()
  }
  myChart.value = echarts.init(EcharRef.value)
  // 监听窗口大小变化
    window.addEventListener('resize', resizeChart)
})
const setOption = () => {
  const chartDom = document.getElementById('main');
  const myChart = echarts.init(chartDom);
  let option = null;
  nextTick(() => {
    option = {
      color: ['#4D8DFF', '#00B8A0'],
      tooltip: {
        trigger: 'axis',
        formatter(params) {
          let relVal = params[0].name;
          for (let i = 0, l = params.length; i < l; i++) {
            relVal += `<br/>${params[i].marker}${params[i].seriesName} ${params[i].value}元`;
          }
          return relVal;
        },
      },
      // 显示图例
      legend: {
        data: ['收入金额', '退款金额'],
        right: '20px',
        top: '0',
        textStyle: {
          color: '#595959',
        },
      },
      grid: {
        left: '0',
        right: '20px',
        top: '50px',
        bottom: '0',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        data: baseTime.value,
        boundaryGap: false,
        axisLabel: {
          color: '#595959',
        },
        axisLine: {
          lineStyle: {
            width: 1,
            color: '#dcdcdc',
          },
        },
      },
      yAxis: {
        type: 'value',
        axisLabel: {
          color: '#595959',
        },
        // nameGap: 15, // 坐标轴名称与轴线间距离
        // min: 0,
        // max: 100,
        splitLine: {
          lineStyle: {
            type: 'dashed', // 线的类型
            color: '#dcdcdc',
          },
        },
      },
      series: [
        {
          name: '收入金额',
          data: baseData.value.incomeData,
          type: 'line',
          smooth: true,
        },
        {
          name: '退款金额',
          data: baseData.value.refundData,
          type: 'line',
          smooth: true,
        },
      ],
    };

    myChart.setOption(option);
  });
};
// 组件卸载时清理资源，避免内存泄漏
onUnmounted(() => {
  window.removeEventListener('resize', resizeChart)
  if (myChart.value) {
    myChart.value.dispose()
    myChart.value = null
  }
})
</script>
