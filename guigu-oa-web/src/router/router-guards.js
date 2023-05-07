import { ACCESS_TOKEN } from '@/store/mutation-types';
import { storage } from '@/utils/Storage';

export function createRouterGuards(router) {
  router.beforeEach((to) => {
    if (to.meta.requireAuth && !storage.get(ACCESS_TOKEN)) {
      return {
        path: '/login',
        query: { redirect: to.fullPath },
      };
    }
  });
}
