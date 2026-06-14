<template>
  <div class="app-container">
    <el-row :gutter="10" class="newBtn mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="Plus" @click="handleAdd"
          >新增房型</el-button
        >
      </el-col>
      <!-- <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar> -->
    </el-row>

    <el-table
      v-loading="loading"
      :data="roomTypeList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column label="序号" align="center" type="index" width="50" />
      <el-table-column label="房型图片" align="center" prop="photo" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.photo" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="房型名称" align="center" prop="name" />
      <el-table-column label="床位费用（元/月）" align="center" prop="price" />
      <!-- 房型介绍，只展示10个字符 -->
      <el-table-column
        label="房型介绍"
        align="center"
        prop="introduction"
        width="180"
      >
        <template #default="scope">
          <el-tooltip
            :content="scope.row.introduction"
            placement="top"
            popper-class="popperWidth"
            v-if="scope.row.introduction&&scope.row.introduction.length > 10"
          >
            <span class="text-ellipsis-2">{{
              scope.row.introduction || '-'
            }}</span>
          </el-tooltip>
          <span v-else>{{
              scope.row.introduction || '-'
            }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="160"
      >
        <template #default="scope">
          <span>{{
            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}')
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status == 1 ? 'success' : 'danger'">{{
            scope.row.status == 1 ? '启用' : '禁用'
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        fixed="right"
        width="200"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            class="font-bt"
            :class="scope.row.linked && scope.row.linked > 0 ? 'disabled' : ''"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <el-button
            link
            type="primary"
            class="btn-dl"
            :class="scope.row.linked&&scope.row.linked > 0 ? 'disabled' : ''"
            icon="Delete"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
          <el-button
            link
            type="primary"
            :class="
              scope.row.linked&&scope.row.linked > 0
                ? 'disabled btn-dl'
                : scope.row.status === 1
                  ? 'btn-dl'
                  : 'font-bt'
            "
            :icon="scope.row.status == 0 ? 'Unlock' : 'lock'"
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
      @pagination="getList"
    />

    <!-- 添加或修改房型对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form
        ref="roomTypeRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="房型名称" prop="name">
          <el-input
            v-model="form.name"
            maxlength="10"
            show-word-limit
            placeholder="请输入"
          />
        </el-form-item>
        <el-form-item label="床位数量" prop="bedCount">
          <el-input
            v-model="form.bedCount"
            placeholder="请输入"
            :min="0"
            :max="99999"
            @input="handleBedCountInput"
          />
        </el-form-item>
        <el-form-item label="床位费用" prop="price">
          <el-input-number
            v-model="form.price"
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
        <el-form-item label="房型介绍" prop="introduction">
          <el-input
            v-model="form.introduction"
            type="textarea"
            placeholder="请输入"
            :autosize="{ minRows: 3, maxRows: 3 }"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="房型图片" prop="photo">
          <!-- <el-input v-model="form.photo" placeholder="请输入照片" /> -->
          <image-upload :limit="1" v-model="form.photo" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 3 }"
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

<script setup name="RoomType">
import {
  addRoomType,
  delRoomType,
  getRoomType,
  listRoomType,
  updateRoomType,
} from '@/api/nursing/roomType';

const { proxy } = getCurrentInstance();

const roomTypeList = ref([]);
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
    status: 0,
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    bedCount: null,
    price: null,
    introduction: null,
    photo: null,
    status: null,
  },
  rules: {
    name: [
      {
        required: true,
        message: '房型名称为空，请输入房型名称',
        trigger: 'blur',
      },
    ],
    bedCount: [
      {
        required: true,
        message: '床位数量为空，请输入床位数量',
        trigger: 'blur',
      },
    ],
    price: [
      {
        required: true,
        message: '床位费用为空，请输入床位费用',
        trigger: 'change',
        validator: (rule, value, callback) => {
        if (value === null || value === undefined || value === 0) {
          callback(new Error('床位费用为空，请输入床位费用'));
        } else {
          callback();
        }
      }
      },
    ],
    introduction: [
      {
        required: true,
        message: '房型介绍为空，请输入房型介绍',
        trigger: 'blur',
      },
    ],
    photo: [
      {
        required: true,
        message: '房型图片为空，请上传房型图片',
        trigger: 'change',
      },
    ],
    status: [
      {
        required: true,
        message: '状态，0：禁用，1：启用不能为空',
        trigger: 'change',
      },
    ],
    createTime: [
      { required: true, message: '创建时间不能为空', trigger: 'blur' },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);
watch(
  form,
  (newValue) => {
    nextTick(() => {
      console.log(newValue);
      if (newValue.photo) {
        proxy.$refs['roomTypeRef'].validateField('photo');
      }
    });
  },
  { deep: true },
);

const computeLength = (val) => {
  return val.slice(0, 10) + (val.length > 10 ? '...' : '');
};
// 添加输入处理函数
const handleBedCountInput = (value) => {
  if (!value) return;

  // 只保留数字
  let filteredValue = value.replace(/[^0-9]/g, '');

  // 禁止连续多个 0（最多允许 1 个前导 0）
  if (filteredValue.length > 1 && filteredValue.startsWith('0')) {
    filteredValue = filteredValue.replace(/^0+/, '');
  }

  // 如果全部是 0，保留一个 0
  if (!filteredValue) {
    filteredValue = '0';
  }

  // // 限制最大位数（如最多 5 位）
  // if (filteredValue.length > 5) {
  //   filteredValue = filteredValue.slice(0, 5);
  // }

  if (filteredValue !== value) {
    form.value.bedCount = filteredValue;
  }
};
/** 查询房型列表 */
function getList() {
  loading.value = true;
  listRoomType(queryParams.value).then((response) => {
    roomTypeList.value = response.rows;
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
    bedCount: null,
    price: 0.00,
    introduction: null,
    photo: null,
    status: 0,
    createTime: null,
    updateTime: null,
    createBy: null,
    updateBy: null,
    remark: null,
  };
  proxy.resetForm('roomTypeRef');
}

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
  title.value = '新增房型';
}

/** 修改按钮操作 */
function handleUpdate(row) {
  if (row.linked > 0) {
    return;
  }
  reset();
  const _id = row.id || ids.value;
  getRoomType(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = '修改房型';
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs['roomTypeRef'].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateRoomType(form.value).then((response) => {
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getList();
        });
      } else {
        addRoomType(form.value).then((response) => {
          proxy.$modal.msgSuccess('新增成功');
          open.value = false;
          getList();
        });
      }
    }
  });
}

