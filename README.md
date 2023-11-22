# Cloud OA 云尚办公

阅读Naive UI Admin项目的源码，然后自己使用Vue（非Vite+TypeScript）实现了一遍项目。

项目框架搭建见博客：<a href="https://sx-code.github.io/wiki/adminpro/01_introduce/index.html" target="_blank">Naive Ui Admin搭建流程</a>

项目文档：<a href="https://sx-code.github.io/wiki/cloudoa/01_introduce/index.html" target="_blank">云尚办公</a>

项目预览：<a href="http://8.130.38.57:82/cloud-oa-admin/" target="_blank">Cloud OA</a>

## 项目介绍

云尚办公系统是一套自动办公系统，系统主要包含：管理端和员工端。

管理端包含：系统管理、权限管理、审批管理、公众号菜单管理。

员工端采用微信公众号操作，包含：办公审批、微信授权登录、消息推送等功能。

后端技术：Spring Boot + Mybatis Plus + MySQL

前端技术：vue cli + axios + pinia + router + js

开发工具：IntelliJ IDEA、Visual Studio Code

## 项目展示
<img src="https://gcore.jsdelivr.net/gh/sx-code/tuchuang@main/cloudoa/guigu-oa-admin.png" />
<img src="https://gcore.jsdelivr.net/gh/sx-code/tuchuang@master/cloudoa/guigu-oa-web.png" />

## 目录说明

```bash
.
├── guigu-oa-admin # 管理端
├── guigu-oa-web # 员工端
├── server # 后端服务
├── guigu-oa.sql # 数据库文件
└── docker-compose.yaml # Docker部署
```

## 管理端项目初始化
```
cd guigu-oa-admin
npm install
```

### 开发环境
```
npm run serve
```

### 生产环境
```
npm run build
```

### 代码格式化
```
npm run lint-fix
```

## 员工端项目初始化
```
cd guigu-oa-web
npm install
```

### 开发环境
```
npm run serve
```

### 生产环境
```
npm run build:prod
```

### 代码格式化
```
npm run lint-fix
```

## 服务端项目初始化
> 使用IDEA打开项目即可
### 项目配置
打开`service-oa/src/resource`下的`application-*.yaml`，修改其中的配置信息

```yaml
spring:
  datasource:
  	url: jdbc:mysql://mysql:3306/guigu-oa?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf-8
    username: root
    password: xxxxxxxx
  redis:
    host: redis
    password: xxxxxxxx
wx:
  mp:
    app-id: xxxxxxxxxxxxxxxxxx
    secret: xxxxxxxxxxxxxxxxxxxxxxx
wechat:
  prefix: http://xxxxxx.yyy.cn

```
这里redis和mysql的host都是Docker容器中的服务名，可自行修改为具体的IP:Port

参数解释：
- app-id：微信公众号的appId
- secret：微信公众号的secret
- prefix：微信公众号的授权回调页面域名

### Redis和MySQL
MySQL版本为8+，使用可视化工具创建数据库`guigu-oa`，执行`guigu-oa.sql`脚本，即可导入数据

## 联系作者

QQ: 2627311935

Email: sonwuxingxox@outlook.com
