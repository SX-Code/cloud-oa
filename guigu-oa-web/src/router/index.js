import { createRouter, createWebHashHistory } from 'vue-router';
import { createRouterGuards } from './router-guards';

const routes = [
  {
    path: '/',
    name: 'Process',
    component: () => import('../views/process/process.vue'),
    meta: {
      title: '审批',
      requireAuth: true,
    },
  },
  {
    path: '/apply/:processTemplateId',
    name: 'Apply',
    component: () => import('../views/apply/apply.vue'),
    meta: {
      title: '发起审批',
      requireAuth: true,
    },
  },
  {
    path: '/list/:activeIndex',
    name: 'List',
    component: () => import('../views/list/list.vue'),
    meta: {
      title: '审批列表',
      requireAuth: true,
    },
  },
  {
    path: '/show/:id/:taskId/:activeIndex',
    name: 'Show',
    component: () => import('../views/show/show.vue'),
    meta: {
      title: '审批详情',
      requireAuth: true,
    },
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/AboutView.vue'),
  },
  {
    path: '/debug',
    name: 'debug',
    component: () => import('../views/debug/debug.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login.vue'),
  },
];

const router = createRouter({
  history: createWebHashHistory('/oa/'),
  routes,
});

export function setupRouter(app) {
  app.use(router);
  createRouterGuards(router);
}

export default router;
