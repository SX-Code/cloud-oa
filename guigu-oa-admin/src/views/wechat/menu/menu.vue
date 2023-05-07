<template>
  <n-card :bordered="false" class="proCard table">
    <BasicTable
      :columns="columns"
      :request="loadDataTable"
      :row-key="(row) => row.id"
      ref="actionRef"
      :actionColumn="actionColumn"
      :scroll-x="1000"
      :pagination="false"
    >
      <template #tableTitle>
        <n-space>
          <!-- 新增菜单 -->
          <n-button
            v-if="hasAddPermission"
            strong
            secondary
            type="primary"
            @click="() => (showAddModal = true)"
          >
            <template #icon>
              <n-icon>
                <PlusOutlined />
              </n-icon>
            </template>
            新增菜单
          </n-button>
          <!-- 同步菜单 -->
          <n-button
            v-if="hasSyncPermission"
            strong
            secondary
            type="success"
            @click="createMenu"
          >
            <template #icon>
              <n-icon>
                <CloudSyncOutlined />
              </n-icon>
            </template>
            同步菜单
          </n-button>
          <!-- 删除菜单 -->
          <n-button
            v-if="hasDeletePermission"
            strong
            secondary
            type="error"
            @click="clearMenu"
          >
            <template #icon>
              <n-icon>
                <DeleteOutlined />
              </n-icon>
            </template>
            删除菜单
          </n-button>
        </n-space>
      </template>
    </BasicTable>
    <!-- 新建弹窗Modal -->
    <n-modal
      v-model:show="showAddModal"
      v-if="hasAddPermission"
      :show-icon="false"
      preset="dialog"
      title="新建"
    >
      <n-form
        :model="params"
        :rules="rules"
        ref="formRef"
        label-placement="left"
        :label-width="80"
        class="py-4"
      >
        <n-form-item label="父级菜单" path="parentId">
          <n-select
            placeholder="请选择父级菜单"
            v-model:value="params.parentId"
            :options="parentOptions"
            @update:value="handParentIdChange"
          />
        </n-form-item>
        <n-form-item label="菜单名称" path="name">
          <n-input placeholder="请输入菜单名称" v-model:value="params.name" />
        </n-form-item>
        <n-form-item label="菜单类型" path="type">
          <n-select
            placeholder="请选择菜单类型"
            v-model:value="params.type"
            :options="typeOptions"
          />
        </n-form-item>
        <n-form-item label="菜单URL" path="url">
          <n-input placeholder="请输入菜单URL" v-model:value="params.url" />
        </n-form-item>
        <n-form-item
          label="菜单KEY"
          path="menuKey"
          v-show="params.type === 'click'"
        >
          <n-input placeholder="请输入菜单KEY" v-model:value="params.menuKey" />
        </n-form-item>
        <n-form-item label="排序号" path="sort">
          <n-input-number placeholder="排序号" v-model:value="params.sort" />
        </n-form-item>
      </n-form>
      <!-- 操作插槽 -->
      <template #action>
        <n-space>
          <n-button @click="() => (showAddModal = false)">取消</n-button>
          <n-button type="info" @click="confirmForm">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </n-card>
</template>
<script>
export default {
  name: 'WechatMenu',
};
</script>
<script setup>
import { columns } from './columns';
import { BasicTable, TableAction } from '@/components/Table';
import {
  getMenuList,
  updateMenu,
  syncMenu,
  deleteMenu,
  addMenu,
} from '@/api/wechat/menu';
import { h, ref, reactive, computed } from 'vue';
import {
  EditOutlined,
  DeleteOutlined,
  SaveOutlined,
  CloseSquareOutlined,
  CloudSyncOutlined,
  PlusOutlined,
} from '@vicons/antd';
import { useDialog } from 'naive-ui';
import { usePermission } from '@/hooks/web/usePermission';

const typeOptions = [
  { label: '网页', value: 'view' },
  { label: '点击', value: 'click' },
  { label: '小程序', value: 'miniprogram' },
];

const rules = {
  name: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入菜单名称',
  },
  roleCode: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入角色编码',
  },
};

const dialog = useDialog();
const actionRef = ref(null);
const formRef = ref(null);
const showAddModal = ref(false);
const parentOptions = ref([{ label: '父菜单', value: 0 }]);
const currentEditKeyRef = ref('');
const { hasPermission } = usePermission();
const hasSyncPermission = computed(() => {
  return hasPermission(['wechat_menu_sync']);
});
const hasDeletePermission = computed(() => {
  return hasPermission(['wechat_menu_delete']);
});
const hasAddPermission = computed(() => {
  return hasPermission(['wechat_menu_add']);
});

const defalutParams = () => {
  return {
    parentId: 0,
    name: '',
    type: 'view',
    url: '',
    menuKey: '',
    sort: 1,
  };
};
const params = reactive(defalutParams());
// 表格列信息
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

function createAction(record) {
  if (!record.editable) {
    return [
      {
        label: '编辑',
        type: 'primary',
        icon: EditOutlined,
        onClick: handleEdit.bind(null, record),
        ifShow: () => {
          return true;
        },
        auth: ['wechat_menu_update'],
      },
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
        auth: ['wechat_menu_remove'],
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
    const { id, name, url, menuKey, order } = record;
    // 提交更新
    await updateMenu({ id, name, url, menuKey, order });
  }
}

const handleDelete = (record) => {
  dialog.info({
    title: '提示',
    content: `您想删除${record.name}`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      // await removeById(record.id);
      reloadTable();
    },
    onNegativeClick: () => {},
  });
};

const loadDataTable = async () => {
  const list = await getMenuList();
  // 组装父菜单选择属性
  parentOptions.value = list.map((item, index) => {
    return { label: item.name, value: item.id, index };
  });
  parentOptions.value.unshift({ label: '父菜单', value: 0 });
  params.sort = list.length + 1;
  return {
    records: list,
  };
};

function reloadTable() {
  actionRef.value.reload();
}

function handParentIdChange(_, option) {
  const { data } = actionRef.value.tableElRef;
  const { index } = option;
  if (index === 0) {
    // 父菜单
    params.sort = data.length + 1;
  } else {
    params.sort = data[index].children?.length + 1;
  }
}

const confirmForm = async () => {
  await addMenu(params);
  showAddModal.value = false;
  Object.assign(params, defalutParams());
  reloadTable();
};

const createMenu = async () => {
  dialog.info({
    title: '提示',
    content: `您确定上传菜单吗，是否继续？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      await syncMenu();
    },
    onNegativeClick: () => {},
  });
};

const clearMenu = async () => {
  dialog.warning({
    title: '提示',
    content: `您确定删除菜单吗，是否继续？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      await deleteMenu();
    },
    onNegativeClick: () => {},
  });
};
</script>
<style scoped></style>
