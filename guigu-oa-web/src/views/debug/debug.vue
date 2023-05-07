<template>
  <div class="debug">
    <n-space vertical style="text-align: center">
      <n-button strong secondary type="warning" @click="clearToken">
        清除缓存
      </n-button>
      <n-button
        strong
        secondary
        type="warning"
        :disabled="!token"
        @click="unBind"
      >
        解除绑定
      </n-button>
      <n-input
        type="textarea"
        :value="token"
        placeholder="已清空"
        :disabled="true"
        style="width: 300px"
      />
    </n-space>
  </div>
</template>
<script>
export default {
  name: 'Debug',
};
</script>
<script setup>
import { ACCESS_TOKEN } from '@/store/mutation-types';
import { storage } from '@/utils/Storage';
import { unbind } from '@/api/user';
import { showToast } from 'vant';
import { ref } from 'vue';
import { useDialog } from 'naive-ui';

const dialog = useDialog();
const token = ref(storage.get(ACCESS_TOKEN));

const unBind = () => {
  dialog.warning({
    title: '警告',
    content: '你确定解除绑定，继续操作？',
    positiveText: '确定',
    negativeText: '不确定',
    onPositiveClick: async () => {
      await unbind();
      clearToken();
    },
    onNegativeClick: () => {},
  });
};

function clearToken() {
  storage.remove(ACCESS_TOKEN);
  token.value = storage.get(ACCESS_TOKEN);
  showToast('删除成功');
}
</script>
<style scoped>
.debug {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: auto;
  align-items: center;
  justify-content: center;
}
</style>
