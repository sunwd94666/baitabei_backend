-- 2025年白塔杯文化创意大赛初始数据
-- 作者：MiniMax Agent
-- 创建时间：2025-07-30

USE `baitabei_2025`;

-- 插入角色数据
INSERT INTO `roles` (`role_code`, `role_name`, `description`, `sort_order`) VALUES
('SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', 1),
('CONTEST_ADMIN', '赛事管理员', '大赛管理员，负责内容管理和项目审核', 2),
('EXPERT', '评审专家', '评审专家，负责项目评分和评审', 3),
('GENERAL_ADMIN', '普通管理员', '普通管理员，负责日常运营', 4),
('PARTICIPANT', '参赛者', '参赛用户，可以报名参赛', 5);

-- 插入权限数据
INSERT INTO `permissions` (`permission_code`, `permission_name`, `resource_type`, `resource_url`, `parent_id`, `sort_order`, `icon`) VALUES
-- 系统管理
('system:manage', '系统管理', 'menu', '/system', NULL, 1, 'SettingOutlined'),
('system:user:view', '用户查看', 'button', '/system/users', 1, 11, NULL),
('system:user:add', '用户新增', 'button', NULL, 1, 12, NULL),
('system:user:edit', '用户编辑', 'button', NULL, 1, 13, NULL),
('system:user:delete', '用户删除', 'button', NULL, 1, 14, NULL),
('system:role:view', '角色查看', 'button', '/system/roles', 1, 21, NULL),
('system:role:add', '角色新增', 'button', NULL, 1, 22, NULL),
('system:role:edit', '角色编辑', 'button', NULL, 1, 23, NULL),
('system:role:delete', '角色删除', 'button', NULL, 1, 24, NULL),
('system:permission:view', '权限查看', 'button', '/system/permissions', 1, 31, NULL),
('system:config:view', '配置查看', 'button', '/system/configs', 1, 41, NULL),
('system:config:edit', '配置编辑', 'button', NULL, 1, 42, NULL),
('system:log:view', '日志查看', 'button', '/system/logs', 1, 51, NULL),

-- 项目管理
('project:manage', '项目管理', 'menu', '/projects', NULL, 2, 'ProjectOutlined'),
('project:view', '项目查看', 'button', '/projects', 13, 11, NULL),
('project:review', '项目审核', 'button', NULL, 13, 12, NULL),
('project:export', '项目导出', 'button', NULL, 13, 13, NULL),
('project:statistics', '项目统计', 'button', NULL, 13, 14, NULL),

-- 评审管理
('evaluation:manage', '评审管理', 'menu', '/evaluations', NULL, 3, 'AuditOutlined'),
('evaluation:view', '评审查看', 'button', '/evaluations', 18, 11, NULL),
('evaluation:score', '评分管理', 'button', NULL, 18, 12, NULL),
('evaluation:assign', '任务分配', 'button', NULL, 18, 13, NULL),
('evaluation:statistics', '评审统计', 'button', NULL, 18, 14, NULL),

-- 内容管理
('content:manage', '内容管理', 'menu', '/content', NULL, 4, 'FileTextOutlined'),
('content:news:view', '新闻查看', 'button', '/content/news', 22, 11, NULL),
('content:news:add', '新闻新增', 'button', NULL, 22, 12, NULL),
('content:news:edit', '新闻编辑', 'button', NULL, 22, 13, NULL),
('content:news:delete', '新闻删除', 'button', NULL, 22, 14, NULL),
('content:track:view', '赛道查看', 'button', '/content/tracks', 22, 21, NULL),
('content:track:edit', '赛道编辑', 'button', NULL, 22, 22, NULL),
('content:expert:view', '专家查看', 'button', '/content/experts', 22, 31, NULL),
('content:expert:add', '专家新增', 'button', NULL, 22, 32, NULL),
('content:expert:edit', '专家编辑', 'button', NULL, 22, 33, NULL),
('content:expert:delete', '专家删除', 'button', NULL, 22, 34, NULL),
('content:banner:view', '轮播图查看', 'button', '/content/banners', 22, 41, NULL),
('content:banner:add', '轮播图新增', 'button', NULL, 22, 42, NULL),
('content:banner:edit', '轮播图编辑', 'button', NULL, 22, 43, NULL),
('content:banner:delete', '轮播图删除', 'button', NULL, 22, 44, NULL),

-- 数据统计
('statistics:view', '数据统计', 'menu', '/statistics', NULL, 5, 'BarChartOutlined'),
('statistics:user', '用户统计', 'button', NULL, 35, 11, NULL),
('statistics:project', '项目统计', 'button', NULL, 35, 12, NULL),
('statistics:evaluation', '评审统计', 'button', NULL, 35, 13, NULL),

