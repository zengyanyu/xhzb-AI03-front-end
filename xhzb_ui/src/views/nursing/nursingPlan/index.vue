<template>
  <div class="app-container">
    <div class="searchBox">
      <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
      >
        <el-form-item label="名称" prop="planName">
          <el-input
            v-model="queryParams.planName"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择"
            clearable
          >
            <el-option
              v-for="dict in nursing_plan_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery"
            >搜索</el-button
          >
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-row :gutter="10" class="newBtn mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="Plus" @click="handleAdd"
          >新增护理计划</el-button
        >
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="planList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column label="序号" type="index" width="50" />
      <el-table-column label="名称" align="center" prop="planName" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template #default="scope">
          <span>{{
            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}')
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        fixed="right"
        width="280"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            class="font-bt"
            :class="scope.row.linked&&scope.row.linked > 0 ? 'disabled' : ''"
            @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <el-button
            link
            type="primary"
            icon="Delete"
            class="btn-dl"
            :class="scope.row.linked&&scope.row.linked > 0 ? 'disabled' : ''"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
          <el-button
            link
            type="primary"
            icon="Delete"
            @click="handleLook(scope.row)"
            >查看</el-button
          >
          <el-button
            link
            type="primary"
            :icon="scope.row.status == 0 ? 'Unlock' : 'lock'"
            :class="
              scope.row.linked&&scope.row.linked > 0
                ? 'disabled btn-dl'
                : scope.row.status === 1
                  ? 'btn-dl'
                  : 'font-bt'
            "
            @click="handleEnable(scope.row)"
            >{{ scope.row.status == 1 ? '禁用' : '启用' }}</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getNursingPalnList"
    />

    <!-- //////////////////////////////// -->
    <el-dialog
      :title="title"
      v-model="dialogVisible"
      width="840"
      @close="cancel()"
    >
      <el-form
        ref="planRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
      >
        <el-row gutter="10">
          <el-col :span="24" class="elcolFlex">
            <el-form-item label="护理计划名称：" prop="planName">
              <el-input
                v-model="formData.planName"
                maxlength="10"
                show-word-limit
                type="text"
                clearable
                placeholder="请输入"
              ></el-input>
            </el-form-item>
            <el-form-item label="状态：" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio
                  v-for="dict in nursing_plan_status"
                  :value="dict.value"
                  :label="dict.value"
                  :key="dict.value"
                  >{{ dict.label }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
            <el-form-item label="排序：" prop="sortNo">
              <el-input-number
                v-model="formData.sortNo"
                :min="1"
                large-number
                :decimal-places="1"
                :precision="0"
                :step="1"
                placeholder="请输入"
                @blur="textBlurNo"
                @change="textBlurNo"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="护理项目：" prop="price">
          <div class="info family">
            <div class="tableHead">
              <div>护理项目名称</div>
              <div>期望服务时间</div>
              <div>执行周期</div>
              <div>执行频次(次)</div>
              <div v-if="!isLook">操作</div>
            </div>
            <div class="tableBody">
              <div
                class="tableColumn"
                v-for="(item, index) in nursingPalnList"
                :key="index"
              >
                <div class="column" :class="{ 'is-error': item.projectIdError }">
                  <el-select
                    v-model="item.projectId"
                    placeholder="请选择"
                    @change="(val) => handleProjectChange(val, index)"
                    clearable
                  >
                    <el-option
                      v-for="option in getAvailableProjectOptions(index)"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    ></el-option>
                  </el-select>
                  <!-- <div v-if="item.projectIdError" class="error-message">请选择护理项目</div> -->
                </div>
                <div class="column" :class="{ 'is-error': item.executeTimeError }">
                  <el-time-picker
                    v-model="item.executeTime"
                    format="HH:mm"
                    value-format="HH:mm"
                    :style="{ width: '100%' }"
                    placeholder="请选择时间"
                    clearable
                    @change="() => clearTimeError(index)"
                  ></el-time-picker>
                  <!-- <div v-if="item.executeTimeError" class="error-message">请选择期望服务时间</div> -->
                </div>
                <div class="column" :class="{ 'is-error': item.executeCycleError }">
                  <el-select
                    v-model="item.executeCycle"
                    placeholder="请选择"
                    @change="() => clearCycleError(index)"
                  >
                    <el-option
                      v-for="item in executeCycleOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                  <!-- <div v-if="item.executeCycleError" class="error-message">请选择执行周期</div> -->
                </div>
                <div class="column" :class="{ 'is-error': item.executeFrequencyError }">
                  <el-input-number
                    v-model="item.executeFrequency"
                    :controls="false"
                    :max="7"
                    :min="1"
                    :precision="0"
                    :step="1"
                    @change="() => clearFrequencyError(index)"
                  />
                  <!-- <div v-if="item.executeFrequencyError" class="error-message">请输入执行频次</div> -->
                </div>
                <div class="column" v-if="!isLook">
                  <el-icon
                    v-if="nursingPalnList.length > 1"
                    @click="handleRowDel(item, index)"
                    class="delect"
                    ><Minus
                  /></el-icon>
                  <el-icon @click="handleRowAdd" class="add"><Plus /></el-icon>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel()">取消</el-button>
      </div>
    </el-dialog>
    <!-- //////////////////////////////// -->
    <!-- 查看 -->
    <DialogLook
      ref="lookRef"
      :visible="lookVisible"
      :base-data="formData"
      :palnData="nursingProjectOptions"
      @handle-close="handleLookClose"
    ></DialogLook>
    <!-- end -->
  </div>
</template>

<script setup name="Plan">
import {
  addNursingPlan,
  delNursingPlan,
  getNursingPlan,
  listNursingPlan,
  planStatus,
  updateNursingPlan,
} from '@/api/nursing/nursingPlan';
import { getProjectAll } from '@/api/nursing/project';
import { onMounted, watch } from 'vue';
// 查看
import DialogLook from './look.vue'
const { proxy } = getCurrentInstance();
const { nursing_plan_status } = proxy.useDict('nursing_plan_status');
const queryRef = ref(); // 表单
const planList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const dialogVisible = ref(false);
const lookVisible =ref(false)
const nursingProjectOptions = ref([]);
const isLook = ref(false);
const formRef = ref(null)
const formData = ref({
  status: '1',
  sortNo: 1,
});
const maxSort = ref(99999999);
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  planName: null,
  status: null,
});

