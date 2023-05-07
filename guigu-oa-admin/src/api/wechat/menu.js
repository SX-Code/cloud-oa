import { http } from '@/utils/http/axios';

const prefix = '/admin/wechat/menu';

/**
 * @description: 同步菜单
 */
export function syncMenu() {
  return http.request(
    {
      url: `${prefix}/sync`,
      method: 'put',
    },
    {
      successMessageText: '菜单同步成功',
    }
  );
}

/**
 * @description: 删除菜单
 */
export function deleteMenu() {
  return http.request(
    {
      url: `${prefix}/delete`,
      method: 'delete',
    },
    {
      successMessageText: '菜单已删除',
    }
  );
}

/**
 * @description: 获取菜单列表
 */
export function getMenuList() {
  return http.request({
    url: `${prefix}`,
    method: 'get',
  });
}

/**
 * @description: 更新菜单信息
 */
export function updateMenu(data) {
  return http.request(
    {
      url: `${prefix}`,
      method: 'put',
      data,
    },
    {
      successMessageText: '更新成功',
    }
  );
}

/**
 * @description: 新增菜单信息
 */
export function addMenu(data) {
  return http.request(
    {
      url: `${prefix}`,
      method: 'post',
      data,
    },
    {
      successMessageText: '增加成功',
    }
  );
}
