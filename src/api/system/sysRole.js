import { http } from '@/utils/http/axios';

const prefix = '/admin/system/sysRole';

/**
 * @description: 获取角色列表-分页
 */
export function getPageList({ page, pageSize }, params) {
  return http.request({
    url: `${prefix}/${page}/${pageSize}`,
    method: 'get',
    params,
  });
}

/**
 * @description: 根据ID删除角色
 */
export function removeById(id) {
  return http.request(
    {
      url: `${prefix}/${id}`,
      method: 'delete',
    },
    {
      successMessageText: '删除成功',
    }
  );
}

/**
 * @description: 添加角色
 */
export function addRole(data) {
  return http.request(
    {
      url: `${prefix}`,
      method: 'POST',
      data,
    },
    {
      successMessageText: '添加成功',
    }
  );
}

/**
 * @description: 更新角色
 */
export function updateRole(data) {
  return http.request(
    {
      url: `${prefix}`,
      method: 'PUT',
      data,
    },
    {
      successMessageText: '更改成功',
    }
  );
}

/**
 * @description: 批量删除角色
 */
export function batchRemove(ids) {
  return http.request(
    {
      url: `${prefix}/batch`,
      method: 'DELETE',
      data: ids,
    },
    {
      successMessageText: '批量删除成功',
    }
  );
}

/**
 * @description: 获取所有角色和当前用户所属角色
 */
export function getRoles(adminId) {
  return http.request({
    url: `${prefix}/toAssign/${adminId}`,
    method: 'get',
  });
}

/**
 * @description: 分配角色
 */
export function assignRoles(data) {
  return http.request(
    {
      url: `${prefix}/doAssign`,
      method: 'post',
      data,
    },
    {
      successMessageText: '分配成功',
    }
  );
}
