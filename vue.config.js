const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  // 基本路径
  publicPath: '/',
  outputDir: 'dist',
  // 开发环境配置
  devServer: {
    proxy: {
      [process.env.VUE_APP_API_URL_PREFIX]: {
        target: 'http://localhost:8800/',
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_API_URL_PREFIX]: ''
        }
      }
    }
  }
});
