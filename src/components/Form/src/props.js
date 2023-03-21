export const basicProps = {
  labelWidth: {
    type: [Number, String],
    default: 80,
  },
  // 表单配置规则
  schemas: {
    type: [Array],
    default: () => [],
  },
  // 布局方式
  layout: {
    type: String,
    default: 'inline',
  },
  // 是否显示行为内表单
  inline: {
    type: Boolean,
    default: false,
  },
  // 大小
  size: {
    type: String,
    default: 'medium',
  },
  // 标签位置
  labelPlacement: {
    type: String,
    default: 'left',
  },
  // 组件是否width 100%
  isFull: {
    type: Boolean,
    default: true,
  },
  // 是否显示操作按钮（查询/重置）
  showActionButtonGroup: {
    default: true,
  },
  // 显示重置按钮
  showResetButton: {
    default: true,
  },
  //重置按钮配置
  resetButtonOptions: Object,
  // 显示确认按钮
  showSubmitButton: {
    default: true,
  },
  // 确认按钮配置
  submitButtonOptions: Object,
  //展开收起按钮
  showAdvancedButton: {
    default: true,
  },
  // 确认按钮文字
  submitButtonText: {
    type: String,
    default: '查询',
  },
  //重置按钮文字
  resetButtonText: {
    type: String,
    default: '重置',
  },
  //grid 配置
  gridProps: Object,
  //gi配置
  giProps: Object,
  //grid 样式
  baseGridStyle: {
    type: Object,
  },
  //是否折叠
  collapsed: {
    type: Boolean,
    default: false,
  },
  //默认展示的行数
  collapsedRows: {
    type: Number,
    default: 1,
  },
};
