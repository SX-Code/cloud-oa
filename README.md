# 云尚办公
云尚办公系统是一套自动办公系统，系统主要包含：管理端和员工端。
管理端包含：系统管理、权限管理、审批管理、公众号菜单管理。
员工端采用微信公众号操作，包含：办公审批、微信授权登录、消息推送等功能。

后端技术：Spring Boot + Mybatis Plus + MySQL
前端技术：vue cli + axios + pinia + router + js
开发工具：IntelliJ IDEA、Visual Studio Code

**[文档](https://sx-code.github.io/wiki/cloudoa/02_install/source_install.html) | [预览]()**

## Git代码
``` bash
git clone https://github.com/SX-Code/naive-ui-admin-js.git
```

## 界面展示
![管理端](https://cdn.staticaly.com/gh/sx-code/tuchuang@master/cloudoa/guigu-oa-admin.png)

![员工端](https://cdn.staticaly.com/gh/sx-code/tuchuang@master/cloudoa/guigu-oa-web.png)
## 目录说明

```bash
.
├── guigu-oa-admin # 管理端
├── guigu-oa-web # 员工端
├── server # 后端服务
├── Dockerfile # 后端docerfile
└── docker-compose.yaml # 后端docker部署
```



## 初始化前端项目
```bash
npm install
```

### Compiles and hot-reloads for development
```bash
npm run serve
```

### Compiles and minifies for production
```bash
npm run build
```
### Lints and fixes files
```bash
npm run lint-fix
```