<template>
  <div class="component-upload-image">
    <el-upload
      multiple
      :action="uploadImgUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      ref="imageUpload"
      :before-remove="handleDelete"
      :show-file-list="true"
      :headers="headers"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{ hide: fileList.length >= limit || isUploading }"
      :on-progress="handleUploadProgress"
    >
      <el-icon class="avatar-uploader-icon"><plus /></el-icon>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip">
      <template v-if="fileSize">
        图片大小不超过<b style="color: #f56c6c">{{ fileSize }}M</b>，
        仅支持上传{{fileType.join("、")}}类型图片
      </template>
    </div>

    <el-dialog
      v-model="dialogVisible"
      title=""
      append-to-body
       class="dialogImagesBox"
       :show-close="false"
    >
    <div class="imagesBox">
    <div class="close">
      <span class="el-image-viewer__btn el-image-viewer__close" @click="dialogVisible = false">
        <el-icon><Close /></el-icon>
      </span>
    </div>
      <img
        :src="dialogImageUrl"
      />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { getToken } from "@/utils/auth";
import { ref, computed, watch, getCurrentInstance } from "vue"; // 补充导入ref

const props = defineProps({
  modelValue: [String, Object, Array],
  // 图片数量限制
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
    default: () => ["png", "jpg", "jpeg"],
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();
const number = ref(0);
const uploadList = ref([]);
const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadImgUrl = ref(import.meta.env.VITE_APP_BASE_API + "/common/upload"); // 上传的图片服务器地址
const headers = ref({ Authorization: "Bearer " + getToken() });
const fileList = ref([]);
// 核心修改3：新增上传中状态变量
const isUploading = ref(false);
// 记录正在上传的文件数量
const uploadingFileCount = ref(0);

const showTip = computed(
  () => props.isShowTip && (props.fileType || props.fileSize)
);

watch(() => props.modelValue, val => {
  if (val) {
    // 首先将值转为数组
    const list = Array.isArray(val) ? val : props.modelValue.split(",");
    // 然后将数组转为对象数组
    fileList.value = list.map(item => {
      if (typeof item === "string") {
        if(item.startsWith("http")){
          item = { name: item, url: item };
        }else{
          item = { name: baseUrl + item, url: baseUrl + item };
        }
      }
      return item;
    });
  } else {
    fileList.value = [];
    return [];
  }
},{ deep: true, immediate: true });

// 上传前loading加载
function handleBeforeUpload(file) {
  let isImg = false;
  if (props.fileType.length) {
    let fileExtension = "";
    if (file.name.lastIndexOf(".") > -1) {
      fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1).toLowerCase();
    }
    isImg = props.fileType.some(type => {
      const lowerType = type.toLowerCase();
      if (file.type.toLowerCase().indexOf(lowerType) > -1) return true;
      if (fileExtension && fileExtension === lowerType) return true;
      return false;
    });
  } else {
    isImg = file.type.indexOf("image") > -1;
  }
  if (!isImg) {
    proxy.$modal.msgError(
      `文件格式不正确，请上传${props.fileType.join("/")}图片格式文件!`
    );
    return false;
  }
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize;
    if (!isLt) {
      proxy.$modal.msgError(`图片大小超过${props.fileSize}m，请重新上传!`);
      return false;
    }
  }
  proxy.$modal.loading("正在上传图片，请稍候...");
  // 核心修改4：开始上传时标记状态
  uploadingFileCount.value++;
  isUploading.value = true;
  number.value++;
}

// 核心修改5：新增上传进度监听事件
function handleUploadProgress(event, file, fileList) {
  // 上传过程中保持isUploading为true
  isUploading.value = true;
}

// 文件个数超出
function handleExceed() {
  proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
}

// 上传成功回调
function handleUploadSuccess(res, file) {
  // 核心修改6：上传成功后减少上传计数
  uploadingFileCount.value--;
  if (uploadingFileCount.value <= 0) {
    isUploading.value = false;
    uploadingFileCount.value = 0; // 重置计数
  }

  if (res.code === 200) {
    uploadList.value.push({ name: res.fileName, url: res.fileName });
    uploadedSuccessfully();
  } else {
    number.value--;
    proxy.$modal.closeLoading();
    proxy.$modal.msgError(res.msg);
    proxy.$refs.imageUpload.handleRemove(file);
    uploadedSuccessfully();
  }
}

// 删除图片
function handleDelete(file) {
  const findex = fileList.value.map(f => f.name).indexOf(file.name);
  if (findex > -1 && uploadList.value.length === number.value) {
    fileList.value.splice(findex, 1);
    emit("update:modelValue", listToString(fileList.value));
    return false;
  }
}

// 上传结束处理
function uploadedSuccessfully() {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = fileList.value.filter(f => f.url !== undefined).concat(uploadList.value);
    uploadList.value = [];
    number.value = 0;
    emit("update:modelValue", listToString(fileList.value));
    proxy.$modal.closeLoading();
  }
}

// 上传失败
function handleUploadError(err, file) {
  // 核心修改7：上传失败后也更新上传状态
  uploadingFileCount.value--;
  if (uploadingFileCount.value <= 0) {
    isUploading.value = false;
    uploadingFileCount.value = 0;
  }

  proxy.$modal.msgError("上传图片失败");
  proxy.$modal.closeLoading();
}

// 预览
function handlePictureCardPreview(file) {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
}

// 对象转成指定字符串分隔
function listToString(list, separator) {
  let strs = "";
  separator = separator || ",";
  for (let i in list) {
    if (undefined !== list[i].url && list[i].url.indexOf("blob:") !== 0) {
      strs += list[i].url.replace(baseUrl, "") + separator;
    }
  }
  return strs != "" ? strs.substr(0, strs.length - 1) : "";
}
</script>

<style scoped lang="scss">
// .el-upload--picture-card 控制加号部分
:deep(.hide .el-upload--picture-card) {
    display: none;
}
.component-upload-image{
  :deep(.el-progress-circle) {
    height: 100px !important; /* 修改进度条高度 */
    width: 100px !important; /* 修改进度条宽度 */
  }
  :deep(.el-upload-list--picture-card .el-progress){
    width: 100px;
  }
}

.close{
  position: absolute;
  right: -100px;
  top: -50px;
}
</style>