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
export function login(data) {
  return http.request(
    {
      url: `${prefix}/login`,
      method: 'POST',
      data,
    },
    {
      isTransformResponse: false,
    }
  );
}

/**
 * @description: 用户登出
 */
export function logout() {
  return http.request({
    url: `${prefix}/logout`,
    method: 'POST',
  });
}
