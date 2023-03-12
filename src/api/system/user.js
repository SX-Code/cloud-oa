import { http } from '@/utils/http/axios';

const prefix = '/admin/system/index';

/**
 * @description: 获取用户信息
 */
export function getUserInfo() {
  return http.request({
    url: `${prefix}/info`,
    method: 'get',
  });
}

/**
 * @description: 用户登录
 */
export function login(params) {
  return http.request(
    {
      url: `${prefix}/login`,
      method: 'POST',
      params,
    },
    {
      isTransformResponse: false,
    }
  );
}

/**
 * @description: 用户登出
 */
export function logout(params) {
  return http.request({
    url: `${prefix}/logout`,
    method: 'POST',
    params,
  });
}
