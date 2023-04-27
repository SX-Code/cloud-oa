import { NAvatar } from 'naive-ui';
import { h } from 'vue';

export const createColumns = ({ currentIndex }) => {
  return [
    {
      type: 'selection',
      key: 'selection',
    },
    {
      title: '序号',
      key: 'index',
      width: 60,
      render: (...[, record]) => {
        return currentIndex() + record + 1;
      },
    },
    {
      title: '审批名称',
      key: 'name',
      edit: true,
      editComponent: 'NInput',
      editRule: true,
    },
    {
      title: '图标',
      key: 'iconUrl',
      width: 100,
      render(row) {
        return h(NAvatar, {
          size: 36,
          src: row.iconUrl,
        });
      },
    },
    {
      title: '审批类型',
      key: 'processTypeName',
    },
    {
      title: '描述',
      key: 'description',
      edit: true,
      editComponent: 'NInput',
    },
    {
      title: '创建时间',
      key: 'createTime',
    },
    {
      title: '修改时间',
      key: 'updateTime',
    },
  ];
};
