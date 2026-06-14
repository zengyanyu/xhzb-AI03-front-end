<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="预约人" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预约状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="全部" clearable @change="handleQuery">
          <el-option label="全部" :value="null" />
          <el-option label="待上门" :value="0" />
          <el-option label="已完成" :value="1" />
          <el-option label="已取消" :value="2" />
          <el-option label="已过期" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="预约时间" prop="timeRange">
        <el-date-picker
          v-model="queryParams.timeRange"
          type="datetimerange"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss"
          @change="handleTimeRangeChange"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- tab导航条件 -->
      <div class="marg-main newBox">
        <SwitchBar
          ref="tabBar"
          :data="reservationTabData"
          @change-id="changeReservationType"
        />
      </div>
    </el-row>

    <el-table v-loading="loading" :data="reservationList" @selection-change="handleSelectionChange">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column label="预约类型" align="center" prop="type">
        <template #default="scope">
          <span>{{ scope.row.type === 0 ? '参观预约' : '探访预约' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约人姓名" align="center" prop="name" />
      <el-table-column label="预约人手机号" align="center" prop="mobile" />
      <el-table-column label="老人姓名" align="center" prop="visitor" />
      <el-table-column label="预约时间" align="center" prop="time" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.time, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约状态" align="center" prop="status">
        <template #default="scope">
          <el-tag
            :type="scope.row.status === 1 ? 'success' : scope.row.status === 0 ? 'warning' : scope.row.status === 2 ? 'danger' : 'info'"
            disable-transitions
          >
            {{ scope.row.status === 0 ? '待上门' : scope.row.status === 1 ? '已完成' : scope.row.status === 2 ? '已取消' : '已过期' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handleArrive(scope.row)"
            :disabled="scope.row.status !== 0"
          >到院</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改预约信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="reservationRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="预约人姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入预约人姓名" />
        </el-form-item>
        <el-form-item label="预约人手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入预约人手机号" />
        </el-form-item>
        <el-form-item label="预约时间" prop="time">
          <el-date-picker clearable
            v-model="form.time"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择预约时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="探访人" prop="visitor">
          <el-input v-model="form.visitor" placeholder="请输入探访人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="确认到院时间"
      v-model="arriveDialogVisible"
      width="500px"
      append-to-body
    >
      <el-form :model="arriveForm" ref="arriveFormRef" label-width="80px">
        <el-form-item label="来访时间" prop="arriveTime" :rules="[{ required: true, message: '请选择来访时间', trigger: 'change' }]">
          <el-date-picker
            v-model="arriveForm.arriveTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择来访时间"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="arriveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitArrive">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Reservation">
import { listReservation, getReservation, delReservation, addReservation, updateReservation } from "@/api/nursing/reservation"
import SwitchBar from '@/components/switchBar/switchBar.vue'

const { proxy } = getCurrentInstance()

const reservationList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    mobile: null,
    time: null,
    type: null,
    status: null,
  },
  rules: {
    name: [
      { required: true, message: "预约人姓名为空，请输入预约人姓名", trigger: "blur" }
    ],
    mobile: [
      { required: true, message: "预约人手机号为空，请输入预约人手机号", trigger: "blur" }
    ],
    time: [
      { required: true, message: "预约时间为空，请选择预约时间", trigger: "blur" }
    ],
    visitor: [
      { required: true, message: "探访人为空，请选择探访人", trigger: "blur" }
    ],
    type: [
      { required: true, message: "预约类型，0：参观预约，1：探访预约不能为空", trigger: "change" }
    ],
    status: [
      { required: true, message: "预约状态，0：待报道，1：已完成，2：取消，3：过期不能为空", trigger: "change" }
    ],
    createTime: [
      { required: true, message: "创建时间不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

const arriveDialogVisible = ref(false)
const arriveForm = reactive({
  id: null,
  arriveTime: ''
})
const arriveFormRef = ref(null)
let currentArriveRow = null

// tab导航数据
const reservationTabData = [
  { id: null, name: '全部' },
  { id: 0, name: '参观预约' },
  { id: 1, name: '探访预约' }
]

// tab切换事件
function changeReservationType(type) {
  queryParams.value.type = type
  queryParams.value.pageNum = 1
  getList()
}

/** 查询预约信息列表 */
function getList() {
  loading.value = true
  listReservation(queryParams.value).then(response => {
    reservationList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    name: null,
    mobile: null,
    time: null,
    visitor: null,
    type: null,
    status: null,
    createTime: null,
    updateTime: null,
    createBy: null,
    updateBy: null,
    remark: null
  }
  proxy.resetForm("reservationRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加预约信息"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getReservation(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改预约信息"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["reservationRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateReservation(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addReservation(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除预约信息编号为"' + _ids + '"的数据项？').then(function() {
    return delReservation(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('nursing/reservation/export', {
    ...queryParams.value
  }, `reservation_${new Date().getTime()}.xlsx`)
}

/** 到院按钮操作 */
function handleArrive(row) {
  // 只允许待上门状态弹窗
  if (row.status !== 0) return
  arriveForm.id = row.id
  arriveForm.arriveTime = row.time // 默认填预约时间
  arriveDialogVisible.value = true
  currentArriveRow = row
}

function submitArrive() {
  // 校验
  proxy.$refs.arriveFormRef.validate(valid => {
    if (!valid) return
    updateReservation({
      id: arriveForm.id,
      status: 1,
      arriveTime: arriveForm.arriveTime // 传给后端
    }).then(() => {
      proxy.$modal.msgSuccess("到院成功")
      arriveDialogVisible.value = false
      getList()
    })
  })
}

// 处理预约时间范围
function handleTimeRangeChange(val) {
  if (val && val.length === 2) {
    queryParams.value.startTime = val[0]
    queryParams.value.endTime = val[1]
  } else {
    queryParams.value.startTime = null
    queryParams.value.endTime = null
  }
}

getList()
</script>
