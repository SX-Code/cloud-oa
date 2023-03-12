import { defineStore } from 'pinia';
import { store } from '@/store';
import projectSetting from '@/settings/projectSetting';

const {
  navMode,
  navTheme,
  isMobile,
  headerSetting,
  showFooter,
  menuSetting,
  multiTabsSetting,
  crumbsSetting,
  permissionMode,
  isPageAnimate,
  pageAnimateType,
} = projectSetting;

export const useProjectSettingStore = defineStore({
  id: 'app-project-setting',
  state: () => ({
    navMode: navMode, //导航模式
    navTheme, //导航风格
    isMobile, // 是否处于移动端模式
    headerSetting, //顶部设置
    showFooter, //页脚
    menuSetting, //多标签
    multiTabsSetting, //多标签
    crumbsSetting, //面包屑
    permissionMode, //权限模式
    isPageAnimate, //是否开启路由动画
    pageAnimateType, //路由动画类型
  }),
  getters: {
    getNavMode() {
      return this.navMode;
    },
    getNavTheme() {
      return this.navTheme;
    },
    getIsMobile() {
      return this.isMobile;
    },
    getHeaderSetting() {
      return this.headerSetting;
    },
    getShowFooter() {
      return this.showFooter;
    },
    getMenuSetting() {
      return this.menuSetting;
    },
    getMultiTabsSetting() {
      return this.multiTabsSetting;
    },
    getCrumbsSetting() {
      return this.multiTabsSetting;
    },
    getPermissionMode() {
      return this.permissionMode;
    },
    getIsPageAnimate() {
      return this.isPageAnimate;
    },
    getPageAnimateType() {
      return this.pageAnimateType;
    },
  },
  actions: {
    setNavTheme(value) {
      this.navTheme = value;
    },
    setIsMobile(value) {
      this.isMobile = value;
    },
  },
});
// Need to be used outside the setup
export function useProjectSettingStoreWithOut() {
  return useProjectSettingStore(store);
}
