<template>
  <div style="height: 100vh; position: relative">
    <n-layout position="absolute">
      <n-layout-header>
        <van-nav-bar title="审批列表" :border="false" />
        <n-tabs
          default-value="1"
          :value="activeIndex"
          justify-content="space-evenly"
          type="line"
          @update:value="updateTab"
        >
          <n-tab name="0"> 待处理 </n-tab>
          <n-tab name="1"> 已处理 </n-tab>
          <n-tab name="2"> 已发起 </n-tab>
        </n-tabs>
      </n-layout-header>
      <n-layout position="absolute" style="top: 88px">
        <n-layout class="list-wrap">
          <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
            <van-list
              v-model:loading="loading"
              :finished="finished"
              finished-text="没有更多了"
              @load="loadData"
              :immediate-check="false"
            >
              <van-cell
                v-for="item in list"
                @click="info(item.id, item?.taskId)"
                :key="item.id"
              >
                <template v-slot:default>
                  <n-thing class="item">
                    <template #header>
                      {{ item.title }}
                    </template>
                    <template #header-extra>
                      {{ item.createTime }}
                    </template>
                    <div class="item-block">
                      <div
                        v-for="(value, key) in item.formValues.formShowData"
                        :key="key"
                      >
                        <div
                          class="datetime"
                          v-if="isArray(value) && isDateTime(value[0])"
                        >
                          <span>{{ key }}:</span>
                          <n-timeline class="timeline">
                            <n-timeline-item
                              type="success"
                              :content="value[0]"
                            />
                            <n-timeline-item type="error" :content="value[1]" />
                          </n-timeline>
                        </div>
                        <div v-else>
                          {{ key }}: <span v-html="value"></span>
                        </div>
                      </div>
                    </div>
                    <template #footer>
                      <span
                        :class="
                          item.status === 1
                            ? '审批中'
                            : item.status === 2
                            ? 'pass'
                            : 'refused'
                        "
                        >{{
                          item.status === 1
                            ? '审批中'
                            : item.status === 2
                            ? '审批通过'
                            : '审批拒绝'
                        }}</span
                      >
                    </template>
                  </n-thing>
                </template>
              </van-cell>
            </van-list>
          </van-pull-refresh>
        </n-layout>
      </n-layout>
    </n-layout>
  </div>
</template>
<script>
export default {
  name: 'ProcessList',
};
</script>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';
import { fetchPending, fetchProcessed, fetchStarted } from '@/api/process';
import { isArray, isDateTime } from '@/utils/is';
import router from '@/router';
import { showToast } from 'vant';

const route = useRoute();
const activeIndex = ref(1);
const list = ref([]);
const param = reactive({
  page: 1,
  pageSize: 10,
  pages: 1,
});
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
function updateTab(value) {
  activeIndex.value = value;
  list.value = [];
  param.page = 1;
  finished.value = false;
  //tabs切换时，如果之前的tab已经滚动到底部（list加载到底部），直接点击其他的tab，将再触发一次onload事件。
  //可能调用2次loadData()方法，延迟执行，通过时间差解决问题
  setTimeout(() => {
    if (!finished.value) {
      loadData();
    }
  }, 500);
}

function loadData() {
  // 停止刷新动作
  if (refreshing.value) {
    list.value = [];
    refreshing.value = false;
  }
  switch (activeIndex.value) {
    case '0':
      pagePending();
      break;
    case '1':
      pageProcessed();
      break;
    case '2':
      pageStarted();
      break;
  }
}

onMounted(() => {
  activeIndex.value = route.params.activeIndex;
  loadData();
});

function onRefresh() {
  finished.value = false;
  param.page = 1;
  loading.value = true;
  loadData();
}

const pagePending = async () => {
  const res = await fetchPending(param);
  handleRes(res);
};

const pageProcessed = async () => {
  const res = await fetchProcessed(param);
  handleRes(res);
};

const pageStarted = async () => {
  const res = await fetchStarted(param);
  handleRes(res);
};

function handleRes(res) {
  if (!res) {
    loading.value = false;
    return showToast('没有数据');
  }
  for (let i = 0; i < res.records.length; i++) {
    let item = res.records[i];
    item.formValues = JSON.parse(item.formValues);
    list.value.push(item);
  }
  param.pages = res.pages;
  loading.value = false;
  if (param.page >= param.pages) {
    finished.value = true;
  }
  param.page = param.page + 1;
}

function info(id, taskId) {
  router.push({ path: '/show/' + id + '/' + taskId + '/' + activeIndex.value });
}
</script>
<style scoped>
.list-wrap {
  position: absolute;
  top: 0px;
  bottom: 0px;
  right: 0px;
  left: 0px;
}
.item {
  padding: 10px;
  text-align: left;
}
.datetime {
  display: flex;
}
.datetime .timeline {
  flex: 1;
  margin-left: 5px;
}
.item-block {
  padding: 4px 0;
  font-size: 14px;
}
.item-block p {
  padding: 0;
  margin: 0;
  line-height: 20px;
}
.pass {
  color: #4cb971;
}
.refused {
  color: #eb8473;
}
.loadmore {
  text-align: center;
  color: rgb(118, 124, 130);
}
</style>
