import { renderIcon } from '@/utils';
import { ProjectOutlined } from '@vicons/antd';

const routes = [
  {
    path: '/about',
    name: 'about',
    component: () => import('@/layout/index.vue'),
    meta: {
      sort: 10,
      isRoot: true,
      activeMenu: 'about_index',
      icon: renderIcon(ProjectOutlined),
    },
    children: [
      {
        path: 'index',
        name: 'about_index',
        meta: {
          title: '关于项目',
          activeMenu: 'about_index',
        },
        component: () => import('@/views/about/index.vue'),
      },
    ],
  },
];

export default routes;
