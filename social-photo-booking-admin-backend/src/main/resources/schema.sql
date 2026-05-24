CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `open_id` VARCHAR(255) UNIQUE,
  `union_id` VARCHAR(255),
  `nickname` VARCHAR(100),
  `avatar` VARCHAR(500),
  `phone` VARCHAR(20),
  `role` INT DEFAULT 0 COMMENT '0: 普通用户, 1: 摄影师, 2: 模特',
  `credit_score` INT DEFAULT 100,
  `status` INT DEFAULT 1 COMMENT '0: 禁用, 1: 正常',
  `last_login_time` DATETIME,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `content` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(200),
  `description` TEXT,
  `image_url` VARCHAR(500),
  `images` TEXT COMMENT 'JSON array of image URLs',
  `tags` TEXT COMMENT 'JSON array of tags',
  `location` VARCHAR(200),
  `price` INT DEFAULT 0,
  `type` INT DEFAULT 0 COMMENT '0: 作品, 1: 需求',
  `status` INT DEFAULT 1 COMMENT '0: 删除, 1: 正常',
  `likes` INT DEFAULT 0,
  `comments` INT DEFAULT 0,
  `views` INT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

CREATE TABLE IF NOT EXISTS `order` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL,
  `photographer_id` BIGINT NOT NULL,
  `content_id` BIGINT,
  `title` VARCHAR(200),
  `description` TEXT,
  `price` INT DEFAULT 0,
  `status` INT DEFAULT 0 COMMENT '0: 待确认, 1: 已确认, 2: 已完成, 3: 已取消',
  `contact_info` VARCHAR(200),
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`photographer_id`) REFERENCES `user`(`id`)
);

CREATE TABLE IF NOT EXISTS `collection` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL,
  `content_id` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`content_id`) REFERENCES `content`(`id`),
  UNIQUE KEY `uk_user_content` (`user_id`, `content_id`)
);

CREATE TABLE IF NOT EXISTS `follow` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL,
  `follow_user_id` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`follow_user_id`) REFERENCES `user`(`id`),
  UNIQUE KEY `uk_user_follow` (`user_id`, `follow_user_id`)
);

CREATE TABLE IF NOT EXISTS `message` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `sender_id` BIGINT NOT NULL,
  `receiver_id` BIGINT NOT NULL,
  `content` TEXT NOT NULL,
  `is_read` INT DEFAULT 0 COMMENT '0: 未读, 1: 已读',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`receiver_id`) REFERENCES `user`(`id`)
);

INSERT INTO `user` (`id`, `nickname`, `avatar`, `role`, `credit_score`, `status`) VALUES
(1, '摄影师小王', 'https://via.placeholder.com/100x100?text=摄影师1', 1, 100, 1),
(2, '模特小李', 'https://via.placeholder.com/100x100?text=模特1', 2, 100, 1),
(3, '普通用户小张', 'https://via.placeholder.com/100x100?text=用户1', 0, 100, 1);

INSERT INTO `content` (`id`, `user_id`, `title`, `description`, `image_url`, `type`, `status`, `likes`, `comments`, `views`) VALUES
(1, 1, '户外人像摄影', '专业的户外人像摄影服务，提供多种风格选择。', 'https://via.placeholder.com/300x400?text=作品1', 0, 1, 100, 20, 500),
(2, 2, '寻找摄影师合作', '寻找有经验的摄影师合作拍摄人像作品。', 'https://via.placeholder.com/300x400?text=需求1', 1, 1, 50, 10, 200),
(3, 1, '艺术摄影', '专业的艺术摄影服务，适合个人写真。', 'https://via.placeholder.com/300x400?text=作品2', 0, 1, 80, 15, 300);

-- 创建管理员用户表
CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `real_name` VARCHAR(50),
  `avatar` VARCHAR(500),
  `role` INT DEFAULT 3 COMMENT '1: 超级管理员, 2: 高级管理员, 3: 普通管理员',
  `status` INT DEFAULT 1 COMMENT '0: 禁用, 1: 正常',
  `last_login_time` DATETIME,
  `last_login_ip` VARCHAR(50),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);