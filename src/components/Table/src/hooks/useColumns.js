import { usePermission } from '@/hooks/web/usePermission';
import { isArray, isBoolean, isFunction, isString } from '@/utils/is';
import { FormOutlined } from '@vicons/antd';
import { cloneDeep, isEqual } from 'lodash-es';
import { NIcon, NTooltip } from 'naive-ui';
import { computed, h, ref, toRaw, unref, watch } from 'vue';
import { renderEditCell } from '../components/editable';

/**
 * 表格列钩子函数
 * @param {Prop} propsRef
 */
export function useColumns(propsRef) {
  const columnsRef = ref(unref(propsRef).columns);
  let cacheColumns = unref(propsRef).columns;

  const getColumnsRef = computed(() => {
    const columns = cloneDeep(unref(columnsRef));

    handleActionColumn(propsRef, columns);
    if (!columns) return [];
    return columns;
  });
  // 添加Action
  function handleActionColumn(propsRef, columns) {
    const { actionColumn } = unref(propsRef);
    if (!actionColumn) return;
    !columns.find((col) => col.key === 'action') &&
      columns.push({
        ...actionColumn,
      });
  }

  const { hasPermission } = usePermission();

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

  const renderTooltip = (trigger, content) => {
    return h(NTooltip, null, {
      trigger: () => trigger,
      default: () => content,
    });
  };

  const getPageColumns = computed(() => {
    const pageColumns = unref(getColumnsRef);
    const columns = cloneDeep(pageColumns);
    return columns
      .filter((column) => {
        return hasPermission(column.auth) && isIfShow(column);
      })
      .map((column) => {
        // 默认 ellipsis 为true
        column.ellipsis =
          typeof column.ellipsis === 'undefined' ? { tooltip: true } : false;
        const { edit } = column;
        if (edit) {
          // 渲染着一列每一行为可编辑模式
          column.render = renderEditCell(column);
          if (edit) {
            // 给这一列表头添加提示和图标
            const title = column.title;
            column.title = () => {
              return renderTooltip(
                h('span', {}, [
                  h('span', { style: { 'margin-right': '5px' } }, title),
                  h(
                    NIcon,
                    {
                      size: 14,
                    },
                    {
                      default: () => h(FormOutlined),
                    }
                  ),
                ]),
                '该列可编辑'
              );
            };
          }
        }
        return column;
      });
  });

  watch(
    () => unref(propsRef).columns,
    (columns) => {
      columnsRef.value = columns;
      cacheColumns = columns;
    }
  );

  // 设置
  function setColumns(columnList) {
    const columns = cloneDeep(columnList);
    if (!isArray(columns)) return;

    if (!columns.length) {
      columnsRef.value = [];
      return;
    }
    const cacheKeys = cacheColumns.map((item) => item.key);
    //针对拖拽排序
    if (!isString(columns[0])) {
      // 列完整信息，直接赋值即可
      columnsRef.value = columns;
    } else {
      // columns中是key的列表，是切换列，增加减少全选
      const newColumns = [];
      // 从columnList中过滤出原始的列，保证所有列都是存在的
      cacheColumns.forEach((item) => {
        if (columnList.includes(item.key)) {
          newColumns.push({ ...item });
        }
      });
      // 不一致，增减了列，按照其原始顺序重排列
      if (!isEqual(cacheKeys, columns)) {
        newColumns.sort((prev, next) => {
          return cacheKeys.indexOf(prev.key) - cacheKeys.indexOf(next.key);
        });
      }
      columnsRef.value = newColumns;
    }
  }

  // 获取
  function getColumns() {
    const columns = toRaw(unref(getColumnsRef));
    return columns.map((item) => {
      return {
        ...item,
        title: item.title,
        key: item.key,
        fixed: item.fixed || undefined,
      };
    });
  }

  //获取原始
  function getCacheColumns(isKey) {
    return isKey ? cacheColumns.map((item) => item.key) : cacheColumns;
  }

  //更新备份数据单个字段
  function setCacheColumnsField(key, value) {
    if (!key || !value) {
      return;
    }
    cacheColumns.forEach((item) => {
      if (item.key === key) {
        Object.assign(item, value);
        return;
      }
    });
  }

  return {
    getColumnsRef,
    getCacheColumns,
    setColumns,
    getColumns,
    getPageColumns,
    setCacheColumnsField,
  };
}
