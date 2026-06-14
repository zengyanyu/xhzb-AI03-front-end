<template>
  <div class="info">
    <h3>
      入住配置<el-tooltip
        class="item"
        effect="dark"
        content="该费用为月账单，每月1号收当月费用"
        placement="top"
      >
        <el-icon><WarningFilled /></el-icon>
      </el-tooltip>
    </h3>
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="入住期限" prop="checkInTime">
            <el-date-picker
              type="datetimerange"
              :readonly="type === 'read'"
              v-model="checkInTime"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              start-placeholder="请选择开始日期"
              end-placeholder="请选择结束日期"
              range-separator="-"
              clearable
              @change="hendleCheckInTime"
              :disabled-date="disabledDate"
              class="datePicker"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="费用期限" prop="feeTime">
            <el-date-picker
              type="datetimerange"
              :readonly="type === 'read'"
              v-model="feeTime"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              start-placeholder="请选择开始日期"
              end-placeholder="请选择结束日期"
              range-separator="-"
              clearable
              @change="hendleFeeTime"
              :disabled-date="disabledDate"
              class="datePicker"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="护理等级" prop="nursingLevelId">
            <el-select
              v-model="formData.nursingLevelId"
              placeholder="请选择"
              clearable
              @change="handleNursing"
            >
              <el-option
                v-for="(item, index) in nursingLevelIdOptions"
                :key="index"
                :label="item.name"
                :value="item.id"
                :disabled="item.disabled"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入住床位" prop="bed">
            <div v-if="type === 'read'">
              {{ formData.bedNumber }}
            </div>
            <el-cascader
              v-else
              v-model="formData.bed"
              :options="bedIdOptions"
              :props="bedProps"
              @change="handleFloor"
              placeholder="请选择"
              clearable
            ></el-cascader>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="押金">
            <span class="method">+</span>
            <el-input
              v-model="formData.deposit"
              :disabled="type === 'read'"
              type="text"
              placeholder="请输入"
              clearable
              :style="{ width: '150px' }"
            ></el-input><span class="unit">元</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="护理费用" prop="nursingFee">
            <span class="method">+</span>
            <el-input-number
              v-model="formData.nursingFee"
              placeholder="0.00"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              @blur="textBlurPrice($event, 1)"
              @change="textBlurPrice($event, 1)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="床位费用" prop="bedFee">
            <span class="method">+</span>
            <el-input-number
              v-model="formData.bedFee"
              placeholder="0.00"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              :decimal-places="2"
              @blur="textBlurPrice($event, 2)"
              @change="textBlurPrice($event, 2)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他费用">
            <span class="method">+</span>
            <el-input-number
              v-model="formData.otherFees"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              placeholder="0.00"
              :decimal-places="2"
              @blur="textBlurPrice($event, 3)"
              @change="textBlurPrice($event, 3)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="医保费用">
            <span class="method2">- </span>
            <el-input-number
              v-model="formData.insurancePayment"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              placeholder="0.00"
              :decimal-places="2"
              @blur="textBlurPrice($event, 4)"
              @change="textBlurPrice($event, 4)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政府补贴">
            <span class="method2"> - </span>
            <el-input-number
              v-model="formData.governmentSubsidy"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              placeholder="0.00"
              :decimal-places="2"
              @blur="textBlurPrice($event, 5)"
              @change="textBlurPrice($event, 5)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup>
import { getNursingLevelAll } from '@/api/nursing/nursingLevel';
import { getFloorBed, getRootById } from '@/api/nursing/floor';
import { onMounted, nextTick } from 'vue';

const formRef = ref();

// 存储原始费用值用于校验
const originalNursingFee = ref(null);
const originalBedFee = ref(null);