const rules = ref({
  planName: [
    {
      required: true,
      message: '护理计划名称为空，请输入护理计划名称',
      trigger: 'blur',
    },
  ],
  status: [
    {
      required: true,
      message: '状态为空，请选择状态',
      trigger: 'change',
    },
  ],
  sortNo: [
    {
      required: true,
      message: '排序为空，请选择排序',
      trigger: 'change',
    },
  ],
});

const executeCycleOptions = ref([
  { label: '每天', value: 0 },
  { label: '每周', value: 1 },
  { label: '每月', value: 2 },
]);

const getDefaultExecuteTime = () => '08:00'

// 获取当前已选择的项目ID（用于选项递减逻辑）
const getSelectedProjectIds = (currentIndex = -1) => {
  return nursingPalnList.value
    .map((item, index) => {
      // 如果是当前行，并且有值，则包含在内（因为当前行正在编辑，它的值应该被排除在可选列表外）
      if (index === currentIndex && item.projectId) {
        return item.projectId;
      }
      // 其他行，如果有值，则包含在内
      if (index !== currentIndex && item.projectId) {
        return item.projectId;
      }
      return null;
    })
    .filter(id => id !== null);
};

// 获取当前行可用的项目选项
const getAvailableProjectOptions = (currentIndex) => {
  // 获取所有已选择的项目ID（排除当前行）
  const selectedIds = nursingPalnList.value
    .map((item, index) => {
      if (index !== currentIndex && item.projectId) {
        return item.projectId;
      }
      return null;
    })
    .filter(id => id !== null);

  // 过滤选项：不在已选择列表中，或者等于当前行已选的值
  return nursingProjectOptions.value.filter(option => {
    // 如果是当前行已选的值，始终显示
    if (option.value === nursingPalnList.value[currentIndex]?.projectId) {
      return true;
    }
    // 否则，不在已选择列表中才显示
    return !selectedIds.includes(option.value);
  });
};

