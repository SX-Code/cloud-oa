import { useUserStore } from '@/store/modules/user';

/**
 * 权限检查方法，用于判断当前用户是否有查看或者操作的权限
 * @returns
 */
export function usePermission() {
  const userStore = useUserStore();

  /**
   * 检查权限
   * @param  accesses
   */
  function _somePermission(accesses) {
    return userStore.getPermissions.some((item) => {
      const { value } = item;
      return accesses.includes(value);
    });
  }

  /**
   * 判断是否存在权限
   * 可用于 v-if 显示逻辑
   */
  function hasPermission(accesses) {
    if (!accesses || !accesses.length) return true;
    return _somePermission(accesses);
  }

  /**
   * 是否包含指定的所有权限
   * @param accesses
   */
  function hasEveryPermission(accesses) {
    const permissionList = userStore.getPermissions;
    if (Array.isArray(accesses)) {
      return permissionList.every((access) => accesses.includes(access.value));
    }
    throw new Error(`[hasEveryPermission]: ${accesses} show be a array !`);
  }

  /**
   * 是否包含其中某个权限
   * @param accesses
   * @param accessMap
   */
  function hasSomePermission(accesses) {
    const permissionList = userStore.getPermissions;
    if (Array.isArray(accesses)) {
      return permissionList.some((access) => accesses.includes(access.value));
    }
    throw new Error(`[hasSomePermission]: ${accesses} should be a array !`);
  }

  return { hasPermission, hasEveryPermission, hasSomePermission };
}
