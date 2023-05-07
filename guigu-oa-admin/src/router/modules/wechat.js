const routeName = 'wechat';
import { Layout } from '@/router/constant';
import { WechatFilled } from '@vicons/antd';
import { renderIcon } from '@/utils/index';

const routes = [
  {
    path: '/wechat',
    name: routeName,
    redirect: '/wechat/menu',
    component: Layout,
    meta: {
      title: '公众号菜单',
      icon: renderIcon(WechatFilled),
      permissions: ['wechat_menu_list'],
      alwaysShow: true,
      sort: 5,
    },
    children: [
      {
        path: 'menu',
        name: `${routeName}_menu`,
        meta: {
          title: '菜单列表',
          permissions: ['wechat_menu_list'],
        },
        component: () => import('@/views/wechat/menu/menu.vue'),
      },
    ],
  },
];

export default routes;
