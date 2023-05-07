<template>
  <div>
    <van-nav-bar :title="title" />
    <div class="main">
      <n-collapse :expanded-names="expanded" @update:expanded-names="update">
        <template #arrow>
          <span></span>
        </template>
        <template #header-extra="{ collapsed }">
          <n-icon size="18" color="#858585">
            <ChevronDown v-show="collapsed" />
            <ChevronUp v-show="!collapsed" />
          </n-icon>
        </template>
        <n-collapse-item
          v-for="item in types"
          :title="item.name"
          :name="item.id"
          :key="item.id"
        >
          <n-grid cols="4">
            <n-grid-item v-for="it in item.processTemplateList" :key="it.id">
              <div class="process_item" @click="apply(it.id)">
                <img :src="it.iconUrl" />
                <span class="name">{{ it.name }}</span>
              </div>
            </n-grid-item>
          </n-grid>
        </n-collapse-item>
      </n-collapse>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Process',
};
</script>
<script setup>
import { ChevronDown, ChevronUp } from '@vicons/ionicons5';
import { getProcess } from '@/api/process';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useGlobSetting } from '@/hooks/setting';

const { title } = useGlobSetting();
const router = useRouter();
const types = ref([]);
const expanded = ref([]);

function update(expandedNames) {
  expanded.value = expandedNames;
}

function apply(id) {
  router.push({ path: '/apply/' + id });
}

onMounted(async () => {
  const res = await getProcess();
  types.value = res;
  expanded.value = res.map((it) => it.id);
});
</script>
<style scoped>
.process_item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.process_item img {
  width: 40px;
  height: 40px;
  border-radius: 10px;
}
.process_item .name {
  font-size: 12px;
  padding: 8px 4px;
  color: rgb(118, 124, 130);
}
</style>
