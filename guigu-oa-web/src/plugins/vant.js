import { Toast } from 'vant';
import 'vant/es/toast/style';

export function setupVant(app) {
  app.use(Toast);
}
