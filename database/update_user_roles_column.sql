-- 添加 roles 字段到 user 表（存储多个角色，用逗号分隔）
ALTER TABLE `user` ADD COLUMN IF NOT EXISTS `roles` VARCHAR(255) NOT NULL DEFAULT 'user' COMMENT '角色: 多个角色用逗号分隔，如 user,model,photographer' AFTER `phone`;

-- 更新现有用户数据：将 role 字段的值转换为 roles 字段
-- role = 0 -> roles = 'user'
UPDATE `user` SET `roles` = 'user' WHERE `roles` = 'user' OR `roles` IS NULL OR `roles` = '';

-- role = 1 (摄影师) -> roles = 'user,photographer'
UPDATE `user` u
JOIN `photographer` p ON u.id = p.user_id
SET u.roles = 'user,photographer'
WHERE u.roles = 'user' OR u.roles IS NULL;

-- role = 2 (模特) -> roles = 'user,model'
UPDATE `user` u
JOIN `model_card` m ON u.id = m.user_id
SET u.roles = 'user,model'
WHERE u.roles = 'user' OR u.roles IS NULL;

-- role = 3 (普通用户) -> roles = 'user'
UPDATE `user` SET `roles` = 'user' WHERE `roles` IS NULL OR `roles` = '';
