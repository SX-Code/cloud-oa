const routeName = 'system';
import { Layout } from '@/router/constant';
import { DashboardOutlined } from '@vicons/antd';
import { renderIcon } from '@/utils/index';

const routes = [
  {
    path: '/system',
    name: routeName,
    redirect: '/system/role',
    component: Layout,
    meta: {
      title: '系统管理',
      icon: renderIcon(DashboardOutlined),
      permissions: ['system_role', 'system_user'],
      sort: 1,
    },
    children: [
      {
        path: 'role',
        name: `${routeName}_role`,
        meta: {
          title: '角色管理',
          permissions: ['system_role'],
        },
        component: () => import('@/views/system/role/list.vue'),
      },
      {
        path: 'user',
        name: `${routeName}_user`,
        meta: {
          title: '用户管理',
          keepAlive: true,
          permissions: ['system_user'],
        },
        component: () => import('@/views/system/user/list.vue'),
      },
      {
        path: 'menu',
        name: `${routeName}_menu`,
        meta: {
          title: '菜单管理',
          keepAlive: true,
          permissions: ['system_menu'],
        },
        component: () => import('@/views/system/menu/menu.vue'),
      },
    ],
  },
];

export default routes;
