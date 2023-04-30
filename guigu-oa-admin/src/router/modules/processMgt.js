const routeName = 'process';
import { Layout } from '@/router/constant';
import { FileProtectOutlined } from '@vicons/antd';
import { renderIcon } from '@/utils/index';

const routes = [
  {
    path: '/processMgt',
    name: `${routeName}Mgt`,
    redirect: '/processMgt/list',
    component: Layout,
    meta: {
      title: '审批管理',
      icon: renderIcon(FileProtectOutlined),
      permissions: ['process_list'],
      sort: 3,
      alwaysShow: true,
    },
    children: [
      {
        path: 'list',
        name: `${routeName}_list`,
        meta: {
          title: '审批列表',
          permissions: ['process_list'],
        },
        component: () => import('@/views/process/list/process.vue'),
      },
    ],
  },
];

export default routes;
