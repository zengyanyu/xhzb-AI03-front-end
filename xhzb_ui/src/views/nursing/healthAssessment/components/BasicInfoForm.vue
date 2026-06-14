<template>
  <div>
    <el-form
      ref="basicFormRef"
      :model="localFormData"
      label-width="180px"
      :rules="rules"
    >
      <!-- 基本信息 -->
      <h3 class="title">基本信息</h3>
      <div class="formMain">
        <el-form-item label="老人姓名" prop="elderName">
          <el-input
            v-model="localFormData.elderName"
            placeholder="请输入"
            maxlength="10"
            clearable
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="身份证号" prop="idCard">
          <el-input
            v-model="localFormData.idCard"
            placeholder="请输入"
            maxlength="18"
            show-word-limit
            clearable
            @input="handleIdCardInput"
            @blur="handleIdCardChange"
          />
        </el-form-item>

        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker
            v-model="localFormData.birthDate"
            type="date"
            placeholder="出生日期"
            disabled
          />
        </el-form-item>

        <el-form-item label="年龄" prop="age">
          <el-input v-model="localFormData.age" placeholder="年龄" disabled />
        </el-form-item>

        <el-form-item label="性别" prop="gender" class="formRadioLabel">
          <el-radio-group v-model="localFormData.gender" disabled>
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="社保卡号" prop="socialSecurityCard">
          <el-input
            v-model="localFormData.socialSecurityCard"
            placeholder="请输入"
            maxlength="20"
            show-word-limit
            clearable
            @input="handleSecurityCardInput"
          />
        </el-form-item>
        <el-form-item label="民族" prop="nation" class="inputMar formRadioLabel">
          <el-radio-group
            v-model="localFormData.nation"
            @change="handleNationChange"
          >
            <el-radio label="汉族"></el-radio>
            <el-radio label="少数民族"> </el-radio>
          </el-radio-group>
          <el-input
            v-if="localFormData.nation&&localFormData.nation  === '少数民族'"
            v-model="localFormData.minorityEthnicity"
            placeholder="请输入"
            class="radioElinput"
            maxlength="20"
            clearable
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="文化程度" prop="educationLevel">
          <el-select
            v-model="localFormData.educationLevel"
            clearable
            placeholder="请选择文化程度"
          >
            <el-option
              v-for="item in degreeEducationData"
              :key="item.label"
              :label="item.label"
              :value="item.label"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="宗教信仰" prop="religiousBelief" class="inputMar formRadioLabel">
          <el-radio-group
            v-model="localFormData.religiousBelief"
            @change="handleReligiousBeliefChange"
          >
            <el-radio label="无"></el-radio>
            <el-radio label="有"> </el-radio>
          </el-radio-group>
          <el-input
            v-if="localFormData.religiousBelief&&localFormData.religiousBelief === '有'"
            v-model="localFormData.religionDetail"
            placeholder="请输入"
            class="radioElinput"
            clearable
            maxlength="20"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="婚姻状况" prop="maritalStatus">
          <el-select v-model="localFormData.maritalStatus" clearable placeholder="请选择">
            <el-option
              v-for="item in maritalStatusData"
              :key="item.label"
              :label="item.label"
              :value="item.label"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="居住情况" prop="livingSituation">
          <el-select
            v-model="localFormData.livingSituation"
            clearable
            placeholder="请选择"
          >
            <el-option
              v-for="item in liveTypeData"
              :key="item.label"
              :label="item.label"
              :value="item.label"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="医疗费用支付方式" prop="medicalPaymentMethod">
          <el-select
            v-model="localFormData.medicalPaymentMethod"
            placeholder="请选择"
            clearable
          >
            <el-option
              v-for="item in payTypeData"
              :key="item.label"
              :label="item.label"
              :value="item.label"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="经济来源" prop="economicSource">
          <el-select
            v-model="localFormData.economicSource"
            placeholder="请选择"
            clearable
          >
            <el-option
              v-for="item in sourceFinanceData"
              :key="item.label"
              :label="item.label"
              :value="item.label"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="老人联系方式" prop="elderContact">
          <el-input
            v-model="localFormData.elderContact"
            placeholder="请输入"
            maxlength="11"
            clearable
            show-word-limit
          />
        </el-form-item>
      </div>
      <!-- 信息提供者及联系人信息 -->
      <h3 class="title">信息提供者及联系人信息</h3>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="信息提供者姓名" prop="providerName">
            <el-input
              v-model="localFormData.providerName"
              placeholder="请输入"
              maxlength="10"
              clearable
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="信息提供者联系方式" prop="providerContact">
            <el-input
              v-model="localFormData.providerContact"
              placeholder="请输入"
              maxlength="11"
              clearable
              show-word-limit
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="信息提供者与老人关系"
            prop="relationshipWithElder"
          >
            <el-select
              v-model="localFormData.relationshipWithElder"
              placeholder="请选择"
              clearable
            >
              <el-option
                v-for="item in relationData"
                :key="item.label"
                :label="item.label"
                :value="item.label"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item
            label="知情同意书"
            prop="informedConsentUrl"
          >
            <FileUpload
              v-model="localFormData.informedConsentUrl"
              :elderName="localFormData.elderName"
              :fileName="localFormData.elderName!==undefined&&localFormData.elderName!==''?localFormData.elderName+'的知情同意书.'+ getFileType(localFormData.informedConsentUrl):'知情同意书.'+ getFileType(localFormData.informedConsentUrl)"
              :limit="1"
              :fileSize="10"
              :fileType="['pdf', 'png', 'jpg', 'jpeg']"
              :urlFileType="urlFileType"
              @change="handleFileChange"
            ></FileUpload>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, watch, computed, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import {
  degreeEducationData,
  maritalStatusData,
  payTypeData,
  liveTypeData,
  sourceFinanceData,
  relationData,
} from '@/utils/baseData';
import {getFileType} from '@/utils';
const props = defineProps({
  formData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  formRef: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits([
  'update:formData',
  'id-card-change',
  'nation-change',
  'file-change',
]);
const basicFormRef = ref(null);

// 基本信息表单校验规则
const rules = {
  elderName: [
    {
      required: true,
      message: '老人姓名为空，请输入老人姓名',
      trigger: 'blur',
    },
  ],
  idCard: [
    {
      required: true,
      message: '身份证号为空，请输入身份证号',
      trigger: 'blur',
    },
    {
      validator: (rule, value, callback) => {
        if (!value) return callback();
        const reg =
          /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\d|3[0-1])\d{3}(\d|[a-zA-Z])$/i;
        if (!reg.test(value)) {
          callback(new Error('身份证格式错误，请重新输入'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
  socialSecurityCard: [
    {
      required: true,
      message: '社保卡号为空，请输入社保卡号',
      trigger: 'blur',
    },
    {
    validator: (rule, value, callback) => {
      if (!value) return callback();
      
      // 只校验是否包含数字和英文字母
      const regex = /^[0-9a-zA-Z]+$/;
      if (!regex.test(value)) {
        callback(new Error('社保卡号格式错误，请重新输入'));
      } else {
        callback();
      }
    },
    trigger: 'blur',
  },
  ],
  nation: [
    {
      required: true,
      message: '民族为空，请选择民族',
      trigger: 'change',
    },
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('民族为空，请选择民族'));
          return;
        }

        // 如果选择少数民族，需要验证少数民族名称
        if (value === '少数民族') {
          if (
            !localFormData.minorityEthnicity ||
            !localFormData.minorityEthnicity.trim()
          ) {
            callback(new Error('民族名称为空，请输入民族名称'));
            return;
          }
        }

        callback();
      },
      trigger: 'change',
    },
  ],
  religiousBelief: [
    {
      required: true,
      message: '宗教信仰为空，请选择宗教信仰',
      trigger: 'change',
    },
    {
      validator: (rule, value, callback) => {
        // 如果选择了"有"，需要验证详细宗教信息
        if (value === '有') {
          if (
            !localFormData.religionDetail ||
            !localFormData.religionDetail.trim()
          ) {
            callback(new Error('宗教名称为空，请输入宗教名称'));
            return;
          }
        }
        callback();
      },
      trigger: 'change',
    },
  ],
  livingSituation: [
    {
      required: true,
      message: '居住情况为空，请选择居住情况',
      trigger: 'change',
    },
  ],
  economicSource: [
    {
      required: true,
      message: '经济来源为空，请选择经济来源',
      trigger: 'change',
    },
  ],
  educationLevel: [
    {
      required: true,
      message: '文化程度为空，请选择文化程度',
      trigger: 'change',
    },
  ],
  maritalStatus: [
    {
      required: true,
      message: '婚姻状况为空，请选择婚姻状况',
      trigger: 'change',
    },
  ],
  medicalPaymentMethod: [
    {
      required: true,
      message: '医疗费用支付方式为空，请选择医疗费用支付方式',
      trigger: 'change',
    },
  ],
  elderContact: [
    {
      required: true,
      message: '老人联系方式为空，请输入老人联系方式',
      trigger: 'blur',
    },
    {
      validator: (rule, value, callback) => {
        if (!value) return callback();
        const reg = /^1[3-9]\d{9}$/;
        if (!reg.test(value)) {
          callback(new Error('手机号格式错误，请重新输入'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
  providerName: [
    {
      required: true,
      message: '信息提供者姓名为空，请输入信息提供者姓名',
      trigger: 'blur',
    },
  ],
  providerContact: [
    {
      required: true,
      message: '信息提供者联系方式为空，请输入信息提供者联系方式',
      trigger: 'blur',
    },
    {
      validator: (rule, value, callback) => {
        if (!value) return callback();
        const reg = /^1[3-9]\d{9}$/;
        if (!reg.test(value)) {
          callback(new Error('手机号格式错误，请重新输入'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
  relationshipWithElder: [
    {
      required: true,
      message: '信息提供者与老人关系为空，请选择信息提供者与老人关系',
      trigger: 'change',
    },
  ],
  informedConsentUrl: [
    {
      required: true,
      message: '知情同意书为空，请上传知情同意书',
      trigger: 'change',
    },
  ],
};

// 使用本地响应式数据副本，使用props.formData初始化
const localFormData = reactive({
  elderName: props.formData.elderName,
  idCard: props.formData.idCard,
  birthDate: props.formData.birthDate,
  age: props.formData.age,
  gender: props.formData.gender,
  nation: props.formData.nation,
  minorityEthnicity: props.formData.minorityEthnicity,
  socialSecurityCard: props.formData.socialSecurityCard,
  religiousBelief: props.formData.religiousBelief,
  religionDetail: props.formData.religionDetail,
  educationLevel: props.formData.educationLevel,
  maritalStatus: props.formData.maritalStatus,
  livingSituation: props.formData.livingSituation,
  medicalPaymentMethod: props.formData.medicalPaymentMethod,
  economicSource: props.formData.economicSource,
  elderContact: props.formData.elderContact,
  providerName: props.formData.providerName,
  providerContact: props.formData.providerContact,
  relationshipWithElder: props.formData.relationshipWithElder,
  informedConsentUrl: props.formData.informedConsentUrl,
  // fileName:props.formData.elderName+'的知情同意书.'+ getFileType(props.formData.informedConsentUrl)
});
const urlFileType = ref('')

// 监听props.formData变化更新localFormData
watch(
  () => props.formData,
  (newFormData) => {
    Object.keys(localFormData).forEach((key) => {
      if (newFormData[key] !== undefined) {
        localFormData[key] = newFormData[key];
      }
    });
  },
  { deep: true },
);

// 监听localFormData变化更新父组件
watch(
  localFormData,
  (newVal) => {
    // 准备要提交的数据，排除额外的UI字段
    console.log('newVal', newVal);
    nextTick(() => {
      if (basicFormRef.value && newVal.informedConsentUrl) {
        basicFormRef.value.validateField('informedConsentUrl');
      }
    });
    const submitData = {
      elderName: newVal.elderName,
      idCard: newVal.idCard,
      birthDate: newVal.birthDate,
      age: newVal.age,
      gender: newVal.gender,
      nation: newVal.nation,
      socialSecurityCard: newVal.socialSecurityCard,
      religiousBelief: newVal.religiousBelief,
      educationLevel: newVal.educationLevel,
      maritalStatus: newVal.maritalStatus,
      livingSituation: newVal.livingSituation,
      medicalPaymentMethod: newVal.medicalPaymentMethod,
      economicSource: newVal.economicSource,
      elderContact: newVal.elderContact,
      providerName: newVal.providerName,
      providerContact: newVal.providerContact,
      relationshipWithElder: newVal.relationshipWithElder,
      informedConsentUrl: newVal.informedConsentUrl,
    };

    // 如果选择了少数民族，需要包含少数民族名称
    if (newVal.nation === '少数民族' && newVal.minorityEthnicity) {
      submitData.minorityEthnicity = newVal.minorityEthnicity;
    }

    // 如果选择了有宗教信仰，需要包含宗教详情
    if (newVal.religiousBelief === '有' && newVal.religionDetail) {
      submitData.religionDetail = newVal.religionDetail;
    }

    emit('update:formData', submitData);
  },
  { deep: true },
);

// 处理身份证号变化
const handleIdCardChange = () => {
  const idCard = localFormData.idCard;

  // 身份证校验
  if (!idCard || idCard.length !== 18) return;

  const reg =
    /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\d|3[0-1])\d{3}(\d|[a-zA-Z])$/i;
  if (!reg.test(idCard)) {
    ElMessage.warning('身份证格式错误');
    return;
  }

  // 提取出生日期字符串
  const birthStr = idCard.substring(6, 14);
  const year = birthStr.substring(0, 4);
  const month = birthStr.substring(4, 6);
  const day = birthStr.substring(6, 8);

  // 创建出生日期（注意月份从0开始，所以要减1）
  const birthDate = new Date(year, month - 1, day);

  // 计算年龄
  const today = new Date();
  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();

  // 如果今年生日还没过，年龄减1
  if (
    monthDiff < 0 ||
    (monthDiff === 0 && today.getDate() < birthDate.getDate())
  ) {
    age--;
  }

  // 计算性别（第17位奇数为男，偶数为女）
  const genderNum = parseInt(idCard.charAt(16));
  const gender = genderNum % 2 === 1 ? '男' : '女';

  // 格式化日期为 YYYY-MM-DD
  const formatDate = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;

  // 更新本地数据
  localFormData.birthDate = formatDate;
  localFormData.age = age.toString();
  localFormData.gender = gender;

  // 触发父组件事件
  emit('id-card-change', idCard);
};
// 处理身份证号输入，只允许数字和 X/x
const handleIdCardInput = (value) => {
  if (!value) return;
  
  // 移除所有非数字和非 X/x 的字符
  const filteredValue = value.replace(/[^0-9a-zA-Z]/g, '');
  
  // 如果过滤后的值与原值不同，更新本地数据
  if (filteredValue !== value) {
    localFormData.idCard = filteredValue;
  }
};
// 处理社保卡号输入，只允许数字和 X/x
const handleSecurityCardInput = (value) => {
  if (!value) return;
  
  // 移除所有非数字和非 X/x 的字符
  const filteredValue = value.replace(/[^0-9a-zA-Z]/g, '');
  
  // 如果过滤后的值与原值不同，更新本地数据
  if (filteredValue !== value) {
    localFormData.socialSecurityCard = filteredValue;
  }
};
// 校验社保卡号
const handleSecurityCardChange =()=>{
  triggerIdCardValidation();
}
// 处理民族选择变化
const handleNationChange = (value) => {
  // 如果选择汉族，清空少数民族名称
  if (value === '汉族') {
    localFormData.minorityEthnicity = '';
  }

  // 触发父组件事件
  emit('nation-change', value);

  // 触发校验
  nextTick(() => {
    if (basicFormRef.value) {
      basicFormRef.value.validateField('nation');
    }
  });
};

// 处理宗教选择变化
const handleReligiousBeliefChange = (value) => {
  // 如果选择不是"有"，清空宗教详情
  if (value !== '有') {
    localFormData.religionDetail = '';
  }

  // 触发校验
  nextTick(() => {
    if (basicFormRef.value) {
      basicFormRef.value.validateField('religiousBelief');
    }
  });
};
const handleClockImageChange = (file) => {
  // const rawFile = file.raw;
  // if (!rawFile) return;
  // // 限制文件大小（10MB）
  // if (rawFile.size > 10 * 1024 * 1024) {
  //   ElMessage.error('文件大小不能超过10MB');
  //   return;
  // }
  // // 显示预览图
  // localFormData.informedConsentUrl = URL.createObjectURL(rawFile);
  // 触发校验
  // triggerClockTestValidation();
};
// 触发画钟测验的验证
const triggerClockTestValidation = () => {
  if (basicFormRef.value) {
    basicFormRef.value.validateField('informedConsentUrl', (error) => {
      if (error) {
        console.log('画钟测验验证失败:', error);
      } else {
        console.log('画钟测验验证成功');
      }
    });
  }
};
// 处理文件上传
const handleFileChange = (file) => {
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过10M');
    return;
  }

  const reader = new FileReader();
  reader.onload = () => {
    localFormData.informedConsentUrl = reader.result;
    // 触发文件变化事件
    // emit('file-change', reader.result);

    // 触发校验
    nextTick(() => {
      if (basicFormRef.value) {
        basicFormRef.value.validateField('informedConsentUrl');
      }
    });
  };
  // reader.readAsDataURL(file.raw);
};

const handleExceed = () => {
  ElMessage.warning('最多只能上传1个文件');
};

// 计算文件列表
const fileList = computed(() => {
  return localFormData.informedConsentUrl
    ? [
        {
          name: '知情同意书',
          url: localFormData.informedConsentUrl,
        },
      ]
    : [];
});

const resetFields = () => {
  if (basicFormRef.value) {
    basicFormRef.value.resetFields();
  }
};

// 提供验证方法
const validate = () => {
  return new Promise((resolve, reject) => {
    if (!basicFormRef.value) {
      resolve(true);
      return;
    }

    basicFormRef.value.validate((valid) => {
      if (valid) {
        resolve(true);
      } else {
        reject(new Error('表单校验失败'));
      }
    });
  });
};

defineExpose({
  basicFormRef,
  localFormData,
  validate,
  resetFields,
});
</script>

<style scoped lang="scss">
h3 {
  margin-top: 20px;
  color: #333;
}

.formMain {
  display: flex;
  flex-wrap: wrap;
}
.formMain .el-form-item {
  flex: 1 1 calc(50% - 7.5px); /* 计算间距 */
  min-width: 250px; /* 防止过小 */
}
.inputMar {
  .el-input {
    margin-left: 5px;
  }
}
</style>
