<template>
  <div class="editable-cell">
    <div v-show="!isEdit" class="editable-cell-content" @click="handleEdit">
      {{ getValues }}
      <!-- 整行编辑时，不显示单元格的编辑图标 -->
      <n-icon class="edit-icon" v-if="!column.editRow">
        <FormOutlined />
      </n-icon>
    </div>
    <div
      class="flex editable-cell-content"
      v-show="isEdit"
      v-click-outside="onClickOutside"
    >
      <div class="editable-cell-content">
        <CellComponent
          v-bind="getComponentProps"
          :component="getComponent"
          :popoverVisible="getRuleVisible"
          :ruleMessage="ruleMessage"
          :rule="getRule"
          :class="getWrapperClass"
          ref="elRef"
          @option-change="handleOptionsChange"
          @pressEnter="handleEnter"
        />
      </div>
      <!-- 可编辑cell的提交和取消按钮，整行编辑时将不显示 -->
      <div class="editable-cell-action" v-if="!getRowEditable">
        <n-icon class="mx-2 cursor-pointer">
          <CheckOutlined @click="handleSubmit" />
        </n-icon>
        <n-icon class="mx-2 cursor-pointer">
          <CloseOutlined @click="handleCancel" />
        </n-icon>
      </div>
    </div>
  </div>
</template>
<script>
import { isFunction, isNumber, isBoolean, isString } from '@/utils/is';
import { isArray, omit, set } from 'lodash-es';
import {
  computed,
  defineComponent,
  nextTick,
  ref,
  toRaw,
  unref,
  watchEffect,
} from 'vue';
import { EventEnum } from '@/components/Table/src/componentMap';
import { FormOutlined, CloseOutlined, CheckOutlined } from '@vicons/antd';
import { useTableContext } from '../../hooks/useTableContext';
import { CellComponent } from './CellComponent';
import clickOutside from '@/directives/clickOutside';
import { createPlaceholderMessage } from './helper';
import { format, parseISO } from 'date-fns';

