import { isProdMode } from '@/utils/env';
import { nextTick, onUnmounted, ref, unref, watch } from 'vue';
import { getDynamicProps } from '@/utils';

export function useForm(props) {
  const formRef = ref(null);
  // 是否已加载form
  const loadedRef = ref(false);

  // 获取form对象
  async function getForm() {
    const form = unref(formRef);
    if (!form) {
      console.error(
        'The form instance has not been obtained, please make sure that the form has been rendered when performing the form operation!'
      );
    }
    await nextTick();
    return form;
  }

  function register(instance) {
    isProdMode() &&
      onUnmounted(() => {
        formRef.value = null;
        loadedRef.value = null;
      });
    // 已加载过，直接返回
    if (unref(loadedRef) && isProdMode() && instance === unref(formRef)) return;

    formRef.value = instance;
    loadedRef.value = true;

    watch(
      () => props,
      () => {
        props && instance.setProps(getDynamicProps(props));
      },
      {
        immediate: true,
        deep: true,
      }
    );
  }

  const methods = {
    setProps: async (formProps) => {
      const form = await getForm();
      await form.setProps(formProps);
    },

    resetFields: async () => {
      getForm().then(async (form) => {
        await form.resetFields();
      });
    },

    clearValidate: async (name) => {
      const form = await getForm();
      await form.clearValidate(name);
    },

    getFieldsValue: () => {
      return unref(formRef)?.getFieldsValue();
    },

    setFieldValue: async (values) => {
      const form = await getForm();
      return form.setFieldValue(values);
    },

    submit: async () => {
      const form = await getForm();
      return form.submit();
    },

    validate: async (nameList) => {
      const form = await getForm();
      return form.validate(nameList);
    },
  };

  return [register, methods];
}
