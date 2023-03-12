module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    "plugin:vue/vue3-essential",
    "eslint:recommended",
    "plugin:prettier/recommended",
  ],
  parserOptions: {
    parser: "@babel/eslint-parser",
  },
  rules: {
    "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",
    // 关闭名称校验
    "vue/multi-word-component-names": "off",
    'vue/no-mutating-props': 0,
    'linebreak-style': [ 0,'error','windows',],
    'quotes': [0, "single"],
    'prettier/prettier': [
      'warn',{
        singleQuote: true
      }
    ]
  },
};
