<template>
  <div>
    <n-grid
      class="mt-4"
      cols="1 s:1 m:1 l:3 xl:3 2xl:3"
      responsive="screen"
      :x-gap="12"
    >
      <n-gi span="1">
        <n-card :segmented="{ content: true }" :bordered="false" size="small">
          <template #header>
            <n-space>
              <n-dropdown
                trigger="hover"
                @select="selectAddMenu"
                :options="addMenuOptions"
              >
                <n-button type="info" ghost icon-placement="right">
                  添加菜单
                  <template #icon>
                    <div class="flex item-center">
                      <n-icon size="14">
                        <DownOutlined />
                      </n-icon>
                    </div>
                  </template>
                </n-button>
              </n-dropdown>
              <n-button
                type="info"
                ghost
                icon-placement="left"
                @click="packHandle"
              >
                全部{{ expandedKeys.length ? '收起' : '展开' }}
                <template #icon>
                  <div class="flex items-center">
                    <n-icon size="14">
                      <AlignLeftOutlined />
                    </n-icon>
                  </div>
                </template>
              </n-button>
            </n-space>
          </template>
          <div class="w-full menu">
            <n-input
              type="input"
              v-model:value="pattern"
              placeholder="输入菜单名称搜索"
            >
              <template #suffix>
                <n-icon size="18" class="cursor-pointer">
                  <SearchOutlined />
                </n-icon>
              </template>
            </n-input>
            <div class="py-3 menu-list">
              <template v-if="loading">
                <div class="flex items-center justify-center py-4">
                  <n-spin size="medium" />
                </div>
              </template>
              <template v-else>
                <n-tree
                  block-line
                  cascade
                  checkable
                  :virtual-scroll="true"
                  :pattern="pattern"
                  :data="treeData"
                  :expandedKeys="expandedKeys"
                  style="max-height: 650px; overflow: hidden"
                  @update:selected-keys="selectedTree"
                  @update:expanded-keys="onExpandedKeys"
                />
              </template>
            </div>
          </div>
        </n-card>
      </n-gi>
      <n-gi span="2">
        <n-card :segmented="{ content: true }" :bordered="false" size="small">
          <template #header>
            <n-space>
              <n-icon size="18">
                <FormOutlined />
              </n-icon>
              <span
                >编辑菜单{{ treeItemTitle ? `：${treeItemTitle}` : '' }}</span
              >
            </n-space>
          </template>
          <n-alert type="info" closable>
            从菜单列表选择一项后，进行编辑</n-alert
          >
          <n-form
            :model="formParams"
            :rules="rules"
            ref="formRef"
            label-placement="left"
            :label-width="100"
            v-if="isEditMenu"
            class="py-4"
          >
            <n-form-item label="类型" path="type">
              <span>{{ formParams.type === 0 ? '一级菜单' : '二级菜单' }}</span>
            </n-form-item>
            <n-form-item label="标题" path="label">
              <n-input
                placeholder="请输入标题"
                v-model:value="formParams.label"
              />
            </n-form-item>
            <n-form-item label="副标题" path="subtitle">
              <n-input
                placeholder="请输入副标题"
                v-model:value="formParams.subtitle"
              />
            </n-form-item>
            <n-form-item label="路径" path="path">
              <n-input
                placeholder="请输入路径"
                v-model:value="formParams.path"
              />
            </n-form-item>
            <n-form-item label="菜单权限" path="auth">
              <n-input
                placeholder="请输入权限，多个权限用，分割"
                v-model:value="formParams.auth"
              />
            </n-form-item>
            <n-form-item
              label="操作权限"
              path="auth"
              class="no-flex"
              v-if="operationAuth"
            >
              <BasicTable
                :columns="columns"
                :actionColumn="actionColumn"
                :pagination="false"
                size="small"
                :canResize="false"
                :dataSource="operationAuth"
              >
                <template #tableTitle>
                  <n-button size="small" strong secondary type="primary">
                    <template #icon>
                      <n-icon size="16">
                        <PlusOutlined />
                      </n-icon>
                    </template>
                    添加
                  </n-button>
                </template>
              </BasicTable>
              <!-- <n-data-table
                :columns="columns"
                :data="operationAuth"
              /> -->
              <!-- <n-space>
                <template v-for="item in operationAuth" :key="item.id">
                  <n-input-group>
                    <n-button quaternary type="default">
                      {{ item.label }}
                    </n-button>
                    <n-input
                      :style="{ width: 'auto' }"
                      v-model:value="item.auth"
                   />
                   <n-button>删除</n-button>
                  </n-input-group>
                </template>
                <n-input-group>
                  <n-button secondary type="success" @click="() => (isAuthOperation = true)">添加权限</n-button>
                  <n-input v-show="isAuthOperation" placeholder="请输入操作权限" v-model:value="newOperation">
                    
                  </n-input>
                  <n-button v-show="isAuthOperation" @click="addOperation">取消</n-button>
                </n-input-group>
              </n-space> -->
            </n-form-item>
            <n-form-item path="auth" style="margin-left: 100px">
              <n-space>
                <n-button
                  type="primary"
                  :loading="subLoading"
                  @click="formSubmit"
                  >保存修改</n-button
                >
                <n-button @click="handleReset">重置</n-button>
                <n-button @click="handleDel">删除</n-button>
              </n-space>
            </n-form-item>
          </n-form>
        </n-card>
      </n-gi>
    </n-grid>
  </div>
