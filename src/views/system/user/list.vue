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
      @fetch-success="fetchSuccess"
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
        </n-space>
      </template>
    </BasicTable>

    <!-- 分配角色弹窗Modal -->
    <n-modal
      v-model:show="showAssignModel"
      :show-icon="false"
      preset="dialog"
      title="分配角色"
    >
      <n-form
        :model="assignFormParams"
        label-placement="left"
        :label-width="80"
        class="py-4"
      >
        <n-form-item label="用户名" path="username">
          <n-input disabled v-model:value="assignFormParams.username" />
        </n-form-item>
        <n-form-item label="角色列表" path="roleIdList">
          <n-checkbox
            :checked="selectAll"
            label="全选"
            @update:checked="handleSelectAll"
            :indeterminate="indeterminate"
          />
        </n-form-item>
        <n-form-item label=" " path="roleIdList">
          <n-checkbox-group
            v-model:value="assignFormParams.roleIdList"
            @update:value="handleRoleSelected"
          >
            <n-space>
              <template
                v-for="(role, index) in allRoleList"
                :key="`${index}-${role.id}`"
              >
                <n-checkbox :value="role.id">
                  {{ role.roleName }}
                </n-checkbox>
              </template>
            </n-space>
          </n-checkbox-group>
        </n-form-item>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="() => (showAssignModel = false)">取消</n-button>
          <n-button
            type="info"
            :loading="formBtnLoading"
            @click="confirmAssignForm"
            >确定</n-button
          >
        </n-space>
      </template>
    </n-modal>
    <!-- 新建弹窗Modal -->
    <!--
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
      <template #action>
        <n-space>
          <n-button @click="() => (showModal = false)">取消</n-button>
          <n-button type="info" :loading="formBtnLoading" @click="confirmForm"
            >确定</n-button
          >
        </n-space>
      </template>
    </n-modal>
    -->
  </n-card>
</template>
<script>
import { useDialog, useMessage } from 'naive-ui';
import { defineComponent, reactive, ref, h, unref, computed } from 'vue';
import { BasicForm, useForm } from '@/components/Form';
import { BasicTable, TableAction } from '@/components/Table';
import { createColumns } from './columns';
import { getPageList, removeById, updateStatus } from '@/api/system/sysUser';
import {
  PlusOutlined,
  DeleteOutlined,
  EditOutlined,
  ControlOutlined,
} from '@vicons/antd';
import { assignRoles, getRoles } from '@/api/system/sysRole';
const schemas = [
  {
    field: 'keyword',
    component: 'NInput',
    label: '关键词',
    defaultValue: '',
    labelMessge: '关键词',
    componentProps: {
      placeholder: '用户名/姓名/手机号码',
    },
    rules: [{ required: true, message: '请输入姓名', trigger: ['blur'] }],
  },
  {
    field: 'createTimeBegin',
    component: 'NDatePicker',
    label: '开始日期',
    labelMessge: '开始日期',
    componentProps: {
      placeholder: '开始日期',
      type: 'datetime',
      clearable: true,
    },
  },
  {
    field: 'createTimeEnd',
    component: 'NDatePicker',
    label: '结束时间',
    labelMessge: '结束时间',
    componentProps: {
      placeholder: '结束时间',
      type: 'datetime',
      clearable: true,
    },
  },
];

