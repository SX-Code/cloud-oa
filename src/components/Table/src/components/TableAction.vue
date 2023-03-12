<template>
  <div class="tableAction">
    <div class="flex items-center justify-center">
      <template v-for="(action, index) in getActions" :key="`${index}-${action.lable}`">
        <n-button v-bind="action" class="mx-2">
          {{ action.lable }}
          <template #icon v-if="action.hasOwnProperty('icon')">
            <n-icon :component="action.icon" />
          </template>
        </n-button>
      </template>
      <!-- 下拉菜单情况 -->
      <n-dropdown
        v-if="dropDownActions && getDropdownList.length"
        trigger="hover"
        :options="getDropdownList"
        @select="select"
      >
        <slot name="more"></slot>
        <n-button v-bind="getMoreProps" class="mx-2" v-if="!$slots.more" icon-placement="right">
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
import { defineComponent } from 'vue'
import { DownOutlined } from '@vicons/antd'
export default defineComponent({
  name: 'TableAction',
  components: { DownOutlined },
  props: {},
  setup() {
    
  },
})
</script>
