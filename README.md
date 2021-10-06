# pdp

#### 介绍

pdp大数据平台后端

#### 软件架构

基于 SpringBoot + Mybatis + Redis 的后台管理系统解决方案


#### 安装教程

```
git clone https://codeup.aliyun.com/60a7c4c62c5969c370c58471/pudu_data_platform/pdp.git    // 把模板下载到本地

运行resources目录下pdp.sql创建表

cd pdp    // 进入模板目录
mvn install         // 安装项目依赖，等待安装完成生成jar

// 开启服务器，浏览器访问 http://localhost:8807/doc.html
release/bin/start.sh
```

#### 使用说明

配合前端系统一同使用。

# pdp-web

#### 介绍

pdp大数据平台前端

#### 软件架构

基于 Vue + Element UI 的pdp大数据平台


#### 安装教程

```
git clone https://codeup.aliyun.com/60a7c4c62c5969c370c58471/pudu_data_platform/pdp-web.git      // 把模板下载到本地
cd pdp-web    // 进入模板目录
npm install         // 安装项目依赖，等待安装完成之后，安装失败可用 cnpm 或 yarn

// 开启服务器，浏览器访问 http://localhost:8080
npm run serve

// 执行构建命令，生成的dist文件夹放在服务器下即可访问
npm run build
```

#### 使用说明

配合后台系统一同使用。


#### 参与贡献

积极参与代码开发