// 定义校验规则
const rules = ref({
  checkInTime: [
    {
      required: true,
      message: '入住期限为空，请选择入住期限',
      trigger: 'change',
    },
  ],
  feeTime: [
    {
      required: true,
      message: '费用期限为空，请选择费用期限',
      trigger: 'change',
    },
  ],
  nursingLevelId: [
    {
      required: true,
      message: '护理等级为空，请选择护理等级',
      trigger: 'change',
    },
  ],
  bed: [
    {
      required: true,
      type: 'array',
      message: '入住床位为空，请选择入住床位',
      trigger: 'change',
    },
  ],
  nursingFee: [
    {
      required: true,
      message: '护理费用为空，请输入护理费用',
      trigger: 'blur',
    },
    {
      validator: (rule, val, callback) => {
        if (val === undefined || val === null || val === '') {
          callback(new Error('护理费用为空，请输入护理费用'));
        } else if (val < 0.01) {
          callback(new Error('护理费用必须大于0'));
        } else if (originalNursingFee.value && (val < originalNursingFee.value * 0.9 || val > originalNursingFee.value * 1.1)) {
          callback(new Error(`该费用已超出/低于原费用10%，请重新输入`));
        } else {
          callback();
        }
      },
      trigger: ['blur', 'change']
    }
  ],
  bedFee: [
    {
      required: true,
      message: '床位费用为空，请输入床位费用',
      trigger: 'blur',
    },
    {
      validator: (rule, val, callback) => {
        if (val === undefined || val === null || val === '') {
          callback(new Error('床位费用为空，请输入床位费用'));
        } else if (val < 0.01) {
          callback(new Error('床位费用必须大于0'));
        } else if (originalBedFee.value && (val < originalBedFee.value * 0.9 || val > originalBedFee.value * 1.1)) {
          callback(new Error(`该费用已超出/低于原费用10%，请重新输入`));
        } else {
          callback();
        }
      },
      trigger: ['blur', 'change']
    }
  ],
});

const data = reactive({
  formData: {
    checkInTime: '',
    feeTime: '',
    nursingLevelId: undefined,
    bed: [],
    deposit: 3000,
    nursingFee: undefined,
    bedFee: undefined,
    otherFees: undefined,
    insurancePayment: undefined,
    governmentSubsidy: undefined,
    startDate: undefined,
    endDate: undefined,
    feeStartDate: undefined,
    feeEndDate: undefined,
    bedId: undefined,
    floorName: undefined,
    floorId: undefined,
    code: undefined,
    roomId: undefined,
    nursingLevelName: undefined,
    bedNumber: undefined,
    // 存储原始费用
    originalNursingFee: undefined,
    originalBedFee: undefined,
  },
});
const { formData } = toRefs(data);

const nursingLevelIdOptions = ref([]);
const bedIdOptions = ref([]);

// 禁用日期函数：早于当前日期的日期置灰
const disabledDate = (time) => {
  // 获取当前日期的开始时间（00:00:00）
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  // 如果选择的时间小于今天，则禁用
  return time.getTime() < today.getTime();
};

// 单独监听护理费用变化，触发护理费用校验
watch(
  () => formData.value.nursingFee,
  () => {
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('nursingFee');
      }
    });
  },
);

// 单独监听床位费用变化，触发床位费用校验
watch(
  () => formData.value.bedFee,
  () => {
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('bedFee');
      }
    });
  },
);

onMounted(() => {
  //初始化护理等级
  getNursingLevelAll().then((res) => {
    nursingLevelIdOptions.value = res.data;
  });

  //初始化楼层房间床位
  getFloorBed(0).then((res) => {
    if (res.code === 200) {
      bedIdOptions.value = res.data;
    }
  });
});

//选择床位之后，给属性赋值
//val===>['6','39','58']  第一个值为楼层id, 第二个为房间id,第三个为床位id
const handleFloor = (val) => {
  console.log(val);
  formData.value.bedId = val[2];
  formData.value.bed = val;
  getRootById(val[1]).then((res) => {
    if (res.code === 200) {
      formData.value.floorName = res.data.floorName;
      formData.value.floorId = res.data.floorId;
      formData.value.bedFee = res.data.price;
      // 设置原始床位费用
      originalBedFee.value = res.data.price;
      formData.value.originalBedFee = res.data.price;
      formData.value.code = res.data.code;
      formData.value.roomId = res.data.roomId;
      formData.value.bedNumber = res.data.code;
      
      // 只触发床位费用的校验，不影响护理费用
      nextTick(() => {
        if (formRef.value) {
          formRef.value.validateField('bedFee');
        }
      });
    }
  });
};

//处理入住时间
const hendleCheckInTime = (val) => {
  if (val && val.length > 0) {
    formData.value.startDate = val[0];
    formData.value.endDate = val[1];
    formData.value.checkInTime = val;
  } else {
    formData.value.startDate = undefined;
    formData.value.endDate = undefined;
    formData.value.checkInTime = undefined;
  }

  // 触发校验
  nextTick(() => {
    if (formRef.value) {
      formRef.value.validateField('checkInTime');
    }
  });
};

