<template>
  <div ref="mainHeight">
    <el-card header="基本信息">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-140">老人姓名：</h1>
          <span>{{ detailData.elderName }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">身份证号：</h1>
          <span>{{ detailData.idCard }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">出生日期：</h1>
          <span>{{ detailData.birthDate }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">年龄：</h1>
          <span>{{ detailData.age }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">性别：</h1>
          <span>{{ detailData.gender === 0 ? '男' : '女' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">社保卡号：</h1>
          <span>{{ detailData.socialSecurityCard }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">民族：</h1>
          <span>{{ detailData.nation }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">文化程度：</h1>
          <span>{{ detailData.educationLevel }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">宗教信仰：</h1>
          <span>{{ detailData.religiousBelief }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">婚姻状况：</h1>
          <span>{{ detailData.maritalStatus }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">居住情况：</h1>
          <span>{{ detailData.livingSituation }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">医疗费用支付方式：</h1>
          <span>{{ detailData.medicalPaymentMethod }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">经济来源：</h1>
          <span>{{ detailData.economicSource }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">老人联系方式：</h1>
          <span>{{ detailData.elderContact }}</span>
        </div>
      </div>
    </el-card>
    <el-card header="信息提供者及联系人信息">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-140">信息提供者姓名：</h1>
          <span>{{ detailData.providerName }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">信息提供者与老人关系：</h1>
          <span>{{ detailData.relationshipWithElder }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-140">信息提供者联系方式：</h1>
          <span>{{ detailData.providerContact }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">知情同意书：</h1>
          <span>
            <div v-if="getFileType(detailData.informedConsentUrl)!=='pdf'">
              <ImagePreview :src="detailData.informedConsentUrl" :width="'100px'" :height="'100px'"></ImagePreview>
            </div>
            <div v-else>
              {{ detailData.elderName }}的知情同意书.{{getFileType(detailData.informedConsentUrl)}}
            <i>
              <a class="font-bt" :href="detailData.informedConsentUrl" target="_blank">查看</a>
            </i>
            </div>
            
          </span>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script setup>
import { reactive, watch, ref, computed, onMounted, nextTick } from 'vue';
import {getFileType} from '@/utils';
// 组件
// 不同年龄人群健康指数分布
import AgeDistribution from './AgeDistribution.vue';
// 不同系统健康指数分布
import SystemDistribution from './SystemDistribution.vue';
const props = defineProps({
  baseData: {
    type: Object,
    default: () => {},
  },
});
const detailData = ref({});
watch(() => props.baseData, (val) => { 
  nextTick(()=>{
    detailData.value=val.basicInfo
  })
  

},
  { deep: true,immediate: true});
</script>