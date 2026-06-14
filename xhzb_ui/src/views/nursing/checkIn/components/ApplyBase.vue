<!-- 需要展示的基本信息 -->
<template>
  <div>
    <!-- 基本信息 -->
    <el-card>
      <h3>
        <span class="header-title">基本信息</span>
      </h3>
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-120">老人姓名：</h1>
          <span>{{ baseData.name }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">身份证号：</h1>
          <span>{{ baseData.idCardNo }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">出生日期：</h1>
          <span>{{ baseData.birthday }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">年龄：</h1>
          <span>{{ baseData.age }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">性别：</h1>
          <span>{{ baseData.sex === 0 ? '男' : '女' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">社保卡号：</h1>
          <span>{{ baseData.socialSecurityCard || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">民族：</h1>
          <span>
            {{ baseData.nation }}
            <span
              v-if="baseData.nation === '少数民族' && baseData.nationDetail"
            >
              （{{ baseData.nationDetail }}）
            </span>
          </span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">文化程度：</h1>
          <span>{{ baseData.educationLevel || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">宗教信仰：</h1>
          <span>
            {{ baseData.religiousBelief }}
            <span
              v-if="
                baseData.religiousBelief === '有' &&
                baseData.religiousBeliefDetail
              "
            >
              （{{ baseData.religiousBeliefDetail }}）
            </span>
          </span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">婚姻状况：</h1>
          <span>{{ baseData.maritalStatus || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">居住情况：</h1>
          <span>{{ baseData.livingSituation || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">医疗费用支付方式：</h1>
          <span>{{ baseData.medicalPaymentMethod || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">经济来源：</h1>
          <span>{{ baseData.economicSource || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">老人联系方式：</h1>
          <span>{{ baseData.phone || '--' }}</span>
        </div>

        <div class="info-item">
          <h1 class="label-wt-120">核心建议：</h1>
          <span>
            <!-- <span v-if="baseData.coreSuggestion === 0" type="danger"
              >不建议入住</span
            >-->
            <span v-if="baseData.coreSuggestion" :class="baseData.coreSuggestion==='建议入住'?'tagCol1':'tagCol2'"
              >{{baseData.coreSuggestion}}</span
            > 
            <span v-else>--</span>
          </span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">家庭住址：</h1>
          <span>{{ baseData.address }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">一寸照片：</h1>
          <ImagePreview :src="baseData.image" :width="'100px'" :height="'100px'"></ImagePreview>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">身份证人像面：</h1>
          <ImagePreview :src="baseData.idCardPortraitImg" :width="'100px'" :height="'100px'"></ImagePreview>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">身份证国徽面：</h1>
          <ImagePreview :src="baseData.idCardNationalEmblemImg" :width="'100px'" :height="'100px'"></ImagePreview>
        </div>
      </div>
    </el-card>
    <!-- end -->
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
// 获取父组件值、方法
const props = defineProps({
  // 基本信息数据
  baseData: {
    type: Object,
    default: () => {
      return {};
    },
  },
});
// ------定义变量------
const oneInchPhoto = ref([
  {
    url: '',
  },
]);
const idCardNationalEmblemImg = ref([
  {
    url: '',
  },
]);
const idCardPortraitImg = ref([
  {
    url: '',
  },
]);
// 绑定上传的文件
watch(props, () => {
  const data = props.baseData;
  oneInchPhoto.value[0].url = data.oneInchPhoto;
  idCardNationalEmblemImg.value[0].url = data.idCardNationalEmblemImg;
  idCardPortraitImg.value[0].url = data.idCardPortraitImg;
});
</script>
