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
          <el-form-item label="老人姓名" prop="elderName">
          <el-input
            v-model="queryParams.elderName"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input
            v-model="queryParams.idCard"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
          <el-form-item label="评估进度" prop="evaluationProgress">
          <el-select
            v-model="queryParams.evaluationProgress"
            placeholder="请选择"
            clearable
          >
            <el-option
              v-for="dict in evaluation_progress"
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
          >新建健康评估</el-button
        >
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="healthAssessmentList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        label="序号"
        align="center"
        type="index"
        min-width="70px"
      />
      <el-table-column
        label="老人姓名"
        align="center"
        prop="elderName"
        width="180"
      />
      <el-table-column
        label="身份证号"
        align="center"
        prop="idCard"
        width="180"
      />
      <!-- <el-table-column label="健康评分" align="center" prop="healthScore" min-width="120px" /> -->
      <el-table-column
        label="核心建议"
        align="center"
        prop="coreSuggestion"
        min-width="120px"
      >
        <template #default="scope">
          <span v-if="scope.row.coreSuggestion === 0"
            ><span class="tagCol2">不建议入住</span></span
          >
          <span v-else-if="scope.row.coreSuggestion === 1"><span class="tagCol1">建议入住</span></span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <!-- <el-table-column
        label="推荐护理等级"
        align="center"
        prop="nursingLevelName"
        width="180"
      /> -->
      <el-table-column
        label="入住情况"
        align="center"
        prop="checkInStatus"
        min-width="120px"
        class-name="spanBlock"
      >
        <template #default="scope">
          <span v-if="scope.row.checkInStatus === 0"
            ><span class="tagBg2"></span>未入住</span
          >
          <span v-else><span class="tagBg1"></span>已入住</span>
        </template>
      </el-table-column>
      <el-table-column
        label="评估进度"
        align="center"
        prop="evaluationProgress"
        min-width="120px"
        class-name="spanBlock"
      >
        <template #default="scope">
          <span v-if="scope.row.evaluationProgress === 0"
            ><span class="tagBg3"></span>评估中</span
          >
          <span v-else-if="scope.row.evaluationProgress === 1"
            ><span class="tagBg1"></span>已完成</span
          >
          <span v-else><span class="tagBg2"></span>已取消</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        min-width="300px"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleEdit(scope.row)"
            class="delButton"
            :disabled="scope.row.evaluationProgress !== 0"
            >修改</el-button
          ><el-button
            link
            type="primary"
            icon="Delete"
            @click="handleDelete(scope.row)"
            :disabled="scope.row.evaluationProgress !== 2"
            >删除</el-button
          ><el-button
            link
            type="primary"
            icon="ZoomIn"
            @click="handleUpdate(scope.row)"
            >查看</el-button
          >
          <el-button
            link
            type="primary"
            icon="warning"
            @click="handleCancel(scope.row)"
            :disabled="scope.row.evaluationProgress !== 0"
            >取消</el-button
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

    <!-- 添加或修改健康评估对话框 -->
    <el-dialog
      title="上传体检报告"
      v-model="open"
      @close="cancel"
      width="600px"
      append-to-body
    >
      <div>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
          <el-form-item label="老人姓名：" prop="elderName">
            <el-input
              v-model="form.elderName"
              maxlength="10"
              show-word-limit
              placeholder="请输入老人姓名"
              @input="handleBlur"
            />
          </el-form-item>
          <el-form-item label="老人身份证号：" prop="idCard">
            <el-input
              v-model="form.idCard"
              maxlength="18"
              show-word-limit
              placeholder="请输入身份证号"
              @input="handleBlur"
            />
          </el-form-item>
          <el-form-item label="体检单位：" prop="physicalExamInstitution">
            <el-input
              v-model="form.physicalExamInstitution"
              maxlength="10"
              show-word-limit
              placeholder="请输入体检机构"
              @input="handleBlur"
            />
          </el-form-item>
          <el-form-item label="体检报告：" prop="physicalReportUrl">
            <el-upload
              ref="pdfUrl"
              v-model:file-list="pdfUrlfileList"
              :action="pdfUrlAction"
              :headers="headers"
              :data="{ idCardNo: form.idCard }"
              accept=".pdf"
              :limit="1"
              :auto-upload="true"
              :multiple="false"
              :on-success="handleSuccess"
              :on-remove="() => handleRemove()"
              :before-upload="pdfUrlBeforeUpload"
              :on-exceed="handleExceed"
              :disabled="isDisabled"
              :class="isDisabled ? 'disabled' : ''"
            >
              <el-button size="small" type="primary">点击上传</el-button>
              <template #tip>
                <span class="fontCol">请上传pdf文件，大小在10M以内</span>
              </template>
            </el-upload>
          </el-form-item>
          <div v-if="updateLoading" class="update">
            <div class="updateLoad">
              <div class="bigBg"></div>
              <div class="smallBg"></div>
              <div class="textInfo">
                <p class="text">AI</p>
                <p>分析中...</p>
              </div>
            </div>
          </div>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm" :disabled="!isClick"
            >确 定</el-button
          >
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- end -->
  </div>
</template>

<script setup name="HealthAssessment">
import { nextTick, onMounted } from 'vue';
import { genFileId } from 'element-plus';
// 获取vuex存储数据
import useUserStore from '@/store/modules/user';
import {
  addHealthAssessment,
  listHealthAssessment,
  delHealthAssessment,
  concelHealthAssessment,
} from '@/api/nursing/healthAssessment';
import { getToken } from '@/utils/auth';
import { isCardID } from '@/utils/validate';

