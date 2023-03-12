import { PageEnum } from '@/enums/pageEnum';
import { useAsyncRouteStoreWidthOut } from '@/store/modules/asyncRoute';
import { useUserStoreWidthOut } from '@/store/modules/user';
import { ACCESS_TOKEN } from '@/store/mutation-types';
import { storage } from '@/utils/Storage';
import { isNavigationFailure } from 'vue-router';
import { ErrorPageRoute } from './base';

const LOGIN_PATH = PageEnum.BASE_LOGIN;

// 路由守卫白名单，即不进行重定向
const whitePathList = [LOGIN_PATH];

// 创建路由守卫
export function createRouterGuards(router) {
  const userStore = useUserStoreWidthOut();
  const asyncRouteStore = useAsyncRouteStoreWidthOut();
  router.beforeEach(async (to, from, next) => {
    const Loading = window['$loading'] || null;
    Loading && Loading.start();
    if (from.path === LOGIN_PATH && to.name === 'errorPage') {
      next(PageEnum.BASE_HOME);
      return;
    }

    // 白名单直接进入
    if (whitePathList.includes(to.path)) {
      next();
      return;
    }
    // 获取登录的TOKEN
    const token = storage.get(ACCESS_TOKEN);
    if (!token) {
      // You can access without permissions. You need to set the routing meta.ignoreAuth to true
      if (to.meta.ignoreAuth) {
        next();
        return;
      }
      // 重定向到登录页，带跳转前的路径
      const redirectData = {
        path: LOGIN_PATH,
        replace: true,
      };
      if (to.path) {
        redirectData.query = {
          ...redirectData.query,
          redirect: to.path,
        };
      }
      next(redirectData);
      return;
    }
    // 动态路由添加完成后，由此放行
    if (asyncRouteStore.getIsDynamicAddedRoute) {
      next();
      return;
    }

    const userInfo = await userStore.GetInfo();
    const routes = asyncRouteStore.generateRoutes(userInfo);
    // 动态添加可访问路由表, 将过滤之后的动态路由添加到路由表形成完整的路由
    routes.forEach((item) => {
      router.addRoute(item);
    });

    // 不需要动态添加404
    const isErrorPage = router
      .getRoutes()
      .findIndex((item) => item.name === ErrorPageRoute.name);
    if (isErrorPage === -1) {
      router.addRoute(ErrorPageRoute);
    }

    const redirectPath = from.query.redirect || to.path;
    const redirect = decodeURIComponent(redirectPath);
    // 解决动态路由白屏问题，https://blog.csdn.net/qq_41912398/article/details/109231418
    const nextData =
      to.path === redirect ? { ...to, replace: true } : { path: redirect };
    // 动态路由添加完成，放行的出口
    asyncRouteStore.setDynamicAddedRoute(true);
    next(nextData);
    Loading && Loading.finish();
  });

  router.afterEach((to, _, failure) => {
    document.title = to?.meta?.title || document.title;
    if (isNavigationFailure(failure)) {
      //console.log('failed navigation', failure)
    }
    const asyncRouteStore = useAsyncRouteStoreWidthOut();
    // 在这里设置需要缓存的组件名称
    const keepAliveComponents = asyncRouteStore.keepAliveComponents;
    const currentComName = to.matched.find(
      (item) => item.name == to.name
    )?.name;
    if (
      currentComName &&
      !keepAliveComponents.includes(currentComName) &&
      to.meta?.keepAlive
    ) {
      // 需要缓存的组件
      keepAliveComponents.push(currentComName);
    } else if (!to.meta?.keepAlive || to.name == 'Redirect') {
      // 不需要缓存的组件
      const index = asyncRouteStore.keepAliveComponents.findIndex(
        (name) => name == currentComName
      );
      if (index != -1) {
        keepAliveComponents.splice(index, 1);
      }
    }
    asyncRouteStore.setKeepAliveComponents(keepAliveComponents);
    const Loading = window['$loading'] || null;
    Loading && Loading.finish();
  });

  router.onError((error) => {
    console.log(error, '路由错误');
  });
}
