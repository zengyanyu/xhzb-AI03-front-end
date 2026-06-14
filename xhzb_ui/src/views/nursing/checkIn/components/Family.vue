<template>
  <div class="info family">
    <h3>家属信息</h3>
    
    <!-- 表格 -->
    <el-table :data="relationData" style="width: 100%">
      <!-- 家属姓名列 -->
      <el-table-column label="家属姓名" min-width="200">
        <template #default="{ row, $index }">
          <el-input
            v-model="row.name"
            maxlength="10"
            clearable
            show-word-limit
            placeholder="请输入"
            :disabled="type === 'read'"
            @input="handleInputChange"
          />
        </template>
      </el-table-column>
      
      <!-- 家属联系方式列 -->
      <el-table-column label="家属联系方式" min-width="220">
        <template #default="{ row, $index }">
          <el-input
            v-model="row.phone"
            placeholder="请输入"
            maxlength="11"
            clearable
            show-word-limit
            :disabled="type === 'read'"
            @input="handlePhoneInput(row)"
            oninput="value=value.replace(/[^\d]/g,'')"
          />
        </template>
      </el-table-column>
      
      <!-- 与老人关系列 -->
      <el-table-column label="与老人关系" min-width="150">
        <template #default="{ row, $index }">
          <el-select
            v-model="row.kinship"
            placeholder="请选择"
            :disabled="type === 'read'"
            @change="handleInputChange"
          >
            <el-option
              v-for="item in elderRelation"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </template>
      </el-table-column>
      
      <!-- 操作列 -->
      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row, $index }">
            <el-icon @click="handleRowAdd" class="add"><Plus /></el-icon>
             <el-icon @click="handleRowDel(row, $index)" v-if="relationData.length > 1" class="delete"><Minus /></el-icon>
          <!-- <div v-if="type !== 'read'">
            <el-button
              type="primary"
              :icon="Plus"
              circle
              size="small"
              @click="handleRowAdd"
              title="新增"
            />
            <el-button
              type="danger"
              :icon="Minus"
              circle
              size="small"
              :disabled="relationData.length <= 1"
              @click="handleRowDel(row, $index)"
              title="删除"
              style="margin-left: 8px;"
            />
          </div>
          <span v-else>--</span> -->
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script setup>
import { watch, ref } from "vue";
import { ElMessage } from 'element-plus';
import { Plus, Minus } from '@element-plus/icons-vue';

const relationData = ref([
  {
    name: '',
    phone: '',
    kinship: '',
  },
]);

const elderRelation = ref([
  { label: '子女', value: '0' },
  { label: '配偶', value: '1' },
  { label: '亲属', value: '2' },
  { label: '朋友', value: '3' },
  { label: '社工', value: '4' },
  { label: '其他', value: '5' },
]);

// 手机号正则验证
const phoneRegex = /^1[3-9]\d{9}$/;

// 接收父组件的数据
const props = defineProps({
  familyInfo: {
    type: Array,
    default: () => [],
  },
  type: {
    type: String,
    default: '',
  },
});

// 监听父组件传递的数据变化
watch(
  () => props.familyInfo,
  (newVal) => {
    if (newVal && newVal.length > 0) {
      relationData.value = newVal.map(item => ({ ...item }));
    } else if (props.type !== 'read') {
      // 如果没有数据且不是只读模式，至少保留一行
      relationData.value = [{
        name: '',
        phone: '',
        kinship: '',
      }];
    }
  },
  { immediate: true, deep: true }
);

// 监听输入变化，触发事件
const handleInputChange = () => {
  emits('setFamily', relationData.value);
};

// 处理手机号输入，只允许数字
const handlePhoneInput = (item) => {
  if (item.phone) {
    item.phone = item.phone.replace(/[^\d]/g, '');
  }
  handleInputChange();
};

// 新增行
const handleRowAdd = () => {
  const obj = {
    name: '',
    phone: '',
    kinship: '',
  };
  relationData.value.push(obj);
  handleInputChange();
};

// 删除行数据
const handleRowDel = (item, index) => {
  if (relationData.value.length === 1) {
    ElMessage.warning('至少保留一条家属信息');
    return;
  }
  relationData.value.splice(index, 1);
  handleInputChange();
};

// 数据完整性检查（包含具体错误提示）
const validate = () => {
  // 如果是只读模式，直接返回成功
  if (props.type === 'read') {
    return true;
  }

  // 检查每一行数据是否完整
  for (let i = 0; i < relationData.value.length; i++) {
    const item = relationData.value[i];

    // 检查家属姓名
    if (!item.name || !item.name.trim()) {
      ElMessage.error(`家属信息不完整，请输入家属姓名`);
      return false;
    }

    // 检查手机号是否为空
    if (!item.phone || !item.phone.trim()) {
      ElMessage.error(`家属信息不完整，请输入家属联系方式`);
      return false;
    }

    // 验证手机号格式
    if (!phoneRegex.test(item.phone)) {
      ElMessage.error(`家属联系方式错误，请重新输入正确的11位手机号`);
      return false;
    }

    // 检查与老人关系
    if (!item.kinship && item.kinship !== 0) {
      ElMessage.error(`家属信息不完整，请选择与老人关系`);
      return false;
    }
  }

  return true;
};

// 获取表单数据
const getFormData = () => {
  return relationData.value;
};

// 声明父组件的方法
const emits = defineEmits(['setFamily']);

// 暴露方法和数据给父组件
defineExpose({
  validate,
  getFormData,
});
</script>

<style scoped lang="scss">
.family {
  h3 {
    margin-bottom: 16px;
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }
  
  .table-footer {
    margin-top: 16px;
    display: flex;
    align-items: center;
    gap: 16px;
    
    .tips {
      font-size: 12px;
      color: #909399;
    }
  }
  
  // 表格样式优化
  :deep(.el-table) {
    .el-input, .el-select {
      width: 100%;
      min-width: 100px;
      max-width: 340px;
    }
    
    .el-table__cell {
      padding: 8px 0;
    }
    
    // 按钮组样式
    .el-button.is-circle {
      padding: 6px;
    }
  }
}
.el-icon{
                    &.add {
                        margin-right: 10px; 
                        color:#00b8a0;
                    }
                    &.delete {
                        margin-right: 10px; 
                        color:#ff4d4f;
                    }
                }
</style>