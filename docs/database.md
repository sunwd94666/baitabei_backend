# 2025年白塔杯文化创意大赛数据库设计文档

## 数据库概述

### 基本信息
- **数据库名称**: `baitabei_2025`
- **字符集**: `utf8mb4`
- **排序规则**: `utf8mb4_unicode_ci`
- **存储引擎**: InnoDB
- **数据库版本**: MySQL 8.0+

### 设计原则
- **规范化**: 遵循第三范式设计
- **性能优化**: 合理建立索引
- **扩展性**: 支持业务发展需要
- **一致性**: 使用外键约束保证数据一致性
- **安全性**: 敏感数据加密存储

## 表结构设计

### 1. 用户相关表

#### 1.1 用户表 (users)

**表描述**: 存储系统中所有用户的基本信息

| 字段名 | 数据类型 | 长度 | 允许空值 | 默认值 | 注释 |
|--------|----------|------|----------|--------|----- |
| id | bigint | - | NO | AUTO_INCREMENT | 用户ID（主键） |
| username | varchar | 50 | NO | - | 用户名（唯一） |
| email | varchar | 100 | NO | - | 邮箱（唯一） |
| phone | varchar | 20 | YES | NULL | 手机号 |
| password | varchar | 255 | NO | - | 密码（加密后） |
| real_name | varchar | 50 | YES | NULL | 真实姓名 |
| gender | tinyint | - | YES | NULL | 性别：0-未知，1-男，2-女 |
| birth_date | date | - | YES | NULL | 出生日期 |
| avatar | varchar | 500 | YES | NULL | 头像URL |
| institution | varchar | 100 | YES | NULL | 所属机构 |
| profession | varchar | 100 | YES | NULL | 职业 |
| bio | text | - | YES | NULL | 个人简介 |
| status | tinyint | - | NO | 1 | 状态：0-禁用，1-正常，2-待审核 |
| email_verified | tinyint | - | NO | 0 | 邮箱是否验证 |
| phone_verified | tinyint | - | NO | 0 | 手机是否验证 |
| last_login_time | datetime | - | YES | NULL | 最后登录时间 |
| last_login_ip | varchar | 50 | YES | NULL | 最后登录IP |
| created_time | datetime | - | NO | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | datetime | - | NO | CURRENT_TIMESTAMP | 更新时间 |
| deleted | tinyint | - | NO | 0 | 删除标志 |

**索引设计**:
- PRIMARY KEY: `id`
- UNIQUE KEY: `username`, `email`
- INDEX: `phone`, `status`, `created_time`

#### 1.2 角色表 (roles)

**表描述**: 系统角色定义

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 角色ID（主键） |
| role_code | varchar | 50 | 角色编码（唯一） |
| role_name | varchar | 50 | 角色名称 |
| description | varchar | 200 | 角色描述 |
| sort_order | int | - | 排序 |
| status | tinyint | - | 状态 |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |
| deleted | tinyint | - | 删除标志 |

#### 1.3 用户角色关联表 (user_roles)

**表描述**: 用户与角色的多对多关联

| 字段名 | 数据类型 | 注释 |
|--------|----------|------|
| id | bigint | ID（主键） |
| user_id | bigint | 用户ID（外键） |
| role_id | bigint | 角色ID（外键） |
| created_time | datetime | 创建时间 |

**约束**:
- FOREIGN KEY: `user_id` REFERENCES `users(id)`
- FOREIGN KEY: `role_id` REFERENCES `roles(id)`
- UNIQUE KEY: `(user_id, role_id)`

#### 1.4 权限表 (permissions)

**表描述**: 系统权限定义

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 权限ID（主键） |
| permission_code | varchar | 100 | 权限编码 |
| permission_name | varchar | 100 | 权限名称 |
| resource_type | varchar | 20 | 资源类型 |
| resource_url | varchar | 200 | 资源路径 |
| parent_id | bigint | - | 父级权限ID |
| sort_order | int | - | 排序 |
| icon | varchar | 100 | 图标 |
| description | varchar | 200 | 描述 |
| status | tinyint | - | 状态 |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |
| deleted | tinyint | - | 删除标志 |

