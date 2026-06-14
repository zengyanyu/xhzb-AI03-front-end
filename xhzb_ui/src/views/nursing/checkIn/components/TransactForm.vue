<template>
  <div class="info">
    <h3>
      签约办理
      <el-tooltip
        :popper-style="{ maxWidth: 300 + 'px' }"
        class="item"
        effect="dark"
        content="入住成功后，系统将会生成首期账单。生成账单后，入住配置和签约办理信息将无法修改，若修改信息，需办理退住流程。"
        placement="top"
      >
        <el-icon><WarningFilled /></el-icon>
      </el-tooltip>
    </h3>
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractName">
            <el-input
              v-model="formData.contractName"
              maxlength="20"
              show-word-limit
              :readonly="type === 'read'"
              type="text"
              placeholder="请输入"
              clearable
              :disabled="type === 'read'"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="签约日期" prop="signDate">
            <el-date-picker
              type="datetime"
              :readonly="type === 'read'"
              v-model="formData.signDate"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择"
              clearable
              :disabled="type === 'read'"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="丙方姓名">
            <el-input
              v-model="formData.thirdPartyName"
              :readonly="type === 'read'"
              type="text"
              placeholder="请输入"
              clearable
              maxlength="10"
              show-word-limit
              :disabled="type === 'read'"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="丙方联系方式" prop="thirdPartyPhone">
            <el-input
              v-model="formData.thirdPartyPhone"
              :readonly="type === 'read'"
              type="text"
              placeholder="请输入"
              clearable
              show-word-limit
              :disabled="type === 'read'"
              maxlength="11"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="入住合同" prop="agreementPath">
            <div v-if="type === 'read' && formData.agreementPath">
              {{ formData.name || '老人' }}的入住合同.pdf
              <a
                :href="formData.agreementPath"
                class="searchPdf"
                target="_blank"
                >查看</a
              >
            </div>
            <div v-else-if="type === 'read' && !formData.agreementPath">
              暂无合同
            </div>
            <!-- <el-upload
              v-else
              ref="agreementPath"
              :file-list="agreementPathfileList"
              :action="agreementPathAction"
              :before-upload="agreementPathBeforeUpload"
              accept=".pdf"
              :headers="headers"
              :on-success="(res) => handleSuccess(res)"
              :on-remove="handleRemove"
              :limit="1"
              :on-exceed="handleExceed"
            >
              <el-button size="small" type="primary">点击上传</el-button>
              <template #tip>
                <div class="el-upload__tip">请上传pdf文件，大小在10M以内</div>
              </template>
            </el-upload> -->
            <FileUpload
              v-model="formData.agreementPath"
              :elderName="formData.name?formData.name+'的入住合同':'入住合同'"
              :limit="1"
              :fileSize="10"
              :fileType="['pdf']"
              @change="handleFileChange"
            ></FileUpload>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus';
import { getToken } from '@/utils/auth';

const formRef = ref();

const headers = {
  Authorization: 'Bearer ' + getToken(),
};

// 定义校验规则
const rules = ref({
  contractName: [
    {
      required: true,
      message: '合同名称为空，请输入合同名称',
      trigger: 'blur',
    },
  ],
  signDate: [
    {
      required: true,
      message: '签约日期为空，请选择签约日期',
      trigger: 'change',
    },
  ],
  agreementPath: [
    {
      required: true,
      message: '入住合同为空，请上传入住合同',
      trigger: 'change',
    },
  ],
  // 丙方联系方式校验（非必填，但输入时必须符合手机号格式）
  thirdPartyPhone: [
    {
      validator: (rule, value, callback) => {
        // 空值不校验（非必填）
        if (!value) {
          callback();
          return;
        }
        // 校验手机号格式
        const phoneReg = /^1[3-9]\d{9}$/;
        if (phoneReg.test(value)) {
          callback();
        } else {
          callback(new Error('丙方联系方式错误，请重新输入'));
        }
      },
      trigger: 'blur',
    },
  ],
});
// 格式化时间为 YYYY-MM-DD HH:mm:ss
const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};
const data = reactive({
  formData: {
    contractName: undefined,
    signDate: formatDate(new Date()),
    thirdPartyName: undefined,
    thirdPartyPhone: undefined,
    agreementPath: null,
    elderName: undefined,
  },
});
const { formData } = toRefs(data);
watch(
  data,
  (newValue) => {
    nextTick(() => {
      if (formRef.value && newValue.formData.agreementPath) {
        formRef.value.validateField('agreementPath');
      }
    });
  },
  { deep: true },
);
// 上传请求路径
const agreementPathAction = ref(
  import.meta.env.VITE_APP_BASE_API + '/common/upload',
);
// 上传文件列表
const agreementPathfileList = ref([]);

