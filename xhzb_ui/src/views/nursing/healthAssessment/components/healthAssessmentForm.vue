<template>
  <!-- 疾病诊断 -->
  <el-form
    ref="healthFormRef"
    :model="healthAssessment"
    label-width="180px"
    :rules="healthFormRules"
  >
    <h3 class="title">疾病诊断</h3>
    
    <div class="formMain">
      <!-- 疾病诊断 -->
      <el-form-item label="痴呆" prop="diseaseDiagnosis.dementia">
        <el-radio-group v-model="healthAssessment.diseaseDiagnosis.dementia">
          <el-radio label="无">无</el-radio>
          <el-radio label="轻度">轻度</el-radio>
          <el-radio label="中度">中度</el-radio>
          <el-radio label="重度">重度</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="精神疾病" prop="diseaseDiagnosis.mentalIllness">
        <el-radio-group
          v-model="healthAssessment.diseaseDiagnosis.mentalIllness"
        >
          <el-radio label="无">无</el-radio>
          <el-radio label="精神分裂症">精神分裂症</el-radio>
          <el-radio label="双相情感障碍">双相情感障碍</el-radio>
          <el-radio label="偏执性精神障碍">偏执性精神障碍</el-radio>
          <el-radio label="分裂情感障碍">分裂情感障碍</el-radio>
          <el-radio label="癫痫所致精神障碍">癫痫所致精神障碍</el-radio>
          <el-radio label="精神发育迟滞伴发精神障碍"
            >精神发育迟滞伴发精神障碍</el-radio
          >
        </el-radio-group>
      </el-form-item>

      <el-form-item label="慢性疾病" prop="diseaseDiagnosis.chronicDiseases">
        <el-input
          v-model="healthAssessment.diseaseDiagnosis.chronicDiseases"
          type="textarea"
          :autosize="{ minRows: 4, maxRows: 6 }"
          placeholder="请输入"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
    </div>

    <h3 class="title">近30天内意外事件</h3>
    <div class="formMain">
      <!-- 近30天内意外事件 -->
      <el-form-item label="跌倒" prop="recent30Days.fall">
        <el-radio-group v-model="healthAssessment.recent30Days.fall">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="走失" prop="recent30Days.lost">
        <el-radio-group v-model="healthAssessment.recent30Days.lost">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="噎食" prop="recent30Days.choking">
        <el-radio-group v-model="healthAssessment.recent30Days.choking">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="自杀" prop="recent30Days.suicideAttempt">
        <el-radio-group v-model="healthAssessment.recent30Days.suicideAttempt">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="昏迷" prop="recent30Days.coma">
        <el-radio-group v-model="healthAssessment.recent30Days.coma">
          <el-radio
            v-for="option in ptionsData"
            :key="option.value"
            :label="option.value"
          >
            {{ option.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </div>

    <h3 class="title">近30天体检报告</h3>
    <!-- 近30天体检报告 -->
    <div class="formMain">
      <el-form-item
        label="体检报告"
        required
        prop="recent30Days.medicalReport"
        
      >
        <FileUpload
          v-model="healthAssessment.recent30Days.medicalReport"
          :fileName="baseData.elderName+'的体检报告.'+ getFileType(healthAssessment.recent30Days.medicalReport)"
          :elderName="baseData.elderName"
          :limit="1"
          :fileSize="10"
          :fileType="['pdf']"

        ></FileUpload>
      </el-form-item>
    </div>
  </el-form>
</template>

<script setup>
import { reactive, watch, ref, nextTick, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import {getFileType} from '@/utils';
// 定义 props 接收父组件传递的数据
const props = defineProps({
  formData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  baseData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  medications: {
    type: Array,
    default: () => [],
  },
});

const healthFormRef = ref(null);
// 初始化数据结构
const initHealthAssessment = () => {
  return {
    elderName: props.baseData.elderName,
    diseaseDiagnosis: {
      dementia: props.formData.diseaseDiagnosis?.dementia || '',
      mentalIllness: props.formData.diseaseDiagnosis?.mentalIllness || '',
      chronicDiseases: props.formData.diseaseDiagnosis?.chronicDiseases || '',
    },
    recent30Days: {
      fall: props.formData.recent30Days?.fall,
      lost: props.formData.recent30Days?.lost,
      choking: props.formData.recent30Days?.choking,
      suicideAttempt: props.formData.recent30Days?.suicideAttempt,
      coma: props.formData.recent30Days?.coma,
      medicalReport: props.formData.recent30Days?.medicalReport || '',
      // medicalReportName:props.baseData.elderName+'的体检报告.'+getFileType(props.formData.recent30Days?.medicalReport)
    },
  };
};

// 健康评估表单校验规则
const healthFormRules = {
  'diseaseDiagnosis.dementia': [
    { required: true, message: '痴呆情况为空，请选择痴呆情况', trigger: 'change' },
  ],
  'diseaseDiagnosis.mentalIllness': [
    { required: true, message: '请选择精神疾病情况', trigger: 'change' },
  ],
  'diseaseDiagnosis.chronicDiseases': [
    { required: true, message: '慢性疾病为空，请输入慢性疾病', trigger: 'blur' },
  ],
  'recent30Days.fall': [
    { required: true, message: '请选择跌倒发生的次数', trigger: 'change' },
  ],
  'recent30Days.lost': [
    { required: true, message: '请选择走失发生的次数', trigger: 'change' },
  ],
  'recent30Days.choking': [
    { required: true, message: '请选择噎食发生的次数', trigger: 'change' },
  ],
  'recent30Days.suicideAttempt': [
    { required: true, message: '请选择自杀发生的次数', trigger: 'change' },
  ],
  'recent30Days.coma': [
    { required: true, message: '请选择昏迷发生的次数', trigger: 'change' },
  ],
  'recent30Days.medicalReport': [
    { required: true, message: '体检报告为空，请上传体检报告', trigger: 'change' },
  ],
};

// 用药数据
const medications = ref([
  {
    name: '',
    method: '',
    dose: '',
    frequency: '',
  },
]);

// 定义 emit 向父组件传递事件
const emit = defineEmits([
  'update:formData',
  'update:medications',
  'file-change',
]);

const ptionsData = [
  { label: '无', value: 0 },
  { label: '发生过1次', value: 1 },
  { label: '发生过2次', value: 2 },
  { label: '发生过3次以上', value: 3 },
];

// 使用本地响应式数据副本
const healthAssessment = reactive(initHealthAssessment());

// 用药数据
const medicationsData = reactive([
  {
    name: '',
    method: '',
    dose: '',
    frequency: '',
  },
]);

// 文件列表
const fileList = ref([]);

// 监听props变化，同步到本地数据
watch(
  () => props.formData,
  (newValue) => {
    console.log('父组件数据变化:', newValue);
    // 更新疾病诊断数据
    if (newValue.diseaseDiagnosis) {
      healthAssessment.diseaseDiagnosis = {
        ...healthAssessment.diseaseDiagnosis,
        ...newValue.diseaseDiagnosis,
      };
    }

    // 更新30天事件数据
    if (newValue.recent30Days) {
      healthAssessment.recent30Days = {
        ...healthAssessment.recent30Days,
        ...newValue.recent30Days,
      };
    }
  },
  { deep: true },
);

// 监听本地数据变化，更新到父组件
watch(
  healthAssessment,
  (newValue) => {
    nextTick(() => {
      console.log(healthFormRef.value && newValue.recent30Days.medicalReport); 
      if (healthFormRef.value && newValue.recent30Days.medicalReport) {
        healthFormRef.value.validateField('recent30Days.medicalReport');
      }
    });
    emit('update:formData', { ...newValue });
  },
  { deep: true },
);

// 添加用药
const addMedication = (index) => {
  medicationsData.splice(index + 1, 0, {
    name: '',
    method: '',
    dose: '',
    frequency: '',
  });
};

// 删除用药
const removeMedication = (index) => {
  medicationsData.splice(index, 1);
};

// 处理文件变化
const handleFileChange = (file) => {
  if (file.raw && file.raw.type === 'application/pdf') {
    healthAssessment.recent30Days.medicalReport = file.raw;
    fileList.value = [file];
    nextTick(() => {
      if (healthFormRef.value) {
        healthFormRef.value.validateField(
          'recent30Days.medicalReport',
          (error) => {
            console.log('medicalReport 验证结果:', error ? '失败' : '成功');
          },
        );
      }
    });
  } else {
    ElMessage.error('请上传PDF文件');
  }
};

// 文件上传前校验
const beforeUpload = (file) => {
  const isPdf = file.type === 'application/pdf';
  const isLt60M = file.size / 1024 / 1024 < 10;

  if (!isPdf) {
    ElMessage.error('上传文件必须是PDF格式');
    return false;
  }

  if (!isLt60M) {
    ElMessage.error('上传文件大小不能超过10MB');
    return false;
  }

  return true;
};

// 处理文件删除
const handleRemove = () => {
  healthAssessment.recent30Days.medicalReport = null;
  fileList.value = [];
};

// 表单验证方法
const validate = () => {
  return new Promise((resolve, reject) => {
    if (!healthFormRef.value) {
      resolve(true);
      return;
    }

    healthFormRef.value.validate((valid) => {
      if (valid) {
        resolve(true);
      } else {
        reject(new Error('表单校验失败'));
      }
    });
  });
};

// 重置表单
const resetFields = () => {
  if (healthFormRef.value) {
    healthFormRef.value.resetFields();
    Object.assign(healthAssessment, initHealthAssessment());
  }
};

// 页面加载时初始化
onMounted(() => {
  console.log('healthAssessment 组件挂载，初始数据:', healthAssessment);
});

defineExpose({
  healthFormRef,
  healthAssessment,
  validate,
  resetFields,
});
</script>

<style scoped>
.title {
  margin: 20px 0 10px 0;
  color: #333;
  font-size: 16px;
  font-weight: bold;
}

.formMain {
  padding: 0 20px;
}

.basicinUpload {
  margin-top: 10px;
}

:deep(.el-radio-group) {
  display: flex;
  flex-wrap: wrap;
}

:deep(.el-radio) {
  margin-right: 20px;
  margin-bottom: 10px;
}

:deep(.el-textarea) {
  width: 100%;
}
</style>
