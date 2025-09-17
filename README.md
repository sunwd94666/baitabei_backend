# 2025年白塔杯文化创意大赛后端服务

## 项目概述

本项目为2025年第四届白塔杯文化创意大赛提供完整的后端API服务，支撑C端赛事官网和B端管理后台的所有功能需求。

## 技术架构

- **开发语言**: Java 8
- **框架**: Spring Boot 2.7.x
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus
- **认证**: JWT + Spring Security
- **文件存储**: 阿里云OSS
- **文档**: Swagger3 + Knife4j
- **构建工具**: Maven

## 功能模块

### 1. 用户管理模块
- 用户注册、登录、个人信息管理
- 角色权限管理（参赛者、评委、管理员）
- 密码加密、找回密码功能

### 2. 项目管理模块
- 项目报名、信息编辑、作品提交
- 项目审核流程、状态管理
- 文件上传、下载管理

### 3. 评审系统模块
- 评分标准配置、评分提交
- 评审任务分配、进度跟踪
- 评分结果统计、排名计算

### 4. 内容管理模块
- 新闻资讯发布、编辑、删除
- 赛道信息管理、专家信息维护
- 轮播图管理、公告发布

### 5. 数据统计模块
- 报名数据统计、用户行为分析
- 评审进度统计、完成率计算
- 数据导出功能

## 快速开始

### 环境要求
- JDK 8+
- MySQL 8.0+
- Maven 3.6+
- Redis（可选，用于缓存）

### 数据库配置
1. 创建数据库：`baitabei_2025`
2. 执行SQL脚本：`sql/schema.sql`
3. 导入初始数据：`sql/data.sql`

### 配置文件
复制 `application.yml.template` 为 `application.yml`，修改以下配置：
- 数据库连接信息
- 阿里云OSS配置
- JWT密钥配置

### 启动应用
```bash
mvn clean install
mvn spring-boot:run
```

### API文档
启动后访问：http://localhost:8080/doc.html

## 部署说明

详见：`docs/deployment.md`

## API接口文档

详见：`docs/api.md`

## 数据库设计

详见：`docs/database.md`
