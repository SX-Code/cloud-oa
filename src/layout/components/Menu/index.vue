<template>
  <NMenu
    :options="menus"
    :inverted="inverted"
    :mode="mode"
    :collapsed="collapsed"
    :collapsed-width="64"
    :collapsed-icon-size="20"
    :indent="24"
    :expanded-keys="openKeys"
    :value="getSelectedKeys"
    @update:value="clickMenuItem"
    @update:expanded-keys="menuExpanded"
  />
</template>
<script>
import {
  computed,
  defineComponent,
  onMounted,
  reactive,
  ref,
  toRefs,
  unref,
  watch,
} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAsyncRouteStore } from '@/store/modules/asyncRoute';
import { generatorMenu } from '@/utils';
import { useProjectSettingStore } from '@/store/modules/projectSetting';

export default defineComponent({
  props: {
    mode: {
      // 菜单模式
      type: String,
      default: 'vertical',
    },
    collapsed: {
      // 侧边栏菜单是否收起
      type: Boolean,
    },
    //位置
    location: {
      type: String,
      default: 'left',
    },
  },
  emits: ['update:collapsed', 'clickMenuItem'],
  setup(props, { emit }) {
    // 当前路由
    const currentRoute = useRoute();
    const router = useRouter();
    const asyncRouteStore = useAsyncRouteStore();
    const settingStore = useProjectSettingStore();
    const menus = ref([]);
    const selectedKeys = ref(currentRoute.name);
    const headerMenuSelectKey = ref('');

    // 获取当前打开的子菜单
    const matched = currentRoute.matched;
    const getOpenKeys =
      matched && matched.length ? matched.map((item) => item.name) : [];
    const state = reactive({
      openKeys: getOpenKeys,
    });

    const inverted = computed(() => {
      return ['dark', 'header-dark'].includes(settingStore.navTheme);
    });
    const getSelectedKeys = computed(() => {
      let location = props.location;
      return location === 'left' ||
        (location === 'header' && settingStore.getNavMode === 'horizontal')
        ? unref(selectedKeys)
        : unref(headerMenuSelectKey);
    });

    // 监听分割菜单
    watch(
      () => currentRoute.fullPath,
      () => {
        updateMenu();
      }
    );

    function updateSelectedKeys() {
      const matched = currentRoute.matched;
      state.openKeys = matched.map((item) => item.name);
      const activeMenu = currentRoute.meta?.activeMenu || '';
      selectedKeys.value = activeMenu ? activeMenu : currentRoute.name;
    }

    function updateMenu() {
      if (!settingStore.menuSetting.mixMenu) {
        menus.value = generatorMenu(asyncRouteStore.getMenus);
      } else {
        // 混合菜单，不予实现
      }
      updateSelectedKeys();
    }

    // 点击菜单
    function clickMenuItem(key) {
      if (/http(s)?:/.test(key)) {
        window.open(key);
      } else {
        router.push({ name: key });
      }
      emit('clickMenuItem', key);
    }
    // 展开菜单
    function menuExpanded(openKeys) {
      if (!openKeys) return;
      const latestOpenKey = openKeys.find(
        (key) => state.openKeys.indexOf(key) === -1
      );
      const isExistChildren = findChildrenLen(latestOpenKey);
      state.openKeys = isExistChildren
        ? latestOpenKey
          ? [latestOpenKey]
          : []
        : openKeys;
    }
    //查找是否存在子路由
    function findChildrenLen(key) {
      if (!key) return false;
      const subRouteChildren = [];
      for (const { children, key } of unref(menus)) {
        if (children && children.length) {
          subRouteChildren.push(key);
        }
      }
      return subRouteChildren.includes(key);
    }

    onMounted(() => {
      updateMenu();
    });

    return {
      ...toRefs(state),
      menus,
      inverted,
      clickMenuItem,
      menuExpanded,
      getSelectedKeys,
      headerMenuSelectKey,
    };
  },
});
</script>