-- 个人中心
('profile:view', '个人中心', 'menu', '/profile', NULL, 6, 'UserOutlined'),
('profile:edit', '个人信息编辑', 'button', NULL, 39, 11, NULL),
('profile:password', '密码修改', 'button', NULL, 39, 12, NULL);

-- 插入角色权限关联数据
-- 超级管理员拥有所有权限
INSERT INTO `role_permissions` (`role_id`, `permission_id`)
SELECT 1, `id` FROM `permissions`;

-- 赛事管理员权限
INSERT INTO `role_permissions` (`role_id`, `permission_id`)
SELECT 2, `id` FROM `permissions` WHERE `permission_code` IN (
  'project:manage', 'project:view', 'project:review', 'project:export', 'project:statistics',
  'evaluation:manage', 'evaluation:view', 'evaluation:assign', 'evaluation:statistics',
  'content:manage', 'content:news:view', 'content:news:add', 'content:news:edit', 'content:news:delete',
  'content:track:view', 'content:track:edit', 'content:expert:view', 'content:expert:add', 'content:expert:edit', 'content:expert:delete',
  'content:banner:view', 'content:banner:add', 'content:banner:edit', 'content:banner:delete',
  'statistics:view', 'statistics:user', 'statistics:project', 'statistics:evaluation',
  'profile:view', 'profile:edit', 'profile:password'
);

-- 评审专家权限
INSERT INTO `role_permissions` (`role_id`, `permission_id`)
SELECT 3, `id` FROM `permissions` WHERE `permission_code` IN (
  'evaluation:manage', 'evaluation:view', 'evaluation:score',
  'project:manage', 'project:view',
  'profile:view', 'profile:edit', 'profile:password'
);

-- 普通管理员权限
INSERT INTO `role_permissions` (`role_id`, `permission_id`)
SELECT 4, `id` FROM `permissions` WHERE `permission_code` IN (
  'project:manage', 'project:view',
  'content:manage', 'content:news:view',
  'statistics:view', 'statistics:user', 'statistics:project',
  'profile:view', 'profile:edit', 'profile:password'
);

-- 参赛者权限
INSERT INTO `role_permissions` (`role_id`, `permission_id`)
SELECT 5, `id` FROM `permissions` WHERE `permission_code` IN (
  'profile:view', 'profile:edit', 'profile:password'
);

-- 插入赛道数据
INSERT INTO `tracks` (`track_code`, `track_name`, `description`, `cover_image`, `detailed_description`, `requirements`, `rewards`, `sort_order`, `status`) VALUES
('cultural_innovation', '文化创新赛道', '传统文化与现代科技的融合创新', '/images/track-cultural.jpg', 
'聚焦传统文化的数字化传承与创新表达，鼓励运用新技术、新媒体、新形式展现中华优秀传统文化的时代价值。', 
'1. 体现中华优秀传统文化元素\n2. 具有创新性表达方式\n3. 能够产生积极的社会影响\n4. 具备可实施性', 
'一等奖：奖金5万元+证书\n二等奖：奖金2万元+证书\n三等奖：奖金1万元+证书', 1, 1),

('creative_design', '创意设计赛道', '文化创意产品设计与品牌塑造', '/images/track-creative.jpg',
'以文化为内核，设计为手段，创造具有文化价值和商业价值的创意产品。',
'1. 具有独特的文化内涵\n2. 设计美观且实用\n3. 具备市场潜力\n4. 符合当代审美',
'一等奖：奖金5万元+证书\n二等奖：奖金2万元+证书\n三等奖：奖金1万元+证书', 2, 1),

('business_model', '商业模式赛道', '文化产业商业模式创新', '/images/track-business.jpg',
'探索文化产业发展新模式，推动文化与商业的深度融合。',
'1. 商业模式清晰可行\n2. 具有创新性和可复制性\n3. 符合文化产业发展趋势\n4. 具备盈利潜力',
'一等奖：奖金5万元+证书\n二等奖：奖金2万元+证书\n三等奖：奖金1万元+证书', 3, 1),

('social_innovation', '社会创新赛道', '文化赋能社会治理与公共服务', '/images/track-social.jpg',
'运用文化力量推动社会问题解决，提升公共服务质量。',
'1. 关注社会问题或公共需求\n2. 具有可操作性和可持续性\n3. 体现文化价值引领\n4. 能够产生积极社会效益',
'一等奖：奖金5万元+证书\n二等奖：奖金2万元+证书\n三等奖：奖金1万元+证书', 4, 1),

