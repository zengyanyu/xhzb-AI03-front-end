<template>
  <div
    :class="classObj"
    class="app-wrapper"
    :style="{ '--current-color': theme }"
  >
    <div
      v-if="device === 'mobile' && sidebar.opened"
      class="drawer-bg"
      @click="handleClickOutside"
    />
    <sidebar v-if="!sidebar.hide" class="sidebar-container" />
    <div
      :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }"
      class="main-container"
    >
      <div :class="{ 'fixed-header': fixedHeader }">
        <navbar @setLayout="setLayout" />
        <!-- <tags-view v-if="needTagsView" /> -->
      </div>
      <app-main />
      <settings ref="settingRef" />
    </div>
  </div>
  <!-- 报警提示弹层 -->
  <Warn
    :visible="visibleWarn"
    :data="warnData"
    :time="time"
    @handleSubmit="handleSubmit"
    @handleClose="handleWarnClose"
  ></Warn>
  <!-- end -->
</template>

<script setup>
import { useWindowSize } from '@vueuse/core';
import Sidebar from './components/Sidebar/index.vue';
import Warn from '@/components/warn/index.vue';
import { AppMain, Navbar, Settings, TagsView } from './components';
import defaultSettings from '@/settings';

import useAppStore from '@/store/modules/app';
import useSettingsStore from '@/store/modules/settings';
import { onMounted } from 'vue';
import useUserStore from '@/store/modules/user';
const settingsStore = useSettingsStore();
const userStore = useUserStore();
const theme = computed(() => settingsStore.theme);
const sideTheme = computed(() => settingsStore.sideTheme);
const sidebar = computed(() => useAppStore().sidebar);
const device = computed(() => useAppStore().device);
const needTagsView = computed(() => settingsStore.tagsView);
const fixedHeader = computed(() => settingsStore.fixedHeader);

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile',
}));

const { width, height } = useWindowSize();
const WIDTH = 992; // refer to Bootstrap's responsive design
const visibleWarn = ref(false); // 报警弹层
const operateTitle = ref(''); // 操作弹层标题
const operateText = ref(''); // 要操作的内容提示
const audioVo = ref(null);
watchEffect(() => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    useAppStore().closeSideBar({ withoutAnimation: false });
  }
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile');
    useAppStore().closeSideBar({ withoutAnimation: true });
  } else {
    useAppStore().toggleDevice('desktop');
  }
});
onMounted(() => {
  setwebSocket();
});
function handleClickOutside() {
  useAppStore().closeSideBar({ withoutAnimation: false });
}

const settingRef = ref(null);
function setLayout() {
  settingRef.value.openSetting();
}
// 语音播报/报警异常
const socket = ref(null);
const warnData = ref({}); // 报警数据
const setwebSocket = () => {
  // TODO 一般传一个随机数字，不过本项目传的是用户id
  // const clientId = Math.random().toString(36).substr(2)
  // socket.value = new WebSocket(`ws://172.16.17.191:9000/ws/${userStore.id}`)
  console.log(`${import.meta.env.VITE_APP_SOCKET_URL}/ws/${userStore.id}`);
  socket.value = new WebSocket(
    `${import.meta.env.VITE_APP_SOCKET_URL}/ws/${userStore.id}`
  );
  socket.value.onmessage = (event) => {
    const res = JSON.parse(event.data);
    warnData.value = res;
    console.log(res);
    if (res.notifyType === 1) {
      // 报警异常
      if (res.isAllConsumer) {
        if (res.physicalLocationType === 0) {
          userStore.setUnusualFloorId(res.deviceDescription?.split(',')[0]);
        } else if (res.physicalLocationType === 2) {
          userStore.setUnusualBedId(res.deviceDescription?.split(',')[2]);
        }
      } else {
        // 添加语音播报/弹层提示
        // 报警提示弹层
        visibleWarn.value = true;
      }
    } else {
      // 解除报警异常
      if (res.physicalLocationType === 0) {
        return userStore.deleteUnusualFloorId(
          res.deviceDescription.split(',')[0]
        );
      }
      if (res.physicalLocationType === 2) {
        return userStore.deleteUnusualBedId(
          res.deviceDescription.split(',')[2]
        );
      }
    }
  };
};
// 关闭警告弹层
const handleWarnClose = () => {
  visibleWarn.value = false;
};
</script>

<style lang="scss" scoped>
@import '@/assets/styles/mixin.scss';
@import '@/assets/styles/variables.module.scss';

.app-wrapper {
  @include clearfix;
  position: relative;
  // height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$base-sidebar-width});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}
</style>
