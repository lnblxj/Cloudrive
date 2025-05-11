# Cloudrive

基于微服务的网盘+社区

WEB端 + 手机APP
## 简介

😈 代码写的很烂

此系统为本人2025年的毕业设计，属于原创，本人允许开发者进行二次开发及传播，仅用于学习用途，不允许进行售卖，否则将追究受益人责任，请遵循GPL-v3.0协议，如需要商业用途，请联系作者
特此声明！

***
## 源代码

即将公开
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
Sentiment：熔断降级
RabbitMQ：消息队列
Minio：文件存储
Sentinel： 熔断限流
Elasticsearch： 搜索

### 运行截图

![446.png](https://img.picui.cn/free/2025/05/11/68200f9ac625b.png)
![229.png](https://img.picui.cn/free/2025/05/11/68200f9be4c1c.png)
![405.png](https://img.picui.cn/free/2025/05/11/68200f9d9ca94.png)
![537.png](https://img.picui.cn/free/2025/05/11/68200fa3cf112.png)
![109.png](https://img.picui.cn/free/2025/05/11/68200fa8b354b.png)
![815.png](https://img.picui.cn/free/2025/05/11/68200fab751d4.png)
![024.png](https://img.picui.cn/free/2025/05/11/68200fac289a9.png)
![111.png](https://img.picui.cn/free/2025/05/11/68200fad5a4a3.png)
