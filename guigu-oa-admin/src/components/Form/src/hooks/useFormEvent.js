import { isFunction } from '@/utils/is';
import { toRaw, unref } from 'vue';

/**
 * 表格事件钩子函数
 * @param {Object} param0
 */
export function useFormEvents({
  emit,
  getProps,
  formModel,
  getSchema,
  formElRef,
  defaultFormModel,
  loadingSub,
  handleFormValues,
}) {
  // 验证
  async function validate() {
    return unref(formElRef)?.validate();
  }

  // 提交
  async function handleSubmit(e) {
    e && e.preventDefault();
    loadingSub.value = true;
    const { submitFunc } = unref(getProps);
    if (submitFunc && isFunction(submitFunc)) {
      await submitFunc();
      return;
    }
    const formEl = unref(formElRef);
    if (!formEl) return;
    try {
      await validate();
      loadingSub.value = false;
      emit('submit', formModel);
      return;
    } catch (error) {
      loadingSub.value = false;
      return;
    }
  }

  // 清空校验
  async function clearValidate() {
    await unref(formElRef)?.restoreValidation();
  }

  // 重置
  async function resetFields() {
    const { resetFunc, submitOnReset } = unref(getProps);
    resetFields && isFunction(resetFunc) && (await resetFunc());

    const formEl = unref(formElRef);
    if (!formEl) return;
    Object.keys(formModel).forEach((key) => {
      formModel[key] = unref(defaultFormModel)[key] || null;
    });
    await clearValidate();
    const formValues = handleFormValues(toRaw(unref(formModel)));
    emit('reset', formValues);
    submitOnReset && (await handleSubmit());
  }

  // 获取表单值
  function getFieldsValue() {
    const formEl = unref(formElRef);
    if (!formEl) return;
    return handleFormValues(toRaw(formModel));
  }

  // 设置表单字段值
  async function setFieldsValue(values) {
    const fields = unref(getSchema)
      .map((item) => item.field)
      .filter(Boolean);

    Object.keys(values).forEach((key) => {
      const value = values[key];
      if (fields.includes(key)) {
        formModel[key] = value;
      }
    });
  }

  return {
    handleSubmit,
    validate,
    resetFields,
    getFieldsValue,
    clearValidate,
    setFieldsValue,
  };
}