('communication_promotion', '传播推广赛道', '文化传播与品牌营销创新', '/images/track-communication.jpg',
'创新文化传播方式，提升文化影响力和品牌价值。',
'1. 传播策略创新有效\n2. 符合目标受众特点\n3. 具有良好的传播效果\n4. 体现文化价值传递',
'一等奖：奖金5万元+证书\n二等奖：奖金2万元+证书\n三等奖：奖金1万元+证书', 5, 1),

('comprehensive_innovation', '综合创新赛道', '跨领域文化创新综合项目', '/images/track-comprehensive.png',
'鼓励跨领域、跨学科的文化创新项目，推动多元融合发展。',
'1. 具有跨领域特征\n2. 创新性突出\n3. 综合价值显著\n4. 具备发展潜力',
'一等奖：奖金5万元+证书\n二等奖：奖金2万元+证书\n三等奖：奖金1万元+证书', 6, 1);

-- 插入专家信息数据
INSERT INTO `experts` (`name`, `title`, `institution`, `expertise`, `bio`, `avatar`, `achievements`, `sort_order`) VALUES
('张文化', '教授、博士生导师', '中央美术学院', '文化创意设计、传统文化传承', '中央美术学院设计学院教授，长期从事文化创意设计研究，主持多项国家级科研项目。', '/images/experts-team.jpeg', '主编《文化创意设计理论与实践》等著作，获得多项设计大奖。', 1),
('李创新', '研究员', '中国社会科学院', '文化产业、商业模式创新', '中国社会科学院文化研究所研究员，文化产业发展专家。', '/images/experts-team.jpeg', '发表学术论文100余篇，出版专著《文化产业发展报告》。', 2),
('王传播', '副教授', '中国传媒大学', '新媒体传播、品牌营销', '中国传媒大学新闻传播学院副教授，新媒体传播专家。', '/images/experts-team.jpeg', '主持多项媒体传播创新项目，获得国家级教学成果奖。', 3),
('陈科技', '总工程师', '北京市科技创新中心', '数字技术、科技创新', '北京市科技创新中心总工程师，数字技术应用专家。', '/images/experts-team.jpeg', '拥有多项技术专利，推动多个科技文化融合项目落地。', 4);

-- 插入系统配置数据
INSERT INTO `system_configs` (`config_key`, `config_value`, `config_type`, `description`, `category`) VALUES
('site.name', '2025年白塔杯文化创意大赛', 'string', '网站名称', 'site'),
('site.logo', '/images/logo.png', 'string', '网站Logo', 'site'),
('site.copyright', '© 2025 北京市西城区人民政府 版权所有', 'string', '版权信息', 'site'),
('competition.name', '2025年第四届"白塔杯"文化创意大赛', 'string', '大赛名称', 'competition'),
('competition.theme', '文化引领，创意西城', 'string', '大赛主题', 'competition'),
('competition.start_time', '2025-09-01 00:00:00', 'string', '大赛开始时间', 'competition'),
('competition.end_time', '2025-12-31 23:59:59', 'string', '大赛结束时间', 'competition'),
('registration.start_time', '2025-09-01 00:00:00', 'string', '报名开始时间', 'registration'),
('registration.end_time', '2025-10-31 23:59:59', 'string', '报名结束时间', 'registration'),
('evaluation.criteria.creativity', '30', 'number', '创意性权重百分比', 'evaluation'),
('evaluation.criteria.practicality', '25', 'number', '实用性权重百分比', 'evaluation'),
('evaluation.criteria.technology', '20', 'number', '技术实现权重百分比', 'evaluation'),
('evaluation.criteria.market', '15', 'number', '市场潜力权重百分比', 'evaluation'),
('evaluation.criteria.culture', '10', 'number', '文化内涵权重百分比', 'evaluation'),
('file.upload.max_size', '52428800', 'number', '文件上传最大大小（字节）', 'file'),
('file.upload.allowed_types', 'jpg,jpeg,png,gif,pdf,doc,docx,ppt,pptx,mp4,mov', 'string', '允许上传的文件类型', 'file');

-- 插入轮播图数据
INSERT INTO `banners` (`title`, `image_url`, `link_url`, `description`, `position`, `sort_order`) VALUES
('2025年第四届白塔杯文化创意大赛', '/images/hero-banner.jpg', '/about', '文化引领，创意西城', 'home', 1),
('赛道介绍', '/images/track-banner.jpg', '/tracks', '六大赛道，展现创意无限', 'home', 2),
('专家团队', '/images/experts-banner.jpg', '/about', '权威专家，专业评审', 'home', 3);