#### 1.5 角色权限关联表 (role_permissions)

**表描述**: 角色与权限的多对多关联

| 字段名 | 数据类型 | 注释 |
|--------|----------|------|
| id | bigint | ID（主键） |
| role_id | bigint | 角色ID（外键） |
| permission_id | bigint | 权限ID（外键） |
| created_time | datetime | 创建时间 |

### 2. 赛事相关表

#### 2.1 赛道表 (tracks)

**表描述**: 大赛赛道信息

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 赛道ID（主键） |
| track_code | varchar | 50 | 赛道编码 |
| track_name | varchar | 100 | 赛道名称 |
| description | text | - | 赛道描述 |
| cover_image | varchar | 500 | 赛道封面图 |
| detailed_description | longtext | - | 详细描述 |
| requirements | longtext | - | 参赛要求 |
| rewards | longtext | - | 奖励设置 |
| sort_order | int | - | 排序 |
| status | tinyint | - | 状态 |
| start_time | datetime | - | 开始时间 |
| end_time | datetime | - | 结束时间 |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |
| deleted | tinyint | - | 删除标志 |

#### 2.2 参赛项目表 (projects)

**表描述**: 参赛项目信息

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 项目ID（主键） |
| project_code | varchar | 50 | 项目编号 |
| project_name | varchar | 200 | 项目名称 |
| track_id | bigint | - | 赛道ID（外键） |
| user_id | bigint | - | 申报用户ID（外键） |
| description | text | - | 项目描述 |
| detailed_description | longtext | - | 详细描述 |
| innovation_points | text | - | 创新点 |
| technical_solution | text | - | 技术方案 |
| market_analysis | text | - | 市场分析 |
| team_info | text | - | 团队信息 |
| cover_image | varchar | 500 | 项目封面图 |
| demo_url | varchar | 500 | 演示地址 |
| video_url | varchar | 500 | 视频地址 |
| status | tinyint | - | 项目状态 |
| submit_time | datetime | - | 提交时间 |
| review_time | datetime | - | 审核时间 |
| review_comment | text | - | 审核意见 |
| final_score | decimal | 5,2 | 最终得分 |
| ranking | int | - | 排名 |
| award_level | varchar | 50 | 获奖等级 |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |
| deleted | tinyint | - | 删除标志 |

**项目状态说明**:
- 1: 草稿
- 2: 已提交
- 3: 初审中
- 4: 初审通过
- 5: 初审不通过
- 6: 复审中
- 7: 复审通过
- 8: 复审不通过
- 9: 终审中
- 10: 获奖
- 11: 淘汰

#### 2.3 项目文件表 (project_files)

**表描述**: 项目相关文件

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 文件ID（主键） |
| project_id | bigint | - | 项目ID（外键） |
| file_name | varchar | 200 | 文件名 |
| file_type | varchar | 50 | 文件类型 |
| file_size | bigint | - | 文件大小（字节） |
| file_url | varchar | 500 | 文件URL |
| file_path | varchar | 500 | 文件路径 |
| category | varchar | 50 | 文件分类 |
| description | varchar | 500 | 文件描述 |
| upload_user_id | bigint | - | 上传用户ID |
| created_time | datetime | - | 创建时间 |
| deleted | tinyint | - | 删除标志 |

### 3. 评审相关表

#### 3.1 评分记录表 (evaluations)