export default defineComponent({
  name: 'EditableCell',
  components: { FormOutlined, CloseOutlined, CheckOutlined, CellComponent },
  directives: {
    clickOutside,
  },
  props: {
    value: {
      type: [String, Number, Boolean, Object],
      default: '',
    },
    record: {
      type: Object,
    },
    column: {
      type: Object,
      default: () => ({}),
    },
    index: {
      type: Number,
    },
  },
  setup(props) {
    const table = useTableContext();
    const isEdit = ref(false);
    const elRef = ref();
    const ruleVisible = ref(false);
    const ruleMessage = ref('');
    const optionsRef = ref([]); // 提供给有选项的组件
    const currentValueRef = ref(props.value);
    const defaultValueRef = ref(props.value);

    // 可编辑组件的类型，默认Input
    const getComponent = computed(
      () => props.column?.editComponent || 'NInput'
    );
    const getRule = computed(() => props.column?.editRule);

    const getRuleVisible = computed(() => {
      return unref(ruleMessage) && unref(ruleVisible);
    });

    const getIsCheckComp = computed(() => {
      const component = unref(getComponent);
      return ['NCheckbox', 'NRadio'].includes(component);
    });

    // 获取组件属性
    const getComponentProps = computed(() => {
      const compProps = props.column?.editComponentProps ?? {};
      const editComponent = props.column?.editComponent ?? null;
      const component = unref(getComponent);
      const apiSelectProps = {};

      const isCheckValue = unref(getIsCheckComp);

      // value字段名，如果是NCheckbox或者NRadio，则是checked，否则是value字段
      let valueField = isCheckValue ? 'checked' : 'value';
      const val = unref(currentValueRef);

      let value = isCheckValue
        ? isNumber(val) && isBoolean(val)
          ? val
          : !!val
        : val;
      //TODO 特殊处理 NDatePicker 可能要根据项目 规范自行调整代码
      if (component === 'NDatePicker') {
        if (isString(value)) {
          if (compProps.valueFormat) {
            valueField = 'formatted-value';
          } else {
            value = parseISO(value).getTime();
          }
        } else if (isArray(value)) {
          if (compProps.valueFormat) {
            valueField = 'formatted-value';
          } else {
            value = value.map((item) => parseISO(item).getTime());
          }
        }
      }

      const onEvent = editComponent ? EventEnum[editComponent] : undefined;

      return {
        placeholder: createPlaceholderMessage(unref(getComponent)),
        ...apiSelectProps,
        ...omit(compProps, 'OnChange'),
        [onEvent]: handleChange,
        [valueField]: value,
      };
    });

    // 组件的值，针对不同组件做了判断处理
    const getValues = computed(() => {
      const { editComponentProps, editValueMap } = props.column;

      const value = unref(currentValueRef);

      if (editValueMap && isFunction(editValueMap)) {
        return editValueMap(value);
      }

      const component = unref(getComponent);

      // Select需要单独处理
      if (!component.includes('NSelect')) {
        return value;
      }

      const options = editComponentProps?.options ?? (unref(optionsRef) || []);
      const option = options.find((item) => `${item.value}` === `${value}`);

      return option?.label ?? value;
    });

    // 组件样式class，align
    const getWrapperClass = computed(() => {
      const { align = 'center' } = props.column;
      return `edit-cell-align-${align}`;
    });
    // 查看是否开启了整行编辑
    const getRowEditable = computed(() => {
      const { editable } = props.record || {};
      return !!editable;
    });

    watchEffect(() => {
      defaultValueRef.value = props.value;
    });

    watchEffect(() => {
      const { editable } = props.column;
      if (isBoolean(editable) || isBoolean(unref(getRowEditable))) {
        // editable为true时以编辑模式显示
        isEdit.value = !!editable || unref(getRowEditable);
      }
    });

    // 处理单元格的编辑，处理获取焦点
    function handleEdit() {
      // 如果开启了整行编辑，直接return
      if (unref(getRowEditable) || unref(props.column?.editRow)) return;
      ruleMessage.value = '';
      // 手动设置，显示编辑模式
      isEdit.value = true;
      nextTick(() => {
        const el = unref(elRef);
        el?.focus?.();
      });
    }

    // 处理数据更改事件，主要更改currentValue
    async function handleChange(e) {
      const component = unref(getComponent);
      const compProps = props.column?.editComponentProps ?? {};
      if (!e) {
        currentValueRef.value = e;
      } else if (e.target && Reflect.has(e.target, 'value')) {
        currentValueRef.value = e.target.value;
      } else if (component === 'NCheckbox') {
        currentValueRef.value = e.target.checked;
      } else if (isString(e) || isBoolean(e) || isNumber(e)) {
        currentValueRef.value = e;
      }

      //TODO 特殊处理 NDatePicker 可能要根据项目 规范自行调整代码
      if (component === 'NDatePicker') {
        if (isNumber(currentValueRef.value)) {
          if (compProps.valueFormat) {
            currentValueRef.value = format(
              currentValueRef.value,
              compProps.valueFormat
            );
          }
        } else if (isArray(currentValueRef.value)) {
          if (compProps.valueFormat) {
            currentValueRef.value = currentValueRef.value.map((item) => {
              format(item, compProps.valueFormat);
            });
          }
        }
      }
      // 提交给该组件提供的onChange事件
      const onChange = props.column?.editComponentProps?.onChange;
      if (onChange && isFunction(onChange)) onChange(...arguments);
      // 触发table的edit-change方法
      table.emit?.('edit-change', {
        column: props.column,
        value: unref(currentValueRef),
        record: toRaw(props.record),
      });
      // 每次更改都需要校验
      await handleSubmiRule();
    }

    // 提交前的规则验证，包括非空校验，规则匹配校验
    async function handleSubmiRule() {
      const { column, record } = props;
      const { editRule } = column;
      const currentValue = unref(currentValueRef);
      if (editRule) {
        if (isBoolean(editRule) && !currentValue && !isNumber(currentValue)) {
          // 提交要求是true，且currentValue未定义，即空值验证
          ruleVisible.value = true;
          const component = unref(getComponent);
          // 根据组件选择提示信息
          ruleMessage.value = createPlaceholderMessage(component);
          return false;
        }
        if (isFunction(editRule)) {
          // 具体的验证规则，使用提供的验证方法验证数据
          const res = await editRule(currentValue, record);
          if (res) {
            ruleMessage.value = res;
            ruleVisible.value = true;
            return false;
          } else {
            // 成功，可以提交
            ruleMessage.value = '';
            return true;
          }
        }
      }
      // 不校验，可以提交
      ruleMessage.value = '';
      return true;
    }

    // 处理提交事件
    async function handleSubmit(needEmit = true, valid = true) {
      if (valid) {
        const isPass = await handleSubmiRule();
        if (!isPass) return false;
      }

      const { column, index, record } = props;
      if (!record) return false;
      const { key } = column;
      const value = unref(currentValueRef);
      if (!key) return;

      const dataKey = key;

      set(record, dataKey, value);
      // 触发父组件的edit-end函数
      needEmit && table.emit?.('edit-end', { record, index, key, value });
      isEdit.value = false;
    }

    // 处理回车Enter事件
    async function handleEnter() {
      if (props.column?.editRow) {
        return;
      }
      await handleSubmit();
    }

    // 取消事件处理
    function handleCancel() {
      isEdit.value = false;
      // 恢复原始数据
      currentValueRef.value = defaultValueRef.value;
      const { column, index, record } = props;
      const { key } = column;
      ruleVisible.value = true;
      ruleMessage.value = '';
      // 触发父组件的edit-cancel函数
      table.emit?.('edit-cancel', {
        record,
        index,
        key: key,
        value: unref(currentValueRef),
      });
    }

    // 点击外部相应事件
    function onClickOutside() {
      if (props.column?.editable || unref(getRowEditable)) {
        return;
      }
      const component = unref(getComponent);

      if (component.includes('NInput')) {
        handleCancel();
      }
    }

    // 只有可选择的组件才会响应
    function handleOptionsChange(options) {
      optionsRef.value = options;
    }

    // 可编辑单元格的相应事件存放到record回调中
    function initCbs(cbs, handle) {
      if (props.record) {
        /* eslint-disable  */
        isArray(props.record[cbs]) ? props.record[cbs]?.push(handle) : (props.record[cbs] = [handle]);
      }
    }

    if (props.record) {
      initCbs('submitCbs', handleSubmit);
      initCbs('validCbs', handleSubmiRule);
      initCbs('cancelCbs', handleCancel);
      if (props.column.key) {
        if (!props.record.editValueRefs) props.record.editValueRefs = {};
        props.record.editValueRefs[props.column.key] = currentValueRef;
      }
      // 给 record 绑定 onCancelEdit 函数
      /* eslint-disable  */
      props.record.onCancelEdit = () => {
        isArray(props.record?.cancelCbs) && props.record?.cancelCbs.forEach((fn) => fn());
      };

      // 给 record 绑定 onSubmitEdit 函数
      /* eslint-disable */
      props.record.onSubmitEdit = async () => {
        if (isArray(props.record?.submitCbs)) {
          const validFns = (props.record?.validCbs || []).map((fn) => fn());

          const res = await Promise.all(validFns);

          const pass = res.every((item) => !!item);

          if (!pass) return;
          const submitFns = props.record?.submitCbs || [];
          submitFns.forEach((fn) => fn(false, false));
          // 该行所有列均提交，触发table的edit-row-end
          table.emit?.('edit-row-end');
          return true;
        }
      };
    }

    return {
      isEdit,
      handleEdit,
      currentValueRef,
      handleSubmit,
      handleChange,
      handleCancel,
      elRef,
      getComponent,
      getRule,
      onClickOutside,
      ruleMessage,
      getRuleVisible,
      getComponentProps,
      handleOptionsChange,
      getWrapperClass,
      getRowEditable,
      getValues,
      handleEnter,
    };
  },
});
</script>
<style lang="less">
  .editable-cell {
    &-content {
      position: relative;
      overflow-wrap: break-word;
      word-break: break-word;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;

      &-comp {
        flex: 1;
      }

      .edit-icon {
        font-size: 14px;
        //position: absolute;
        //top: 4px;
        //right: 0;
        display: none;
        width: 20px;
        cursor: pointer;
      }

      &:hover {
        .edit-icon {
          display: inline-block;
        }
      }
    }

    &-action {
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
</style>