export default defineComponent({
  components: { PlusOutlined, BasicForm, BasicTable },
  setup() {
    const message = useMessage();
    const dialog = useDialog();
    const actionRef = ref();
    const selections = ref([]);
    const currentIndex = ref(1);
    const showAssignModel = ref(false);
    const formBtnLoading = ref(false);
    const selectAll = ref(false);
    const columns = createColumns({
      currentIndex() {
        return unref(currentIndex);
      },
      switchStatus: async (row) => {
        const { id, status } = row;
        const update = status === 1 ? 0 : 1;
        row.status = await updateStatus(id, update);
      },
    });

    const defalutParams = () => {
      return {
        keyword: '',
        createTimeBegin: '',
        createTimeEnd: '',
      };
    };
    const params = reactive(defalutParams());
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
          actions: createAction(record),
        });
      },
    });

    const pagination = reactive({
      prefix: (info) => {
        return `共 ${info.itemCount} 条`;
      },
    });

    // 分配角色
    const assignFormParams = reactive({
      username: '',
      userId: null,
      roleIdList: [],
    });
    const allRoleListRef = ref([]);
    const indeterminate = computed(() => {
      if (assignFormParams.roleIdList.length === 0) return false;
      else
        return (
          unref(allRoleListRef).length != assignFormParams.roleIdList.length
        );
    });

    function createAction(record) {
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
          auth: ['system_role'],
        },
        {
          label: '分配角色',
          type: 'warning',
          icon: ControlOutlined,
          onClick: handleAssign.bind(null, record),
          ifShow: () => {
            return true;
          },
          auth: ['system_role'],
        },
      ];
    }

    // 表单

    // 注册表单
    const [register, { setProps }] = useForm({
      gridProps: { cols: '1 s:1 m:2 l:3 xl:4 2xl:5' },
      showAdvancedButton: true,
      labelWidth: 80,
      schemas,
    });

    function handleSubmit(values) {
      Object.assign(params, values);
      reloadTable();
    }

    function handleReset() {}

    // 表格
    async function loadDataTable(res) {
      return await getPageList({ ...res }, params);
    }

    function reloadTable() {
      actionRef.value.reload();
    }

    function onCheckedRow(rowKeys) {
      selections.value = rowKeys;
    }

    function handleDelete(record) {
      dialog.info({
        title: '提示',
        content: `您想删除${record.name}`,
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: async () => {
          await removeById(record.id);
          reloadTable();
        },
        onNegativeClick: () => {},
      });
    }

    function handleEdit(record) {
      console.log(record);
    }

    async function handleAssign(record) {
      const { username, id } = record;
      // 获取所有角色allRoles，用户所属角色assignRoleList
      const roles = await getRoles(id);
      const { assignRoleList, allRoles } = roles;
      // 系统没有角色
      if (allRoles && allRoles.length === 0) return message.info('无角色信息');
      // 解析出用户所属角色id
      const roleIdList = assignRoleList.map((role) => role.id);
      // 赋值操作
      Object.assign(assignFormParams, { username, userId: id, roleIdList });
      allRoleListRef.value = allRoles;
      showAssignModel.value = true;
    }
    function fetchSuccess() {
      currentIndex.value =
        (actionRef.value.pagination.page - 1) *
        actionRef.value.pagination.pageSize;
    }

    // Modal
    function addTable() {
      message.info('添加用户');
    }

    // 全选按钮选中事件
    function handleSelectAll() {
      if (indeterminate.value || !selectAll.value) {
        // true，此时是部分选中，点击进行全选
        const allRoleIds = unref(allRoleListRef).map((role) => role.id);
        assignFormParams.roleIdList = allRoleIds;
      } else {
        // false，全取消选中
        assignFormParams.roleIdList = [];
      }
      selectAll.value = !selectAll.value;
    }

    function handleRoleSelected(value) {
      selectAll.value = value.length === assignFormParams.roleIdList.length;
    }

    function confirmAssignForm() {
      assignRoles(assignFormParams).then(() => {
        showAssignModel.value = false;
      });
    }

    return {
      schemas,
      columns,
      actionRef,
      pagination,
      actionColumn,
      register,
      setProps,
      handleSubmit,
      handleReset,
      loadDataTable,
      onCheckedRow,
      fetchSuccess,
      formBtnLoading,
      addTable,
      showAssignModel,
      assignFormParams,
      allRoleList: allRoleListRef,
      selectAll,
      handleRoleSelected,
      handleSelectAll,
      indeterminate,
      confirmAssignForm,
    };
  },
});
</script>
