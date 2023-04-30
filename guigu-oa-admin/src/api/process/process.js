import { http } from '@/utils/http/axios';

const prefix = '/admin/process/';

/**
 * @description: 获取审批模板列表-条件分页
 */
export function pageQueryProcess({ page, pageSize }, params) {
  return http.request({
    url: `${prefix}/${page}/${pageSize}`,
    method: 'GET',
    params,
  });
}
