-- 为 photographer_application 表添加审核相关字段
-- 此脚本用于修复 Unknown column 'auditor_id' 错误

-- 添加 auditor_id 字段（如果不存在）
ALTER TABLE `photographer_application`
ADD COLUMN `auditor_id` BIGINT(20) NULL COMMENT '审核人ID' AFTER `reject_reason`;

-- 添加 audit_time 字段（如果不存在）
ALTER TABLE `photographer_application`
ADD COLUMN `audit_time` DATETIME NULL COMMENT '审核时间' AFTER `auditor_id`;

-- 为 model_card 表添加审核相关字段（如果不存在）
ALTER TABLE `model_card`
ADD COLUMN `auditor_id` BIGINT(20) NULL COMMENT '审核人ID' AFTER `reject_reason`;

ALTER TABLE `model_card`
ADD COLUMN `audit_time` DATETIME NULL COMMENT '审核时间' AFTER `auditor_id`;

-- 验证字段添加是否成功
SELECT 'photographer_application 表结构:' AS '';
DESCRIBE `photographer_application`;

SELECT 'model_card 表结构:' AS '';
DESCRIBE `model_card`;
