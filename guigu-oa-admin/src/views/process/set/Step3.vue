<template>
  <div style="max-width: 400px; margin: 40px auto">
    <n-upload
      directory-dnd
      v-model:file-list="fileList"
      :custom-request="customRequest"
      @before-upload="beforeUpload"
      @remove="remove"
      @finish="handleFinish"
    >
      <n-upload-dragger>
        <div style="margin-bottom: 12px">
          <n-icon size="48" :depth="3">
            <archive-icon />
          </n-icon>
        </div>
        <n-text style="font-size: 16px">
          将Activiti流程设计文件拖到此处，或点击上传
        </n-text>
        <n-p depth="3" style="margin: 8px 0 0 0">
          只能上传zip格式的压缩文件，请重新上传
        </n-p>
      </n-upload-dragger>
    </n-upload>
    <div class="tools">
      <n-space>
        <n-button type="primary" @click="formSubmit">提交</n-button>
        <n-button type="default" ghost @click="prevStep">上一步</n-button>
      </n-space>
    </div>
  </div>
</template>
<script setup>
import { defineEmits, defineProps, ref, toRefs } from 'vue';
import { ArchiveOutline as ArchiveIcon } from '@vicons/ionicons5';
import { useMessage } from 'naive-ui';
import {
  uplodProcessDefinition,
  deleteProcessDefinition,
} from '@/api/process/processTemplate';

const emit = defineEmits([
  'finish',
  'prevStep',
  'update:files',
  'update-value',
]);
const props = defineProps({
  value: { type: Object },
  files: [Array],
});

const message = useMessage();
const { files, value } = toRefs(props);
const fileList = ref(files.value);

async function beforeUpload(data) {
  if (!/\.zip$/.test(data.file.file?.name)) {
    message.error('只能上传zip格式的压缩文件，请重新上传');
    return false;
  }
  if (fileList.value.length === 1) {
    message.error('只能上传一个文件');
    return false;
  }
  return true;
}

function prevStep() {
  emit('prevStep');
}

function formSubmit() {
  if (value.value.processDefinitionPath && value.value.processDefinitionKey) {
    emit('finish');
  } else {
    message.warning('您并未上传流程文件');
  }
}

function setData(data) {
  emit('update:files', fileList);
  emit('update-value', data);
}

const customRequest = ({ file, onFinish, onProgress, onError }) => {
  const formData = new FormData();
  formData.append('file', file.file);
  uplodProcessDefinition(formData, {
    onUploadProgress: ({ percent }) => {
      onProgress({ percent: Math.ceil(percent) });
    },
  })
    .then((res) => {
      setData(res);
      onFinish();
    })
    .catch(() => {
      onError();
    });
};
function handleFinish({ file }) {
  // 改名
  // file.name = props.value?.processDefinitionKey + '.zip';
  // emit('update:file-list', [file]);
  return file;
}

// 删除文件
async function remove() {
  const filename = value.value.processDefinitionPath;
  await deleteProcessDefinition(filename);
  const res = {
    processDefinitionKey: null,
    processDefinitionPath: null,
  };
  fileList.value = [];
  setData(res);
}
</script>
<style scoped>
.tools {
  display: flex-end;
  margin-top: 20px;
}
</style>
