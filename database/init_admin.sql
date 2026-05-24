-- 初始化后台管理系统数据库

-- 创建管理员表
CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `avatar` VARCHAR(255) COMMENT '头像',
  `role` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '角色: 1-普通管理员, 0-超级管理员',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(20) COMMENT '最后登录IP',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 创建管理员日志表
CREATE TABLE IF NOT EXISTS `admin_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_user_id` BIGINT(20) NOT NULL COMMENT '管理员ID',
  `action` VARCHAR(100) NOT NULL COMMENT '操作',
  `content` TEXT COMMENT '操作内容',
  `ip` VARCHAR(20) COMMENT '操作IP',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_admin_user_id` (`admin_user_id`),
  KEY `idx_action` (`action`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_admin_log_admin_user_id` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员日志表';

-- 插入默认管理员数据
INSERT INTO `admin_user` (`username`, `password`, `real_name`, `avatar`, `role`, `status`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 'https://api.dicebear.com/7.x/personas/svg?seed=admin&size=100', 0, 1);

-- 检查摄影师表是否存在
CREATE TABLE IF NOT EXISTS `photographer` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '摄影师ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '摄影师名称',
  `avatar` VARCHAR(255) COMMENT '头像',
  `bio` TEXT COMMENT '个人简介',
  `experience` VARCHAR(100) COMMENT '从业经验',
  `specialty` VARCHAR(255) COMMENT '擅长领域',
  `equipment` VARCHAR(255) COMMENT '设备',
  `price` DECIMAL(10,2) COMMENT '价格',
  `rating` DECIMAL(3,1) DEFAULT 5.0 COMMENT '评分',
  `review_count` INT(11) DEFAULT 0 COMMENT '评价数量',
  `portfolio` TEXT COMMENT '作品集',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_price` (`price`),
  KEY `idx_rating` (`rating`),
  CONSTRAINT `fk_photographer_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄影师表';

-- 检查摄影师套餐表是否存在
CREATE TABLE IF NOT EXISTS `photographer_package` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '套餐ID',
  `photographer_id` BIGINT(20) NOT NULL COMMENT '摄影师ID',
  `name` VARCHAR(100) NOT NULL COMMENT '套餐名称',
  `description` TEXT COMMENT '套餐描述',
  `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
  `duration` VARCHAR(50) COMMENT '拍摄时长',
  `photo_count` INT(11) COMMENT '照片数量',
  `features` TEXT COMMENT '套餐特性',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_photographer_id` (`photographer_id`),
  KEY `idx_status` (`status`),
  KEY `idx_price` (`price`),
  CONSTRAINT `fk_photographer_package_photographer_id` FOREIGN KEY (`photographer_id`) REFERENCES `photographer` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄影师套餐表';

-- 为有作品的用户创建摄影师记录
INSERT INTO `photographer` (`user_id`, `name`, `avatar`, `bio`, `price`, `rating`, `status`)
SELECT 
  u.id, 
  u.nickname, 
  u.avatar, 
  CONCAT('专注摄影', FLOOR(RAND() * 10) + 1, '年'), 
  199.00 + FLOOR(RAND() * 800), 
  4.5 + RAND() * 0.5, 
  1
FROM `user` u
WHERE u.role = 1
AND NOT EXISTS (SELECT 1 FROM `photographer` p WHERE p.user_id = u.id);

-- 为每个摄影师创建基础套餐
INSERT INTO `photographer_package` (`photographer_id`, `name`, `description`, `price`, `duration`, `photo_count`, `features`, `status`)
SELECT 
  p.id, 
  '基础套餐', 
  '基础摄影服务，适合日常拍摄需求', 
  p.price, 
  '1小时', 
  5, 
  '["1小时拍摄时间", "5张精修照片", "基础后期处理", "电子版照片"]', 
  1
FROM `photographer` p
WHERE NOT EXISTS (SELECT 1 FROM `photographer_package` pp WHERE pp.photographer_id = p.id);

-- 插入测试订单数据
INSERT INTO `order` (`order_no`, `user_id`, `photographer_id`, `content_id`, `total_amount`, `deposit_amount`, `paid_deposit`, `paid_balance`, `location`, `shoot_time`, `notes`, `contact_name`, `contact_phone`, `status`)
VALUES
('20260412001', 3, 1, 1, 500.00, 200.00, 200.00, 0.00, '北京市朝阳区', '2026-04-15 14:00:00', '希望拍摄户外人像', '小张', '13800138000', 1),
('20260412002', 3, 1, 3, 800.00, 300.00, 0.00, 0.00, '北京市海淀区', '2026-04-16 10:00:00', '艺术摄影', '小张', '13800138000', 0),
('20260412003', 3, 1, 1, 600.00, 200.00, 200.00, 400.00, '北京市西城区', '2026-04-10 09:00:00', '已完成拍摄', '小张', '13800138000', 4);
