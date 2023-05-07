import { NTag } from 'naive-ui';
import { h } from 'vue';

export const columns = [
  {
    title: '名称',
    key: 'name',
    width: 250,
    edit: true,
    editRule: true,
    editRow: true,
    editComponent: 'NInput',
  },
  {
    title: '类型',
    key: 'type',
    render(row) {
      if (!row.type) return;
      return h(
        NTag,
        {
          type: 'info',
          bordered: false,
        },
        {
          default: () => {
            if (row.type === 'view') {
              return '网页';
            }
            if (row.type === 'click') {
              return '点击';
            }
            if (row.type === 'miniprogram') {
              return '小程序';
            }
          },
        }
      );
    },
  },
  {
    title: '菜单URL',
    key: 'url',
    edit: true,
    editRow: true,
    editComponent: 'NInput',
  },
  {
    title: '菜单KEY',
    key: 'menuKey',
    edit: true,
    editRow: true,
    editComponent: 'NInput',
  },
  {
    title: '排序号',
    key: 'sort',
    edit: true,
    editRow: true,
    editComponent: 'NInputNumber',
  },
];
