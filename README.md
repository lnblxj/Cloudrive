# <img width="60" height="60" src="https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/logo-light.png" /> Cloudrive

## 简介

基于微服务的网盘+社区

WEB端 + 手机APP

😈 代码写的很烂

请遵循GPL-v3.0协议

***

## 概况

### 主要功能

网盘功能： 文件上传、下载、删除、重命名、生成分享链接

个人信息： 昵称修改、头像修改、容量展示、疑难解答

社区功能： 发布文章、评论、举报

登陆： 扫码登陆、账号密码登陆、邮箱验证、账号创建、密码找回

### 前端

uniapp + vue3 + pinia + uview

前端写了两个版本：Uniapp 版本 和 Uniapp x 版本（夭折了🤣）

### 后端
#### 微服务划分
认证服务（auth-service）

文件服务（file-service）

文件广场服务（blog-service）

用户服务(user-service)

管理员服务(admin-service)

微服务网关（gateway-service）

#### 中间件

Nacos：服务注册与发现

Redis：缓存

MySQL：数据库

MongoDB：数据库

RabbitMQ：消息队列

Minio：文件存储

Sentinel： 熔断限流

Elasticsearch： 搜索

### 运行截图

![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot1.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot2.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot3.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot4.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot5.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot6.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot7.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot8.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot9.png)
![](https://raw.githubusercontent.com/lnblxj/Cloudrive/main/assets/screenshot11.png)
