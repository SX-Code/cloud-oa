import { NAvatar } from 'naive-ui';
import { h } from 'vue';

export const renderSingleSelectTag = ({ option }) => {
  return h(
    'div',
    {
      style: {
        display: 'flex',
        alignItems: 'center',
      },
    },
    [
      h(NAvatar, {
        src: option.value,
        size: 24,
      }),
    ]
  );
};

export const renderLabel = (option) => {
  return h(
    'div',
    {
      style: {
        display: 'flex',
        alignItems: 'center',
      },
    },
    [
      h(NAvatar, {
        src: option.value,
        size: 'small',
      }),
    ]
  );
};

export const icons = [
  {
    label: '出差',
    value: 'https://gw.alicdn.com/tfs/TB1bHOWCSzqK1RjSZFjXXblCFXa-112-112.png',
  },
  {
    label: '合同审批',
    value:
      'https://gw.alicdn.com/imgextra/i3/O1CN01LLn0YV1LhBXs7T2iO_!!6000000001330-2-tps-120-120.png',
  },
  {
    label: '合同借阅',
    value: 'https://gw.alicdn.com/tfs/TB1e76lCOLaK1RjSZFxXXamPFXa-112-112.png',
  },
  {
    label: '机票出差',
    value: 'https://gw.alicdn.com/tfs/TB1cbCYCPTpK1RjSZKPXXa3UpXa-112-112.png',
  },
  {
    label: '补卡申请',
    value: 'https://gw.alicdn.com/tfs/TB1Yfa0CG6qK1RjSZFmXXX0PFXa-112-112.png',
  },
  {
    label: '加班',
    value: 'https://gw.alicdn.com/tfs/TB1Y8PlCNjaK1RjSZKzXXXVwXXa-112-112.png',
  },
  {
    label: '请假',
    value: 'https://gw.alicdn.com/tfs/TB1_YG.COrpK1RjSZFhXXXSdXXa-102-102.png',
  },
  {
    label: '调岗',
    value: 'https://gw.alicdn.com/tfs/TB13ca1CMDqK1RjSZSyXXaxEVXa-102-102.png',
  },
  {
    label: '离职',
    value: 'https://gw.alicdn.com/tfs/TB1U9iBCSzqK1RjSZPcXXbTepXa-102-102.png',
  },
];
