-- 初始化数据库表结构

-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` VARCHAR(100) NOT NULL COMMENT '微信openid',
  `nickname` VARCHAR(50) NOT NULL COMMENT '用户名',
  `avatar` VARCHAR(255) COMMENT '头像',
  `gender` TINYINT(1) COMMENT '性别: 0-未知, 1-男, 2-女',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `role` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '角色: 0-普通用户, 1-摄影师, 2-模特',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
  `credit_score` INT(11) NOT NULL DEFAULT 100 COMMENT '信用分',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_openid` (`openid`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`),
  KEY `idx_credit_score` (`credit_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建用户资料表
CREATE TABLE IF NOT EXISTS `user_profile` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `id_card` VARCHAR(20) COMMENT '身份证号',
  `city` VARCHAR(50) COMMENT '城市',
  `introduction` TEXT COMMENT '个人简介',
  `tags` VARCHAR(255) COMMENT '标签',
  `portfolio` VARCHAR(255) COMMENT '作品集',
  `service_type` TINYINT(1) COMMENT '服务类型',
  `price` DECIMAL(10,2) COMMENT '价格',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`),
  KEY `idx_city` (`city`),
  KEY `idx_service_type` (`service_type`),
  KEY `idx_price` (`price`),
  CONSTRAINT `fk_user_profile_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户资料表';

-- 创建内容表
CREATE TABLE IF NOT EXISTS `content` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '内容ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `description` TEXT COMMENT '描述',
  `images` TEXT NOT NULL COMMENT '图片',
  `tags` VARCHAR(255) COMMENT '标签',
  `price` DECIMAL(10,2) COMMENT '价格',
  `location` VARCHAR(100) COMMENT '地点',
  `type` TINYINT(1) NOT NULL COMMENT '类型: 0-作品, 1-需求',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态: 0-待审核, 1-审核通过, 2-审核拒绝',
  `view_count` INT(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` INT(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` INT(11) NOT NULL DEFAULT 0 COMMENT '评论数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_price` (`price`),
  KEY `idx_location` (`location`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_content_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';

-- 创建订单表
CREATE TABLE IF NOT EXISTS `order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `photographer_id` BIGINT(20) NOT NULL COMMENT '摄影师ID',
  `content_id` BIGINT(20) NOT NULL COMMENT '内容ID',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
  `deposit_amount` DECIMAL(10,2) NOT NULL COMMENT '定金金额',
  `paid_deposit` DECIMAL(10,2) NOT NULL COMMENT '已付定金',
  `paid_balance` DECIMAL(10,2) NOT NULL COMMENT '已付尾款',
  `location` VARCHAR(100) NOT NULL COMMENT '拍摄地点',
  `shoot_time` DATETIME NOT NULL COMMENT '拍摄时间',
  `notes` TEXT COMMENT '备注',
  `contact_name` VARCHAR(50) COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) COMMENT '联系人电话',
  `status` INT(11) NOT NULL DEFAULT 0 COMMENT '状态: 0-待支付定金, 1-定金已支付, 2-拍摄完成, 3-已支付尾款, 4-已完成, 5-已取消',
  `user_rating` TINYINT(1) COMMENT '用户评分',
  `user_comment` TEXT COMMENT '用户评论',
  `photographer_rating` TINYINT(1) COMMENT '摄影师评分',
  `photographer_comment` TEXT COMMENT '摄影师评论',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_photographer_id` (`photographer_id`),
  KEY `idx_content_id` (`content_id`),
  KEY `idx_status` (`status`),
  KEY `idx_shoot_time` (`shoot_time`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_order_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_photographer_id` FOREIGN KEY (`photographer_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 创建支付记录表
CREATE TABLE IF NOT EXISTS `payment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
  `payment_method` VARCHAR(50) NOT NULL COMMENT '支付方式',
  `transaction_id` VARCHAR(100) COMMENT '交易流水号',
  `type` TINYINT(1) NOT NULL COMMENT '类型: 0-定金, 1-尾款',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态: 0-待支付, 1-支付成功, 2-支付失败',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_payment_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- 创建消息表
CREATE TABLE IF NOT EXISTS `message` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `send_user_id` BIGINT(20) NOT NULL COMMENT '发送用户ID',
  `receive_user_id` BIGINT(20) NOT NULL COMMENT '接收用户ID',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `type` TINYINT(1) NOT NULL COMMENT '类型: 0-私信, 1-订单通知, 2-系统通知',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态: 0-未读, 1-已读',
  `related_id` BIGINT(20) COMMENT '关联ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_send_user_id` (`send_user_id`),
  KEY `idx_receive_user_id` (`receive_user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_related_id` (`related_id`),
  CONSTRAINT `fk_message_send_user_id` FOREIGN KEY (`send_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_message_receive_user_id` FOREIGN KEY (`receive_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- 创建系统配置表
CREATE TABLE IF NOT EXISTS `system_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `value` TEXT COMMENT '配置值',
  `description` VARCHAR(255) COMMENT '描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 创建评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `content_id` BIGINT(20) NOT NULL COMMENT '内容ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_content_id` (`content_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_comment_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 创建收藏表
CREATE TABLE IF NOT EXISTS `collection` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `content_id` BIGINT(20) NOT NULL COMMENT '内容ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_content` (`user_id`, `content_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_content_id` (`content_id`),
  CONSTRAINT `fk_collection_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_collection_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 创建关注表
CREATE TABLE IF NOT EXISTS `follow` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '关注ID',
  `follower_id` BIGINT(20) NOT NULL COMMENT '关注者ID',
  `followed_id` BIGINT(20) NOT NULL COMMENT '被关注者ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_follower_followed` (`follower_id`, `followed_id`),
  KEY `idx_follower_id` (`follower_id`),
  KEY `idx_followed_id` (`followed_id`),
  CONSTRAINT `fk_follow_follower_id` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_follow_followed_id` FOREIGN KEY (`followed_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注表';

-- 创建举报表
CREATE TABLE IF NOT EXISTS `report` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '举报ID',
  `reporter_id` BIGINT(20) NOT NULL COMMENT '举报人ID',
  `reported_id` BIGINT(20) NOT NULL COMMENT '被举报人ID',
  `content_id` BIGINT(20) COMMENT '内容ID',
  `type` TINYINT(1) NOT NULL COMMENT '类型: 0-内容违规, 1-未按时赴约, 2-未提供服务',
  `content` TEXT NOT NULL COMMENT '举报内容',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态: 0-待处理, 1-已处理',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_reporter_id` (`reporter_id`),
  KEY `idx_reported_id` (`reported_id`),
  KEY `idx_content_id` (`content_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_report_reporter_id` FOREIGN KEY (`reporter_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_report_reported_id` FOREIGN KEY (`reported_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_report_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报表';
