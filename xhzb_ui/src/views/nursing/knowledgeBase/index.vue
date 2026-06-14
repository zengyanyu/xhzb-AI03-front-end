<template>
  <div class="app-container">
    <div class="searchBox">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="知识标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择" clearable>
          <el-option label="核心业务管理类" value="1" />
          <el-option label="运营支持管理类" value="2" />
          <el-option label="安全与合规管理类" value="3" />
          <el-option label="服务质量与评估类" value="4" />
          <el-option label="支持系统与设施类" value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['nursing:knowledgeBase:add']"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="knowledgeBaseList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" type="index" align="center"  width="50"/>
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="分类" align="center" prop="category" >
        <template #default="scope">
          <span>
            {{
              scope.row.category == 1 ? '核心业务管理类' :
              scope.row.category == 2 ? '运营支持管理类' :
              scope.row.category == 3 ? '安全与合规管理类' :
              scope.row.category == 4 ? '服务质量与评估类' :
              scope.row.category == 5 ? '支持系统与设施类' :
              ''
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="标签" align="center" prop="tags" />
      <el-table-column label="状态" align="center" prop="status" >
        <template #default="scope">
          <el-tag :type="scope.row.status == 1 ? 'success' : 'danger'">{{
            scope.row.status === 1 ? '启用' : '禁用'
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority" />
      <!-- <el-table-column label="文档访问URL" align="center" prop="documentUrl" /> -->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handlePreview(scope.row)" v-hasPermi="['nursing:knowledgeBase:edit']">预览</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['nursing:knowledgeBase:remove']">删除</el-button>
          <el-button
            link
            type="primary"
            :icon="scope.row.status == 0 ? 'Unlock' : 'lock'"
            @click="handleEnable(scope.row)"
            >{{ scope.row.status == 1 ? '禁用' : '启用' }}</el-button
          >
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

    <!-- 添加或修改知识库对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body :close-on-click-modal="!dialogLoading" :close-on-press-escape="!dialogLoading" :show-close="!dialogLoading">
      <div class="dialog-content-wrapper">
        <el-form ref="knowledgeBaseRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="分类" prop="category">
            <el-select v-model="form.category" placeholder="请选择分类">
              <el-option label="核心业务管理类" value="1" />
              <el-option label="运营支持管理类" value="2" />
              <el-option label="安全与合规管理类" value="3" />
              <el-option label="服务质量与评估类" value="4" />
              <el-option label="支持系统与设施类" value="5" />
            </el-select>
          </el-form-item>
          <el-form-item label="标签数组" prop="tags">
            <el-input v-model="form.tags" placeholder="请输入标签数组" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :label="0">禁用</el-radio>
              <el-radio :label="1">启用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="form.priority" placeholder="请选择优先级">
              <el-option label="1" value="1" />
              <el-option label="2" value="2" />
              <el-option label="3" value="3" />
              <el-option label="4" value="4" />
              <el-option label="5" value="5" />
            </el-select>
          </el-form-item>
          <el-form-item label="文档上传" prop="documentUrl">
            <el-upload ref="documentUrl" :file-list="documentUrlfileList" :action="documentUrlAction"
              :before-upload="documentUrlBeforeUpload" accept=".pdf"
              :headers="headers"
              :on-success="(res) => handleSuccess(res)"
              :on-remove="handleRemove">
              <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">上传不超过 10MB 的文件</div>
            </el-upload>
          </el-form-item>
        </el-form>
        <!-- 遮罩层 -->
        <div v-if="dialogLoading" class="submit-loading-overlay">
          <div class="loading-content">
            <el-icon class="loading-icon"><Loading /></el-icon>
            <span class="loading-text">正在保存，请稍候...</span>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :loading="dialogLoading" @click="submitForm">确 定</el-button>
          <el-button @click="cancel" :disabled="dialogLoading">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="KnowledgeBase">
import { listKnowledgeBase, getKnowledgeBase, delKnowledgeBase, addKnowledgeBase, updateKnowledgeBase } from "@/api/nursing/knowledgeBase"
import { Loading } from '@element-plus/icons-vue'
const baseUrl = import.meta.env.VITE_APP_BASE_API;

const { proxy } = getCurrentInstance()

import { getToken } from '@/utils/auth';

const headers = {
    Authorization: 'Bearer ' + getToken(),
};

const knowledgeBaseList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const submitLoading = ref(false)
const dialogLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: null,
    category: null,
    status: null,
    priority: null,
  },
  rules: {
    title: [
      { required: true, message: "知识标题不能为空", trigger: "blur" }
    ],
    category: [
      { required: true, message: "分类不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态 0-禁用  1-启用不能为空", trigger: "change" }
    ],
    priority: [
      { required: true, message: "优先级(1-5)不能为空", trigger: "blur" }
    ],
    createTime: [
      { required: true, message: "创建时间不能为空", trigger: "blur" }
    ],
    updateTime: [
      { required: true, message: "修改时间不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)


// const documentUrlAction = ref('/dev-api/nursing/knowledgeBase/upload')
const documentUrlAction = ref(import.meta.env.VITE_APP_BASE_API+'/nursing/knowledgeBase/upload')
// 上传文件列表
const documentUrlfileList = ref([])
/**
 * @name: 上传之前的文件判断
 * @description: 上传之前的文件判断，判断文件大小文件类型等
 * @param {*} file
 * @return {*}
 */
function documentUrlBeforeUpload(file) {
    let isRightSize = file.size / 1024 / 1024 < 2
    if (!isRightSize) {
        ElMessage.error('文件大小超过 2MB')
    }
    return isRightSize
}

//上传成功之后给属性pdfUrl赋值
const handleSuccess = (val) => {
  form.value['documentUrl'] = val.url
  form.value['title'] = val.originalFilename.substring(0, val.originalFilename.lastIndexOf('.'))
}
//删除文件给属性pdfUrl赋值为空
const handleRemove = () => {
  form.value['documentUrl'] = ''
  form.value['title'] = ''
}

/** 查询知识库列表 */
function getList() {
  loading.value = true
  listKnowledgeBase(queryParams.value).then(response => {
    knowledgeBaseList.value = response.rows
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
    title: null,
    category: null,
    tags: null,
    status: null,
    priority: null,
    file: null,
    createTime: null,
    updateTime: null,
    createBy: null,
    updateBy: null,
    remark: null
  }
  proxy.resetForm("knowledgeBaseRef")
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
  title.value = "添加知识库"
  handleRemove()
  documentUrlfileList.value=[]
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getKnowledgeBase(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改知识库"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["knowledgeBaseRef"].validate(valid => {
    if (valid) {
      dialogLoading.value = true
      if (form.value.id != null) {
        updateKnowledgeBase(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        }).finally(() => {
          dialogLoading.value = false
        })
      } else {
        addKnowledgeBase(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        }).finally(() => {
          dialogLoading.value = false
        })
      }
    }
  })
}

// 使用 async/await 语法优化异步操作
const handleEnable = async (row) => {
  try {
    // 获取状态
    const status = row.status;
    const info = status === 0 ? '启用' : '禁用';

    // 使用模板字符串
    const confirmMessage = `是否确认${info}护理项目的数据项？`;

    // 确认操作
    if (await proxy.$modal.confirm(confirmMessage)) {
      // 更新参数
      const param = {
        id: row.id,
        status: status === 0 ? 1 : 0,
      };

      // 执行更新操作
      await updateKnowledgeBase(param);
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
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除知识库编号为"' + _ids + '"的数据项？').then(function() {
    return delKnowledgeBase(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('nursing/knowledgeBase/export', {
    ...queryParams.value
  }, `knowledgeBase_${new Date().getTime()}.xlsx`)
}

/** 预览PDF文档 */
function handlePreview(row) {
  if (!row.documentUrl) {
    proxy.$modal.msgError('未找到文档URL');
    return;
  }
  // 若documentUrl已包含http则直接用，否则拼baseUrl
  const url = row.documentUrl.startsWith('http') ? row.documentUrl : baseUrl + row.documentUrl;
  window.open(url, '_blank');
}

getList()
</script>

<style scoped>
.dialog-content-wrapper {
  position: relative;
}

.submit-loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  border-radius: 4px;
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.loading-icon {
  font-size: 32px;
  color: #409eff;
  animation: rotating 2s linear infinite;
}

.loading-text {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
