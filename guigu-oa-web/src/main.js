import { createApp } from 'vue';
import App from './App.vue';
import { setupNaive, setupNaiveDiscreteApi } from './plugins';
import router, { setupRouter } from './router';
import formCreate from '@form-create/naive-ui';
import install from '@form-create/naive-ui/auto-import';
import { setupVant } from './plugins/vant';

async function bootstrap() {
  const app = createApp(App);

  // 注册全局常用的 naive-ui 组件
  setupNaive(app);

  setupVant(app);

  // 挂载路由
  setupRouter(app);

  // 挂载 naive-ui 脱离上下文的 Api
  setupNaiveDiscreteApi();

  // 路由准备就绪后挂载 APP 实例
  // https://router.vuejs.org/api/interfaces/router.html#isready
  await router.isReady();

  formCreate.use(install);
  app.use(formCreate);

  app.mount('#app', true);
}

bootstrap();