**表描述**: 评审评分记录

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 评分ID（主键） |
| project_id | bigint | - | 项目ID（外键） |
| evaluator_id | bigint | - | 评审员ID（外键） |
| round | tinyint | - | 评审轮次 |
| creativity_score | decimal | 4,2 | 创意性得分 |
| practicality_score | decimal | 4,2 | 实用性得分 |
| technology_score | decimal | 4,2 | 技术实现得分 |
| market_score | decimal | 4,2 | 市场潜力得分 |
| culture_score | decimal | 4,2 | 文化内涵得分 |
| total_score | decimal | 5,2 | 总分 |
| weighted_score | decimal | 5,2 | 加权总分 |
| comments | text | - | 评审意见 |
| suggestions | text | - | 改进建议 |
| status | tinyint | - | 状态 |
| submit_time | datetime | - | 提交时间 |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |
| deleted | tinyint | - | 删除标志 |

**评分权重**:
- 创意性: 30%
- 实用性: 25%
- 技术实现: 20%
- 市场潜力: 15%
- 文化内涵: 10%

**评审轮次**:
- 1: 初审
- 2: 复审
- 3: 终审

### 4. 内容管理表

#### 4.1 新闻资讯表 (news)

**表描述**: 新闻资讯信息

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 新闻ID（主键） |
| title | varchar | 200 | 标题 |
| subtitle | varchar | 500 | 副标题 |
| summary | text | - | 摘要 |
| content | longtext | - | 内容 |
| cover_image | varchar | 500 | 封面图 |
| author | varchar | 100 | 作者 |
| source | varchar | 100 | 来源 |
| category | varchar | 50 | 分类 |
| tags | varchar | 500 | 标签 |
| view_count | bigint | - | 浏览量 |
| like_count | bigint | - | 点赞数 |
| comment_count | bigint | - | 评论数 |
| is_top | tinyint | - | 是否置顶 |
| is_hot | tinyint | - | 是否热门 |
| status | tinyint | - | 状态 |
| publish_time | datetime | - | 发布时间 |
| publisher_id | bigint | - | 发布者ID |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |
| deleted | tinyint | - | 删除标志 |

#### 4.2 专家信息表 (experts)

**表描述**: 评审专家信息

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 专家ID（主键） |
| name | varchar | 100 | 姓名 |
| title | varchar | 100 | 职称 |
| institution | varchar | 200 | 所属机构 |
| expertise | varchar | 500 | 专业领域 |
| bio | text | - | 个人简介 |
| avatar | varchar | 500 | 头像 |
| achievements | text | - | 主要成就 |
| sort_order | int | - | 排序 |
| status | tinyint | - | 状态 |
| user_id | bigint | - | 关联用户ID |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |
| deleted | tinyint | - | 删除标志 |

#### 4.3 轮播图表 (banners)

**表描述**: 首页轮播图管理

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 轮播图ID（主键） |
| title | varchar | 200 | 标题 |
| image_url | varchar | 500 | 图片URL |
| link_url | varchar | 500 | 链接地址 |
| description | varchar | 500 | 描述 |
| position | varchar | 50 | 位置 |
| sort_order | int | - | 排序 |
| start_time | datetime | - | 开始时间 |
| end_time | datetime | - | 结束时间 |
| status | tinyint | - | 状态 |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |
| deleted | tinyint | - | 删除标志 |

### 5. 系统管理表

#### 5.1 操作日志表 (operation_logs)

**表描述**: 系统操作日志

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 日志ID（主键） |
| user_id | bigint | - | 操作用户ID |
| username | varchar | 50 | 操作用户名 |
| operation | varchar | 100 | 操作类型 |
| method | varchar | 20 | HTTP方法 |
| request_url | varchar | 500 | 请求URL |
| request_params | text | - | 请求参数 |
| response_result | text | - | 响应结果 |
| ip_address | varchar | 50 | IP地址 |
| user_agent | varchar | 1000 | User Agent |
| execute_time | bigint | - | 执行时间（毫秒） |
| status | tinyint | - | 状态 |
| error_message | text | - | 错误信息 |
| created_time | datetime | - | 创建时间 |

#### 5.2 系统配置表 (system_configs)

**表描述**: 系统配置参数

