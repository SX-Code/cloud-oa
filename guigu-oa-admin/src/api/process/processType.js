import { http } from '@/utils/http/axios';

const prefix = '/admin/process/processType';

/**
 * @description: 获取审批类型列表
 */
export function listProcessType() {
  return http.request({
    url: `${prefix}`,
    method: 'GET',
  });
}

/**
 * @description: 获取审批类型列表-分页
 */
export function pageProcessType({ page, pageSize }) {
  return http.request({
    url: `${prefix}/${page}/${pageSize}`,
    method: 'GET',
  });
}

/**
 * @description: 获取审批类型
 */
export function getProcessType(id) {
  return http.request({
    url: `${prefix}/${id}`,
    method: 'GET',
  });
}

/**
 * @description: 新增审批类型
 */
export function addProcessType(data) {
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
 * @description: 更新审批类型
 */
export function updateProcessType(data) {
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
 * @description: 删除审批类型
 */
export function removeProcessType(id) {
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
