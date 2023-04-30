import { h } from 'vue';
import EditableCell from './EditableCell.vue';

/**
 * 渲染为可编辑单元格，并挂载属性
 * @param {Array} column
 */
export function renderEditCell(column) {
  // column.render(rowData: object, rowIndex: number) => VNodeChild
  return (record, index) => {
    const _key = column.key;
    // 该行该列的数据
    const value = record[_key];
    record.onEdit = async (edit, submit = false) => {
      if (!submit) {
        record.editable = edit;
      }

      // 提交编辑
      if (!edit && submit) {
        const res = await record.onSubmitEdit?.();
        if (res) {
          record.editable = false;
          return true;
        }
        return false;
      }
      // 取消编辑
      if (!edit && !submit) {
        record.onCancelEdit?.();
      }
      return true;
    };
    return h(EditableCell, {
      value,
      record,
      column,
      index,
    });
  };
}