//处理费用时间
const hendleFeeTime = (val) => {
  if (val && val.length > 0) {
    formData.value.feeStartDate = val[0];
    formData.value.feeEndDate = val[1];
    formData.value.feeTime = val;
  } else {
    formData.value.feeStartDate = undefined;
    formData.value.feeEndDate = undefined;
    formData.value.feeTime = undefined;
  }

  // 触发校验
  nextTick(() => {
    if (formRef.value) {
      formRef.value.validateField('feeTime');
    }
  });
};

//选择了护理等级之后，触发，给护理费用和护理名称赋值
const handleNursing = () => {
  if (formData.value.nursingLevelId) {
    //根据id从集合中找对应的护理等级对象
    const obj = nursingLevelIdOptions.value.find(
      (item) => item.id === formData.value.nursingLevelId,
    );
    console.log(obj);
    //给属性赋值
    formData.value.nursingFee = obj.fee;
    // 设置原始护理费用
    originalNursingFee.value = obj.fee;
    formData.value.originalNursingFee = obj.fee;
    formData.value.nursingLevelName = obj.name;
    
    // 只触发护理费用的校验，不影响床位费用
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('nursingFee');
        formRef.value.validateField('nursingLevelId');
      }
    });
  } else {
    delete formData.value.nursingFee;
    delete formData.value.nursingLevelName;
    originalNursingFee.value = null;
    formData.value.originalNursingFee = undefined;
    
    // 清空时也触发校验
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('nursingFee');
        formRef.value.validateField('nursingLevelId');
      }
    });
  }
};

const bedOptions = ref([]);
// props设置
const bedProps = ref({
  multiple: false,
  expandTrigger: 'hover',
});

const props = defineProps({
  configInfo: {
    type: Object,
    default: () => ({}),
  },
  type: {
    type: String,
    default: '',
  },
});

const checkInTime = ref([]);
const feeTime = ref([]);

watch(
  props,
  () => {
    if (props.configInfo && Object.keys(props.configInfo).length > 0) {
      formData.value = { ...formData.value, ...props.configInfo };
      
      // 设置原始费用值（用于编辑时）
      if (props.configInfo.nursingFee) {
        originalNursingFee.value = props.configInfo.nursingFee;
        formData.value.originalNursingFee = props.configInfo.nursingFee;
      }
      if (props.configInfo.bedFee) {
        originalBedFee.value = props.configInfo.bedFee;
        formData.value.originalBedFee = props.configInfo.bedFee;
      }
      
      if (props.configInfo.startDate && props.configInfo.endDate) {
        checkInTime.value = [
          props.configInfo.startDate,
          props.configInfo.endDate,
        ];
      }
      if (props.configInfo.feeStartDate && props.configInfo.feeEndDate) {
        feeTime.value = [
          props.configInfo.feeStartDate,
          props.configInfo.feeEndDate,
        ];
      }
      if (props.configInfo.bedId) {
        formData.value.bed = [
          props.configInfo.floorId,
          props.configInfo.roomId,
          props.configInfo.bedId,
        ];
      }
    }
  },
  { immediate: true, deep: true },
);

// 监听价格变化
const textBlurPrice = (val, num) => {
  let data = null
  if (num === 1) {
    data = Number(formData.value.nursingFee)
    formData.value.nursingFee = data < 0 ? 0 : data
  } else if (num === 2) {
    data = Number(formData.value.bedFee)
    formData.value.bedFee = data < 0 ? 0 : data
  } else if (num === 3) {
    data = Number(formData.value.otherFees)
    formData.value.otherFees = data < 0 ? 0 : data
    // 最大值设置
    if (data > 99999999) {
      formData.value.otherFees = 99999999
    }
  } else if (num === 4) {
    data = Number(formData.value.insurancePayment)
    formData.value.insurancePayment = data < 0 ? 0 : data
    // 最大值设置
    if (data > 99999999) {
      formData.value.insurancePayment = 99999999
    }
  } else {
    data = Number(formData.value.governmentSubsidy)
    formData.value.governmentSubsidy = data < 0 ? 0 : data
    // 最大值设置
    if (data > 99999999) {
      formData.value.governmentSubsidy = 99999999
    }
  }
}

// 提供校验方法
const validate = async () => {
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

//声明父组件的方法
const emits = defineEmits(['getFormData']);
//监听数据变化，传递到父组件
watch(
  formData,
  (val) => {
    //调用父组件的方法
    emits('getFormData', val);
  },
  { deep: true, immediate: true },
);

//暴露表单校验的方法给外面，给父组件
defineExpose({
  validate,
  getFormData,
});
</script>
<style></style>