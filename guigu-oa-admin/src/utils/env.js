import { warn } from '@/utils/log';

export function getAppEnvConfig() {
  const ENV = process.env;
  const {
    VUE_APP_TITLE,
    VUE_APP_API_URL,
    VUE_APP_SHORT_NAME,
    VUE_APP_API_URL_PREFIX,
    VUE_APP_UPLOAD_URL,
    VUE_APP_PROD_MOCK,
    VUE_APP_IMG_URL,
  } = ENV;

  if (!/^[a-zA-Z_]*$/.test(VUE_APP_SHORT_NAME)) {
    warn(
      `VITE_VUE_APP_SHORT_NAME Variables can only be characters/underscores, please modify in the environment variables and re-running.`
    );
  }

  return {
    VUE_APP_TITLE,
    VUE_APP_API_URL,
    VUE_APP_SHORT_NAME,
    VUE_APP_API_URL_PREFIX,
    VUE_APP_UPLOAD_URL,
    VUE_APP_PROD_MOCK,
    VUE_APP_IMG_URL,
  };
}

/**
 * @description: Development model
 */
export const devMode = 'development';

/**
 * @description: Production mode
 */
export const prodMode = 'production';

/**
 * @description: Get environment variables
 * @returns:
 * @example:
 */
export function getEnv() {
  return process.env.NODE_ENV;
}

/**
 * @description: Is it a development mode
 * @returns:
 * @example:
 */
export function isDevMode() {
  return process.env.NODE_ENV === devMode;
}

/**
 * @description: Is it a production mode
 * @returns:
 * @example:
 */
export function isProdMode() {
  return process.env.NODE_ENV === devMode;
}
