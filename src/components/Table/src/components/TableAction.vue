<template>
  <div class="tableAction">
    <div class="flex items-center justify-center">
      <template
        v-for="(action, index) in getActions"
        :key="`${index}-${action.lable}`"
      >
        <n-button v-if="showLabel" v-bind="action" class="mx-2">
          {{ action.label }}
          <template #icon v-if="action.hasOwnProperty('icon')">
            <n-icon :component="action.icon" />
          </template>
        </n-button>
        <n-tooltip v-else>
          <template #trigger>
            <n-button v-bind="action" class="mx-2">
              <template #icon v-if="action.hasOwnProperty('icon')">
                <n-icon :component="action.icon" />
              </template>
            </n-button>
          </template>
          {{ action.label }}
        </n-tooltip>
      </template>
      <!-- 下拉菜单情况 -->
      <n-dropdown
        v-if="dropDownActions && getDropdownList.length"
        trigger="hover"
        :options="getDropdownList"
        @select="select"
      >
        <slot name="more"></slot>
        <n-button
          v-bind="getMoreProps"
          class="mx-2"
          v-if="!$slots.more"
          icon-placement="right"
        >
          <div class="flex items-center">
            <span>更多</span>
            <n-icon size="14" class="ml-1">
              <DownOutlined />
            </n-icon>
          </div>
        </n-button>
      </n-dropdown>
    </div>
  </div>
</template>
<script>
import { computed, defineComponent, toRaw } from 'vue';
import { DownOutlined } from '@vicons/antd';
import { usePermission } from '@/hooks/web/usePermission';
import { isBoolean, isFunction } from '@/utils/is';
export default defineComponent({
  name: 'TableAction',
  components: { DownOutlined },
  props: {
    actions: {
      type: Array,
      default: null,
      required: true,
    },
    dropDownActions: {
      type: Array,
      default: null,
    },
    style: {
      type: String,
      default: 'button',
    },
    showLabel: {
      type: Boolean,
      default: true,
    },
    select: {
      type: Function,
      default: () => {},
    },
  },
  setup(props) {
    const { hasPermission } = usePermission();

    // 渲染组件类型
    const actionType =
      props.style === 'button'
        ? 'defalut'
        : props.style === 'text'
        ? 'primary'
        : 'default';
    const actionText =
      props.style === 'button'
        ? undefined
        : props.style === 'text'
        ? true
        : undefined;

    const getMoreProps = computed(() => {
      return {
        text: actionText,
        type: actionType,
        size: 'small',
      };
    });

    // 获取更多操作的下拉菜单选项
    const getDropdownList = computed(() => {
      return (toRaw(props.dropDownActions) || [])
        .filter((action) => {
          return hasPermission(action.auth) && isIfShow(action);
        })
        .map((action) => {
          const { popConfirm } = action;
          return {
            size: 'small',
            text: actionText,
            type: actionType,
            ...action,
            ...popConfirm,
            onConfirm: popConfirm?.confirm,
            onCancel: popConfirm?.cancel,
          };
        });
    });

    function isIfShow(action) {
      const ifShow = action.ifShow;

      let isIfShow = true;

      if (isBoolean(ifShow)) {
        isIfShow = ifShow;
      }
      if (isFunction(ifShow)) {
        isIfShow = ifShow(action);
      }
      return isIfShow;
    }

    // 获取操作选项
    const getActions = computed(() => {
      return (toRaw(props.actions) || [])
        .filter((action) => {
          return hasPermission(action.auth) && isIfShow(action);
        })
        .map((action) => {
          const { popConfirm } = action;
          //需要展示什么风格，自己修改一下参数
          return {
            size: 'small',
            text: actionText,
            type: actionType,
            ...action,
            ...(confirm || {}),
            onConfirm: popConfirm?.confirm,
            onCancel: popConfirm?.cancel,
            enable: !!popConfirm,
          };
        });
    });

    return {
      getActions,
      getMoreProps,
      getDropdownList,
    };
  },
});
</script>
