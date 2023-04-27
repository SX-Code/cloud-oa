const routeName = 'process';
import { Layout } from '@/router/constant';
import { SettingOutlined } from '@vicons/antd';
import { renderIcon } from '@/utils/index';

const routes = [
  {
    path: '/process',
    name: routeName,
    redirect: '/process/type',
    component: Layout,
    meta: {
      title: '审批设置',
      icon: renderIcon(SettingOutlined),
      permissions: ['process_type'],
      sort: 2,
    },
    children: [
      {
        path: 'type',
        name: `${routeName}_type`,
        meta: {
          title: '审批类型',
          permissions: ['process_type'],
        },
        component: () => import('@/views/process/type/type.vue'),
      },
      {
        path: 'template',
        name: `${routeName}_template`,
        meta: {
          title: '审批模板',
          permissions: ['process_template'],
        },
        component: () => import('@/views/process/template/template.vue'),
      },
      {
        path: 'set',
        name: `${routeName}_template_set`,
        hidden: true,
        meta: {
          title: '审批模版设置',
          hidden: true,
          alwaysShow: false,
          permissions: ['process_template_set'],
        },
        component: () => import('@/views/process/set/set.vue'),
      },
    ],
  },
];

export default routes;
