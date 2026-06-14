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
        <el-form-item label="等级名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="queryParams.status"
            clearable
            placeholder="请选择"
            style="width: 240px"
          >
            <el-option
              v-for="item in nursing_level_status"
              :key="item.value"
              :label="item.label"
              :value="item.value"
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
        <el-button
          type="primary"
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['nursing:nursingLevel:add']"
          >新增护理等级</el-button
        >
      </el-col>
      <!--  <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['nursing:nursingLevel:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['nursing:nursingLevel:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['nursing:nursingLevel:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar> -->
    </el-row>

    <el-table
      v-loading="loading"
      :data="nursingLevelList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        label="序号"
        align="center"
        type="index"
        min-width="65px"
      />
      <el-table-column
        label="护理等级名称"
        align="center"
        prop="name"
        min-width="180"
      />
      <el-table-column
        label="执行护理计划"
        align="center"
        prop="planName"
        min-width="180"
      />
      <el-table-column
        label="护理费用（元/月）"
        align="center"
        prop="fee"
        min-width="180"
      />
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        min-width="120"
      >
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="等级说明"
        align="center"
        prop="description"
        min-width="180"
      >
        <template #default="scope">
          <el-tooltip
            :content="scope.row.description"
            placement="top"
            popper-class="popperWidth"
            :disabled="!scope.row.description"
            v-if="scope.row.description&&scope.row.description.length > 10"
          >
            <span class="text-ellipsis-2">{{
              scope.row.description || '-'
            }}</span>
          </el-tooltip>
          <span v-else>{{
              scope.row.description || '-'
            }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        min-width="180"
      >
        <template #default="scope">
          <span>{{
            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}')
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        min-width="220px"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            class="font-bt"
            :class="scope.row.linked && scope.row.linked > 0 ? 'disabled' : ''"
            v-hasPermi="['nursing:nursingLevel:edit']"
            >修改</el-button
          >
          <el-button
            link
            type="primary"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['nursing:nursingLevel:remove']"
            class="btn-dl"
            :class="scope.row.linked && scope.row.linked > 0 ? 'disabled' : ''"
            >删除</el-button
          >
          <el-button
            link
            type="primary"
            :icon="scope.row.status == 0 ? 'Lock' : 'Unlock'"
            @click="handleEnable(scope.row)"
            :class="
              scope.row.linked && scope.row.linked > 0
                ? 'disabled btn-dl'
                : scope.row.status === 1
                  ? 'btn-dl'
                  : 'font-bt'
            "
            >{{ scope.row.status == 0 ? '启用' : '禁用' }}</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改护理等级对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form
        ref="nursingLevelRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="等级名称" prop="name">
          <el-input
            v-model="form.name"
            clearable
            placeholder="请输入"
            maxlength="10"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="护理计划" prop="lplanId">
          <el-select v-model="form.lplanId" clearable placeholder="请选择">
            <el-option
              v-for="item in nursingPlanList"
              :key="item.id"
              :label="item.planName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="护理费用" prop="fee">
          <el-input-number
            v-model="form.fee"
            :min="0"
            :step="10"
            :max="priceMax"
            placeholder="0.00"
            :decimal-places="2"
            :precision="2"
            :controls="true"
            @blur="handlePriceBlur"
            @change="handlePriceChange"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in nursing_level_status"
              :key="dict.value"
              :value="dict.value"
              :label="dict.value"
              >{{ dict.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="等级说明" prop="description">
          <el-input
            v-model="form.description"
            :autosize="{ minRows: 3, maxRows: 5 }"
            type="textarea"
            clearable
            maxlength="50"
            show-word-limit
            placeholder="请输入"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="NursingLevel">
import {
  addNursingLevel,
  delNursingLevel,
  getNursingLevel,
  listNursingLevel,
  updateNursingLevel,
} from '@/api/nursing/nursingLevel';

import { getNursingPlanAll } from '@/api/nursing/nursingPlan';

const { proxy } = getCurrentInstance();

const { nursing_level_status } = proxy.useDict('nursing_level_status');

const nursingLevelList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const priceMax = ref(100000);
const data = reactive({
  form: {
    status: '1',
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    status: null,
  },
  rules: {
    name: [
      {
        required: true,
        message: '等级名称为空，请输入等级名称',
        trigger: 'blur',
      },
    ],
    lplanId: [
      {
        required: true,
        message: '护理计划为空，请选择护理计划',
        trigger: 'change',
      },
    ],
    fee: [
      {
        required: true,
        message: '护理费用为空，请输入护理费用',
        trigger: 'change',
      validator: (rule, value, callback) => {
        if (value === null || value === undefined || value === 0) {
          callback(new Error('护理费用为空，请输入护理费用'));
        } else {
          callback();
        }
      }
      },
    ],
    status: [
      { required: true, message: '状态为空，请选择状态', trigger: 'change' },
    ],
    description: [
      {
        required: true,
        message: '等级说明为空，请输入等级说明',
        trigger: 'blur',
      },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询护理等级列表 */
function getList() {
  loading.value = true;
  listNursingLevel(queryParams.value).then((response) => {
    nursingLevelList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    name: null,
    lplanId: null,
    fee: 0.00,
    status: '1',
    description: null,
    createTime: null,
    createBy: null,
    updateBy: null,
    remark: null,
    updateTime: null,
  };
  proxy.resetForm('nursingLevelRef');
}

const nursingPlanList = ref([]);

const getAllNursingPlanList = () => {
  getNursingPlanAll().then((response) => {
    nursingPlanList.value = response.data;
  });
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
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
  reset();
  open.value = true;
  title.value = '新增护理等级';
}

/** 修改按钮操作 */
function handleUpdate(row) {
  if (row.linked > 0) {
    return;
  }
  reset();
  const _id = row.id || ids.value;
  getNursingLevel(_id).then((response) => {
    form.value = response.data;
    // 由数字改为字符转
    form.value.status = String(form.value.status);
    open.value = true;
    title.value = '修改护理等级';
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs['nursingLevelRef'].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateNursingLevel(form.value).then((response) => {
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getList();
        });
      } else {
        addNursingLevel(form.value).then((response) => {
          proxy.$modal.msgSuccess('新增成功');
          open.value = false;
          getList();
        });
      }
    }
  });
}

//启用或禁用
const handleEnable = (row) => {
  if (row.linked > 0) {
    return;
  }
  //获取状态
  const status = row.status;
  //提示信息
  const info = status == 0 ? '启用' : '禁用';
  //构建参数
  const params = {
    id: row.id,
    status: status == 0 ? 1 : 0,
  };
  proxy.$modal
    .confirm(`此操作将${info}该${row.name}，是否继续？`)
    .then(function () {
      return updateNursingLevel(params);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(info + '成功');
    })
    .catch(() => {});
};

/** 删除按钮操作 */
function handleDelete(row) {
  if (row.linked > 0) {
    return;
  }
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('此操作将删除该' + row.name + '，是否继续？')
    .then(function () {
      return delNursingLevel(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
// 价格输入框失焦处理
const handlePriceBlur = () => {
  if (form.value.fee === null || form.value.fee === undefined || form.value.fee === '') {
    form.value.fee = 0.00;
  }
  if (form.value.fee < 0) {
    form.value.fee = 0.00;
  }
  // 限制最大值为 10 万
  if (form.value.fee > priceMax.value) {
    form.value.fee = priceMax.value;
  }
};

// 价格输入框变化处理
const handlePriceChange = (val) => {
  if (val === null || val === undefined || val === '') {
    form.value.fee = 0.00;
  }
  if (val < 0) {
    form.value.fee = 0.00;
  }
  // 限制最大值为 10 万
  if (val > priceMax.value) {
    form.value.fee = priceMax.value;
  }
};
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'nursing/nursingLevel/export',
    {
      ...queryParams.value,
    },
    `nursingLevel_${new Date().getTime()}.xlsx`,
  );
}

getList();
getAllNursingPlanList();
</script>
