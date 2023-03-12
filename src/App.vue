<template>
  <NConfigProvider
    :locale="zhCN"
    :date-locale="dateZhCN"
    :theme="getDarkTheme"
    :theme-overrides="getThemeOverrides"
  >
    <AppProvider>
      <RouterView />
    </AppProvider>
  </NConfigProvider>
</template>

<script>
import { computed, defineComponent } from 'vue';
// locale & dateLocale
import { zhCN, dateZhCN, darkTheme } from 'naive-ui';
import { AppProvider } from '@/components/Application';
import { useDesignSettingStore } from '@/store/modules/designSetting';
import { lighten } from '@/utils/index';

export default defineComponent({
  components: { AppProvider },
  setup() {
    const designStore = useDesignSettingStore();
    /**
     * @type import('naive-ui').GlobalThemeOverrides
     */
    const getThemeOverrides = computed(() => {
      const appTheme = designStore.appTheme;
      const lightenStr = lighten(designStore.appTheme, 6);
      return {
        common: {
          primaryColor: appTheme,
          primaryColorHover: lightenStr,
          primaryColorPressed: lightenStr,
          primaryColorSuppl: appTheme,
        },
        LoadingBar: {
          colorLoading: appTheme,
        },
      };
    });

    const getDarkTheme = computed(() =>
      designStore.darkTheme ? darkTheme : undefined
    );
    return {
      zhCN,
      dateZhCN,
      getDarkTheme,
      getThemeOverrides,
    };
  },
});
</script>
<style lang="less">
@import 'styles/index.less';
</style>