// 处理项目变更
const handleProjectChange = (val, index) => {
  // 清除当前行的错误状态
  clearProjectError(index);
  
  // 强制刷新其他行的选项（通过响应式系统自动完成）
  nursingPalnList.value = [...nursingPalnList.value];
};

onMounted(() => {
  getAllProjectList();
});

//查询所有护理项目
const getAllProjectList = () => {
  getProjectAll().then((res) => {
    nursingProjectOptions.value = res.data;
  });
};

// 初始化护理项目列表项的错误状态
const initNursingPlanItem = () => ({
  projectId: '',
  projectIdError: false,
  executeTime: getDefaultExecuteTime(),
  executeTimeError: false,
  executeCycle: 1,
  executeCycleError: false,
  executeFrequency: '1',
  executeFrequencyError: false,
});

const nursingPalnList = ref([initNursingPlanItem()]);

const handleRowAdd = () => {
  nursingPalnList.value.push(initNursingPlanItem());
};

//删除行数据
const handleRowDel = (item, index) => {
  if (nursingPalnList.value.length === 1) return;
  nursingPalnList.value.splice(index, 1);
  // 强制刷新，让选项重新计算
  nursingPalnList.value = [...nursingPalnList.value];
};

// 清除各字段错误状态的方法
const clearProjectError = (index) => {
  if (nursingPalnList.value[index]) {
    nursingPalnList.value[index].projectIdError = false;
  }
};

const clearTimeError = (index) => {
  if (nursingPalnList.value[index]) {
    nursingPalnList.value[index].executeTimeError = false;
  }
};

const clearCycleError = (index) => {
  if (nursingPalnList.value[index]) {
    nursingPalnList.value[index].executeCycleError = false;
  }
};

const clearFrequencyError = (index) => {
  if (nursingPalnList.value[index]) {
    nursingPalnList.value[index].executeFrequencyError = false;
  }
};

// 清空所有错误状态
const clearAllErrors = () => {
  nursingPalnList.value.forEach((item, index) => {
    item.projectIdError = false;
    item.executeTimeError = false;
    item.executeCycleError = false;
    item.executeFrequencyError = false;
  });
};

// 验证护理项目列表是否填写完整
const validateNursingPlanList = () => {
  let isValid = true;
  
  nursingPalnList.value.forEach((item, index) => {
    // 验证护理项目ID
    if (!item.projectId) {
      item.projectIdError = true;
      isValid = false;
    } else {
      item.projectIdError = false;
    }
    
    // 验证期望服务时间
    if (!item.executeTime) {
      item.executeTimeError = true;
      isValid = false;
    } else {
      item.executeTimeError = false;
    }
    
    // 验证执行周期
    if (!item.executeCycle && item.executeCycle !== 0) {
      item.executeCycleError = true;
      isValid = false;
    } else {
      item.executeCycleError = false;
    }
    
    // 验证执行频次
    if (!item.executeFrequency && item.executeFrequency !== 0) {
      item.executeFrequencyError = true;
      isValid = false;
    } else {
      item.executeFrequencyError = false;
    }
  });
  
  return isValid;
};

