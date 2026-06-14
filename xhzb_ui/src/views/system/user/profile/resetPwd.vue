<template>
  <el-form ref="pwdRef" :model="user" :rules="rules" label-width="100px">
    <el-form-item label="原密码" prop="oldPassword">
      <el-input
        v-model="user.oldPassword"
        placeholder="请输入"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input
        v-model="user.newPassword"
        placeholder="请输入"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item label="确认新密码" prop="confirmPassword">
      <el-input
        v-model="user.confirmPassword"
        placeholder="请输入"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { updateUserPwd } from '@/api/system/user';
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
const { proxy } = getCurrentInstance();
const user = reactive({
  oldPassword: undefined,
  newPassword: undefined,
  confirmPassword: undefined,
});

const equalToPassword = (rule, value, callback) => {
  if (user.newPassword !== value) {
    callback(new Error('新密码与确认新密码不一致，请重新输入'));
  } else {
    callback();
  }
};

const rules = ref({
  oldPassword: [
    { required: true, message: '原密码为空，请输入原密码', trigger: 'blur' },
  ],
  newPassword: [
    { required: true, message: '新密码为空，请输入新密码', trigger: 'blur' },
    {
      min: 8,
      max: 20,
      message: '密码长度显示8-20位, 请重新输入',
      trigger: 'blur',
    },
    {
      validator: (rule, value, callback) => {
        // 检查是否包含数字
        const hasNumber = /\d/.test(value);
        if (!hasNumber) {
          callback(new Error('必须包含数字，请重新输入'));
          return;
        }

        // 检查是否包含小写字母
        const hasLowercase = /[a-z]/.test(value);
        if (!hasLowercase) {
          callback(new Error('必须包含小写字母，请重新输入'));
          return;
        }

        // 检查是否包含大写字母
        const hasUppercase = /[A-Z]/.test(value);
        if (!hasUppercase) {
          callback(new Error('必须包含大写字母，请重新输入'));
          return;
        }

        callback();
      },
      trigger: 'blur',
    },
  ],
  confirmPassword: [
    {
      required: true,
      message: '确认新密码为空，请输入确认新密码',
      trigger: 'blur',
    },
    { required: true, validator: equalToPassword, trigger: 'blur' },
  ],
});

/** 提交按钮 */
function submit() {
  proxy.$refs.pwdRef.validate((valid) => {
    if (valid) {
      proxy.$modal
        .confirm('密码修改成功后，需重新登录，是否继续？', '确认修改')
        .then(() => {
          updateUserPwd(user.oldPassword, user.newPassword).then((response) => {
            proxy.$modal.msgSuccess('修改成功');
            userStore.logOut().then(() => {
      location.href = '/index';
    })
          });
        })
        .catch(() => {});
    }
  });
}

/** 关闭按钮 */
function close() {
  proxy.$tab.closePage();
}
</script>
