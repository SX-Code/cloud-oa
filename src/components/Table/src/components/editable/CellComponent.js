import { componentMap } from '@/components/Table/src/componentMap';
import { NPopover } from 'naive-ui';
import { h } from 'vue';

// 定义组件
export const CellComponent = (
  { component = 'NInput', rule = true, ruleMessage, popoverVisible },
  { attrs }
) => {
  const Comp = componentMap.get(component);

  // 要渲染的组件
  const DefaultComp = h(Comp, attrs);
  if (!rule) {
    return DefaultComp;
  }
  // 显示rule提示
  return h(
    NPopover,
    { 'display-directive': 'show', show: !!popoverVisible, manual: 'manual' },
    {
      trigger: () => DefaultComp,
      default: () =>
        h(
          'span',
          {
            style: {
              color: 'red',
              width: '90px',
              dispaly: 'inline-block',
            },
          },
          {
            default: () => ruleMessage,
          }
        ),
    }
  );
};
