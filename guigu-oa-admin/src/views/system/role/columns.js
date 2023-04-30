export const columns = [
  {
    type: 'selection',
    key: 'selection',
  },
  {
    title: 'id',
    key: 'id',
  },
  {
    title: '角色名称',
    key: 'roleName',
    edit: true,
    editRow: true,
    editComponent: 'NInput',
    editRule: true,
  },
  {
    title: '角色编码',
    key: 'roleCode',
    editComponent: 'NInput',
    edit: true,
    editRow: true,
  },
  {
    title: '创建时间',
    key: 'createTime',
  },
];
