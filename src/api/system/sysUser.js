import { http } from '@/utils/http/axios';

const prefix = '/admin/system/sysUser';

/**
 * @description: 获取用户列表-分页
 */
export function getPageList({ page, pageSize }, params) {
  return http.request({
    url: `${prefix}/${page}/${pageSize}`,
    method: 'get',
    params,
  });
}

/**
 * @description: 根据ID删除用户
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
 * @description: 添加用户
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
 * @description: 更新用户
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
 * @description: 批量删除用户
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
 * @description: 更改角色状态
 */
export function updateStatus(id, status) {
  return http.request(
    {
      url: `${prefix}/updateStatus/${id}/${status}`,
      method: 'get',
    },
    {
      successMessageText: '状态更改成功',
    }
  );
}
