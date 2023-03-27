import { NSwitch } from 'naive-ui';
import { h } from 'vue';

export const createColumns = ({ currentIndex, switchStatus }) => {
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
      title: '用户名',
      key: 'username',
      editRule: true,
    },
    {
      title: '姓名',
      key: 'name',
      editRule: true,
    },
    {
      title: '手机',
      key: 'phone',
      width: 120,
      editRule: true,
    },
    {
      title: '岗位',
      key: 'dept',
      editRule: true,
    },
    {
      title: '部门',
      key: 'post',
      editRule: true,
    },
    {
      title: '所属角色',
      key: 'roleName',
      editRule: true,
    },
    {
      title: '状态',
      key: 'status',
      render(row) {
        return h(NSwitch, {
          value: row.status === 1 ? true : false,
          onClick: () => switchStatus(row),
        });
      },
    },
    {
      title: '创建时间',
      key: 'createTime',
    },
  ];
};