| 字段名 | 数据类型 | 长度 | 注释 |
|--------|----------|------|------|
| id | bigint | - | 配置ID（主键） |
| config_key | varchar | 100 | 配置键 |
| config_value | text | - | 配置值 |
| config_type | varchar | 50 | 配置类型 |
| description | varchar | 500 | 描述 |
| category | varchar | 50 | 分类 |
| sort_order | int | - | 排序 |
| is_system | tinyint | - | 是否系统配置 |
| created_time | datetime | - | 创建时间 |
| updated_time | datetime | - | 更新时间 |

## 数据关系图

```
用户表 (users)
    ↓ 1:N
用户角色关联表 (user_roles)
    ↓ N:1
角色表 (roles)
    ↓ 1:N
角色权限关联表 (role_permissions)
    ↓ N:1
权限表 (permissions)

用户表 (users)
    ↓ 1:N
参赛项目表 (projects)
    ↓ N:1
赛道表 (tracks)

参赛项目表 (projects)
    ↓ 1:N
项目文件表 (project_files)

参赛项目表 (projects)
    ↓ 1:N
评分记录表 (evaluations)
    ↓ N:1
用户表 (users) [评审员]
```

## 索引策略

### 主要索引

1. **主键索引**: 所有表的`id`字段
2. **唯一索引**: 
   - `users.username`
   - `users.email`
   - `roles.role_code`
   - `permissions.permission_code`
   - `tracks.track_code`
   - `projects.project_code`

3. **普通索引**:
   - `users.phone`
   - `users.status`
   - `users.created_time`
   - `projects.track_id`
   - `projects.user_id`
   - `projects.status`
   - `projects.submit_time`
   - `evaluations.project_id`
   - `evaluations.evaluator_id`
   - `news.category`
   - `news.status`
   - `news.publish_time`

4. **复合索引**:
   - `user_roles(user_id, role_id)`
   - `role_permissions(role_id, permission_id)`
   - `evaluations(project_id, evaluator_id, round)`

## 数据字典

### 用户状态
- 0: 禁用
- 1: 正常
- 2: 待审核

### 项目状态
- 1: 草稿
- 2: 已提交
- 3: 初审中
- 4: 初审通过
- 5: 初审不通过
- 6: 复审中
- 7: 复审通过
- 8: 复审不通过
- 9: 终审中
- 10: 获奖
- 11: 淘汰

### 评审轮次
- 1: 初审
- 2: 复审
- 3: 终审

### 新闻分类
- notice: 公告
- news: 新闻
- guide: 指南

### 文件分类
- document: 文档
- image: 图片
- video: 视频
- other: 其他

## 数据备份策略

### 备份频率
- **全量备份**: 每天凌晨2点
- **增量备份**: 每4小时
- **日志备份**: 实时

### 备份保留策略
- **日备份**: 保留30天
- **周备份**: 保留12周
- **月备份**: 保留12个月
- **年备份**: 永久保留

### 备份命令

```bash
# 全量备份
mysqldump -u root -p --single-transaction --routines --triggers baitabei_2025 > backup_$(date +%Y%m%d_%H%M%S).sql

# 备份到远程服务器
mysqldump -u root -p --single-transaction --routines --triggers baitabei_2025 | gzip | ssh backup-server "cat > /backup/db_$(date +%Y%m%d_%H%M%S).sql.gz"
```

## 性能优化建议

### 查询优化
1. 合理使用索引
2. 避免全表扫描
3. 优化JOIN查询
4. 使用LIMIT限制结果集
5. 避免在WHERE子句中使用函数

### 表结构优化
1. 选择合适的数据类型
2. 避免使用NULL值
3. 合理设置字段长度
4. 使用分区表处理大数据量

### 配置优化
1. 调整缓冲池大小
2. 优化连接数配置
3. 配置慢查询日志
4. 启用查询缓存

---

**作者**: MiniMax Agent  
**日期**: 2025-07-30  
**版本**: 1.0.0
