<template>
  <div class="container">
    <van-nav-bar
      title="审批详情"
      left-text="返回"
      left-arrow
      @click-left="returnListPage"
    />
    <van-cell>
      <span :class="status[0]">{{ status[1] }}</span>
      <template #title>
        <span class="custom-text">{{ process?.title }}</span>
      </template>
    </van-cell>
    <div class="main">
      <FormCreate :rule="rule" :option="options" />
    </div>
    <van-cell style="margin-top: 15px">
      <template #title>
        <div class="result">
          <h3>流程</h3>
          <van-steps direction="vertical" :active="processRecordList.length">
            <van-step :key="item.id" v-for="item in processRecordList">
              <h4>{{ item.operateUser }} {{ item.description }}</h4>
              <p>{{ item.createTime }}</p>
            </van-step>
            <van-step>
              <h4>{{ process?.description }}</h4>
            </van-step>
          </van-steps>
        </div>
      </template>
    </van-cell>
    <div class="footer" v-if="isApprove">
      <div class="left-action">
        <div class="action back" @click="() => $router.back()">
          <van-icon name="revoke" />
          <span>返回</span>
        </div>
      </div>
      <div class="right-button">
        <van-button @click="approve(-1)" type="default" size="small"
          >审批拒绝</van-button
        >
        <span style="margin: 0 4px"></span>
        <van-button @click="approve(1)" type="primary" size="small"
          >审批通过</van-button
        >
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'ShowDetail',
};
</script>

<script setup>
import { ref, onMounted } from 'vue';
import { showProcess, approveProcess } from '@/api/process';
import { useRoute, useRouter } from 'vue-router';
import { showToast } from 'vant';
import formCreate from '@form-create/naive-ui';

const FormCreate = formCreate.$form();
const router = useRouter();
const route = useRoute();
const taskId = ref(null);
const activeIndex = ref(0);
const process = ref(null);
const formValues = ref(null);
const processRecordList = ref([]);
const isApprove = ref(false);
const rule = ref([]);
const options = ref({});
const defaultStatus = {
  1: ['ing', '审批中'],
  2: ['pass', '已通过'],
  3: ['fail', '已拒绝'],
};
const status = ref(defaultStatus[1]);

const fetchData = async (id) => {
  const res = await showProcess(id);
  if (!res) return showToast('获取失败');
  process.value = res.process;
  status.value = defaultStatus[res.process.status];
  formValues.value = JSON.parse(res.process.formValues);
  processRecordList.value = res.processRecordList;
  isApprove.value = res.isApprove;
  handleTemplateData(res.processTemplate);
};

function handleTemplateData(template) {
  rule.value = JSON.parse(template.formProps);
  options.value = JSON.parse(template.formOptions);
  options.value.form.labelPlacement = 'top';
  options.value.submitBtn.show = false;
  rule.value.map((item) => {
    item['props']['disabled'] = true;
    item['value'] = formValues.value.formData[item['field']];
  });
}

const approve = async (status) => {
  let approvalVo = {
    processId: process.value.id,
    taskId: taskId.value,
    status,
  };
  approveProcess(approvalVo)
    .then(() => {
      showToast('审批通过');
      router.push({ path: '/list/1' });
    })
    .catch(() => {
      showToast('审批失败');
    });
};

function returnListPage() {
  router.push({ path: '/list/' + activeIndex.value });
}

onMounted(() => {
  taskId.value = route.params.taskId;
  activeIndex.value = route.params.activeIndex;
  let id = route.params.id;
  fetchData(id);
});
</script>
<style>
h1,
h2,
h3,
h4,
h5,
h6 {
  margin: 0;
  font-size: inherit;
}
.custom-text {
  font-size: 16px;
}
.fail {
  color: rgb(255, 68, 68);
}
.pass {
  color: rgb(7, 193, 96);
}

.footer {
  padding: 10px;
  background: #f8f8f8;
  display: flex;
  align-items: center;
  position: fixed;
  width: 100%;
  bottom: 0;
  z-index: 10;
}
.footer .right-button {
  margin-right: 20px;
}
.footer .left-action {
  flex: 1;
}
.footer .left-action .action {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.footer .left-action .action span {
  font-size: 12px;
  color: #838485;
}
</style>
