-- 创建模特申请表
CREATE TABLE IF NOT EXISTS `model_card` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `height` INT(11) COMMENT '身高(cm)',
  `weight` INT(11) COMMENT '体重(kg)',
  `styles` VARCHAR(255) COMMENT '擅长风格',
  `is_paid` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否付费: 0-免费, 1-付费',
  `availability` VARCHAR(255) COMMENT '可预约时间',
  `portfolio` TEXT COMMENT '作品集',
  `introduction` TEXT COMMENT '个人介绍',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态: 0-待审核, 1-审核通过, 2-审核拒绝',
  `reject_reason` TEXT COMMENT '拒绝原因',
  `auditor_id` BIGINT(20) COMMENT '审核人ID',
  `audit_time` DATETIME COMMENT '审核时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_model_card_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模特申请表';