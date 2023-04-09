import { storage } from '@/utils/Storage';
import { defineStore } from 'pinia';
import { store } from '@/store';
import { ACCESS_TOKEN, CURRENT_USER } from '../mutation-types';
import { getUserInfo, login } from '@/api/user';
import { ResultEnum } from '@/enums/httpEnum';

export const useUserStore = defineStore({
  id: 'app-user',
  state: () => ({
    token: storage.get(ACCESS_TOKEN, ''),
    username: '',
    welcome: '',
    avatar: '',
    permissions: [],
    info: storage.get(CURRENT_USER, {}),
  }),
  getters: {
    getToken() {
      return this.token;
    },
    getAvatar() {
      return this.avatar;
    },
    getNickname() {
      return this.username;
    },
    getPermissions() {
      return this.permissions;
    },
    getUserInfo() {
      return this.info;
    },
  },
  actions: {
    setToken(token) {
      this.token = token;
    },
    setAvatar(avatar) {
      this.avatar = avatar;
    },
    setPermissions(permissions) {
      this.permissions = permissions;
    },
    setUserInfo(info) {
      this.info = info;
    },
    // 登录
    async login(userInfo) {
      try {
        const response = await login(userInfo);
        const { data: result, code } = response;
        if (code === ResultEnum.SUCCESS) {
          const ex = 7 * 24 * 60 * 60;
          storage.set(ACCESS_TOKEN, result.token, ex);
          this.setToken(result.token);
        }
        return Promise.resolve(response);
      } catch (e) {
        return Promise.reject(e);
      }
    },

    // 获取用户信息
    GetInfo() {
      const that = this;
      return new Promise((resolve, reject) => {
        getUserInfo()
          .then((res) => {
            const result = res;
            if (result.permissions && result.permissions.length) {
              const ex = 7 * 24 * 60 * 60;
              storage.set(CURRENT_USER, result, ex);
              const permissionsList = result.permissions;
              that.setPermissions(permissionsList);
              that.setUserInfo(result);
              this.setUserInfo(result);
            } else {
              reject(
                new Error('getInfo: permissionsList must be a non-null array !')
              );
            }
            that.setAvatar(result.avatar);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 登出
    async logout() {
      this.setPermissions([]);
      this.setUserInfo('');
      storage.remove(ACCESS_TOKEN);
      storage.remove(CURRENT_USER);
      return Promise.resolve('');
    },
  },
});

// Need to be used outside the setup
export function useUserStoreWidthOut() {
  return useUserStore(store);
}
