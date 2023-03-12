import * as NaiveUI from 'naive-ui';

const naive = NaiveUI.create({
  components: [
    NaiveUI.NMessageProvider,
    NaiveUI.NDialogProvider,
    NaiveUI.NConfigProvider,
    NaiveUI.NButton,
    NaiveUI.NIcon,
    NaiveUI.NDrawer,
    NaiveUI.NDrawerContent,
    NaiveUI.NLayout,
    NaiveUI.NLayoutHeader,
    NaiveUI.NLayoutContent,
    NaiveUI.NLayoutFooter,
    NaiveUI.NLayoutSider,
    NaiveUI.NMenu,
    NaiveUI.NBreadcrumb,
    NaiveUI.NBreadcrumbItem,
    NaiveUI.NDropdown,
    NaiveUI.NSpace,
    NaiveUI.NTooltip,
    NaiveUI.NAvatar,
    NaiveUI.NTabs,
    NaiveUI.NTabPane,
    NaiveUI.NBackTop,
    NaiveUI.NNotificationProvider,
    NaiveUI.NProgress,
    NaiveUI.NLoadingBarProvider,
    NaiveUI.NForm,
    NaiveUI.NFormItem,
    NaiveUI.NCheckbox,
    NaiveUI.NCheckboxGroup,
    NaiveUI.NInput,
    NaiveUI.NDataTable,
    NaiveUI.NCard,
    NaiveUI.NSwitch,
    NaiveUI.NDivider,
    NaiveUI.NPopover,
  ],
});
export function setupNaive(app) {
  app.use(naive);
}