const { proxy } = getCurrentInstance();
const { evaluation_progress } = proxy.useDict('evaluation_progress');
const route = useRoute();
const router = useRouter(); //获取全局
const store = useUserStore();
const healthAssessmentList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const isDisabled = ref(true);
// 上传请求路径
const pdfUrlAction = ref(
  import.meta.env.VITE_APP_BASE_API + '/nursing/healthAssessment/upload',
);
const headers = {
  Authorization: 'Bearer ' + getToken(),
};
const updateLoading = ref(false); //图片上传中
// 上传文件列表
const formRef = ref();
const pdfUrlfileList = ref([]);
const pdfUrl = ref(null);
const isClick = ref(false); //是否可以触发确定按钮
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderName: null,
    idCard: null,
    evaluationProgress: null,
  },
  rules: {
    elderName: [
      {
        required: true,
        message: '请输入老人姓名',
        trigger: 'blur',
      },
    ],
    idCard: [
      {
        required: true,
        message: '请输入老人身份证号',
        trigger: 'blur',
      },
      {
        validator: isCardID,
        message: '身份证格式错误，请重新输入',
        trigger: 'blur',
      },
    ],
    physicalExamInstitution: [
      {
        required: true,
        message: '请输入体检单位',
        trigger: 'blur',
      },
    ],
    physicalReportUrl: [
      { required: true, message: '请上传体检报告', trigger: 'change' },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

const handleBlur = () => {
  if (
    form.value.elderName &&
    form.value.idCard &&
    form.value.physicalExamInstitution
  ) {
    isDisabled.value = false;
  } else {
    isDisabled.value = true;
  }
};
/** 查询健康评估列表 */
function getList() {
  loading.value = true;
  listHealthAssessment(queryParams.value).then((response) => {
    healthAssessmentList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  updateLoading.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    elderName: null,
    idCard: null,
    physicalExamInstitution: null,
    physicalReportUrl: null,
  };
  pdfUrlfileList.value = [];
  proxy.resetForm('healthAssessmentRef');
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
  // reset();
  // open.value = true;
  // title.value = '添加健康评估';
  store.setStepActive(0);
  proxy.$router.push({
    path: '/enterQuit/detailsSteps',
  });
}

/** 修改按钮操作 */
function handleUpdate(row) {
  proxy.$router.push({
    path: '/enterQuit/healthDetails',
    query: { id: row.id },
  });
}
// 修改
const handleEdit = (row) => {
  router.push({
    path: '/enterQuit/detailsSteps',
    query: { id: row.id },
  });
  // proxy.$router.push({
  //   path: '/enterQuit/detailsSteps',
  //   query: { id: row.id },
  // });
};
// 删除按钮操作
function handleDelete(row) {
  proxy.$modal
    .confirm('此操作将删除健康评估，是否继续？', '确认删除')
    .then(function () {
      return delHealth(row.id);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
// 删除
const delHealth = async (id) => {
  await delHealthAssessment(id);
};
// 取消
const cancelHeal = async (id) => {
  await concelHealthAssessment(id);
};
// 取消按钮操作
function handleCancel(row) {
  proxy.$modal
    .confirm('此操作将取消健康评估，是否继续？', '确认取消')
    .then(function () {
      return cancelHeal(row.id);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('操作成功');
    })
    .catch(() => {});
}
// 上传之前的文件判断，判断文件大小文件类型等
const pdfUrlBeforeUpload = (file) => {
  // 校检文件大小
  const isLt = file.size / 1024 / 1024 < 60;
  if (!isLt) {
    proxy.$modal.msgError(`文件大小超过10m，请重新上传!`);
    return false;
  }
};
const handleExceed = (files) => {
  pdfUrl.value.clearFiles();
  const file = files[0];
  file.uid = genFileId();
  pdfUrl.value.handleStart(file);
};
//上传文件成功的处理
const handleSuccess = (res) => {
  console.log(res);
  if (res.code === 200) {
    isClick.value = true;
    form.value.physicalReportUrl = res.url;
    formRef.value.validateField('physicalReportUrl');
  } else {
    isClick.value = false;
  }
};

//删除文件的处理
const handleRemove = () => {
  form.value.physicalReportUrl = '';
  isClick.value = false;
  isDisabled.value = false;
};
/** 提交按钮 */
function submitForm() {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    updateLoading.value = true;
    const res = await addHealthAssessment(form.value);
    if (res.code === 200) {
      proxy.$modal.msgSuccess(`操作成功`);
      cancel();
      proxy.$router.push({
        path: '/enterQuit/healthDetails',
        query: { id: res.data },
      });
    }
  });
}
// 监听路由参数变化，当有refresh参数时重新获取数据
watch(
  () => route.query,
  (newQuery) => {
    console.log('检测到需要刷新列表', newQuery);
    if (newQuery._t) {
      getList();

      // 清除query参数，避免重复刷新
      // 注意：这里可能需要更优雅的处理方式，避免直接修改路由
    }
  },
  { immediate: true },
);
onMounted(() => {
  nextTick(() => {
    getList();
  });
});
</script>
<style lang="scss" scoped>
@import '@/assets/styles/variables.module.scss';
</style>
