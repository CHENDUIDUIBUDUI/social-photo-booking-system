-- 创建档期管理表
CREATE TABLE IF NOT EXISTS `schedule` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '档期ID',
  `photographer_id` BIGINT(20) NOT NULL COMMENT '摄影师ID',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态: 0-可用, 1-已预约, 2-已完成, 3-已取消',
  `order_id` BIGINT(20) COMMENT '关联的订单ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_photographer_id` (`photographer_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_status` (`status`),
  KEY `idx_order_id` (`order_id`),
  CONSTRAINT `fk_schedule_photographer_id` FOREIGN KEY (`photographer_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_schedule_order_id` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='档期管理表';

-- 为订单表添加档期ID字段
ALTER TABLE `order` ADD COLUMN `schedule_id` BIGINT(20) COMMENT '关联的档期ID' AFTER `contact_phone`;
ALTER TABLE `order` ADD KEY `idx_schedule_id` (`schedule_id`);
ALTER TABLE `order` ADD CONSTRAINT `fk_order_schedule_id` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON DELETE SET NULL;