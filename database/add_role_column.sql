-- 添加 role 字段到 user 表（如果不存在）
ALTER TABLE `user` ADD COLUMN IF NOT EXISTS `role` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '角色: 0-普通用户, 1-摄影师, 2-模特' AFTER `email`;

-- 更新现有用户的 role 字段
UPDATE `user` SET `role` = 3 WHERE `role` = 0;

-- 为摄影师用户设置 role = 1
UPDATE `user` u
JOIN `photographer` p ON u.id = p.user_id
SET u.role = 1;

-- 插入一些测试数据
INSERT INTO `user` (`openid`, `nickname`, `avatar`, `role`, `status`) VALUES
('test_openid_1', '测试用户1', 'https://api.dicebear.com/7.x/personas/svg?seed=user1&size=100', 3, 1),
('test_openid_2', '测试用户2', 'https://api.dicebear.com/7.x/personas/svg?seed=user2&size=100', 3, 1),
('test_openid_3', '测试用户3', 'https://api.dicebear.com/7.x/personas/svg?seed=user3&size=100', 3, 1);

-- 插入更多测试订单数据
INSERT INTO `order` (`order_no`, `user_id`, `photographer_id`, `content_id`, `total_amount`, `deposit_amount`, `paid_deposit`, `paid_balance`, `location`, `shoot_time`, `notes`, `contact_name`, `contact_phone`, `status`)
VALUES
('20260412004', 4, 1, 1, 700.00, 250.00, 250.00, 0.00, '北京市东城区', '2026-04-17 15:00:00', '家庭摄影', '测试用户1', '13800138001', 1),
('20260412005', 5, 1, 3, 900.00, 300.00, 0.00, 0.00, '北京市丰台区', '2026-04-18 11:00:00', '商业摄影', '测试用户2', '13800138002', 0),
('20260412006', 6, 1, 1, 550.00, 200.00, 200.00, 350.00, '北京市石景山区', '2026-04-09 10:00:00', '已完成拍摄', '测试用户3', '13800138003', 4);
