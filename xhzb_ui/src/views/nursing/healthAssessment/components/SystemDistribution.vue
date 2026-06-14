<template>
  <div id="main1" ref="EcharRef" class="monitorContainer"></div>
</template>

<script setup>
import { onMounted, ref, watch, nextTick } from 'vue';
import * as echarts from 'echarts';

const props = defineProps({
  systemScore: {
    type: Object,
    default: () => ({}),
  },
  reportData: {
    type: Object,
    default: () => ({}),
  },

});

const EcharRef = ref(null);
let myChart = null;

// 监听 systemScore 变化
watch(
  () => props.systemScore,
  (val) => {
    if (val && Object.keys(val).length > 0) {
      nextTick(() => {
        setOption();
      });
    }
  },
  { deep: true }
);

onMounted(() => {
  initChart();
});

// 初始化图表
const initChart = () => {
  const chartDom = document.getElementById('main1');
  if (!chartDom) {
    console.error('找不到图表容器');
    return;
  }
  
  myChart = echarts.init(chartDom);
  
  // 监听窗口大小变化
  window.addEventListener('resize', () => {
    if (myChart) {
      myChart.resize();
    }
  });
  
  // 初始渲染
  if (props.systemScore && Object.keys(props.systemScore).length > 0) {
    setOption();
  }
};

const setOption = () => {
  if (!myChart) {
    console.error('图表未初始化');
    return;
  }
  
  const data = props.systemScore || {};
  
  // 确保数据存在，使用默认值
  const arr = [
    data.digestiveSystem || 0,
    data.breathingSystem || 0,
    data.senseSystem || 0,
    data.motionSystem || 0,
    data.urinarySystem || 0,
    data.circulatorySystem || 0,
    data.immuneSystem || 0,
    data.endocrineSystem || 0,
  ];
  
  console.log('雷达图数据:', arr);
  
  // // 计算平均值
  // const calculateAverage = (data) => {
  //   if (!data || data.length === 0) return 0;
  //   const sum = data.reduce((prev, curr) => prev + curr, 0);
  //   return (sum / data.length).toFixed(2);
  // };
  
  // const average = calculateAverage(arr);
  
  const option = {
    title: {
      text: '不同系统健康指数分布',
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'item'
    },
    radar: {
      shape: 'circle',
      splitNumber: 4,
      center: ['50%', '55%'],
      radius: '65%',
      axisLine: {
        lineStyle: {
          color: 'rgba(131,141,158,.1)',
        },
      },
      indicator: [
        { name: '消化系统', max: 100, value: arr[0] },
        { name: '呼吸系统', max: 100, value: arr[1] },
        { name: '感官系统', max: 100, value: arr[2] },
        { name: '运动系统', max: 100, value: arr[3] },
        { name: '泌尿系统', max: 100, value: arr[4] },
        { name: '循环系统', max: 100, value: arr[5] },
        { name: '免疫系统', max: 100, value: arr[6] },
        { name: '内分泌系统', max: 100, value: arr[7] },
      ],
      axisName: {
        color: '#333',
        lineHeight: 18,
        textAlign: 'center',
        padding: [-5, 0],
        formatter: (value, indicator) => {
          // return `{a|${indicator.value}分}\n{b|${value}}`;
          return `${value}\n`;
        },
        rich: {
          a: {
            fontSize: 12,
            color: '#409eff',
            textAlign: 'center',
          },
          b: {
            fontSize: 12,
            lineHeight: 16,
            textAlign: 'center',
          },
        },
      },
      splitArea: {
        show: true,
        areaStyle: {
          color: ['rgba(64,158,255,0.05)', 'rgba(64,158,255,0.1)']
        },
      },
      splitLine: {
        show: true,
        lineStyle: {
          width: 1,
          type: 'dashed',
          color: '#d9d9d9',
        },
      },
    },
    series: [
      {
        type: 'radar',
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: {
          color: '#409eff'
        },
        lineStyle: {
          width: 2,
          color: '#409eff'
        },
        areaStyle: {
          color: 'rgba(64,158,255,0.3)',
        },
        data: [
          {
            value: arr,
            name: '系统健康指数',
          },
        ],
      },
    ],
    graphic: {
      type: 'text',
      left: 'center',
      top: '55%',
      z: 100,
      style: {
        text: props.reportData.healthScore + '分',
        textAlign: 'center',
        fill: '#409eff',
        fontSize: 28,
        fontWeight: 'bold'
      },
    },
  };
  
  myChart.setOption(option);
};

// 确保组件销毁时释放资源
onUnmounted(() => {
  if (myChart) {
    myChart.dispose();
    myChart = null;
  }
  window.removeEventListener('resize', () => {});
});
</script>

<style scoped>
.monitorContainer {
  width: 100%;
  height: 500px;
  /* min-height: 500px; */
}
</style>