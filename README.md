# 社交摄影预约系统 (Social Photo Booking System)

## 项目简介

这是一个**毕业设计项目**，旨在构建一个社交摄影预约平台。用户可以通过微信小程序浏览摄影师作品、预约拍摄服务，摄影师可以发布作品集和套餐，后台管理员负责内容审核和用户管理。

> ⚠️ **项目说明**：本项目为毕业设计作品，**部分功能尚未完善**，仅实现了核心功能模块，仍存在优化空间。

## 功能模块

### 用户端（微信小程序）
- 用户注册与登录（手机号登录）
- 角色选择（普通用户/摄影师）
- 摄影师发现与搜索
- 摄影师主页与作品展示
- 预约拍摄服务
- 作品发布与社交互动（点赞、评论、收藏）
- 个人中心与消息通知

### 摄影师端（微信小程序）
- 摄影师入驻申请
- 作品集管理
- 套餐发布与管理
- 预约订单管理
- 评分与评价

### 后台管理系统
- 数据看板
- 用户管理
- 内容审核
- 摄影师管理
- 订单管理
- 公告管理

## 技术栈

### 后端
- **Spring Boot 2.7.15** - 后端框架
- **MyBatis** - ORM框架
- **MySQL** - 数据库
- **Redis** - 缓存（配置未启用）
- **JWT** - 身份认证

### 前端
- **微信小程序** - 用户端小程序
- **Vue 3 + Vite** - 后台管理前端
- **Element Plus** - UI组件库
- **Axios** - HTTP客户端
- **ECharts** - 数据可视化

## 项目结构

```
social-photo-booking-system/
├── social-photo-booking-backend/      # 小程序后端（端口: 8081）
├── social-photo-booking-admin-backend/ # 管理后台后端（端口: 8086）
├── social-photo-booking-admin/        # 管理后台前端（端口: 3000）
├── social-photo-booking-miniprogram/  # 微信小程序
├── database/                          # 数据库脚本
├── docs/                              # 项目文档
└── xainmu/                            # 系统截图
```

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+
- 微信开发者工具

### 1. 数据库配置

创建数据库 `social_shoot`，执行 `database/` 目录下的 SQL 脚本初始化表结构和测试数据。

### 2. 后端启动

**启动小程序后端：**
```bash
cd social-photo-booking-backend
mvn spring-boot:run
```

**启动管理后台后端：**
```bash
cd social-photo-booking-admin-backend
mvn spring-boot:run
```

### 3. 前端启动

**启动管理后台前端：**
```bash
cd social-photo-booking-admin
npm install
npm run dev
```

**微信小程序：**
使用微信开发者工具导入 `social-photo-booking-miniprogram` 目录。

### 4. 访问地址

- 管理后台：http://localhost:3000/
- 后台接口：http://localhost:8086/admin
- 小程序接口：http://localhost:8081/

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |

## 项目截图

项目截图存放在 `xainmu/` 目录中。

## 待完善功能

以下功能在本版本中**尚未实现或需要优化**：

- [ ] 完整的支付功能集成（当前仅模拟）
- [ ] 实时聊天功能
- [ ] 消息推送通知
- [ ] 摄影师排期管理
- [ ] 完整的积分系统
- [ ] 举报与违规处理流程
- [ ] 数据统计与分析功能
- [ ] 移动端适配优化
- [ ] 性能优化与缓存策略
- [ ] 安全加固与权限控制

## 注意事项

1. 本项目为**毕设作品**，代码质量和功能完整性可能存在不足
2. 部分配置使用了占位符（如微信支付、第三方API等），需替换为真实配置
3. 数据库配置中的密码等信息仅为本地开发使用
4. Redis、RabbitMQ 等中间件配置已预留但未实际启用

## 致谢

感谢在毕业设计过程中给予帮助的老师和同学。

## License

本项目仅供学习交流使用。