/** 查询护理计划列表 */
function getNursingPalnList() {
  loading.value = true;
  listNursingPlan(queryParams.value).then((response) => {
    planList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  dialogVisible.value = false;
  isLook.value = false;
  reset();
}

// 表单重置
function reset() {
  formData.value = {
    planName: null,
    status: '1',
    sortNo: 1,
  };
  nursingPalnList.value = [initNursingPlanItem()];
  clearAllErrors();
  proxy.resetForm('planRef');
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getNursingPalnList();
}

/** 重置按钮操作 */
function resetQuery() {
  dialogVisible.value = false;
  proxy.resetForm('queryRef');
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  dialogVisible.value = true;
  title.value = '新增护理计划';
}
/** 修改按钮操作 */
function handleUpdate(row) {
  // row.linked大于1，禁止往下执行
  if (row.linked > 0) return
  dialogVisible.value = true;
  const _id = row.id || ids.value;
  getDetails(_id);
}
// 获取详情
const getDetails = (id) => {
  getNursingPlan(id).then((response) => {
    formData.value = response.data;
    formData.value.status = String(formData.value.status);
    
    // 处理项目列表，添加错误状态字段
    nursingPalnList.value = formData.value.projectPlans.map(item => ({
      ...item,
      projectIdError: false,
      executeTimeError: false,
      executeCycleError: false,
      executeFrequencyError: false
    }));
    title.value = '修改护理计划';
  });
};
/** 提交按钮 */
function submitForm() {
  
  
  formData.value['projectPlans'] = nursingPalnList.value;

  proxy.$refs['planRef'].validate((valid) => {
    // 先验证护理项目列表是否填写完整
  if (!validateNursingPlanList()) {
    proxy.$modal.msgError('护理项目信息不完整，请重新设置');
    return;
  }
  
  // 判断选择的护理项目是否有重复的
  if (hasDuplicateIds(nursingPalnList.value)) {
    proxy.$modal.msgError('请勿选择重复的护理项目');
    return;
  }
    if (valid) {
      if (formData.value.id != null) {
        updateNursingPlan(formData.value).then((response) => {
          proxy.$modal.msgSuccess('修改成功');
          cancel();
          getNursingPalnList();
        });
      } else {
        addNursingPlan(formData.value).then((response) => {
          proxy.$modal.msgSuccess('新增成功');
          cancel();
          getNursingPalnList();
        });
      }
    }
  });
}

// 判断数组中的对象的id是否有相同的
function hasDuplicateIds(objectsArray) {
  if (!Array.isArray(objectsArray) || objectsArray.length === 0) {
    return false;
  }

  const idSet = new Set();

  for (const obj of objectsArray) {
    if (obj.projectId !== undefined && obj.projectId !== '') {
      if (idSet.has(obj.projectId)) {
        return true; // 发现有重复ID，直接返回true
      }
      idSet.add(obj.projectId);
    }
  }

  return false; // 没有重复ID
}

// 使用 async/await 语法优化异步操作
const handleEnable = async (row) => {
  if (row.linked > 0) return
  try {
    const status = row.status;
    const info = status === 0 ? '启用' : '禁用';

    const confirmMessage = `此操作将${info}该${row.planName}，是否继续？`;

    if (await proxy.$modal.confirm(confirmMessage)) {
      const param = {
        id: row.id,
        status: status === 0 ? 1 : 0,
      };

      await planStatus(param);
      getNursingPalnList();
      proxy.$modal.msgSuccess(`${info}成功`);
    }
  } catch (error) {
    console.error('操作失败，请重试或联系管理员。');
  }
};

/** 删除按钮操作 */
function handleDelete(row) {
  if (row.linked > 0) return
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('此操作将删除该' + row.planName + '，是否继续？')
    .then(function () {
      return delNursingPlan(_ids);
    })
    .then(() => {
      getNursingPalnList();
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
//监听排序
const textBlurNo = () => {
  const data = formData.value.sortNo;
  
  if (data <= 1) {
    formData.value.sortNo = 1;
  }
  if (data > maxSort.value) {
    formData.value.sortNo = maxSort.value;
  }
};
// 查看
const handleLook = (row) => {
  lookVisible.value = true
  getDetails(row.id);
};
const handleLookClose=()=>{
  lookVisible.value = false

}
getNursingPalnList();
</script>
<style src="./index.scss" lang="scss"></style>
