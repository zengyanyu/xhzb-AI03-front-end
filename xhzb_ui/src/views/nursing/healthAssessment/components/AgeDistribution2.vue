<template>
  <div id="main" ref="EcharRef" class="monitorContainer"></div>
</template>

<script setup>
import { nextTick, onMounted, ref, watch, onUnmounted } from 'vue';
import * as echarts from 'echarts';

const props = defineProps({
  baseData: {
    type: Object,
    default: () => ({}),
  },
});

const EcharRef = ref(null);
let myChart = null;

// 监听 baseData 变化
watch(
  () => props.baseData,
  (val) => {
    console.log('baseData 变化:', val);
    if (val && Object.keys(val).length > 0) {
      nextTick(() => {
        initChart();
      });
    }
  },
  { deep: true, immediate: true }
);

onMounted(() => {
  console.log('年龄分布组件挂载');
  initChart();
  
  // 监听窗口变化
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  if (myChart) {
    myChart.dispose();
    myChart = null;
  }
  window.removeEventListener('resize', handleResize);
});

const handleResize = () => {
  if (myChart) {
    myChart.resize();
  }
};

const initChart = () => {
  // 确保容器存在
  const chartDom = document.getElementById('main');
  if (!chartDom) {
    console.error('找不到图表容器');
    return;
  }
  
  // 如果图表已存在，先销毁
  if (myChart) {
    myChart.dispose();
  }
  
  myChart = echarts.init(chartDom);
  setOption();
};

const setOption = () => {
  if (!myChart) {
    console.error('图表未初始化');
    return;
  }

  const rawData = [
    [50, 45, 40, 35, 30],
    [20, 25, 25, 25, 20],
    [15, 15, 20, 20, 20],
    [10, 10, 10, 15, 20],
    [5, 5, 5, 5, 10],
  ];

  // 计算总数
  const totalData = [];
  for (let i = 0; i < rawData[0].length; ++i) {
    let sum = 0;
    for (let j = 0; j < rawData.length; ++j) {
      sum += rawData[j][i];
    }
    totalData.push(sum);
  }

  const grid = {
    left: 60,
    right: 60,
    top: 50,
    bottom: 100, // 为图例留出空间
  };

  const ageData = ['50岁-', '60岁', '70岁', '80岁', '90岁+'];
  
  // 获取年龄并计算标记点
  let ageIndex = -1;
  let healthScore = 0;
  
  if (props.baseData && props.baseData.age && props.baseData.healthScore) {
    const age = Number(props.baseData.age);
    const score = Number(props.baseData.healthScore);
    
    console.log('年龄:', age, '健康分数:', score);
    
    // 确定年龄段
    if (age >= 50 && age < 60) {
      ageIndex = 0; // 50岁-
    } else if (age >= 60 && age < 70) {
      ageIndex = 1; // 60岁
    } else if (age >= 70 && age < 80) {
      ageIndex = 2; // 70岁
    } else if (age >= 80 && age < 90) {
      ageIndex = 3; // 80岁
    } else if (age >= 90) {
      ageIndex = 4; // 90岁+
    }
    
    // 计算健康分数在Y轴的位置（转换为百分比）
    healthScore = score / 100;
    
    console.log('年龄索引:', ageIndex, '健康分数位置:', healthScore);
  }

  const series = ['健康', '提示', '风险', '危险', '严重危险'].map(
    (name, sid) => {
      const data = rawData[sid].map((d, did) => {
        const total = totalData[did];
        return total > 0 ? d / total : 0;
      });
      
      // 创建系列配置
      const seriesConfig = {
        name,
        type: 'bar',
        stack: 'total',
        barWidth: '60%',
        label: {
          show: false,
        },
        data,
      };
      
      // 如果是当前年龄段，添加标记点
      if (sid === ageIndex && ageIndex !== -1) {
        seriesConfig.markPoint = {
          symbol: 'pin',
          symbolSize: 50,
          symbolRotate: 0,
          label: {
            show: true,
            formatter: '{b}',
            position: 'top',
            color: '#fff',
            backgroundColor: '#ff0000',
            padding: [4, 6],
            borderRadius: 4,
          },
          itemStyle: {
            color: '#ff0000',
          },
          data: [
            {
              name: '当前用户',
              coord: [ageIndex, healthScore],
              value: healthScore,
            },
          ],
        };
      }
      
      return seriesConfig;
    }
  );

  const option = {
    title: {
      text: '不同年龄人群健康指数分布',
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold',
      },
    },
    color: ['#57c227', '#fbcf00', '#fea102', '#fe464d', '#537cfe'],
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
      },
      formatter: (params) => {
        let result = `<div style="margin-bottom: 5px;">${params[0].axisValue}</div>`;
        params.forEach(item => {
          const value = (item.value * 100).toFixed(1);
          result += `
            <div style="display: flex; align-items: center; margin: 3px 0;">
              <span style="display: inline-block; width: 10px; height: 10px; background-color: ${item.color}; margin-right: 5px; border-radius: 2px;"></span>
              <span>${item.seriesName}: ${value}%</span>
            </div>
          `;
        });
        return result;
      },
    },
    legend: {
      bottom: 20,
      left: 'center',
      itemWidth: 12,
      itemHeight: 12,
      textStyle: {
        fontSize: 12,
      },
    },
    grid,
    yAxis: {
      type: 'value',
      min: 0,
      max: 1,
      axisLabel: {
        formatter: (value) => {
          return (value * 100) + '%';
        },
      },
      splitLine: {
        lineStyle: {
          type: 'dashed',
        },
      },
    },
    xAxis: {
      type: 'category',
      data: ageData,
      axisLabel: {
        fontSize: 12,
      },
    },
    series,
  };

  myChart.setOption(option);
  
  // 手动触发一次 resize，确保图表正确显示
  setTimeout(() => {
    if (myChart) {
      myChart.resize();
    }
  }, 100);
};
</script>

<style scoped>
.monitorContainer {
  width: 100%;
  height: 500px;
  min-height: 500px;
}
</style>