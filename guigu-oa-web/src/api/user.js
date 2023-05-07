import { http } from '@/utils/http/axios';

const prefix = '/admin/wechat';

/**
 * @description: 获得用户信息
 */
export function getUserInfo(code) {
  return http.request({
    url: `${prefix}/userInfo`,
    method: 'get',
    params: {
      code,
    },
  });
}

/**
 * 获取微信授权的跳转地址
 * @param callbackUrl 授权后回调链接
 */
export function jump2Auth(callbackUrl) {
  return http.request({
    url: `${prefix}/authorize`,
    method: 'get',
    params: {
      redirect_url: callbackUrl,
    },
  });
}

/**
 * @description: 绑定openid
 */
export function bind(data) {
  return http.request({
    url: `${prefix}/bind`,
    method: 'post',
    data,
  });
}

/**
 * @description: 解除绑定
 */
export function unbind(id) {
  return http.request({
    url: `${prefix}/unbind`,
    method: 'post',
    id,
  });
}
