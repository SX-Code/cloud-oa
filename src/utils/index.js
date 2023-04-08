import { PageEnum } from '@/enums/pageEnum';
import { NIcon } from 'naive-ui';
import { h, unref } from 'vue';
import { isObject } from './is';

/**
 * render 图标
 * */
export function renderIcon(icon) {
  return () => h(NIcon, null, { default: () => h(icon) });
}
/**
 * 递归组装菜单格式
 */
export function generatorMenu(routerMap) {
  return filterRouter(routerMap).map((item) => {
    const isRoot = isRootRouter(item);
    const info = isRoot ? item.children[0] : item;
    const currentMenu = {
      ...info,
      ...info.meta,
      label: info.meta?.title,
      key: info.name,
      icon: isRoot ? item.meta?.icon : info.meta?.icon,
    };
    // 是否有子菜单，并递归处理
    if (info.children && info.children.length > 0) {
      // 递归
      currentMenu.children = generatorMenu(info.children);
    }
    return currentMenu;
  });
}

/**
 * 判断根路由 Router
 * */
export function isRootRouter(item) {
  return (
    item.meta?.alwaysShow != true &&
    item?.children?.filter((item) => !item?.meta?.hidden)?.length === 1
  );
}
/**
 * 排除Router
 */
export function filterRouter(routerMap) {
  return routerMap.filter((item) => {
    return (
      (item.meta?.hidden || false) != true &&
      !['/:path(.*)*', '/', PageEnum.REDIRECT, PageEnum.BASE_LOGIN].includes(
        item.path
      )
    );
  });
}
/**
 * Sums the passed percentage to the R, G or B of a HEX color
 * @param {string} color The color to change
 * @param {number} amount The amount to change the color by
 * @returns {string} The processed part of the color
 */
function addLight(color, amount) {
  const cc = parseInt(color, 16) + amount;
  const c = cc > 255 ? 255 : cc;
  return c.toString(16).length > 1 ? c.toString(16) : `0${c.toString(16)}`;
}

/**
 * Lightens a 6 char HEX color according to the passed percentage
 * @param {string} color The color to change
 * @param {number} amount The amount to change the color by
 * @returns {string} The processed color represented as HEX
 */
export function lighten(color, amount) {
  color = color.indexOf('#') >= 0 ? color.substring(1, color.length) : color;
  amount = Math.trunc((255 * amount) / 100);
  return `#${addLight(color.substring(0, 2), amount)}${addLight(
    color.substring(2, 4),
    amount
  )}${addLight(color.substring(4, 6), amount)}`;
}

export function deepMerge(src = {}, target = {}) {
  let key;
  for (key in target) {
    src[key] = isObject(src[key])
      ? deepMerge(src[key], target[key])
      : (src[key] = target[key]);
  }
  return src;
}

// dynamic use hook props
export function getDynamicProps(props) {
  const ret = {};

  Object.keys(props).map((key) => {
    ret[key] = unref(props[key]);
  });

  return ret;
}

/**
 *  找到对应的节点
 * */
let result = null;
export function getTreeItem(data, key) {
  data.map((item) => {
    if (item.key === key) {
      result = item;
    } else {
      if (item.children && item.children.length) {
        getTreeItem(item.children, key);
      }
    }
  });
  return result;
}

/**
 *  找到所有节点
 * */
const treeAll = [];
export function getTreeAll(data) {
  data.map((item) => {
    treeAll.push(item.key);
    if (item.children && item.children.length) {
      getTreeAll(item.children);
    }
  });
  return treeAll;
}

export function transformTree(treeMenuList, isCheckedKeys = false) {
  const checkedKeys = [];
  treeMenuList.map((item) => {
    item['key'] = item.id;
    if (isCheckedKeys && item['isSelect']) checkedKeys.push(item.id);
    if (item.children && item.children.length) {
      transformTree(item.children);
    } else {
      item.children = null;
    }
  });
  treeMenuList = JSON.parse(
    JSON.stringify(treeMenuList).replace(/\btitle/g, 'label')
  );
  return { treeMenuList, checkedKeys };
}

/**
 * 转化后端的菜单数据以符合Naive UI
 */
export function transformTreeDepth2(treeMenuList, isCheckedKeys = false) {
  const checkedKeys = [];
  treeMenuList.map((item) => {
    if (item.children && item.children.length) {
      item.children.map((citem) => {
        citem['key'] = citem.id;
        citem['operation'] = citem.children;
        citem['children'] = null;
        if (isCheckedKeys && citem['isSelect']) checkedKeys.push(citem.id);
      });
    }
    item['key'] = item.id;
    if (isCheckedKeys && item['isSelect']) checkedKeys.push(item.id);
  });
  treeMenuList = JSON.parse(
    JSON.stringify(treeMenuList).replace(/\btitle/g, 'label')
  );
  return { treeMenuList, checkedKeys };
}
