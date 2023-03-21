import {
  isArray,
  isFunction,
  isNullOrUnDef,
  isObject,
  isString,
} from '@/utils/is';
import { set } from 'lodash-es';
import { unref } from 'vue';

export function useFormValues({ defaultFormModel, getSchema, formModel }) {
  // 加工 form values
  function handleFormValues(values) {
    if (!isObject(values)) {
      return {};
    }
    const res = {};
    for (const item of Object.entries(values)) {
      let [, value] = item;
      const [key] = item;
      if (
        !key ||
        (isArray(value) && value.length === 0) ||
        isFunction(value) ||
        isNullOrUnDef(value)
      ) {
        continue;
      }
      // 删除空格
      if (isString(value)) {
        value = value.trim();
      }
      set(res, key, value);
    }
    return res;
  }

  // 初始化默认值
  function initDefault() {
    const schemas = unref(getSchema);
    const obj = {};
    schemas.forEach((item) => {
      const { defaultValue } = item;
      if (!isNullOrUnDef(defaultValue)) {
        obj[item.field] = defaultValue;
        formModel[item.field] = defaultValue;
      }
    });
    defaultFormModel.value = obj;
  }

  return { handleFormValues, initDefault };
}
