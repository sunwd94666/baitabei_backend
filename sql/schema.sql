-- 2025年白塔杯文化创意大赛数据库表结构设计
-- 作者：MiniMax Agent
-- 创建时间：2025-07-30

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `baitabei_2025` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `baitabei_2025`;

-- 1. 用户表
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) NOT NULL COMMENT '密码（加密后）',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint DEFAULT NULL COMMENT '性别：0-未知，1-男，2-女',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `institution` varchar(100) DEFAULT NULL COMMENT '所属机构',
  `profession` varchar(100) DEFAULT NULL COMMENT '职业',
  `bio` text COMMENT '个人简介',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常，2-待审核',
  `email_verified` tinyint NOT NULL DEFAULT 0 COMMENT '邮箱是否验证：0-未验证，1-已验证',
  `phone_verified` tinyint NOT NULL DEFAULT 0 COMMENT '手机是否验证：0-未验证，1-已验证',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 角色表
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 3. 用户角色关联表
CREATE TABLE `user_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 4. 权限表
CREATE TABLE `permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_code` varchar(100) NOT NULL COMMENT '权限编码',
  `permission_name` varchar(100) NOT NULL COMMENT '权限名称',
  `resource_type` varchar(20) NOT NULL COMMENT '资源类型：menu-菜单，button-按钮，api-接口',
  `resource_url` varchar(200) DEFAULT NULL COMMENT '资源路径',
  `parent_id` bigint DEFAULT NULL COMMENT '父级权限ID',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 5. 角色权限关联表
CREATE TABLE `role_permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 6. 赛道表
CREATE TABLE `tracks` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '赛道ID',
  `track_code` varchar(50) NOT NULL COMMENT '赛道编码',
  `track_name` varchar(100) NOT NULL COMMENT '赛道名称',
  `description` text COMMENT '赛道描述',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '赛道封面图',
  `detailed_description` longtext COMMENT '详细描述',
  `requirements` longtext COMMENT '参赛要求',
  `rewards` longtext COMMENT '奖励设置',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-关闭，1-开放',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_track_code` (`track_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='赛道表';

-- 7. 参赛项目表
CREATE TABLE `projects` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `project_code` varchar(50) NOT NULL COMMENT '项目编号',
  `project_name` varchar(200) NOT NULL COMMENT '项目名称',
  `track_id` bigint NOT NULL COMMENT '赛道ID',
  `user_id` bigint NOT NULL COMMENT '申报用户ID',
  `description` text COMMENT '项目描述',
  `detailed_description` longtext COMMENT '详细描述',
  `innovation_points` text COMMENT '创新点',
  `technical_solution` text COMMENT '技术方案',
  `market_analysis` text COMMENT '市场分析',
  `team_info` text COMMENT '团队信息',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '项目封面图',
  `demo_url` varchar(500) DEFAULT NULL COMMENT '演示地址',
  `video_url` varchar(500) DEFAULT NULL COMMENT '视频地址',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-草稿，2-已提交，3-初审中，4-初审通过，5-初审不通过，6-复审中，7-复审通过，8-复审不通过，9-终审中，10-获奖，11-淘汰',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `review_comment` text COMMENT '审核意见',
  `final_score` decimal(5,2) DEFAULT NULL COMMENT '最终得分',
  `ranking` int DEFAULT NULL COMMENT '排名',
  `award_level` varchar(50) DEFAULT NULL COMMENT '获奖等级',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_code` (`project_code`),
  KEY `idx_track_id` (`track_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_submit_time` (`submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参赛项目表';

