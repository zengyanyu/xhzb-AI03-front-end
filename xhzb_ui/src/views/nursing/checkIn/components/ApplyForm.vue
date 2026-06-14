<template>
  <div class="info">
    <h3>基本信息</h3>
    <!-- 添加el-form包裹，参照TransactForm.vue -->
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="130px">
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="老人姓名" prop="name">
            <el-input
              v-model="formData.name"
              type="text"
              @focus="selectFlavor"
              @blur="outSelect"
              placeholder="请选择"
              
            >
            <!-- clearable
              @clear="handleNameClear" -->
              <template #suffix
                ><el-icon>
                  <ArrowDown v-if="!mak" />
                  <ArrowUp v-else /> </el-icon
              ></template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号">
            <el-input
              v-model="formData.idCardNo"
              type="text"
              placeholder="身份证号"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="出生日期">
            <el-date-picker
              v-model="formData.birthday"
              :readonly="type == 'read'"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              placeholder="出生日期"
              disabled
              clearable
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄">
            <el-input
              v-model="formData.age"
              type="text"
              :readonly="type == 'read'"
              placeholder="年龄"
              disabled
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="性别" class="formInInfoRadioLabel">
            <el-radio-group
              v-model="formData.sex"
              :readonly="type == 'read'"
              size="default"
              disabled
            >
              <el-radio
                v-for="(item, index) in sexOptions"
                :key="index"
                :label="item.value"
                :disabled="item.disabled"
                >{{ item.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="社保卡号">
            <el-input
              v-model="formData.socialSecurityCard"
              type="text"
              :readonly="type == 'read'"
              placeholder="社保卡号"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="民族" class="formInInfoRadioLabel">
            <el-radio-group
              v-model="formData.nation"
              :readonly="type === 'read'"
              disabled
            >
              <el-radio label="汉族">汉族</el-radio>
              <el-radio label="少数民族">少数民族</el-radio>
            </el-radio-group>
            <el-input
              v-if="formData.nation !== '汉族'"
              v-model="formData.nationDetail"
              placeholder=""
              disabled
              class="radioElinput"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文化程度">
            <el-input
              v-model="formData.educationLevel"
              type="text"
              :readonly="type === 'read'"
              placeholder="文化程度"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="宗教信仰" class="formInInfoRadioLabel">
            <el-radio-group
              v-model="formData.religiousBelief"
              :readonly="type === 'read'"
              disabled
            >
              <el-radio label="无">无</el-radio>
              <el-radio label="有">有</el-radio>
            </el-radio-group>
            <el-input
              v-if="formData.religiousBelief !== '无'"
              v-model="formData.religiousBeliefDetail"
              placeholder=""
              class="radioElinput"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="婚姻状况">
            <el-input
              v-model="formData.maritalStatus"
              type="text"
              :readonly="type === 'read'"
              placeholder="婚姻状况"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="居住情况">
            <el-input
              v-model="formData.livingSituation"
              type="text"
              :readonly="type === 'read'"
              placeholder="居住情况"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="医疗费用支付方式">
            <el-input
              v-model="formData.medicalPaymentMethod"
              type="text"
              :readonly="type === 'read'"
              placeholder="医疗费用支付方式"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="经济来源">
            <el-input
              v-model="formData.economicSource"
              type="text"
              :readonly="type === 'read'"
              placeholder="经济来源"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="老人联系方式">
            <el-input
              v-model="formData.phone"
              type="text"
              :readonly="type === 'read'"
              placeholder="老人联系方式"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row gutter="15">
        <el-col :span="12">
          <!-- <el-form-item label="评估报告">
            <el-input
              v-model="formData.assessmentReport"
              type="textarea"
              :readonly="type === 'read'"
              placeholder="请输入评估报告"
              :autosize="{ minRows: 3, maxRows: 5 }"
              :style="{ width: '100%' }"
              disabled
            ></el-input>
          </el-form-item> -->
          <el-form-item label="核心建议">
            <!-- <span v-if="formData.coreSuggestion === 0"
              ><span class="tagCol2">不建议入住</span></span
            >
            <span v-else><span class="tagCol1">建议入住</span></span> -->
            <el-input
              v-model="formData.coreSuggestion"
              type="text"
              :readonly="type === 'read'"
              placeholder="核心建议"
              clearable
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="家庭住址" prop="address">
            <el-input
              v-model="formData.address"
              type="textarea"
              :readonly="type == 'read'"
              placeholder="请输入"
              maxlength="100"
              show-word-limit
              :autosize="{ minRows: 4, maxRows: 4 }"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="一寸照片" prop="image">
            <span v-if="type === 'read'" class="read-only-text">
              <!-- 点击可放大图片 -->
              <el-image
                v-if="type == 'read'"
                style="width: 100px; height: 100px"
                :src="formData.image"
                :preview-src-list="[formData.image]"
              ></el-image>
            </span>
            <image-upload
              v-else
              v-model="formData.image"
              :limit="1"
              :fileSize="2"
              :fileType="['png', 'jpg', 'jpeg']"
              @change="handleClockImageChange"
              :disabled="type === 'read'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证人像面" prop="idCardPortraitImg">
            <span v-if="type === 'read'" class="read-only-text">
              <!-- 点击可放大图片 -->
              <el-image
                style="width: 100px; height: 100px"
                :src="formData.image"
                :preview-src-list="[formData.image]"
              ></el-image>
            </span>
            <image-upload
              v-else
              v-model="formData.idCardPortraitImg"
              :limit="1"
              :fileSize="2"
              :fileType="['png', 'jpg', 'jpeg']"
              @change="handleIdCardPortraitImageChange"
              :disabled="type === 'read'"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="身份证国徽面" prop="idCardNationalEmblemImg">
            <!-- 点击可放大图片 -->
            <el-image
              v-if="type == 'read'"
              style="width: 100px; height: 100px"
              :src="formData.idCardNationalEmblemImg"
              :preview-src-list="[formData.idCardNationalEmblemImg]"
            ></el-image>
            <image-upload
              v-else
              v-model="formData.idCardNationalEmblemImg"
              :limit="1"
              :fileSize="2"
              :fileType="['png', 'jpg', 'jpeg']"
              @change="handleIdCardNationalEmblemImageChange"
              :disabled="type === 'read'"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- 老人选择弹层 -->
    <OldManSelect
      :dialogVisible="dialogVisible"
      :dialog-data="listOldManData"
      :pagination="pagination"
      :formData="formData"
      :dataLoading="dataLoading"
      @handle-search="handleSearch"
      @handle-reset="handleReset"
      @handle-close-dialog="handleCloseDialog"
      @get-current="getCurrent"
      @handle-select-old="handleSelectOld"
      @getOldList="getOldList"
    ></OldManSelect>
    <!-- end -->
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus';
import { getToken } from '@/utils/auth';
import { healthPageQuery } from '@/api/nursing/checkIn';
import { Plus } from '@element-plus/icons-vue';
import { getOldCheckIn } from '@/api/nursing/checkIn';
// 选择老人列表弹层
import OldManSelect from './OldManSelect.vue';

const formRef = ref();
const headers = {
  Authorization: 'Bearer ' + getToken(),
};

// 定义校验规则 - 参照
const rules = ref({
  name: [
    {
      required: true,
      message: '老人姓名为空，请选择老人',
      trigger: 'change',
    },
  ],
  idCardNo: [
    {
      required: true,
      message: '老人姓名为空，请输入身份证号',
      trigger: 'blur',
    },
  ],
  address: [
    {
      required: true,
      message: '家庭住址为空，请输入家庭住址',
      trigger: 'blur',
    },
  ],
  image: [
    {
      required: true,
      message: '一寸照片为空，请上传一寸照片',
      trigger: 'change',
    },
  ],
  idCardPortraitImg: [
    {
      required: true,
      message: '身份证人像面为空，请上传身份证人像面',
      trigger: 'change',
    },
  ],
  idCardNationalEmblemImg: [
    {
      required: true,
      message: '身份证国徽面为空，请上传身份证国徽面',
      trigger: 'change',
    },
  ],
});

const data = reactive({
  formData: {
    name: '',
    idCardNo: undefined,
    birthday: null,
    age: undefined,
    sex: undefined,
    phone: undefined,
    address: undefined,
    image: null,
    idCardPortraitImg: null,
    idCardNationalEmblemImg: null,
    socialSecurityCard: undefined,
    nation: '',
    nationDetail: undefined,
    educationLevel: undefined,
    religiousBelief: '',
    religionDetail: undefined,
    maritalStatus: undefined,
    livingSituation: undefined,
    medicalPaymentMethod: undefined,
    economicSource: undefined,
    assessmentReport: undefined,
    coreRecommendation: undefined,
  },
});
const { formData } = toRefs(data);

const sexOptions = ref([
  {
    label: '男',
    value: 0,
  },
  {
    label: '女',
    value: 1,
  },
]);

const imageAction = ref(import.meta.env.VITE_APP_BASE_API + '/common/upload');
// 上传文件列表
const imagefileList = ref([]);
// 上传请求路径
const idCardPortraitImgAction = ref(
  import.meta.env.VITE_APP_BASE_API + '/common/upload',
);
// 上传文件列表
const idCardPortraitImgfileList = ref([]);
// 上传请求路径
const idCardNationalEmblemImgAction = ref(
  import.meta.env.VITE_APP_BASE_API + '/common/upload',
);
// 上传文件列表
const idCardNationalEmblemImgfileList = ref([]);
const mak = ref(false); // 控制input上下箭头
const readonly = ref(false); // 是否只读
const dialogVisible = ref(false); // 老人选择弹窗
const listOldManData = ref([]); // 获取所有老人
const pagination = ref({
  pageSize: 5,
  pageNum: 1, // 默认当前页
  admissionStatus: 1,
  evaluationProgress: 1,
  checkInStatus: 0,
});

//接收父组件的数据
const props = defineProps({
  applyFormInfo: {
    type: Object,
    default: () => {
      return {};
    },
  },
  type: {
    type: String,
    default: () => {
      return {};
    },
  },
});

// 参照TransactForm.vue的watch写法
watch(
  props,
  () => {
    if (props.applyFormInfo && Object.keys(props.applyFormInfo).length > 0) {
      formData.value = { ...formData.value, ...props.applyFormInfo };
    }
  },
  { immediate: true, deep: true },
);
const dataLoading = ref(false);
// 获取老人列表数据
const getOldList = async () => {
  dataLoading.value = true
  const res = await healthPageQuery(pagination.value); // 获取列表数据
  
  if (res.code === 200) {
    listOldManData.value = res.rows;
    pagination.value.total = res.total;
    dataLoading.value = false
  }
};

// 搜索功能
const handleSearch = (val) => {
  pagination.value = {
    ...val,
    pageSize: pagination.value.pageSize,
    admissionStatus: 1,
    pageNum: 1,
    evaluationProgress: 1,
    checkInStatus: 0,
  };
  getOldList();
};
// 重置，清空搜索框
const handleReset = () => {
  // 重置页码
  pagination.value = {
    pageSize: 5,
    pageNum: 1,
    admissionStatus: 1,
    evaluationProgress: 1,
    checkInStatus: 0,
  };
  getOldList();
};
// 关闭普通列表弹层
const handleCloseDialog = () => {
  dialogVisible.value = false; // 直接修改弹层显示状态
  pagination.value = {
    pageSize: 5,
    pageNum: 1,
    admissionStatus: 1,
    evaluationProgress: 1,
    checkInStatus: 0,
  };
  // 重置状态
  mak.value = false;
  readonly.value = false;
  isClearing.value = false;
};
// 翻页设置当前页
const getCurrent = (val) => {
  pagination.value.pageNum = val.current;
  pagination.value.pageSize = val.pageSize;
  getOldList();
};
// 提交普通列表弹层
const handleSelectOld = async (val) => {
  const res = await getOldCheckIn(val.id?val.id:val.healthAssessmentId);
  if (res.code === 200) {
    Object.assign(formData.value, res.data);
    // 根据身份证号获取出生日期和年龄性别
    const idCardNo = val.idCard;
    const birthDate = idCardNo.substring(6, 14);
    formData.value.healthAssessmentId = val.id;
    formData.value.birthday = `${birthDate.substring(0, 4)}-${birthDate.substring(4, 6)}-${birthDate.substring(6, 8)}`;
    formData.value.age =
      new Date().getFullYear() - parseInt(birthDate.substring(0, 4));
    formData.value.sex = parseInt(idCardNo[16]) % 2 === 1 ? 0 : 1;
    if (formData.value.nation && formData.value.nation !== '汉族') {
      formData.value.nationDetail = formData.value.nation;
      formData.value.nation = '少数民族';
    }
    if (
      formData.value.religiousBelief &&
      formData.value.religiousBelief !== '无'
    ) {
      formData.value.religiousBeliefDetail = formData.value.religiousBelief;
      formData.value.religiousBelief = '有';
    }
    if (
      res.data.coreSuggestion !== null &&
      res.data.coreSuggestion !== undefined
    ) {
      if (res.data.coreSuggestion === 0) {
        formData.value.coreSuggestion = '不建议入住';
      } else {
        formData.value.coreSuggestion = '建议入住';
      }
    }
  }
  console.log('选择的老人信息', formData.value, val.id);
  emit('getFormData', formData.value);
};
watch(
  formData.value,
  (newValue) => {
    nextTick(() => {
      if (formRef.value && newValue.image) {
        formRef.value.validateField('image');
      }
      if (formRef.value && newValue.idCardPortraitImg) {
        formRef.value.validateField('idCardPortraitImg');
      }
      if (formRef.value && newValue.idCardNationalEmblemImg) {
        formRef.value.validateField('idCardNationalEmblemImg');
      }
    });
    emit('update:formData', { ...newValue });
  },
  { deep: true },
);
// 处理画钟图片上传变化
const handleClockImageChange = (file) => {
  console.log('handleClockImageChange', file);
  const rawFile = file.raw;
  if (!rawFile) return;

  // 限制文件大小（10MB）
  if (rawFile.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过10MB');
    return;
  }

  // 显示预览图
  formData.value.image = URL.createObjectURL(rawFile);
  console.log(formData.value.image);
};

const handleIdCardPortraitImageChange = (file) => {
  const rawFile = file.raw;
  if (!rawFile) return;

  // 限制文件大小（10MB）
  if (rawFile.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过10MB');
    return;
  }

  // 显示预览图
  formData.value.idCardPortraitImg = URL.createObjectURL(rawFile);
};

const handleIdCardNationalEmblemImageChange = (file) => {
  const rawFile = file.raw;
  if (!rawFile) return;

  // 限制文件大小（10MB）
  if (rawFile.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过10MB');
    return;
  }

  // 显示预览图
  formData.value.idCardNationalEmblemImg = URL.createObjectURL(rawFile);
};

/**
 * @name: 上传之前的文件判断
 * @description: 上传之前的文件判断，判断文件大小文件类型等
 * @param {*} file
 * @return {*}
 */
function imageBeforeUpload(file) {
  let isRightSize = file.size / 1024 / 1024 < 2;
  if (!isRightSize) {
    ElMessage.error('文件大小超过 2MB');
  }
  return isRightSize;
}
/**
 * @name: 上传之前的文件判断
 * @description: 上传之前的文件判断，判断文件大小文件类型等
 * @param {*} file
 * @return {*}
 */
function idCardPortraitImgBeforeUpload(file) {
  let isRightSize = file.size / 1024 / 1024 < 2;
  if (!isRightSize) {
    ElMessage.error('文件大小超过 2MB');
  }
  return isRightSize;
}
/**
 * @name: 上传之前的文件判断
 * @description: 上传之前的文件判断，判断文件大小文件类型等
 * @param {*} file
 * @return {*}
 */
function idCardNationalEmblemImgBeforeUpload(file) {
  let isRightSize = file.size / 1024 / 1024 < 2;
  if (!isRightSize) {
    ElMessage.error('文件大小超过 2MB');
  }
  return isRightSize;
}
// 统一的文件上传前验证
const beforeUpload = (file) => {
  // 验证文件类型
  const validTypes = ['image/png', 'image/jpg', 'image/jpeg'];
  const isImage = validTypes.includes(file.type);
  if (!isImage) {
    ElMessage.error('仅支持上传PNG、JPG、JPEG格式的图片');
    return false;
  }

  // 验证文件大小（不超过2M）
  const isLessThan2M = file.size / 1024 / 1024 < 2;
  if (!isLessThan2M) {
    ElMessage.error('图片大小不能超过2M');
    return false;
  }

  return true;
};

// 处理超出上传数量限制
const handleExceed = () => {
  ElMessage.warning('只能上传一张图片，如需更换请先删除原图片');
};

// 上传成功处理
const handleAvatarSuccess = (res, key) => {
  if (res.code === 200) {
    formData.value[key] = res.url;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error(res.msg || '上传失败');
  }
};

// 移除文件处理
const handleRemove = (key) => {
  formData.value[key] = '';
  // 清空对应的fileList
  switch (key) {
    case 'image':
      imagefileList.value = [];
      break;
    case 'idCardPortraitImg':
      idCardPortraitImgfileList.value = [];
      break;
    case 'idCardNationalEmblemImg':
      idCardNationalEmblemImgfileList.value = [];
      break;
  }
};
const isClearing = ref(false);
// 选择老人获取相关联的信息
const selectFlavor = () => {
  // 强制重置isClearing，避免残留状态影响
  isClearing.value = false;
  mak.value = true;
  readonly.value = true;
  dialogVisible.value = true;
  getOldList();
};

const outSelect = () => {
  // 缩短延时时间，减少状态残留
  setTimeout(() => {
    readonly.value = false;
    // 仅在非弹层打开状态下重置isClearing
    if (!dialogVisible.value) {
      isClearing.value = false;
    }
  }, 50); // 从200ms缩短到50ms
};

// 参照TransactForm.vue，添加validate方法
const validate = async () => {
  try {
    await formRef.value.validate();
    return true;
  } catch (error) {
    return false;
  }
};

// 参照TransactForm.vue，添加getFormData方法
const getFormData = () => {
  return formData.value;
};
const handleNameClear = () => {
  isClearing.value = true;
  formData.value = {};
  setTimeout(() => {
    isClearing.value = false;
  }, 50);
};
//声明父组件的方法
const emit = defineEmits(['getFormData']);

//监听数据，如果发生变化，则发送数据给父组件 - 参照TransactForm.vue
watch(
  formData.value,
  (val) => {
    emit('getFormData', val);
  },
  { deep: true, immediate: true },
);

// 参照TransactForm.vue，暴露validate和getFormData方法
defineExpose({
  validate,
  getFormData,
});
</script>

<style scoped lang="scss">
.info {
  .avatar-uploader {
    display: inline-block;
  }

  :deep(.avatar-uploader .el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    width: 100px !important;
    height: 100px !important;
    display: block;
  }

  :deep(.avatar-uploader .el-upload:hover) {
    border-color: var(--el-color-primary);
  }

  :deep(.el-icon.avatar-uploader-icon) {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .avatar {
    width: 100px;
    height: 100px;
    display: block;
    object-fit: cover;
  }

  .el-upload__tip {
    margin-top: 8px;
    font-size: 12px;
    color: #909399;
    line-height: 1.4;
    /* width: 100px; */
  }

  .upload-placeholder {
    width: 100px;
    height: 100px;
    border: 1px dashed #dcdfe6;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #909399;
    font-size: 14px;
    background-color: #f5f7fa;
  }
}
</style>
<style src="../index.scss"></style>
