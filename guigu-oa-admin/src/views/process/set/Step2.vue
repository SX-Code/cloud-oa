<template>
  <div>
    <div class="tools">
      <n-space>
        <n-button type="primary" @click="formSubmit">提交</n-button>
        <n-button type="default" ghost @click="prevStep">上一步</n-button>
      </n-space>
    </div>
    <fc-designer ref="designer" style="min-height: 600px" />
  </div>
</template>
<script>
export default {
  name: 'FormCreate',
};
</script>
<script setup>
import { ref, defineEmits, defineProps, watch } from 'vue';
import FcDesigner from 'form-designer-naiveui';
import formCreate from '@form-create/naive-ui';
import { useDialog } from 'naive-ui';
import is from '@form-create/utils/lib/type';

const emit = defineEmits(['nextStep', 'prevStep', 'update']);
const props = defineProps({
  value: [Object],
});

const designer = ref(null);
const dialog = useDialog();

watch(
  () => designer.value,
  () => {
    loadData();
  }
);

function loadData() {
  const { formProps, formOptions } = props.value;
  const val = JSON.parse(formOptions);
  designer.value?.setRule(formCreate.parseJson(formProps));
  if (is.Object(val) || !val) {
    return;
  }
  designer.value?.setOption(val);
}
function prevStep() {
  emit('prevStep');
}

function submit() {
  const res = {
    formProps: formCreate.toJson(designer?.value.getRule()),
    formOptions: JSON.stringify(designer.value.getOption()),
  };
  emit('update', res);
  emit('nextStep');
}

function formSubmit() {
  const rule = formCreate.toJson(designer?.value.getRule());
  if ('[]' === rule) {
    dialog.warning({
      title: '提示',
      content: '注意到你并未设计表单，您确定继续提交？',
      positiveText: '确定',
      negativeText: '不确定',
      maskClosable: false,
      onPositiveClick: () => {
        submit();
      },
    });
  } else {
    submit();
  }
}
</script>
<style scoped>
.tools {
  width: 100%;
  padding: 20px 0;
  display: flex;
  justify-content: flex-end;
}
</style>