-- 插入新闻数据
INSERT INTO `news` (`title`, `subtitle`, `summary`, `content`, `cover_image`, `author`, `source`, `category`, `status`, `publish_time`, `publisher_id`) VALUES
('2025年第四届白塔杯文化创意大赛正式启动', '文化引领，创意西城', '2025年第四届"白塔杯"文化创意大赛正式启动，聚焦"数字文化创新"与"消费新场景重构"两大核心命题。', 
'<p>2025年第四届"白塔杯"文化创意大赛于今日正式启动。本届大赛以"文化引领，创意西城"为主题，聚焦"数字文化创新"与"消费新场景重构"两大核心命题，旨在推动文化与科技、消费的深度融合。</p>
<p>大赛设置六大赛道，包括文化创新、创意设计、商业模式、社会创新、传播推广和综合创新，总奖金池超过100万元。</p>
<p>欢迎广大文化创意从业者、高等院校师生、科技企业等积极参与，共同推动文化创意产业发展。</p>', 
'/images/news-cover.jpg', 'MiniMax Agent', '大赛组委会', 'notice', 1, '2025-07-30 10:00:00', NULL),

('大赛报名指南发布', '详细报名流程及注意事项', '为帮助参赛者顺利完成报名，大赛组委会发布详细的报名指南。', 
'<p>为确保参赛者能够顺利完成报名，大赛组委会特别发布详细的报名指南。</p>
<p><strong>报名时间：</strong>2025年9月1日至10月31日</p>
<p><strong>报名方式：</strong>登录大赛官网在线报名</p>
<p><strong>报名材料：</strong>项目计划书、团队介绍、作品展示等</p>
<p><strong>注意事项：</strong>请确保材料真实有效，逾期不予受理</p>', 
'/images/news-cover.jpg', 'MiniMax Agent', '大赛组委会', 'guide', 1, '2025-07-30 11:00:00', NULL),

('评审专家团队介绍', '权威专家阵容，确保评审公正', '本届大赛邀请了多位行业权威专家担任评委，确保评审的专业性和公正性。', 
'<p>本届大赛评审团由来自高等院校、科研院所、文化企业的权威专家组成，涵盖文化创意、设计艺术、商业管理、技术创新等多个领域。</p>
<p>所有评审专家都具有丰富的行业经验和专业背景，将从创意性、实用性、技术实现、市场潜力、文化内涵等多个维度对参赛作品进行评审。</p>
<p>大赛采用匿名评审制度，确保评审过程的公平公正。</p>', 
'/images/news-cover.jpg', 'MiniMax Agent', '大赛组委会', 'news', 1, '2025-07-30 12:00:00', NULL);

-- 创建默认超级管理员用户（密码为 admin123，请在生产环境中修改）
INSERT INTO `users` (`username`, `email`, `password`, `real_name`, `status`, `email_verified`) VALUES
('admin', 'admin@baitabei.com', '$2a$10$N.zmdr9k7uOIUBEaWnVz.OU8LnpNWrE.xf4bgHw9knFZDGl/0z/P6', '系统管理员', 1, 1);

-- 给默认用户分配超级管理员角色
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES (1, 1);

-- 更新权限表中的parent_id（需要在插入权限后更新）
UPDATE `permissions` SET `parent_id` = (SELECT `id` FROM (SELECT * FROM `permissions`) AS p WHERE p.`permission_code` = 'system:manage') WHERE `permission_code` LIKE 'system:%' AND `permission_code` != 'system:manage';
UPDATE `permissions` SET `parent_id` = (SELECT `id` FROM (SELECT * FROM `permissions`) AS p WHERE p.`permission_code` = 'project:manage') WHERE `permission_code` LIKE 'project:%' AND `permission_code` != 'project:manage';
UPDATE `permissions` SET `parent_id` = (SELECT `id` FROM (SELECT * FROM `permissions`) AS p WHERE p.`permission_code` = 'evaluation:manage') WHERE `permission_code` LIKE 'evaluation:%' AND `permission_code` != 'evaluation:manage';
UPDATE `permissions` SET `parent_id` = (SELECT `id` FROM (SELECT * FROM `permissions`) AS p WHERE p.`permission_code` = 'content:manage') WHERE `permission_code` LIKE 'content:%' AND `permission_code` != 'content:manage';
UPDATE `permissions` SET `parent_id` = (SELECT `id` FROM (SELECT * FROM `permissions`) AS p WHERE p.`permission_code` = 'statistics:view') WHERE `permission_code` LIKE 'statistics:%' AND `permission_code` != 'statistics:view';
UPDATE `permissions` SET `parent_id` = (SELECT `id` FROM (SELECT * FROM `permissions`) AS p WHERE p.`permission_code` = 'profile:view') WHERE `permission_code` LIKE 'profile:%' AND `permission_code` != 'profile:view';
