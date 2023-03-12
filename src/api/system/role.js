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
