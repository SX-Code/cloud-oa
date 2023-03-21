<template>
  <n-card :bordered="false" class="proCard">
    <BasicForm
      :gridProps="{ cols: 1 }"
      :schemas="schemas"
      :showAdvancedButton="false"
      @submit="handleSubmit"
      @reset="handleReset"
    >
    </BasicForm>
  </n-card>
  <n-card :bordered="false" class="proCard table">
    <BasicTable
      :columns="columns"
      :request="loadDataTable"
      :row-key="(row) => row.id"
      ref="actionRef"
      :actionColumn="actionColumn"
      :scroll-x="1000"
      @update:checked-row-keys="onCheckedRow"
    />
  </n-card>
</template>

<script>
import { defineComponent, h, reactive, ref } from 'vue';
import { BasicTable, TableAction } from '@/components/Table';
import { BasicForm } from '@/components/Form';
import { getPageList } from '@/api/system/role';
import { columns } from './columns';
import { DeleteOutlined, EditOutlined } from '@vicons/antd';
import { useDialog, useMessage } from 'naive-ui';

const schemas = [
  {
    field: 'roleNme',
    component: 'NInput',
    label: '角色名称',
    labelMessge: '筛选角色名称',
    componentProps: {
      placeholder: '请输入角色名称',
    },
    rules: [{ required: true, message: '请输入姓名', trigger: ['blur'] }],
  },
];

export default defineComponent({
  components: { BasicTable, BasicForm },
  setup() {
    const message = useMessage();
    const dialog = useDialog();
    const actionRef = ref();
    const params = reactive({
      roleName: '',
    });

    console.log(actionRef);

    const actionColumn = reactive({
      width: 150,
      title: '操作',
      fixed: 'right',
      align: 'center',
      render(record) {
        return h(TableAction, {
          style: 'text',
          actions: createActions(record),
        });
      },
    });

    function createActions(record) {
      return [
        {
          label: '删除',
          type: 'error',
          // 配置color会覆盖type
          color: 'red',
          icon: DeleteOutlined,
          onClick: handleDelete.bind(null, record),
          // 根据业务控制是否显示 isShow 和 auth 是并且关系
          ifShow: () => {
            return true;
          },
          // 根据权限控制是否显示: 有权限，会显示，支持多个
          auth: ['system_role'],
        },
        {
          label: '编辑',
          type: 'primary',
          icon: EditOutlined,
          onClick: handleEdit.bind(null, record),
          ifShow: () => {
            return true;
          },
          // 根据权限控制是否显示: 有权限，会显示，支持多个
          auth: ['system_role'],
        },
      ];
    }

    const loadDataTable = async (res) => {
      return await getPageList({ ...res }, params);
    };

    function onCheckedRow(rowKeys) {
      console.log(rowKeys);
    }

    function handleDelete(record) {
      console.log(record);
      dialog.info({
        title: '提示',
        content: `您想删除${record.roleName}`,
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: () => {
          message.success('删除成功');
        },
        onNegativeClick: () => {},
      });
    }

    function handleEdit(record) {
      console.log(record);
      message.success('您点击了编辑按钮');
    }

    function handleSubmit(values) {
      console.log(values);
    }

    function handleReset(values) {
      console.log(values);
    }
    return {
      columns,
      schemas,
      loadDataTable,
      onCheckedRow,
      actionColumn,
      actionRef,
      handleSubmit,
      handleReset,
    };
  },
});
</script>
<style lang="less" scoped></style>