/**
 * @name: 上传之前的文件判断
 * @description: 上传之前的文件判断，判断文件大小文件类型等
 * @param {*} file
 * @return {*}
 */
function agreementPathBeforeUpload(file) {
  let isRightSize = file.size / 1024 / 1024 < 10;
  if (!isRightSize) {
    ElMessage.error('上传文件大小不能超过 10MB!');
    return false;
  }

  // 验证文件类型
  const isPDF = file.type === 'application/pdf';
  if (!isPDF) {
    ElMessage.error('只能上传PDF格式的文件');
    return false;
  }

  return isRightSize;
}

// 处理超出上传数量限制
const handleExceed = () => {
  ElMessage.warning('只能上传一个文件，如需更换请先删除原文件');
};

const props = defineProps({
  transactFormInfo: {
    type: Object,
    default: () => ({}),
  },
  type: {
    type: String,
    default: '',
  },
});

watch(
  props,
  () => {
    if (
      props.transactFormInfo &&
      Object.keys(props.transactFormInfo).length > 0
    ) {
      formData.value = { ...formData.value, ...props.transactFormInfo };

      // 如果有合同文件，添加到文件列表
      if (props.transactFormInfo.agreementPath) {
        agreementPathfileList.value = [
          {
            name: '入住合同.pdf',
            url: props.transactFormInfo.agreementPath,
          },
        ];
      }
    }
  },
  { immediate: true, deep: true },
);

//上传成功之后给属性pdfUrl赋值
const handleSuccess = (val) => {
  if (val.code === 200) {
    formData.value.agreementPath = val.url;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error(val.msg || '上传失败');
  }
};

//删除文件给属性pdfUrl赋值为空
const handleRemove = () => {
  formData.value.agreementPath = '';
  agreementPathfileList.value = [];
};
// 提供校验方法
const validate = async () => {
  // 验证手机号格式（必须是11位纯数字，且符合手机号规则）

  try {
    await formRef.value.validate();

    return true;
  } catch (error) {
    return false;
  }
};

// 获取表单数据
const getFormData = () => {
  return formData.value;
};
// 处理文件上传
const handleFileChange = (file) => {
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过10M');
    return;
  }

  const reader = new FileReader();
  reader.onload = () => {
    formData.value.agreementPath = reader.result;
    // 触发文件变化事件
    // emit('file-change', reader.result);

    // 触发校验
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('agreementPath');
      }
    });
  };
  // reader.readAsDataURL(file.raw);
};
//定义emit函数，用于向父组件发送自定义事件
const emit = defineEmits(['getFormData']);
// 监听formData的变化，并在变化时触发getFormData自定义事件，将新值传递给父组件
watch(
  formData.value,
  (val) => {
    emit('getFormData', val);
  },
  { deep: true, immediate: true },
);

// 将方法和数据暴露给父组件使用
defineExpose({
  validate,
  getFormData,
});
</script>
<style scoped>
.el-upload__tip {
  line-height: 1.2;
}
:deep(.el-upload) {
  /* width:100%; */
  height: 100%;
}
.searchPdf {
  margin-left: 10px;
  color: #409eff;
  text-decoration: none;
}
.searchPdf:hover {
  text-decoration: underline;
}
</style>
