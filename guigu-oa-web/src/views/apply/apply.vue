<template>
  <div>
    <van-nav-bar
      title="发起审批"
      left-text="返回"
      left-arrow
      @click-left="() => router.back()"
    />
    <div class="main">
      <FormCreate :rule="rule" :option="options" @submit="onSubmit" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Apply',
};
</script>
<script setup>
import { useRoute, useRouter } from 'vue-router';
import formCreate from '@form-create/naive-ui';
import { onMounted, ref } from 'vue';
import { getProcessTemplate, startUp } from '@/api/process';

const FormCreate = formCreate.$form();
const router = useRouter();
const route = useRoute();
const rule = ref([]);
const options = ref({});
const processTemplate = ref(null);

const loadData = async (processTemplateId) => {
  const res = await getProcessTemplate(processTemplateId);
  processTemplate.value = res;
  rule.value = JSON.parse(res.formProps);
  options.value = JSON.parse(res.formOptions);
};

function onSubmit(formData) {
  let formShowData = {};
  rule.value.forEach((item) => {
    console.log(item);
    for (let key in formData) {
      if (key === item.field) {
        if (item.type === 'datePicker' && item?.props?.type.includes('range')) {
          //
        }
        formShowData[item.title] = formData[key];
      }
    }
  });
  let DATA = {
    formData: formData,
    formShowData: formShowData,
  };

  let processFormVo = {
    processTemplateId: processTemplate.value.id,
    processTypeId: processTemplate.value.processTypeId,
    formValues: JSON.stringify(DATA),
  };
  startUp(processFormVo).then(() => {
    router.push({ path: '/' });
  });
}

onMounted(() => {
  let processTemplateId = route.params.processTemplateId;
  loadData(processTemplateId);
});
</script>
<style scoped></style>
