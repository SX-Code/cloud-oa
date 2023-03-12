import { warn } from '@/utils/log';
import { getAppEnvConfig } from '@/utils/env';

export const useGlobSetting = () => {
  const {
    VUE_APP_TITLE,
    VUE_APP_API_URL,
    VUE_APP_SHORT_NAME,
    VUE_APP_API_URL_PREFIX,
    VUE_APP_UPLOAD_URL,
    VUE_APP_PROD_MOCK,
    VUE_APP_IMG_URL,
  } = getAppEnvConfig();

  if (!/[a-zA-Z_]*/.test(VUE_APP_SHORT_NAME)) {
    warn(
      `VUE_APP_APP_SHORT_NAME Variables can only be characters/underscores, please modify in the environment variables and re-running.`
    );
  }

  // Take global configuration
  const glob = {
    title: VUE_APP_TITLE,
    apiUrl: VUE_APP_API_URL,
    shortName: VUE_APP_SHORT_NAME,
    urlPrefix: VUE_APP_API_URL_PREFIX,
    uploadUrl: VUE_APP_UPLOAD_URL,
    prodMock: VUE_APP_PROD_MOCK,
    imgUrl: VUE_APP_IMG_URL,
  };
  return glob;
};
