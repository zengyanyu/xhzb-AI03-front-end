<template>
  <el-input-number
    v-model="priceValue"
    :min="min"
    :step="step"
    :precision="precision"
    :controls="controls"
    :placeholder="placeholder"
    :disabled="disabled"
    class="price-input"
    @blur="handleBlur"
    @change="handleChange"
  />
</template>

<script setup name="PriceInput">
import { ref, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: [Number, String],
    default: 0.00,
  },
  min: {
    type: Number,
    default: 0,
  },
  step: {
    type: Number,
    default: 10,
  },
  precision: {
    type: Number,
    default: 2,
  },
  controls: {
    type: Boolean,
    default: true,
  },
  placeholder: {
    type: String,
    default: '0.00',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:modelValue', 'blur', 'change']);

const priceValue = ref(props.modelValue ?? 0.00);

// 监听外部值变化
watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal !== priceValue.value) {
      priceValue.value = newVal ?? 0.00;
    }
  }
);

// 失焦处理
const handleBlur = () => {
  if (priceValue.value === null || priceValue.value === undefined || priceValue.value === '') {
    priceValue.value = 0.00;
  }
  if (priceValue.value < props.min) {
    priceValue.value = props.min;
  }
  emit('update:modelValue', priceValue.value);
  emit('blur', priceValue.value);
};

// 值变化处理
const handleChange = (val) => {
  if (val === null || val === undefined || val === '') {
    priceValue.value = 0.00;
  }
  if (val < props.min) {
    priceValue.value = props.min;
  }
  emit('update:modelValue', priceValue.value);
  emit('change', priceValue.value);
};
</script>

<style scoped>
.price-input {
  width: 100%;
}
</style>