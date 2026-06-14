<template>
  <div class="upload-file">
    <el-upload
      multiple
      :action="uploadFileUrl"
      :before-upload="handleBeforeUpload"
      :file-list="fileList"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :on-success="handleUploadSuccess"
      :show-file-list="false"
      :headers="headers"
      class="upload-file-uploader"
      ref="fileUpload"
    >
      <!-- 上传按钮 -->
      <el-button type="primary">选取文件</el-button>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip">
      <template v-if="fileSize">
        请上传{{ fileType.join('、') }}文件，大小在<b style="color: #f56c6c"
          >{{ fileSize }}MB以内</b
        >
      </template>
      <!-- 请上传
      <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB以内</b> </template>
      <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("、") }}</b> </template>
      的文件 -->
    </div>
    <!-- 文件列表 -->
    <!-- 文件列表 -->
    <transition-group
      class="upload-file-list el-upload-list el-upload-list--text"
      name="el-fade-in-linear"
      tag="ul"
    >
      <li
        :key="file.uid"
        class="el-upload-list__item ele-upload-list__item-content"
        v-for="(file, index) in fileList"
      >
        <!-- PDF 文件 -->
        <div v-if="getFileType(file.url) === 'pdf'" class="file-item-pdf">
          <el-link :href="file.url" :underline="false" target="_blank">
            <el-icon><Document /></el-icon>
            <span>{{
              newFileType !== '' ? newFileType : fileName || 'PDF 文件'
            }}</span>
          </el-link>
          <div class="ele-upload-list__item-content-action">
            <el-link
              :underline="false"
              @click="handleDelete(index)"
              type="danger"
              >删除</el-link
            >
          </div>
        </div>

        <!-- 图片文件 -->
        <div v-else class="file-item-image">
          <ImagePreview
            :src="file.url"
            :width="'100px'"
            :height="'100px'"
          ></ImagePreview>
          <div class="ele-upload-list__item-content-action">
            <el-link
              :underline="false"
              @click="handleDelete(index)"
              type="danger"
              >删除</el-link
            >
          </div>
        </div>

        <!-- 其他文件 -->
      </li>
    </transition-group>
  </div>
</template>

<script setup>
import { getToken } from '@/utils/auth';
import { getFileType } from '@/utils';
const props = defineProps({
  modelValue: [String, Object, Array],
  // 数量限制
  limit: {
    type: Number,
    default: 5,
  },
  // 大小限制(MB)
  fileSize: {
    type: Number,
    default: 5,
  },
  // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileType: {
    type: Array,
    default: () => ['doc', 'xls', 'ppt', 'txt', 'pdf'],
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true,
  },
  elderName: {
    type: String,
    default: '',
  },
  fileName: {
    type: String,
    default: '',
  },
  urlFileType: {
    type: String,
    default: '',
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();
const number = ref(0);
const uploadList = ref([]);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadFileUrl = ref(import.meta.env.VITE_APP_BASE_API + '/common/upload'); // 上传文件服务器地址
const headers = ref({ Authorization: 'Bearer ' + getToken() });
const fileList = ref([]);
const showTip = computed(
  () => props.isShowTip && (props.fileType || props.fileSize),
);
const newFileType = ref('');
watch(
  () => props.fileName,
  (val) => {
    newFileType.value = val;
    console.log(val);
  },
);
watch(
  () => props.modelValue,
  (val) => {
    if (val) {
      console.log(val);
      let temp = 1;
      // 首先将值转为数组
      const list = Array.isArray(val) ? val : props.modelValue.split(',');
      // 然后将数组转为对象数组
      fileList.value = list.map((item) => {
        if (typeof item === 'string') {
          item = { name: item, url: item };
        }
        item.uid = item.uid || new Date().getTime() + temp++;
        return item;
      });
    } else {
      fileList.value = [];
      return [];
    }
  },
  { deep: true, immediate: true },
);

// 上传前校检格式和大小
function handleBeforeUpload(file) {
  // 校检文件类型
  if (props.fileType.length) {
    const fileName = file.name.split('.');
    const fileExt = fileName[fileName.length - 1].toLowerCase();
    // 获取image/jpeg中的jpeg
    newFileType.value =
      props.elderName +
      '.' +
      props.fileType.find((item) => item.indexOf(fileExt) !== -1);
    const isTypeOk = props.fileType.some(
      (type) => type.toLowerCase() === fileExt,
    );
    if (!isTypeOk) {
      proxy.$modal.msgError(
        `文件格式不正确，请上传${props.fileType.join('/')}格式文件!`,
      );
      return false;
    }
  }
  // 校检文件大小
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize;
    if (!isLt) {
      proxy.$modal.msgError(`上传文件大小不能超过 ${props.fileSize} MB!`);
      return false;
    }
  }
  proxy.$modal.loading('正在上传文件，请稍候...');
  number.value++;
  return true;
}

// 文件个数超出
function handleExceed() {
  proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
}

// 上传失败
function handleUploadError(err) {
  proxy.$modal.msgError('上传文件失败');
}
const getFileName = ref(null);
// 上传成功回调
function handleUploadSuccess(res, file) {
  console.log(res, file);
  if (res.code === 200) {
    getFileName.value = res.originalFilename;
    uploadList.value.push({ name: res.fileName, url: res.fileName });
    uploadedSuccessfully();
  } else {
    number.value--;
    proxy.$modal.closeLoading();
    proxy.$modal.msgError(res.msg);
    proxy.$refs.fileUpload.handleRemove(file);
    uploadedSuccessfully();
  }
}

// 删除文件
function handleDelete(index) {
  fileList.value.splice(index, 1);
  emit('update:modelValue', listToString(fileList.value));
}

// 上传结束处理
function uploadedSuccessfully() {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = fileList.value
      .filter((f) => f.url !== undefined)
      .concat(uploadList.value);
    uploadList.value = [];
    number.value = 0;
    emit('update:modelValue', listToString(fileList.value));
    proxy.$modal.closeLoading();
  }
}

// // 获取文件名称
// function getFileName(name) {
//   // 如果是url那么取最后的名字 如果不是直接返回
//   if (name.lastIndexOf("/") > -1) {
//     return name.slice(name.lastIndexOf("/") + 1);
//   } else {
//     return name;
//   }
// }

// 对象转成指定字符串分隔
function listToString(list, separator) {
  let strs = '';
  separator = separator || ',';
  for (let i in list) {
    if (list[i].url) {
      strs += list[i].url + separator;
    }
  }
  return strs != '' ? strs.substr(0, strs.length - 1) : '';
}
</script>

<style scoped lang="scss">
.upload-file-uploader {
  margin-bottom: 5px;
}
.upload-file-list .el-upload-list__item {
  // border: 1px solid #e4e7ed;
  line-height: 2;
  margin-bottom: 10px;
  position: relative;
  &:hover {
    background: transparent;
  }
}
.upload-file-list .ele-upload-list__item-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: inherit;
  .file-item-pdf {
    display: flex;
  }
}
.ele-upload-list__item-content-action .el-link {
  margin-right: 10px;
}
.file-item-image {
  display: flex;
  width: 100%;
  align-items: center;
  .ele-upload-list__item-content-action {
    flex: 1;
    text-align: right;
  }
}
</style>
