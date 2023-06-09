<template>
  <div class="view-account">
    <div class="view-account-header"></div>
    <div class="view-account-container">
      <div class="view-account-top">
        <div class="view-account-top-logo">
          <h1 class="text-5xl">欢迎登录</h1>
        </div>
        <div class="view-account-top-desc">{{ websiteConfig.loginDesc }}</div>
      </div>
      <div class="view-account-form">
        <n-form
          ref="formRef"
          label-placement="left"
          size="large"
          :model="formInline"
          :rules="rules"
        >
          <n-form-item path="username">
            <n-input
              v-model:value="formInline.username"
              placeholder="请输入用户名"
            >
              <template #prefix>
                <n-icon size="18" color="#808695">
                  <PersonOutline />
                </n-icon>
              </template>
            </n-input>
          </n-form-item>
          <n-form-item path="password">
            <n-input
              v-model:value="formInline.password"
              type="password"
              showPasswordOn="click"
              placeholder="请输入密码"
            >
              <template #prefix>
                <n-icon size="18" color="#808695">
                  <LockClosedOutline />
                </n-icon>
              </template>
            </n-input>
          </n-form-item>
          <n-form-item class="default-color">
            <div class="flex justify-between">
              <div class="flex-initial">
                <n-checkbox v-model:checked="autoLogin">自动登录</n-checkbox>
              </div>
            </div>
          </n-form-item>
          <n-form-item>
            <n-button
              type="primary"
              @click="handleSubmit"
              size="large"
              :loading="loading"
              block
            >
              登录
            </n-button>
          </n-form-item>
        </n-form>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref, defineComponent } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '@/store/modules/user';
import { useMessage } from 'naive-ui';
import { ResultEnum } from '@/enums/httpEnum';
import { PersonOutline, LockClosedOutline } from '@vicons/ionicons5';
import { PageEnum } from '@/enums/pageEnum';
import { websiteConfig } from '@/config/website.config';
export default defineComponent({
  components: { PersonOutline, LockClosedOutline },
  setup() {
    const formRef = ref();
    const message = useMessage();
    const loading = ref(false);
    const autoLogin = ref(true);
    const LOGIN_NAME = PageEnum.BASE_LOGIN_NAME;
    const formInline = reactive({
      username: 'admin',
      password: '123456',
    });
    const rules = {
      username: { required: true, message: '请输入用户名', trigger: 'blur' },
      password: { required: true, message: '请输入密码', trigger: 'blur' },
    };
    const userStore = useUserStore();
    const router = useRouter();
    const route = useRoute();
    const handleSubmit = (e) => {
      e.preventDefault();
      formRef.value.validate(async (errors) => {
        if (!errors) {
          const { username, password } = formInline;
          message.loading('登录中...');
          loading.value = true;

          const params = {
            username,
            password,
          };
          try {
            const { code, message: msg } = await userStore.login(params);
            message.destroyAll();
            if (code == ResultEnum.SUCCESS) {
              const toPath = decodeURIComponent(route.query?.redirect || '/');
              message.success('登录成功，即将进入系统');
              if (route.name === LOGIN_NAME) {
                router.replace('/');
              } else router.replace(toPath);
            } else {
              message.error(msg || '登录失败');
            }
          } finally {
            loading.value = false;
          }
        } else {
          message.error('请填写完整信息，并且进行验证码校验');
        }
      });
    };
    return {
      rules,
      loading,
      formRef,
      autoLogin,
      formInline,
      handleSubmit,
      websiteConfig,
    };
  },
});
</script>
<style lang="less" scoped>
.view-account {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: auto;

  &-container {
    flex: 1;
    padding: 32px 12px;
    max-width: 384px;
    min-width: 320px;
    margin: 0 auto;
  }

  &-top {
    padding: 32px 0;
    text-align: center;

    &-desc {
      font-size: 14px;
      color: #808695;
    }
  }

  &-other {
    width: 100%;
  }

  .default-color {
    color: #515a6e;

    .ant-checkbox-wrapper {
      color: #515a6e;
    }
  }
}

@media (min-width: 768px) {
  .view-account {
    background-image: url('../../assets/images/login.svg');
    background-repeat: no-repeat;
    background-position: 50%;
    background-size: 100%;
  }

  .page-account-container {
    padding: 32px 0 24px 0;
  }
}
</style>
