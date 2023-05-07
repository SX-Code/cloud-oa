import * as NaiveUI from 'naive-ui';

const naive = NaiveUI.create({
  components: [
    NaiveUI.NMessageProvider,
    NaiveUI.NDialogProvider,
    NaiveUI.NConfigProvider,
    NaiveUI.NButton,
    NaiveUI.NCollapse,
    NaiveUI.NCollapseItem,
    NaiveUI.NIcon,
    NaiveUI.NAvatar,
    NaiveUI.NGridItem,
    NaiveUI.NGrid,
    NaiveUI.NTabs,
    NaiveUI.NTab,
    NaiveUI.NThing,
    NaiveUI.NTimeline,
    NaiveUI.NTimelineItem,
    NaiveUI.NSpace,
    NaiveUI.NLayout,
    NaiveUI.NLayoutHeader,
  ],
});
export function setupNaive(app) {
  app.use(naive);
}
