<template>
  <n-card :bordered="false" class="proCard">
    <BasicTable
      :columns="columns"
      :request="loadDataTable"
      :row-key="(row) => row.id"
      :scroll-x="1000"
      @update:checked-row-keys="onCheckedRow"
    />
  </n-card>
</template>

<script>
import { defineComponent, reactive } from 'vue';
import { BasicTable } from '@/components/Table';
import { getPageList } from '@/api/system/role';
import { columns } from './columns';

export default defineComponent({
  components: { BasicTable },
  setup() {
    const params = reactive({
      roleName: '',
    });

    const loadDataTable = async (res) => {
      return await getPageList({ ...res }, params);
    };

    function onCheckedRow(rowKeys) {
      console.log(rowKeys);
    }
    return {
      columns,
      loadDataTable,
      onCheckedRow,
    };
  },
});
</script>
<style lang="less" scoped></style>