// 使用 async/await 语法优化异步操作
const handleEnable = async (row) => {
  if (row.linked > 0) {
    return;
  }
  try {
    // 获取状态
    const status = row.status;
    const info = status === 0 ? '启用' : '禁用';

    // 使用模板字符串
    const confirmMessage = `此操作将${info}该${row.name}，是否继续？`;

    // 确认操作
    if (await proxy.$modal.confirm(confirmMessage)) {
      // 更新参数
      const param = {
        id: row.id,
        status: status === 0 ? 1 : 0,
      };

      // 执行更新操作
      await updateRoomType(param);
      // 刷新列表
      getList();
      // 成功消息
      proxy.$modal.msgSuccess(`${info}成功`);
    }
  } catch (error) {
    // 异常处理：这里可以根据实际需求进行调整，例如打印错误日志或显示用户友好的错误信息
    console.error('操作失败，请重试或联系管理员。');
  }
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
      return delRoomType(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
// 价格输入框失焦处理
const handlePriceBlur = () => {
  if (form.value.price === null || form.value.price === undefined || form.value.price === '') {
    form.value.price = 0.00;
  }
  if (form.value.price < 0) {
    form.value.price = 0.00;
  }
  // 限制最大值为 10 万
  if (form.value.price > priceMax.value) {
    form.value.price = priceMax.value;
  }
};

// 价格输入框变化处理
const handlePriceChange = (val) => {
  if (val === null || val === undefined || val === '') {
    form.value.price = 0.00;
  }
  if (val < 0) {
    form.value.price = 0.00;
  }
  // 限制最大值为 10 万
  if (val > priceMax.value) {
    form.value.price = priceMax.value;
  }
};
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'elder/roomType/export',
    {
      ...queryParams.value,
    },
    `roomType_${new Date().getTime()}.xlsx`,
  );
}

getList();
</script>
