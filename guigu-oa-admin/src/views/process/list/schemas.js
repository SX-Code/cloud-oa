export const schemas = [
  {
    field: 'keyword',
    component: 'NInput',
    label: '关键词',
    defaultValue: '',
    labelMessge: '关键词',
    componentProps: {
      placeholder: '审批编号/标题/手机号/姓名',
    },
  },
  {
    field: 'status',
    component: 'NSelect',
    label: '状态',
    defaultValue: null,
    labelMessge: '选择状态',
    componentProps: {
      placeholder: '请选状态',
      options: [
        { label: '默认', value: 0 },
        { label: '审批中', value: 1 },
        { label: '审批通过', value: 2 },
        { label: '驳回', value: -1 },
      ],
    },
  },
  {
    field: 'range',
    component: 'NDatePicker',
    label: '操作时间',
    labelMessge: '操作时间',
    componentProps: {
      type: 'datetimerange',
      clearable: true,
      shortcuts: {
        近2小时: () => {
          const cur = new Date().getTime();
          return [cur - 2 * 60 * 60 * 1000, cur];
        },
      },
    },
  },
];
