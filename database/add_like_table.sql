-- 创建点赞表
CREATE TABLE IF NOT EXISTS `like` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `content_id` BIGINT(20) NOT NULL COMMENT '内容ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_content` (`user_id`, `content_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_content_id` (`content_id`),
  CONSTRAINT `fk_like_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_like_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- 创建评论点赞表
CREATE TABLE IF NOT EXISTS `comment_like` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论点赞ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `comment_id` BIGINT(20) NOT NULL COMMENT '评论ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_comment` (`user_id`, `comment_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_comment_id` (`comment_id`),
  CONSTRAINT `fk_comment_like_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_like_comment_id` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞表';
