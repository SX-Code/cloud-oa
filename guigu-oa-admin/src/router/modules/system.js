const routeName = 'system';
import { Layout } from '@/router/constant';
import { WindowsFilled } from '@vicons/antd';
import { renderIcon } from '@/utils/index';

const routes = [
  {
    path: '/system',
    name: routeName,
    redirect: '/system/role',
    component: Layout,
    meta: {
      title: '系统管理',
      icon: renderIcon(WindowsFilled),
      permissions: ['system_role_list', 'system_user_list', 'system_menu_list'],
      sort: 1,
    },
    children: [
      {
        path: 'role',
        name: `${routeName}_role`,
        meta: {
          title: '角色管理',
          permissions: ['system_role_list'],
        },
        component: () => import('@/views/system/role/list.vue'),
      },
      {
        path: 'user',
        name: `${routeName}_user`,
        meta: {
          title: '用户管理',
          keepAlive: true,
          permissions: ['system_user_list'],
        },
        component: () => import('@/views/system/user/list.vue'),
      },
      {
        path: 'menu',
        name: `${routeName}_menu`,
        meta: {
          title: '菜单管理',
          keepAlive: true,
          permissions: ['system_menu_list'],
        },
        component: () => import('@/views/system/menu/menu.vue'),
      },
    ],
  },
];

export default routes;
