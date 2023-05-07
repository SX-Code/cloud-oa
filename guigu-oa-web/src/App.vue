<template>
  <div>
    <NConfigProvider :locale="zhCN" :date-locale="dateZhCN">
      <AppProvider>
        <RouterView />
      </AppProvider>
    </NConfigProvider>
  </div>
</template>
<script setup>
import { AppProvider } from '@/components/Application';
import { zhCN, dateZhCN } from 'naive-ui';
// import { ref, onMounted, reactive } from 'vue';
// import { showToast } from 'vant';
// import { storage } from '@/utils/Storage';
// import { ACCESS_TOKEN } from './store/mutation-types';
// import { bind } from '@/api/user';

// const show = ref(false);
// const bindPhoneVo = reactive({
//   openId: '',
//   phone: '',
// });

// onMounted(() => {
// wechatLogin();
// });

// const onSubmit = async () => {
//   if (bindPhoneVo.phone.length != 11) {
//     alert('手机号码格式不正确');
//     return;
//   }
//   const res = await bind(bindPhoneVo);
//   storage.set(ACCESS_TOKEN, res.token);
//   show.value = false;
//   location.reload();
//   // window.location = 'http://uhkbdk.natappfree.cc/oa/#';
// };

// const wechatLogin = () => {
//   let token = getQueryString('token') || '';
//   let openId = getQueryString('openId') || '';
//   if (token === '' && openId != '') {
//     // 未绑定账号
//     bindPhoneVo.openId = openId;
//     show.value = true;
//   } else {
//     // 如果绑定了，授权登录直接返回token
//     if (token !== '') {
//       const ex = 7 * 24 * 60 * 60;
//       storage.set(ACCESS_TOKEN, token, ex);
//     }
//     token = storage.get(ACCESS_TOKEN);
//     if (token == null || token == '') {
//       let url = window.location.href.replace('#', 'guiguoa');
//       window.location =
//         'http://swcodeoa.free.svipss.top/admin/wechat/authorize?returnUrl=' +
//         url;
//     }
//   }
// };

// function getQueryString(paramName) {
//   if (window.location.href.indexOf('?') == -1) return '';

//   let searchString = window.location.href.split('?')[1];
//   let i,
//     val,
//     params = searchString.split('&');

//   for (i = 0; i < params.length; i++) {
//     val = params[i].split('=');
//     if (val[0] == paramName) {
//       return val[1];
//     }
//   }
//   return '';
// }
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
.container {
  background-color: #eff2f5;
}
.main {
  margin-top: 15px;
  padding: 15px 20px 0px 20px;
  background-color: #ffffff;
}
</style>
