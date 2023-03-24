import { NDataTable } from 'naive-ui';
export const basicProps = {
  ...NDataTable.props, // 这里继承原 UI 组件的 props
  title: {
    type: String,
    default: null,
  },
  titleTooltip: {
    type: String,
    default: null,
  },
  size: {
    type: String,
    default: 'medium',
  },
  dataSource: {
    type: [Object],
    default: () => [],
  },
  columns: {
    type: [Array],
    default: () => [],
    required: true,
  },
  beforeRequest: {
    type: Function,
    default: null,
  },
  request: {
    type: Function,
    default: null,
  },
  afterRequest: {
    type: Function,
    default: null,
  },
  rowKey: {
    type: [String, Function],
    default: undefined,
  },
  pagination: {
    type: [Object, Boolean],
    default: () => {},
  },
  //废弃
  showPagination: {
    type: [String, Boolean],
    default: 'auto',
  },
  actionColumn: {
    type: Object,
    default: null,
  },
  canResize: {
    type: Boolean,
    default: true,
  },
  resizeHeightOffset: {
    type: Number,
    default: 0,
  },
};
