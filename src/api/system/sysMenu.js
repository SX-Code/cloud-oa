import { http } from '@/utils/http/axios';

const prefix = '/admin/system/sysMenu';

/**
 * @description: 获取菜单列表
 */
export function getMenuList() {
  return http.request({
    url: `${prefix}/findNodes`,
    method: 'get',
  });
}

/**
 * @description: 修改菜单
 */
export function updateMenu(menuItem) {
  return http.request(
    {
      url: `${prefix}`,
      method: 'put',
      data: menuItem,
    },
    {
      successMessageText: '修改成功',
    }
  );
}
