<template>
  <n-card :bordered="false" class="proCard">
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
import { getPageList } from '@/api/system/role';
import { columns } from './columns';
import { DeleteOutlined, EditOutlined } from '@vicons/antd';
import { useDialog, useMessage } from 'naive-ui';

export default defineComponent({
  components: { BasicTable },
  setup() {
    const message = useMessage();
    const dialog = useDialog();
    const actionRef = ref();
    const params = reactive({
      roleName: '',
    });

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
    return {
      columns,
      loadDataTable,
      onCheckedRow,
      actionColumn,
      actionRef,
    };
  },
});
</script>
<style lang="less" scoped></style>
