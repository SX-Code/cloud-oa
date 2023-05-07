<template>
  <div class="login">
    <div class="main">
      <h1 class="text">云尚办公</h1>
      <img alt="logo" src="../assets/logo.png" />
      <n-button
        type="primary"
        @click="handleLogin"
        size="large"
        :loading="loading"
        block
      >
        登陆
      </n-button>
    </div>
    <van-popup
      v-model:show="show"
      round
      position="bottom"
      :style="{ padding: '10px 20px' }"
    >
      <van-form @submit="onSubmit">
        <h4>绑定你的手机号，建立云尚办公系统关联关系</h4>
        <van-field
          v-model="bindPhoneVo.phone"
          required
          clearable
          label="手机号"
          right-icon="question-o"
          placeholder="请输入用户名"
          :rules="[{ required: true, message: '请填写手机号' }]"
          @click-right-icon="showToast('与系统录入手机号保持一致')"
        />
        <div style="margin: 16px">
          <van-button round block type="primary" native-type="submit">
            提交
          </van-button>
        </div>
      </van-form>
    </van-popup>
  </div>
</template>
<script setup>
import { getRouteQuery, redirectTo } from '@/hooks/usePage';
import { getUserInfo, jump2Auth, bind } from '@/api/user';
import { storage } from '@/utils/Storage';
import { ACCESS_TOKEN, LAND_PAGE } from '@/store/mutation-types';
import { ref } from 'vue';
import { reactive } from 'vue';

const show = ref(false);
const loading = ref(false);
const bindPhoneVo = reactive({
  openId: '',
  phone: '',
});
let code = getRouteQuery().code;
if (code) {
  loading.value = true;
  getUserInfo(code).then((res) => {
    const { token, openId } = res;
    if (!token && openId) {
      // 未绑定
      bindPhoneVo.openId = openId;
      show.value = true;
    } else {
      loading.value = false;
      // 记录token
      storage.set(ACCESS_TOKEN, res.token);
      const redirect = storage.get(LAND_PAGE) || '/';
      redirectTo(redirect);
    }
  });
}

function handleLogin() {
  loading.value = true;
  const token = storage.get(ACCESS_TOKEN);
  // 记录上一个页面地址
  const { redirect } = getRouteQuery();
  if (redirect) {
    storage.set(LAND_PAGE, redirect);
  }
  if (!token) {
    // 跳转授权
    const REDIRECT_URI = window.location.href;
    const REDIRECT_URI_EC = encodeURIComponent(REDIRECT_URI);
    jump2Auth(REDIRECT_URI_EC).then((res) => {
      window.location.replace(res.redirectUrl);
    });
  }
  // 点击返回进入的该页面，此时无code
  redirectTo(redirect);
}

const onSubmit = async () => {
  if (bindPhoneVo.phone.length != 11) {
    alert('手机号码格式不正确');
    return;
  }
  const res = await bind(bindPhoneVo);
  storage.set(ACCESS_TOKEN, res.token);
  show.value = false;
  const redirect = storage.get(LAND_PAGE) || '/';
  redirectTo(redirect);
};
</script>
<style scoped>
h1 {
  margin: 0;
  padding: 0;
}
.login {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: auto;
}
.login .main {
  flex: 1;
  max-width: 320px;
  min-width: 300px;
  margin: 40px auto;
  display: table-cell;
  vertical-align: middle;
  text-align: center;
}
.login .main .text {
  padding: 32px 0;
  text-align: center;
}
.login .main img {
  width: 80px;
  margin-bottom: 32px;
}
</style>
