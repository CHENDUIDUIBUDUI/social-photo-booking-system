CREATE TABLE `like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `content_id` bigint NOT NULL,
  `type` int NOT NULL COMMENT '1: 点赞, 2: 收藏',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_content_type` (`user_id`,`content_id`,`type`),
  KEY `idx_content_type` (`content_id`,`type`),
  KEY `idx_user_type` (`user_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;