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
      title: '类型名称',
      key: 'name',
      edit: true,
      editRow: true,
      editComponent: 'NInput',
      editRule: true,
    },
    {
      title: '描述',
      key: 'description',
      editComponent: 'NInput',
      edit: true,
      editRow: true,
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
