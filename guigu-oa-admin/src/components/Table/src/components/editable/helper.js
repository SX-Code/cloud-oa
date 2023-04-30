/**
 * 根据组件选择提示信息
 * @param {import("vue").Component} component
 * @returns 提示信息
 */
export function createPlaceholderMessage(component) {
  if (component === 'NInput') return '请输入';
  if (
    [
      'NPicker',
      'NSelect',
      'NCheckbox',
      'NRadio',
      'NSwitch',
      'NDatePicker',
      'NTimePicker',
    ].includes(component)
  )
    return '请选择';
  return '';
}
