<!-- 查看护理计划弹窗 - Element Plus版本 -->
<template>
  <div class="dialogBox lookBox">
    <el-dialog
      v-model="formVisible"
      title="查看护理计划"
      :show-close="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="60%"
      @close="onClickCloseBtn"
      destroy-on-close
    >
      <!-- 表单内容 -->
      <div>
        <el-form
          ref="formRef"
          :model="formData"
          label-width="110px"
          label-position="right"
        >
          <el-form-item label="护理计划名称：">
            <span>{{ formData.planName }}</span>
          </el-form-item>
          <el-form-item label="状态：">
            <span>{{ formData.status==='1'?'启用':'禁用' }}</span>
          </el-form-item>
          <el-form-item label="排序：">
            <span>{{ formData.sortNo }}</span>
          </el-form-item>
          <el-form-item label="护理项目：" class="lookPro">
            <div class="scrollTable dialogHeight">
              <el-table
                ref="tableRef"
                :data="formData.projectPlans"
                height="423"
                row-key="key"
                style="width: 100%"
                :cell-style="{ fontSize: '14px' }"
              >
                <el-table-column
                  prop="projectName"
                  label="护理项目名称"
                  min-width="120"
                  show-overflow-tooltip
                >
                <template #default="{ row }">
                   {{ palnData.find(item => item.value === row.projectId)?.label || row.projectId }}
                </template>
                </el-table-column>
                
                <el-table-column
                  prop="executeTime"
                  label="期望服务时间"
                  min-width="120"
                  show-overflow-tooltip
                />
                
                <el-table-column
                  prop="executeCycle"
                  label="执行周期"
                  min-width="100"
                >
                  <template #default="{ row }">
                    <span v-if="row.executeCycle === 0">每日</span>
                    <span v-else-if="row.executeCycle === 1">每周</span>
                    <span v-else>每月</span>
                  </template>
                </el-table-column>
                
                <el-table-column
                  prop="executeFrequency"
                  label="执行频次(次)"
                  width="100"
                />
              </el-table>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElDialog, ElForm, ElFormItem, ElTable, ElTableColumn } from 'element-plus'
import type { FormInstance } from 'element-plus'

// 定义props
const props = defineProps({
  // 弹层隐藏显示
  visible: {
    type: Boolean,
    default: false
  },
  // 详情数据
  baseData: {
    type: Object,
    default: () => ({})
  },
  // 所有项目（如果需要使用的话）
  palnData: {
    type: Array,
    default: () => []
  }
})

// 定义事件
const emit = defineEmits([
  'handleClose',
  'fetchData',
  'handleAdd',
  'handleEdit',
  'selectChange'
])

// 定义响应式变量
const formVisible = ref(false)
const formData = ref<any>({})
const formRef = ref<FormInstance>()
const tableRef = ref()

// 监听父级传递的visible值
watch(
  () => props.visible,
  (newVal) => {
    formVisible.value = newVal
  }
)

// 监听父级传递的data值
watch(
  () => props.baseData,
  (newVal) => {
    formData.value = newVal || {}
  },
  { deep: true }
)

// 点击关闭
const onClickCloseBtn = () => {
  formVisible.value = false
  emit('handleClose')
}
</script>

<style scoped lang="scss">
.dialog-content {
  padding: 20px 0;
}

.scrollTable {
  width: 100%;
}

.dialogHeight {
  min-height: 423px;
}

/* Element Plus 表格样式优化 */
:deep(.el-table) {
  --el-table-border-color: #e8e8e8;
  --el-table-header-bg-color: #f5f7fa;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #333;
  font-weight: 500;
}

:deep(.el-table td) {
  color: #666;
}

/* 表单样式优化 */
:deep(.el-form-item) {
  margin-bottom: 22px;
}

:deep(.el-form-item__label) {
  color: #333;
  font-weight: 400;
}

/* 确保弹窗宽度合适 */
:deep(.el-dialog) {
  max-width: 90%;
}
.lookBox{
    :deep(.el-dialog__body){
        padding-top: 15px;
    }
    .el-form{
        // display: flex;一行两个
        display: flex;
        flex-wrap: wrap;
        width: 100%;
         .el-form-item{
            width: 50%;
            margin-bottom: 10px;
            :deep(.el-form-item__label){
                padding: 0;
            }
            &.lookPro{
                width: 100%;
            }
         }
    }
}
</style>