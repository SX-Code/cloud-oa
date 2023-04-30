<template>
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
            @click="setProcessTemplate"
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
    <n-modal v-model:show="formDialogVisible">
      <n-card
        style="width: 600px"
        title="查看审批设置"
        preset="card"
        :bordered="false"
        size="huge"
      >
        <h3>基本信息</h3>
        <n-divider />
        <n-form
          :model="templateFormModel"
          label-placement="left"
          :show-feedback="false"
        >
          <n-form-item label="审批类型">
            {{ templateFormModel.processTypeName }}
          </n-form-item>
          <n-form-item label="名称"> {{ templateFormModel.name }} </n-form-item>
          <n-form-item label="创建时间">
            {{ templateFormModel.createTime }}
          </n-form-item>
        </n-form>
        <h3>表单信息</h3>
        <n-divider />
        <div>
          <FormCreate
            :rule="formCreateData.rule"
            :option="formCreateData.options"
          />
        </div>
        <template #footer>
          <div style="display: flex; justify-content: flex-end">
            <n-button @click="formDialogVisible = false">确定</n-button>
          </div>
        </template>
      </n-card>
    </n-modal>
  </n-card>
</template>
<script>
export default {
  name: 'ProcessTemplate',
};
</script>

<script setup>
import {
  pageProcessTemplate,
  removeProcessTemplate,
  publishProcessTemplate,
} from '@/api/process/processTemplate';
import { usePermission } from '@/hooks/web/usePermission';
import { useDialog } from 'naive-ui';
import { computed, h, reactive, ref, unref } from 'vue';
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  EyeOutlined,
} from '@vicons/antd';
import { PaperPlaneOutline } from '@vicons/ionicons5';
import { BasicTable, TableAction } from '@/components/Table';
import { createColumns } from './columns';
import { useRouter } from 'vue-router';
import formCreate from '@form-create/naive-ui';

const FormCreate = formCreate.$form();
const router = useRouter();
const dialog = useDialog();
const actionRef = ref();
const templateFormModel = ref(null);
const formDialogVisible = ref(false);
const { hasPermission } = usePermission();
const currentIndex = ref(1);
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
  return hasPermission(['process_template_set']);
});
const formCreateData = reactive({
  rule: null,
  options: null,
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
  return [
    {
      label: '查看',
      type: 'primary',
      icon: EyeOutlined,
      onClick: handleView.bind(null, record),
      auth: ['process_template_list'],
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
      auth: ['process_template_update'],
    },
    {
      label: '发布',
      type: 'success',
      icon: PaperPlaneOutline,
      onClick: handlePublish.bind(null, record),
      ifShow: () => {
        return record.status === 0;
      },
      auth: ['process_template_publish'],
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
      auth: ['process_template_remove'],
    },
  ];
}

// 跳转到模板添加页面
function setProcessTemplate() {
  router.push('/process/set');
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
    (actionRef.value.pagination.page - 1) * actionRef.value.pagination.pageSize;
}
// 加载表格数据
const loadDataTable = async (res) => {
  return await pageProcessTemplate({ ...res });
};
// 查看点击事件
function handleView(record) {
  formCreateData.rule = JSON.parse(record.formProps);
  formCreateData.options = JSON.parse(record.formOptions);
  templateFormModel.value = record;
  formDialogVisible.value = true;
}
// 编辑点击事件
function handleEdit() {}
// 发布点击事件
async function handlePublish(record) {
  await publishProcessTemplate(record.id);
  reloadTable();
}
// 单行删除
function handleDelete(record) {
  dialog.info({
    title: '提示',
    content: `您想删除${record.name}`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      await removeProcessTemplate(record.id);
      reloadTable();
    },
    onNegativeClick: () => {},
  });
}
</script>
