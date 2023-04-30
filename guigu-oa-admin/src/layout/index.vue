<template>
  <n-layout class="layout" :position="fixedMenu" has-sider>
    <n-layout-sider
      v-if="!isMobile && navMode === 'vertical'"
      show-trigger="bar"
      @collapse="collapsed = true"
      :position="fixedMenu"
      @expand="collapsed = false"
      :collapsed="collapsed"
      collapsed-mode="width"
      :collapsed-width="64"
      :width="leftMenuWidth"
      :native-scrollbar="false"
      :inverted="inverted"
      class="layout-sider"
    >
      <Logo :collapsed="collapsed" />
      <AsideMenu
        v-model:collapsed="collapsed"
        v-model:location="getMenuLocation"
      />
    </n-layout-sider>
    <!-- 移动端侧边栏 -->
    <n-drawer
      v-model:show="showSideDrawder"
      :width="menuWidth"
      :placement="'left'"
      class="layout-side-drawer"
    >
      <Logo :collapsed="collapsed" />
      <AsideMenu @clickMenuItem="collapsed = false" />
    </n-drawer>
    <!-- 头部 -->
    <n-layout :inverted="inverted">
      <n-layout-header :inverted="getHeaderInverted" :position="fixedHeader">
        <PageHeader v-model:collapsed="collapsed" :inverted="inverted" />
      </n-layout-header>
      <!-- 中间部分 -->
      <n-layout-content
        class="layout-content"
        :class="{ 'layout-default-background': getDarkTheme === false }"
      >
        <div
          class="layout-content-main"
          :class="{
            'layout-content-main-fix': fixedMulti,
            'fluid-header': fixedHeader === 'static',
          }"
        >
          <TabsView v-if="isMultiTabs" v-model:collapsed="collapsed" />
          <div
            class="main-view"
            :class="{
              'main-view-fix': fixedMulti,
              noMultiTabs: !isMultiTabs,
              'mt-3': !isMultiTabs,
            }"
          >
            <MainView />
          </div>
        </div>
      </n-layout-content>
      <n-back-top :right="100" />
    </n-layout>
  </n-layout>
</template>
<script>
import { computed, defineComponent, onMounted, ref, unref } from 'vue';
import { Logo } from './components/Logo';
import { PageHeader } from './components/Header';
import { MainView } from './components/Main';
import { TabsView } from './components/TagsView';
import { AsideMenu } from './components/Menu';
import { useProjectSetting } from '@/hooks/setting/useProjectSetting';
import { useProjectSettingStore } from '@/store/modules/projectSetting';
import { useDesignSetting } from '@/hooks/setting/useDesignSetting';

export default defineComponent({
  components: { Logo, PageHeader, MainView, TabsView, AsideMenu },
  setup() {
    const {
      getNavMode,
      getNavTheme,
      getHeaderSetting,
      getMenuSetting,
      getMultiTabsSetting,
    } = useProjectSetting();
    const settingStore = useProjectSettingStore();
    const { getDarkTheme } = useDesignSetting();
    const navMode = getNavMode;

    const collapsed = ref(false);

    const { mobileWidth, menuWidth } = unref(getMenuSetting);

    const isMobile = computed({
      get: () => settingStore.getIsMobile,
      set: (val) => settingStore.setIsMobile(val),
    });
    // 固定头部
    const fixedHeader = computed(() => {
      const { fixed } = unref(getHeaderSetting);
      return fixed ? 'absolute' : 'static';
    });
    // 固定菜单
    const fixedMenu = computed(() => {
      const { fixed } = unref(getHeaderSetting);
      return fixed ? 'absolute' : 'static';
    });
    // 多标签页
    const isMultiTabs = computed(() => {
      return unref(getMultiTabsSetting).show;
    });
    const fixedMulti = computed(() => {
      return unref(getMultiTabsSetting).fixed;
    });
    const leftMenuWidth = computed(() => {
      const { minMenuWidth, menuWidth } = unref(getMenuSetting);
      return collapsed.value ? minMenuWidth : menuWidth;
    });

    const inverted = computed(() => {
      return ['dark', 'header-dark'].includes(unref(getNavTheme));
    });

    const getHeaderInverted = computed(() => {
      const navTheme = unref(getNavTheme);
      return ['light', 'header-dark'].includes(navTheme)
        ? unref(inverted)
        : !unref(inverted);
    });

    const getMenuLocation = computed(() => {
      return 'left';
    });

    // 控制显示或隐藏移动端侧边栏
    const showSideDrawder = computed({
      get: () => isMobile.value && collapsed.value,
      set: (val) => (collapsed.value = val),
    });

    //判断是否触发移动端模式
    const checkMobileMode = () => {
      if (document.body.clientWidth <= mobileWidth) {
        isMobile.value = true;
      } else {
        isMobile.value = false;
      }
      collapsed.value = false;
    };
    // 监听宽度变化
    const watchWidth = () => {
      const Width = document.body.clientWidth;
      if (Width <= 950) {
        collapsed.value = true;
      } else collapsed.value = false;

      checkMobileMode();
    };

    onMounted(() => {
      checkMobileMode();
      window.addEventListener('resize', watchWidth);
    });

    return {
      navMode,
      isMultiTabs,
      menuWidth,
      isMobile,
      fixedMulti,
      fixedMenu,
      fixedHeader,
      collapsed,
      inverted,
      checkMobileMode,
      getDarkTheme,
      getHeaderInverted,
      leftMenuWidth,
      getMenuLocation,
      showSideDrawder,
    };
  },
});
</script>
<style lang="less">
.layout-side-drawer {
  background-color: rgb(0, 20, 40);

  .layout-sider {
    min-height: 100vh;
    box-shadow: 2px 0 8px 0 rgb(29 35 41 / 5%);
    position: relative;
    z-index: 13;
    transition: all 0.2s ease-in-out;
  }
}
</style>
<style lang="less" scoped>
.layout {
  display: flex;
  flex-direction: row;
  flex: auto;

  &-default-background {
    background: #f5f7f9;
  }

  .layout-sider {
    min-height: 100vh;
    box-shadow: 2px 0 8px 0 rgb(29 35 41 / 5%);
    position: relative;
    z-index: 13;
    transition: all 0.2s ease-in-out;
  }

  .layout-sider-fix {
    position: fixed;
    top: 0;
    left: 0;
  }

  .ant-layout {
    overflow: hidden;
  }

  .layout-right-fix {
    overflow-x: hidden;
    padding-left: 200px;
    min-height: 100vh;
    transition: all 0.2s ease-in-out;
  }

  .layout-content {
    flex: auto;
    min-height: 100vh;
  }

  .n-layout-header.n-layout-header--absolute-positioned {
    z-index: 11;
  }

  .n-layout-footer {
    background: none;
  }
}

.layout-content-main {
  margin: 0 10px 10px;
  position: relative;
  padding-top: 64px;
}

.layout-content-main-fix {
  padding-top: 64px;
}

.fluid-header {
  padding-top: 0;
}

.main-view-fix {
  padding-top: 44px;
}

.noMultiTabs {
  padding-top: 0;
}
</style>
