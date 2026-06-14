<template>
  <!-- 家属信息 -->
  <div>
    <el-card>
      <h3>
        <span class="header-title">家属信息</span>
      </h3>

    
    <el-table
      ref="tableRef"
      :data="familyData"
      style="width: 100%"
      :header-cell-style="headerCellStyle"
      :cell-style="cellStyle"
      :empty-text="emptyText"
    >
      <el-table-column
        prop="name"
        label="家属姓名"
        min-width="200"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <span>{{ row.name || '-' }}</span>
        </template>
      </el-table-column>
      
      <el-table-column
        prop="phone"
        label="家属联系方式"
        min-width="200"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <span>{{ row.phone || '-' }}</span>
        </template>
      </el-table-column>
      
      <el-table-column
        prop="kinship"
        label="与老人关系"
        min-width="200"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <span>{{ getKinshipText(row.kinship) }}</span>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  </div>
  
</template>

<script setup>
import { ref, computed, watch } from 'vue'

// 关系映射
const kinshipMap = {
  '0': '子女',
  '1': '配偶',
  '2': '亲属',
  '3': '朋友',
  '4': '社工',
  '5': '其他'
}

// 获取父组件值、方法
const props = defineProps({
  // 家属信息数据
  baseData: {
    type: [Array, Object],
    default: () => {
      return []
    }
  }
})

// ------定义变量------
const tableRef = ref()
const emptyText = '暂无家属信息'

// 处理家属数据，确保是数组格式
const familyData = computed(() => {
  if (Array.isArray(props.baseData)) {
    return props.baseData
  } else if (props.baseData && typeof props.baseData === 'object') {
    // 如果是对象，尝试转换为数组
    return [props.baseData]
  }
  return []
})

// 获取关系文本
const getKinshipText = (value) => {
  if (value === undefined || value === null || value === '') {
    return '-'
  }
  return kinshipMap[value] || value
}

// 表头样式
const headerCellStyle = {
  backgroundColor: '#f5f7fa',
  color: '#333',
  fontWeight: 500,
  fontSize: '14px',
  textAlign: 'center'
}

// 单元格样式
const cellStyle = {
  textAlign: 'center',
  fontSize: '14px'
}

// 监听数据变化
watch(() => props.baseData, (newVal) => {
  console.log('家属信息更新:', newVal)
}, { deep: true, immediate: true })
</script>

<style scoped lang="scss">
.family-card {
  margin-bottom: 20px;
  
  :deep(.el-card__header) {
    background-color: #f5f7fa;
    padding: 12px 20px;
    border-bottom: 1px solid #e4e7ed;
    
    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      .header-title {
        font-weight: 500;
        font-size: 16px;
        color: #333;
      }
    }
  }
  
  :deep(.el-card__body) {
    padding: 20px;
  }
  
  :deep(.el-table) {
    border-radius: 4px;
    overflow: hidden;
    
    th.el-table__cell {
      padding: 12px 0;
      background-color: #f5f7fa;
      
      .cell {
        font-weight: 500;
        color: #333;
        text-align: center;
        padding: 0 8px;
      }
    }
    
    td.el-table__cell {
      padding: 10px 0;
      
      .cell {
        text-align: center;
        padding: 0 8px;
      }
    }
    
    // 条纹效果
    .el-table__row--striped {
      background-color: #fafafa;
    }
    
    // 悬停效果
    .el-table__row:hover > td {
      background-color: #ecf5ff !important;
    }
    
    // 空数据样式
    .el-table__empty-block {
      min-height: 120px;
      
      .el-table__empty-text {
        color: #909399;
        font-size: 14px;
      }
    }
  }
}
</style>