import router from '@/router';

/**
 * 重定向
 * @param path 路径
 */
export function redirectTo(path) {
  const { replace } = router;
  replace({
    path,
  });
}
/**
 * 获取路由上query参数
 */
export function getRouteQuery() {
  let url = decodeURIComponent(window.location.href);
  if (url.includes('#')) {
    const split = url.split('#');
    return { ...queryParse(split[0]), ...queryParse(split[1]) };
  } else {
    return queryParse(url);
  }
}

/**
 * 从url中提取所有参数
 * @param {*} url
 * @returns
 */
function queryParse(url) {
  var paramObj = {};
  if (url.indexOf('?') > -1) {
    var _url = url.split('?')[1];
    var strArr = _url.split('&');
    strArr.forEach((ele) => {
      var key = ele.split('=')[0];
      var val = ele.split('=')[1];
      paramObj[key] = val;
    });
  }
  return paramObj;
}
