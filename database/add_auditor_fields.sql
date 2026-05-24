-- 为 photographer_application 表添加审核相关字段
ALTER TABLE `photographer_application`
ADD COLUMN IF NOT EXISTS `auditor_id` BIGINT(20) COMMENT '审核人ID' AFTER `reject_reason`,
ADD COLUMN IF NOT EXISTS `audit_time` DATETIME COMMENT '审核时间' AFTER `auditor_id`;

-- 为 model_card 表添加审核相关字段
ALTER TABLE `model_card`
ADD COLUMN IF NOT EXISTS `auditor_id` BIGINT(20) COMMENT '审核人ID' AFTER `reject_reason`,
ADD COLUMN IF NOT EXISTS `audit_time` DATETIME COMMENT '审核时间' AFTER `auditor_id`;

-- 验证字段添加是否成功
DESCRIBE `photographer_application`;
DESCRIBE `model_card`;