</template>
<script>
import {
  computed,
  defineComponent,
  h,
  onMounted,
  reactive,
  ref,
  unref,
} from 'vue';
import {
  DownOutlined,
  AlignLeftOutlined,
  SearchOutlined,
  FormOutlined,
  EditOutlined,
  CloseSquareOutlined,
  DeleteOutlined,
  SaveOutlined,
  PlusOutlined,
} from '@vicons/antd';
import { getMenuList, updateMenu } from '@/api/system/sysMenu';
import { getTreeItem, transformTreeDepth2 } from '@/utils';
import { useDialog, useMessage } from 'naive-ui';
import { BasicTable, TableAction } from '@/components/Table';
import { usePermission } from '@/hooks/web/usePermission';

const rules = {
  label: {
    required: true,
    message: '请输入标题',
    trigger: 'blur',
  },
  path: {
    required: true,
    message: '请输入路径',
    trigger: 'blur',
  },
};

const columns = [
  {
    title: '名称',
    key: 'label',
    width: 120,
  },
  {
    title: '权限',
    key: 'auth',
    edit: true,
    editRow: true,
    editComponent: 'NInput',
  },
];

export default defineComponent({
  components: {
    DownOutlined,
    AlignLeftOutlined,
    SearchOutlined,
    FormOutlined,
    BasicTable,
    PlusOutlined,
  },
  setup() {
    const formRef = ref(null);
    const message = useMessage();
    const dialog = useDialog();
    const { hasPermission } = usePermission();
    let treeItemKey = ref([]);

    let expandedKeys = ref([]);

    const treeData = ref([]);

    const loading = ref(true);
    const subLoading = ref(false);
    const isEditMenu = ref(false);
    const isAuthOperation = ref(false);
    const treeItemTitle = ref('');
    const pattern = ref('');

    const isAddSon = computed(() => {
      return !treeItemKey.value.length;
    });
    const addMenuOptions = ref([
      {
        label: '添加顶级菜单',
        key: 'home',
        disabled: false,
      },
      {
        label: '添加子菜单',
        key: 'son',
        disabled: isAddSon,
      },
    ]);

    const formParams = reactive({
      type: 1,
      label: '',
      subtitle: '',
      path: '',
      auth: '',
      openType: 1,
    });
    // 操作权限
    const operationAuth = ref([]);
    const newOperation = ref('');
    // 表格列信息
    const currentEditKeyRef = ref('');
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
        const { id, auth } = record;
        // 提交更新
        await updateMenu({ id, auth });
      }
    }

    // 单行删除
    function handleDelete(record) {
      console.log(record);
      dialog.info({
        title: '提示',
        content: `您想删除${record.label}`,
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: async () => {
          // await removeById(record.id);
          console.log(record);
          // reloadTable();
        },
        onNegativeClick: () => {},
      });
    }

    // header
    function selectAddMenu() {}

    function packHandle() {
      if (expandedKeys.value.length) {
        expandedKeys.value = [];
      } else {
        expandedKeys.value = unref(treeData).map((item) => {
          if (item.subtitle) {
            return item.subtitle;
          } else {
            item.id;
          }
        });
      }
    }

    function selectedTree(keys) {
      if (keys.length) {
        const treeItem = getTreeItem(unref(treeData), keys[0]);
        treeItemKey.value = keys;
        treeItemTitle.value = treeItem.label;
        Object.assign(formParams, treeItem);
        operationAuth.value = treeItem?.operation;
        isEditMenu.value = true;
      } else {
        isEditMenu.value = false;
        treeItemKey.value = [];
        treeItemTitle.value = '';
        operationAuth.value = [];
      }
    }

    function handleDel() {
      dialog.info({
        title: '提示',
        content: `您确定想删除此权限吗?`,
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: () => {
          message.success('删除成功');
        },
        onNegativeClick: () => {
          message.error('已取消');
        },
      });
    }

    function handleReset() {
      const treeItem = getTreeItem(unref(treeData), treeItemKey.value[0]);
      Object.assign(formParams, treeItem);
    }

    function formSubmit() {
      formRef.value?.validate(async (errors) => {
        if (!errors) {
          // 权限验证
          if (!hasPermission(['system_menu'])) {
            return message.error('抱歉，您没有该权限');
          }
          const { id, label: title, subtitle, path, auth } = formParams;
          console.log(formParams);
          await updateMenu({ id, title, subtitle, path, auth });
        } else {
          message.error('请填写完整信息');
        }
      });
    }

    function addOperation() {}

    onMounted(async () => {
      const menuList = await getMenuList();
      const keys = menuList.map((item) => item.id);
      Object.assign(formParams, keys);
      const { treeMenuList } = transformTreeDepth2(menuList);
      treeData.value = treeMenuList;
      loading.value = false;
    });

    function onExpandedKeys(keys) {
      expandedKeys.value = keys;
    }

    return {
      rules,
      formRef,
      treeData,
      formParams,
      operationAuth,
      loading,
      subLoading,
      isEditMenu,
      pattern,
      expandedKeys,
      addMenuOptions,
      treeItemTitle,
      isAuthOperation,
      newOperation,
      selectAddMenu,
      packHandle,
      selectedTree,
      onExpandedKeys,
      handleDel,
      handleReset,
      addOperation,
      formSubmit,
      columns,
      actionColumn,
    };
  },
});
</script>
<style>
.no-flex .n-form-item-blank {
  display: block;
}
</style>
