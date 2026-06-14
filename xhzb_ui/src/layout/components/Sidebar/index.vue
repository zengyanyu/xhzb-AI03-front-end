<template>
  <div
    :class="{ 'has-logo': showLogo }"
    :style="{
      backgroundColor:
        sideTheme === 'theme-dark'
          ? variables.menuBackground
          : variables.menuLightBackground,
    }"
  >
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar :class="sideTheme" wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="
          sideTheme === 'theme-dark'
            ? variables.menuBackground
            : variables.menuLightBackground
        "
        :text-color="
          sideTheme === 'theme-dark'
            ? variables.menuColor
            : variables.menuLightColor
        "
        :unique-opened="true"
        :active-text-color="theme"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
    <!-- <div class="sideFoot">
      <div class="avatar-wrapper">
        <img :src="userStore.avatar" class="user-avatar" />
        {{ userStore.name }}
        <span class="quit"></span>
      </div>
      <span class="hamburger" @click="toggleSideBar"></span>

    </div> -->
  </div>
</template>

<script setup>
import Logo from './Logo';
import SidebarItem from './SidebarItem';
import variables from '@/assets/styles/variables.module.scss';
import useAppStore from '@/store/modules/app';
import useSettingsStore from '@/store/modules/settings';
import usePermissionStore from '@/store/modules/permission';
import useUserStore from '@/store/modules/user';
const route = useRoute();
const appStore = useAppStore();
const settingsStore = useSettingsStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();
const sidebarRouters = computed(() => permissionStore.sidebarRouters);
const showLogo = computed(() => settingsStore.sidebarLogo);
const sideTheme = computed(() => settingsStore.sideTheme);
const theme = computed(() => settingsStore.theme);
const isCollapse = computed(() => !appStore.sidebar.opened);

const activeMenu = computed(() => {
  const { meta, path } = route;
  // if set path, the sidebar will highlight the path you set
  if (meta.activeMenu) {
    return meta.activeMenu;
  }
  return path;
});
function toggleSideBar() {
  appStore.toggleSideBar();
}
</script>
<style scoped lang="scss">
.sideFoot {
  position: absolute;
  bottom: 10px;
  z-index: 99;
  width: 100%;
  height: 40px;
  font-size: 14px;
  display: flex;
  align-items: center;
  .avatar-wrapper {
    display: flex;
    align-items: center;
    padding-left: 20px;
    flex: 1;
    
    img {
      width: 24px;
      height: 24px;
      margin-right: 6px;
    }
  }
  .quit,.hamburger{
      display: inline-block;
      width: 28px;
      height: 28px;
      cursor: pointer;
      &:hover{
        background-color: #E1F0EC;
        border-radius: 3px;
      }
    }
    .quit{
      background: url('@/assets/icons/btn_quit.png') no-repeat;
      background-size: contain;
      margin-left: 7px;
    }
    .hamburger{
      background: url('@/assets/icons/btn_kaiguan.png') no-repeat;
      background-size: contain;
      margin-right: 10px;
    }
}
.hideSidebar{
  .avatar-wrapper{
    display: none;
  }
  .hamburger{
    margin: 0 0 0 10px;
  }
}
</style>
