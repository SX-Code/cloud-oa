import { asyncRoutes, constantRouter } from '@/router';
import { defineStore } from 'pinia';
import { toRaw, unref } from 'vue';
import { store } from '@/store';
import { useProjectSetting } from '@/hooks/setting/useProjectSetting';

const DEFAULT_CONFIG = {
  id: 'id',
  children: 'children',
  pid: 'pid',
};
const getConfig = (config) => Object.assign({}, DEFAULT_CONFIG, config);

function filter(tree, func, config = {}) {
  config = getConfig(config);
  const children = config.children;

  function listFilter(list) {
    return list
      .map((node) => ({ ...node }))
      .filter((node) => {
        node[children] = node[children] && listFilter(node[children]);
        return func(node) || (node[children] && node[children].length);
      });
  }
  return listFilter(tree);
}

export const useAsyncRouteStore = defineStore({
  id: 'app-async-route',
  state: () => ({
    menus: [],
    routers: constantRouter,
    addRouters: [],
    keepAliveComponents: [],
    // Whether the route has been dynamically added
    isDynamicAddedRoute: false,
  }),
  getters: {
    getMenus() {
      return this.menus;
    },
    getIsDynamicAddedRoute() {
      return this.isDynamicAddedRoute;
    },
  },
  actions: {
    getRouters() {
      return toRaw(this.addRouters);
    },
    setDynamicAddedRoute(added) {
      this.isDynamicAddedRoute = added;
    },
    // 设置动态路由
    setRouters(routers) {
      this.addRouters = routers;
      this.routers = constantRouter.concat(routers);
    },
    setMenus(menus) {
      // 设置动态路由
      this.menus = menus;
    },
    setKeepAliveComponents(compNames) {
      // 设置需要缓存的组件
      this.keepAliveComponents = compNames;
    },
    generateRoutes(data) {
      let accessedRouters;
      const permissionsList = data.permissions || [];
      const routeFilter = (route) => {
        const { meta } = route;
        const { permissions } = meta || {};
        if (!permissions) return true;
        return permissionsList.some((item) => permissions.includes(item.value));
      };
      const { getPermissionMode } = useProjectSetting();
      const permissionMode = unref(getPermissionMode);
      if (permissionMode === 'BACK') {
        // 从后端获取数据，动态生成菜单
      } else {
        try {
          // 过滤账户是否拥有某一个权限，并将菜单从加载列表移除
          accessedRouters = filter(asyncRoutes, routeFilter);
        } catch (error) {
          console.log(error);
        }
      }
      accessedRouters = accessedRouters.filter(routeFilter);
      this.setRouters(accessedRouters);
      this.setMenus(accessedRouters);
      return toRaw(accessedRouters);
    },
  },
});
// Need to be used outside the setup
export function useAsyncRouteStoreWidthOut() {
  return useAsyncRouteStore(store);
}
