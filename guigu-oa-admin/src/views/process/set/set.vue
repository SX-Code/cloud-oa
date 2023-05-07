<template>
  <div>
    <div class="n-layout-page-header"></div>
    <n-card :bordered="false" class="mt-4 proCard">
      <n-space vertical class="steps" justify="center">
        <n-steps :current="currentTab" :status="currentStatus">
          <n-step title="基本设置" />
          <n-step title="表单设置" />
          <n-step title="流程设置" />
        </n-steps>
        <step1
          v-if="currentTab === 1"
          :formValue="step1Data"
          :typeOptions="typeOptions"
          @nextStep="nextStep"
          @back="back"
        />
        <step2
          v-if="currentTab === 2"
          :value="step2Data"
          @nextStep="nextStep"
          @prevStep="prevStep"
          @update="updateStep2Data"
        />
        <step3
          v-if="currentTab === 3"
          v-model:value="step3Data"
          v-model:files="fileList"
          @prevStep="prevStep"
          @finish="finish"
          @update-value="updateStep3Data"
        />
      </n-space>
    </n-card>
  </div>
</template>

<script>
export default {
  name: 'ProcessSet',
};
</script>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import step1 from './Step1.vue';
import step2 from './Step2.vue';
import step3 from './Step3.vue';
import { listProcessType } from '@/api/process/processType';
import { addProcessTemplate } from '@/api/process/processTemplate';
import { useRouter } from 'vue-router';

const router = useRouter();
const currentTab = ref(1);
const currentStatus = ref('process');
const typeOptions = ref([]);
const fileList = ref([]);
const step1Data = reactive({
  processTypeId: null,
  name: '',
  iconUrl: null,
  description: '',
});
const step2Data = reactive({
  formProps: '[]',
  formOptions: null,
});

const step3Data = reactive({
  processDefinitionPath: '',
  processDefinitionKey: '',
});

function updateStep2Data(data) {
  Object.assign(step2Data, data);
}

function updateStep3Data(data) {
  Object.assign(step3Data, data);
}

function nextStep() {
  if (currentTab.value < 3) {
    currentTab.value += 1;
  }
}

function prevStep() {
  if (currentTab.value > 1) {
    currentTab.value -= 1;
  }
}

function back() {
  router.push('/processSet/template');
}

function finish() {
  const processTemplate = { ...step1Data, ...step2Data, ...step3Data };
  addProcessTemplate(processTemplate)
    .then(() => {
      back();
    })
    .catch((err) => {
      console.log(err);
    });
}

async function fetchProcessType() {
  const types = await listProcessType();
  if (types && types.length > 0) {
    typeOptions.value = types.map((item) => {
      return { label: item.name, value: item.id };
    });
  }
}

onMounted(() => {
  // 填充类型选项
  fetchProcessType();
});
</script>
<style lang="less" scoped>
.steps {
  width: 100%;
  margin: 16px auto;
}
</style>
