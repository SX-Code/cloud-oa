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
      title: '审批编号',
      key: 'processCode',
    },
    {
      title: '标题',
      key: 'title',
    },
    {
      title: '用户',
      key: 'name',
    },
    {
      title: '审批类型',
      key: 'processTypeName',
    },
    {
      title: '审批模版',
      key: 'processTemplateName',
    },
    {
      title: '描述',
      key: 'description',
    },
    {
      title: '状态',
      key: 'status',
      align: 'center',
      width: 60,
    },
    {
      title: '创建时间',
      key: 'createTime',
    },
  ];
};
