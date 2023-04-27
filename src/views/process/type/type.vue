<template :bordered="false" class="proCard table">
  <n-card>
    <BasicTable
      :columns="columns"
      :request="loadDataTable"
      :row-key="(row) => row.id"
      ref="actionRef"
      :actionColumn="actionColumn"
      :scroll-x="1000"
      :pagination="pagination"
      @fetch-success="fetchSuccess"
    >
      <template #tableTitle>
        <n-space>
          <!-- 新增 -->
          <n-button
            v-if="hasAddPermision"
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
            添加
          </n-button>
        </n-space>
      </template>
    </BasicTable>
    <n-modal
      v-model:show="showAddModal"
      v-if="hasAddPermision"
      :show-icon="false"
      preset="dialog"
      title="新建审批类型"
      @after-leave="clearForm"
    >
      <n-form
        :model="typeFormParams"
        :rules="rules"
        ref="typeFormRef"
        label-placement="left"
        :label-width="80"
        class="py-4"
      >
        <n-form-item label="名称" path="name">
          <n-input
            placeholder="请输入类型名"
            v-model:value="typeFormParams.name"
            clearable
          />
        </n-form-item>
        <n-form-item label="描述" path="description">
          <n-input
            placeholder="请输入类型描述"
            v-model:value="typeFormParams.description"
            clearable
          />
        </n-form-item>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="() => (showAddModal = false)">取消</n-button>
          <n-button
            type="info"
            :loading="formBtnLoading"
            @click="confirmTypeForm"
            >确定</n-button
          >
        </n-space>
      </template>
    </n-modal>
  </n-card>
</template>
<script>
import { computed, defineComponent, h, reactive, ref, unref } from 'vue';
import { BasicTable, TableAction } from '@/components/Table';
import { createColumns } from './columns';
import {
  pageProcessType,
  removeProcessType,
  updateProcessType,
} from '@/api/process/processType';
import {
  PlusOutlined,
  DeleteOutlined,
  EditOutlined,
  SaveOutlined,
  CloseSquareOutlined,
} from '@vicons/antd';
import { usePermission } from '@/hooks/web/usePermission';
import { useDialog } from 'naive-ui';

const rules = {
  name: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入类型名称',
  },
};

export default defineComponent({
  components: { BasicTable, PlusOutlined },
  setup() {
    const dialog = useDialog();
    const actionRef = ref();
    const typeFormRef = ref();
    const { hasPermission } = usePermission();
    const currentEditKeyRef = ref('');
    const currentIndex = ref(1);
    const formBtnLoading = ref(false);
    const showAddModal = ref(false);
    const typeFormParams = reactive({
      name: '',
      description: '',
    });
    const pagination = reactive({
      prefix: (info) => {
        return `共 ${info.itemCount} 条`;
      },
    });

    const columns = createColumns({
      currentIndex() {
        return unref(currentIndex);
      },
    });

    const hasAddPermision = computed(() => {
      return hasPermission(['process_type_add']);
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

    function createActions(record) {
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
            // 根据权限控制是否显示: 有权限，会显示，支持多个
            auth: ['process_type_update'],
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
            auth: ['process_type_remove'],
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

    /**
     * 表格处理事件
     */
    // 刷新表格
    function reloadTable() {
      actionRef.value.reload();
    }
    function fetchSuccess() {
      currentIndex.value =
        (actionRef.value.pagination.page - 1) *
        actionRef.value.pagination.pageSize;
    }
    // 加载表格数据
    const loadDataTable = async (res) => {
      return await pageProcessType({ ...res });
    };
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
        const { id, name, description } = record;
        // 提交更新
        await updateProcessType({ id, name, description });
      }
    }
    // 单行删除
    function handleDelete(record) {
      dialog.info({
        title: '提示',
        content: `您想删除${record.name}`,
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: async () => {
          await removeProcessType(record.id);
          reloadTable();
        },
        onNegativeClick: () => {},
      });
    }

    function clearForm() {
      typeFormParams.name = '';
      typeFormParams.description = '';
    }

    function confirmTypeForm() {
      console.log(typeFormParams);
    }

    return {
      rules,
      columns,
      actionRef,
      typeFormRef,
      pagination,
      showAddModal,
      actionColumn,
      typeFormParams,
      formBtnLoading,
      hasAddPermision,
      loadDataTable,
      fetchSuccess,
      clearForm,
      confirmTypeForm,
    };
  },
});
</script>
<style scoped></style>