-- 8. 项目文件表
CREATE TABLE `project_files` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `file_name` varchar(200) NOT NULL COMMENT '文件名',
  `file_type` varchar(50) NOT NULL COMMENT '文件类型',
  `file_size` bigint NOT NULL COMMENT '文件大小（字节）',
  `file_url` varchar(500) NOT NULL COMMENT '文件URL',
  `file_path` varchar(500) DEFAULT NULL COMMENT '文件路径',
  `category` varchar(50) DEFAULT NULL COMMENT '文件分类：document-文档，image-图片，video-视频，other-其他',
  `description` varchar(500) DEFAULT NULL COMMENT '文件描述',
  `upload_user_id` bigint NOT NULL COMMENT '上传用户ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_category` (`category`),
  KEY `idx_upload_user_id` (`upload_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目文件表';

-- 9. 评分记录表
CREATE TABLE `evaluations` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `evaluator_id` bigint NOT NULL COMMENT '评审员ID',
  `round` tinyint NOT NULL COMMENT '评审轮次：1-初审，2-复审，3-终审',
  `creativity_score` decimal(4,2) DEFAULT NULL COMMENT '创意性得分（0-100）',
  `practicality_score` decimal(4,2) DEFAULT NULL COMMENT '实用性得分（0-100）',
  `technology_score` decimal(4,2) DEFAULT NULL COMMENT '技术实现得分（0-100）',
  `market_score` decimal(4,2) DEFAULT NULL COMMENT '市场潜力得分（0-100）',
  `culture_score` decimal(4,2) DEFAULT NULL COMMENT '文化内涵得分（0-100）',
  `total_score` decimal(5,2) DEFAULT NULL COMMENT '总分',
  `weighted_score` decimal(5,2) DEFAULT NULL COMMENT '加权总分',
  `comments` text COMMENT '评审意见',
  `suggestions` text COMMENT '改进建议',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-草稿，2-已提交',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_evaluator_round` (`project_id`, `evaluator_id`, `round`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_evaluator_id` (`evaluator_id`),
  KEY `idx_round` (`round`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评分记录表';

-- 10. 新闻资讯表
CREATE TABLE `news` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '新闻ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `subtitle` varchar(500) DEFAULT NULL COMMENT '副标题',
  `summary` text COMMENT '摘要',
  `content` longtext COMMENT '内容',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '封面图',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `source` varchar(100) DEFAULT NULL COMMENT '来源',
  `category` varchar(50) DEFAULT NULL COMMENT '分类：notice-公告，news-新闻，guide-指南',
  `tags` varchar(500) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `view_count` bigint NOT NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` bigint NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` bigint NOT NULL DEFAULT 0 COMMENT '评论数',
  `is_top` tinyint NOT NULL DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
  `is_hot` tinyint NOT NULL DEFAULT 0 COMMENT '是否热门：0-否，1-是',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-草稿，1-已发布，2-已下线',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `publisher_id` bigint DEFAULT NULL COMMENT '发布者ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_is_top` (`is_top`),
  KEY `idx_is_hot` (`is_hot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻资讯表';

-- 11. 专家信息表
CREATE TABLE `experts` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '专家ID',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `title` varchar(100) DEFAULT NULL COMMENT '职称',
  `institution` varchar(200) DEFAULT NULL COMMENT '所属机构',
  `expertise` varchar(500) DEFAULT NULL COMMENT '专业领域',
  `bio` text COMMENT '个人简介',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像',
  `achievements` text COMMENT '主要成就',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `user_id` bigint DEFAULT NULL COMMENT '关联用户ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专家信息表';

-- 12. 轮播图表
CREATE TABLE `banners` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `image_url` varchar(500) NOT NULL COMMENT '图片URL',
  `link_url` varchar(500) DEFAULT NULL COMMENT '链接地址',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `position` varchar(50) NOT NULL DEFAULT 'home' COMMENT '位置：home-首页，news-新闻页',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_position` (`position`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图表';

-- 13. 操作日志表
CREATE TABLE `operation_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(100) NOT NULL COMMENT '操作类型',
  `method` varchar(20) NOT NULL COMMENT 'HTTP方法',
  `request_url` varchar(500) NOT NULL COMMENT '请求URL',
  `request_params` text COMMENT '请求参数',
  `response_result` text COMMENT '响应结果',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT 'User Agent',
  `execute_time` bigint DEFAULT NULL COMMENT '执行时间（毫秒）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-失败，1-成功',
  `error_message` text COMMENT '错误信息',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation` (`operation`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 14. 系统配置表
CREATE TABLE `system_configs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(50) NOT NULL DEFAULT 'string' COMMENT '配置类型：string-字符串，number-数字，boolean-布尔，json-JSON',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `category` varchar(50) DEFAULT NULL COMMENT '分类',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `is_system` tinyint NOT NULL DEFAULT 0 COMMENT '是否系统配置：0-否，1-是',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 添加外键约束
ALTER TABLE `user_roles` ADD CONSTRAINT `fk_user_roles_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `user_roles` ADD CONSTRAINT `fk_user_roles_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE;
ALTER TABLE `role_permissions` ADD CONSTRAINT `fk_role_permissions_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE;
ALTER TABLE `role_permissions` ADD CONSTRAINT `fk_role_permissions_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE;
ALTER TABLE `projects` ADD CONSTRAINT `fk_projects_track_id` FOREIGN KEY (`track_id`) REFERENCES `tracks` (`id`);
ALTER TABLE `projects` ADD CONSTRAINT `fk_projects_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `project_files` ADD CONSTRAINT `fk_project_files_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`) ON DELETE CASCADE;
ALTER TABLE `project_files` ADD CONSTRAINT `fk_project_files_upload_user_id` FOREIGN KEY (`upload_user_id`) REFERENCES `users` (`id`);
ALTER TABLE `evaluations` ADD CONSTRAINT `fk_evaluations_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`) ON DELETE CASCADE;
ALTER TABLE `evaluations` ADD CONSTRAINT `fk_evaluations_evaluator_id` FOREIGN KEY (`evaluator_id`) REFERENCES `users` (`id`);
ALTER TABLE `experts` ADD CONSTRAINT `fk_experts_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
