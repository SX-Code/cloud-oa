import { isBoolean, isFunction } from '@/utils/is';
import { computed, onMounted, ref, unref, watch, watchEffect } from 'vue';
import { APISETTING } from '../const';

/**
 *
 * @param {Object} propsRef 父组件传过来的值
 * @param {*} param1
 * @param {*} emit
 */
export function useDataSource(
  propsRef,
  { getPaginationInfo, setPagination, setLoading, tableData },
  emit
) {
  // 表格数据的Ref
  const dataSourceRef = ref([]);

  watchEffect(() => {
    tableData.value = unref(dataSourceRef);
  });

  watch(
    () => unref(propsRef).dataSource,
    () => {
      const { dataSource } = unref(propsRef);
      dataSource && (dataSourceRef.value = dataSource);
    },
    {
      immediate: true,
    }
  );

  // 在子组件获得表格每行的key
  const getRowKey = computed(() => {
    const { rowKey } = unref(propsRef);
    return rowKey
      ? rowKey
      : () => {
          return 'key';
        };
  });

  const getDataSourceRef = computed(() => {
    const dataSource = unref(dataSourceRef);
    if (!dataSource || dataSource.length === 0) {
      return unref(dataSourceRef);
    }
    return unref(dataSourceRef);
  });

  // 获取数据
  async function fetch(opt) {
    try {
      // 显示加载动画
      setLoading(true);
      // 从父组件结构出方法，可能父组件绑定了方法
      // afterRequest和beforeRequest是钩子函数，用于修改信息的
      const { request, pagination, beforeRequest, afterRequest } =
        unref(propsRef);
      // 其中request是父组件一定要绑定的方法，该方法负责请求数据
      if (!request) return;
      // 组装分页信息，这些字段名请根据项目实际情况修改
      const pageField = APISETTING.pageField;
      const sizeField = APISETTING.sizeField;
      const totalField = APISETTING.totalField;
      const listField = APISETTING.listField;
      const itemCountField = APISETTING.itemCountField;
      // 分页参数，当前页以及每页数量
      let pageParams = {};
      // 获取分页信息，缺省则赋予1和10
      const { page = 1, pageSize = 10 } = unref(getPaginationInfo);

      if (
        (isBoolean(pagination) && !pagination) ||
        isBoolean(getPaginationInfo)
      ) {
        // 没有开启分页
        pageParams = {};
      } else {
        // page，即当前页，如果opt参数中有的话，从opt中赋值/
        pageParams[pageField] = (opt && opt[pageField]) || page;
        pageParams[sizeField] = pageSize;
      }

      let params = {
        ...pageParams,
      };
      // 如果提供了beforeRequest，则执行
      if (beforeRequest && isFunction(beforeRequest)) {
        // The params parameter can be modified by outsiders
        params = (await beforeRequest(params)) || params;
      }
      // 提供分页信息
      const res = await request(params);

      // 请根据自己项目实际情况修改字段名
      // 从结果中拿到总页数，缺省值为0。
      const resultTotal = res[totalField] || 0;
      // 从结果中拿到当前页
      const currentPage = res[pageField];
      // 从结果拿到总数据数
      const totalData = res[itemCountField];

      // 如果数据异常，需获取正确的页码再次执行
      if (resultTotal) {
        if (page > resultTotal) {
          setPagination({
            [pageField]: resultTotal,
          });
          fetch(opt);
        }
      }
      let resultInfo = res[listField] ? res[listField] : [];
      // 如果提供了afterRequest，则执行
      if (afterRequest && isFunction(afterRequest)) {
        // can modify the data returned by the interface for processing
        resultInfo = (await afterRequest(resultInfo)) || resultInfo;
      }
      // 赋予表格数据
      dataSourceRef.value = resultInfo;
      // 赋予分页信息
      setPagination({
        [pageField]: currentPage,
        [totalField]: resultTotal,
        [itemCountField]: totalData,
      });
      // 如果传入了page参数，则从opt获取
      if (opt && opt[pageField]) {
        setPagination({
          [pageField]: opt[pageField] || 1,
        });
      }
      emit('fetch-success', {
        items: unref(resultInfo),
        resultTotal,
      });
    } catch (error) {
      console.error(error);
      emit('fetch-error', error);
      // 失败，数据清空
      dataSourceRef.value = [];
    } finally {
      // 数据获取成功与否，都取消加载动画
      setLoading(false);
    }
  }

  onMounted(() => {
    setTimeout(() => {
      fetch();
    }, 16);
  });

  // 设置表格数据
  function setTableData(values) {
    dataSourceRef.value = values;
  }

  // 获取表格数据
  function getDataSource() {
    return getDataSourceRef.value;
  }

  // 重新加载数据
  async function reload(opt) {
    await fetch(opt);
  }

  return {
    fetch,
    getRowKey,
    getDataSourceRef,
    getDataSource,
    setTableData,
    reload,
  };
}
