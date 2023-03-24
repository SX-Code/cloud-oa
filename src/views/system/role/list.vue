<template>
  <n-card :bordered="false" class="proCard table">
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
      @edit-row-end="rowEditEnd"
      @update:checked-row-keys="onCheckedRow"
    >
      <!-- 表格上方左侧的位置 -->
      <template #tableTitle>
        <n-space>
          <!-- 新增 -->
          <n-button strong secondary type="primary" @click="addTable">
            <template #icon>
              <n-icon>
                <PlusOutlined />
              </n-icon>
            </template>
            添加
          </n-button>
          <!-- 批量删除 -->
          <n-button strong secondary type="error" @click="removeRows">
            <template #icon>
              <n-icon>
                <DeleteOutlined />
              </n-icon>
            </template>
            批量删除
          </n-button>
        </n-space>
      </template>
    </BasicTable>

    <!-- 新建弹窗Modal -->
    <n-modal
      v-model:show="showModel"
      :show-icon="false"
      preset="dialog"
      title="新建"
    >
      <n-form
        :model="formParams"
        :rules="rules"
        ref="formRef"
        label-placement="left"
        :label-width="80"
        class="py-4"
      >
        <n-form-item label="角色名称" path="roleName">
          <n-input
            placeholder="请输入角色名称"
            v-model:value="formParams.roleName"
          />
        </n-form-item>
        <n-form-item label="角色编码" path="roleCode">
          <n-input
            placeholder="请输入角色编码"
            v-model:value="formParams.roleCode"
          />
        </n-form-item>
      </n-form>
      <!-- 操作插槽 -->
      <template #action>
        <n-space>
          <n-button @click="() => (showModal = false)">取消</n-button>
          <n-button type="info" :loading="formBtnLoading" @click="confirmForm"
            >确定</n-button
          >
        </n-space>
      </template>
    </n-modal>
  </n-card>
</template>

<script>
import { defineComponent, h, reactive, ref, unref } from 'vue';
import { BasicTable, TableAction } from '@/components/Table';
import { BasicForm, useForm } from '@/components/Form';
import {
  getPageList,
  removeById,
  addRole,
  updateRole,
  batchRemove,
} from '@/api/system/role';
import { columns } from './columns';
import {
  DeleteOutlined,
  EditOutlined,
  PlusOutlined,
  SaveOutlined,
  CloseSquareOutlined,
} from '@vicons/antd';
import { useDialog, useMessage } from 'naive-ui';

const rules = {
  roleName: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入角色名称',
  },
  roleCode: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入角色编码',
  },
};

const schemas = [
  {
    field: 'roleName',
    component: 'NInput',
    label: '角色名称',
    defaultValue: '',
    labelMessge: '筛选角色名称',
    componentProps: {
      placeholder: '请输入角色名称',
    },
    rules: [{ required: true, message: '请输入姓名', trigger: ['blur'] }],
  },
];

export default defineComponent({
  components: { BasicTable, BasicForm, PlusOutlined, DeleteOutlined },
  setup() {
    const message = useMessage();
    const dialog = useDialog();
    const formRef = ref(null);
    const actionRef = ref();
    const currentEditKeyRef = ref('');
    const defalutParams = () => {
      return {
        roleName: '',
        roleCode: '',
      };
    };
    const params = reactive(defalutParams());

    // 新增表格数据
    const showModel = ref(false);
    const formBtnLoading = ref(false);
    const formParams = reactive(defalutParams());

    // 选中行
    const selections = ref([]);

    // 分页参数
    const pagination = reactive({
      prefix: (info) => {
        return `共 ${info.itemCount} 条`;
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
          actions: createActions(record),
        });
      },
    });

    // 编辑点击事件
    function handleEdit(record) {
      currentEditKeyRef.value = record.key;
      record.onEdit?.(true);
    }
    // 取消编辑事件
    function handleCancel(record) {
      currentEditKeyRef.value = '';
      record.onEdit?.(false, false);
    }
    // 处理编辑的保存事件
    async function handleSave(record) {
      const pass = await record.onEdit?.(false, true);
      if (pass) {
        currentEditKeyRef.value = '';
        const { id, roleName, roleCode } = record;
        // 提交更新
        await updateRole({ id, roleName, roleCode });
      }
    }

    function createActions(record) {
      if (!record.editable) {
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
      } else {
        return [
          {
            label: '保存',
            type: 'success',
            icon: SaveOutlined,
            onClick: handleSave.bind(null, record),
          },
          {
            label: '取消',
            type: 'warning',
            icon: CloseSquareOutlined,
            onClick: handleCancel.bind(null, record),
          },
        ];
      }
    }

    // 注册表单
    const [register, { setProps }] = useForm({
      gridProps: { cols: 3 },
      showAdvancedButton: false,
      schemas,
    });

    // 显示新增模态框
    function addTable() {
      showModel.value = true;
    }

    // 加载数据
    const loadDataTable = async (res) => {
      return await getPageList({ ...res }, params);
    };

    // 选中行
    function onCheckedRow(rowKeys) {
      selections.value = rowKeys;
    }

    // 单行删除
    function handleDelete(record) {
      console.log(record);
      dialog.info({
        title: '提示',
        content: `您想删除${record.roleName}`,
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: async () => {
          await removeById(record.id);
          reloadTable();
        },
        onNegativeClick: () => {},
      });
    }

    // 刷新表格
    function reloadTable() {
      actionRef.value.reload();
    }

    // 查询提交方法
    function handleSubmit(values) {
      Object.assign(params, values);
      reloadTable();
    }

    // 查询重置方法
    function handleReset() {
      Object.assign(params, defalutParams());
      console.log(params);
      reloadTable();
    }

    // 新建表单提交方法
    function confirmForm(e) {
      e.preventDefault();
      formBtnLoading.value = true;
      formRef.value?.validate(async (errors) => {
        if (!errors) {
          await addRole(formParams);
          setTimeout(() => {
            showModel.value = false;
            reloadTable();
          });
        } else {
          console.log(errors);
          message.error('请填写完整信息');
        }
        formBtnLoading.value = false;
      });
    }

    // 行编辑完成回调
    function rowEditEnd() {
      reloadTable();
    }

    // 批量删除
    async function removeRows() {
      const ids = unref(selections);
      if (ids.length === 0) {
        message.warning('请至少勾选一列');
        return;
      }
      dialog.warning({
        title: '提示',
        content: `您要删除${ids}`,
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: async () => {
          await batchRemove(ids);
          reloadTable();
        },
        onNegativeClick: () => {},
      });
    }

    return {
      rules,
      columns,
      schemas,
      register,
      setProps,
      formRef,
      rowEditEnd,
      pagination,
      formParams,
      showModel,
      addTable,
      removeRows,
      confirmForm,
      formBtnLoading,
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
