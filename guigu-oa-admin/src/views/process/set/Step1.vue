<template>
  <n-form
    :label-width="90"
    :model="formValue"
    :rules="rules"
    label-placement="left"
    ref="formRef"
    style="max-width: 500px; margin: 40px auto"
  >
    <n-form-item label="审批类型" path="processTypeId">
      <n-select
        v-model:value="formValue.processTypeId"
        placeholder="请选择审批类型"
        :options="typeOptions"
        clearable
        style="width: 300px"
      />
    </n-form-item>
    <n-form-item label="审批图标" path="iconUrl">
      <n-select
        v-model:value="formValue.iconUrl"
        placeholder="请选择审批图标"
        :options="iconOptions"
        :render-label="renderLabel"
        :render-tag="renderSingleSelectTag"
        clearable
        remote
        style="width: 300px"
      />
    </n-form-item>
    <n-form-item label="审批名称" path="name">
      <n-input v-model:value="formValue.name" placeholder="请输入审批名称" />
    </n-form-item>
    <n-form-item label="描述" path="description">
      <n-input
        v-model:value="formValue.description"
        placeholder="描述"
        type="textarea"
        :autosize="{
          minRows: 3,
          maxRows: 5,
        }"
      />
    </n-form-item>
    <div style="margin-left: 90px">
      <n-space>
        <n-button type="default" ghost @click="back">返回</n-button>
        <n-button type="primary" @click="formSubmit">下一步</n-button>
      </n-space>
    </div>
  </n-form>
</template>
<script>
import { useMessage } from 'naive-ui';
import { defineComponent, ref } from 'vue';

import { renderLabel, renderSingleSelectTag, icons } from './render';
const rules = {
  processTypeId: {
    type: 'number',
    required: true,
    trigger: ['blur', 'change'],
    message: '请选择审批类型',
  },
  name: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入审批名称',
  },
  description: {
    trigger: ['blur', 'input'],
    message: '最大长度不能超过255',
    validator: (...[, value]) => {
      return value?.length < 255;
    },
  },
};

export default defineComponent({
  name: 'BasicSetting',
  emits: ['nextStep', 'update', 'back'],
  props: {
    formValue: [Object],
    typeOptions: [Array],
  },
  setup(props, { emit }) {
    const message = useMessage();
    const formRef = ref();
    const iconOptions = ref(icons);

    function back() {
      emit('back');
    }

    function formSubmit(e) {
      e.preventDefault();
      formRef.value?.validate((errors) => {
        if (!errors) {
          emit('nextStep');
        } else {
          console.log(errors);
          message.error('验证失败，请完整填写信息!');
        }
      });
    }

    return {
      rules,
      formRef,
      iconOptions,
      back,
      formSubmit,
      renderLabel,
      renderSingleSelectTag,
    };
  },
});
</script>
<style scoped></style>
