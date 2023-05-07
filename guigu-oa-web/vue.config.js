const { defineConfig } = require("@vue/cli-service");
const { VantResolver } = require('unplugin-vue-components/resolvers');
const ComponentsPlugin = require('unplugin-vue-components/webpack');
module.exports = defineConfig({
  configureWebpack: {
    optimization: {
      nodeEnv: false,
    },
    plugins: [
      ComponentsPlugin({
        resolvers: [VantResolver()],
      }),
    ],
  },
  chainWebpack: config => {
    config.plugin('html')
        .tap(args => {
          args[0].title = '云尚办公';
          return args;
        })
  },
  transpileDependencies: true,
  // 基本路径
  publicPath: '/oa/',
  outputDir: 'oa',
  // 开发环境配置
  devServer: {
    host: '0.0.0.0',
    port: 9090,
    hot: true,
    historyApiFallback: true,
    allowedHosts: 'all',
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
