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

/**
 * @description: 根据ID删除菜单
 */
export function removeById(id) {
  return http.request(
    {
      url: `${prefix}/${id}`,
      method: 'DELETE',
    },
    {
      successMessageText: '删除成功',
    }
  );
}

/**
 * @description: 获取某个角色的菜单权限列表
 */
export function toAssign(roleId) {
  return http.request({
    url: `${prefix}/toAssign/${roleId}`,
    method: 'get',
  });
}

/**
 * @description: 获取某个角色的菜单权限ID列表
 */
export function getRoleMenuIdList(roleId) {
  return http.request({
    url: `${prefix}/menuId/${roleId}`,
    method: 'get',
  });
}

/**
 * @description: 获取某个角色的菜单操作权限ID列表
 */
export function getRoleMenuIdTypeList(roleId) {
  return http.request({
    url: `${prefix}/menuIdType/${roleId}`,
    method: 'get',
  });
}

/**
 * @description: 获取某个角色的菜单权限ID列表
 */
export function assignMenu(data) {
  return http.request({
    url: `${prefix}/doAssign`,
    method: 'post',
    data,
  });
}
