import {getInfo, login, logout} from '@/api/login';
import {getToken, removeToken, setToken} from '@/utils/auth';
import defAva from '@/assets/images/profile.jpg';

const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    id: '',
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
    floorInfo: {},
    unusualFloorId: [], // 异常数据的楼层集合
    unusualBedId: [], // 异常数据的床位集合
    active: localStorage.getItem("active"),
    stepNumber: JSON.parse(localStorage.getItem("step")),
  }),
  persist: {
    enabled: true,
    strategies: [
      { storage: localStorage, paths: ["adminToken", "adminUserInfo", "active", "step","tabNumber"] },
    ],
  },
  actions: {
    // 登录
    login(userInfo) {
      const username = userInfo.username.trim();
      const password = userInfo.password;
      const code = userInfo.code;
      const uuid = userInfo.uuid;
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid)
          .then((res) => {
            setToken(res.token);
            this.token = res.token;
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取用户信息
    getInfo() {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            const user = res.user;
            const avatar =
              user.avatar == '' || user.avatar == null
                ? defAva
                : import.meta.env.VITE_APP_BASE_API + user.avatar;

            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              this.roles = res.roles;
              this.permissions = res.permissions;
            } else {
              this.roles = ['ROLE_DEFAULT'];
            }
            this.id = user.userId;
            this.name = user.userName;
            this.avatar = avatar;
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 退出系统
    logOut() {
      return new Promise((resolve, reject) => {
        logout(this.token)
          .then(() => {
            this.token = '';
            this.roles = [];
            this.permissions = [];
            removeToken();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 设置楼层信息
    async setFloorInfo(data) {
      this.floorInfo = data;
    },
    async deleteUnusualFloorId(data) {
      this.unusualFloorId = this.unusualFloorId.filter((item) => item !== data)
    },
    async deleteUnusualBedId(data) {
      this.unusualBedId = this.unusualBedId.filter((item) => item !== data)
    },
    async setUnusualFloorId(data) {
      const arr = []
      arr.push(data)
      this.unusualFloorId = [...arr]
    },
    async setUnusualBedId(data) {
      const arr = []
      arr.push(data)
      this.unusualBedId = [...arr]
    },
    //设置课程管理步骤条
    async setStepActive(active) {
      this.active = active;
      localStorage.setItem("active", active);
    },
    // 设置课程管理步骤条的可以触发的状态
    async setStepNum(step) {
      this.stepNumber = step;
      localStorage.setItem("step", step);
    },
  },
  getters: {
    // 获取课程管理步骤条的当前状态
    getStepActive: (state) => {
      return state.active;
    },
    // 获取课程管理步骤条的可以触发的状态
    getStepNum: (state) => {
      return state.stepNumber;
    },
  }
});

export default useUserStore;
