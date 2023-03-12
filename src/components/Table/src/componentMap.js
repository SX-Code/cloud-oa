import {
  NInput,
  NSelect,
  NCheckbox,
  NInputNumber,
  NSwitch,
  NDatePicker,
  NTimePicker,
} from 'naive-ui';

export const EventEnum = {
  NInput: 'on-input',
  NInputNumber: 'on-input',
  NSelect: 'on-update:value',
  NSwitch: 'on-update:value',
  NCheckbox: 'on-update:value',
  NDatePicker: 'on-update:value',
  NTimePicker: 'on-update:value',
};

const componentMap = new Map();

componentMap.set('NInput', NInput);
componentMap.set('NSelect', NSelect);
componentMap.set('NCheckbox', NCheckbox);
componentMap.set('NInputNumber', NInputNumber);
componentMap.set('NSwitch', NSwitch);
componentMap.set('NDatePicker', NDatePicker);
componentMap.set('NTimePicker', NTimePicker);

export function add(compName, component) {
  componentMap.set(compName, component);
}

export { componentMap };
