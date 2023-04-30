<template>
  <n-card :bordered="false" class="proCard">
    <BasicForm
      @register="register"
      @submit="handleSubmit"
      @reset="handleReset"
    />
    <BasicTable
      :columns="columns"
      :request="loadDataTable"
      :row-key="(row) => row.id"
      ref="actionRef"
      :actionColumn="actionColumn"
      :scroll-x="1000"
      :pagination="pagination"
      @fetch-success="fetchSuccess"
    />
  </n-card>
</template>

<script>
export default {
  name: 'ProcessList',
};
</script>

<script setup>
import { useForm, BasicForm } from '@/components/Form';
import { createColumns } from './columns';
import { schemas } from './schemas';
import { h, ref, unref, reactive } from 'vue';
import { BasicTable, TableAction } from '@/components/Table';
import { EyeOutlined } from '@vicons/antd';
import { pageQueryProcess } from '@/api/process/process';

const [register] = useForm({
  gridProps: { cols: '1 m:2 l:3' },
  labelWidth: 80,
  schemas,
});
// 搜索表单参数
const defalutParams = () => {
  return {
    keyword: null,
    status: null,
    createTimeBegin: null,
    createTimeEnd: null,
  };
};
const params = reactive(defalutParams());

const currentIndex = ref(1);
const actionRef = ref(null);
const columns = createColumns({
  currentIndex() {
    return unref(currentIndex);
  },
});
// 操作列属性
const actionColumn = reactive({
  width: 150,
  title: '操作',
  key: 'action',
  fixed: 'right',
  align: 'center',
  render(record) {
    return h(TableAction, {
      style: 'text',
      showLabel: false,
      actions: createActions(record),
    });
  },
});
const pagination = reactive({
  prefix: (info) => {
    return `共 ${info.itemCount} 条`;
  },
});

function handleSubmit(fromModel) {
  const { keyword, status, range } = fromModel;
  Object.assign(params, {
    keyword,
    status,
    createTimeBegin: range?.[0],
    createTimeEnd: range?.[1],
  });
  reloadTable();
}

function handleReset() {
  Object.assign(params, defalutParams());
  reloadTable();
}

function createActions(record) {
  return [
    {
      label: '查看',
      type: 'primary',
      icon: EyeOutlined,
      onClick: handleView.bind(null, record),
      auth: ['process_list'],
    },
  ];
}

function reloadTable() {
  actionRef.value.reload();
}

async function loadDataTable(res) {
  return await pageQueryProcess({ ...res }, params);
}

function fetchSuccess() {
  currentIndex.value =
    (actionRef.value.pagination.page - 1) * actionRef.value.pagination.pageSize;
}

function handleView() {}
</script>
<style></style>
