-- 摄影师和模特入驻审核测试数据
-- 基于当前数据库中 user 表的最大ID生成
-- 从 user_id=134 开始

-- 插入待审核摄影师申请的普通用户 (ID: 134-136, role_id=3)
INSERT INTO `user` (`open_id`, `nickname`, `avatar`, `phone`, `credit_score`, `status`, `password`, `role_id`, `create_time`, `update_time`) VALUES
('pending_photo_user_1', '待审核摄影师张三', 'https://api.dicebear.com/7.x/personas/svg?seed=photo1&size=100', '13900001001', 100, 1, '123456', 3, NOW(), NOW()),
('pending_photo_user_2', '待审核摄影师李四', 'https://api.dicebear.com/7.x/personas/svg?seed=photo2&size=100', '13900001002', 100, 1, '123456', 3, NOW(), NOW()),
('pending_photo_user_3', '待审核摄影师王五', 'https://api.dicebear.com/7.x/personas/svg?seed=photo3&size=100', '13900001003', 100, 1, '123456', 3, NOW(), NOW());

-- 插入待审核模特申请的普通用户 (ID: 137-139, role_id=3)
INSERT INTO `user` (`open_id`, `nickname`, `avatar`, `phone`, `credit_score`, `status`, `password`, `role_id`, `create_time`, `update_time`) VALUES
('pending_model_user_1', '待审核模特小红', 'https://api.dicebear.com/7.x/personas/svg?seed=model1&size=100', '13900002001', 100, 1, '123456', 3, NOW(), NOW()),
('pending_model_user_2', '待审核模特小丽', 'https://api.dicebear.com/7.x/personas/svg?seed=model2&size=100', '13900002002', 100, 1, '123456', 3, NOW(), NOW()),
('pending_model_user_3', '待审核模特小美', 'https://api.dicebear.com/7.x/personas/svg?seed=model3&size=100', '13900002003', 100, 1, '123456', 3, NOW(), NOW());

-- 插入已拒绝的摄影师申请用户 (ID: 140, role_id=3)
INSERT INTO `user` (`open_id`, `nickname`, `avatar`, `phone`, `credit_score`, `status`, `password`, `role_id`, `create_time`, `update_time`) VALUES
('rejected_photo_user_1', '已拒绝摄影师赵六', 'https://api.dicebear.com/7.x/personas/svg?seed=rejected1&size=100', '13900003001', 100, 1, '123456', 3, NOW(), NOW());

-- 插入已拒绝的模特申请用户 (ID: 141, role_id=3)
INSERT INTO `user` (`open_id`, `nickname`, `avatar`, `phone`, `credit_score`, `status`, `password`, `role_id`, `create_time`, `update_time`) VALUES
('rejected_model_user_1', '已拒绝模特小翠', 'https://api.dicebear.com/7.x/personas/svg?seed=rejected2&size=100', '13900003002', 100, 1, '123456', 3, NOW(), NOW());

-- 插入摄影师入驻申请（待审核状态 status=0）
INSERT INTO `photographer_application` (`user_id`, `real_name`, `id_number`, `phone`, `styles`, `portfolio`, `status`, `create_time`, `update_time`) VALUES
(134, '张三', '110101199001011234', '13900001001', '人像,写真,街拍', 'https://example.com/portfolio/zhangsan', 0, NOW(), NOW()),
(135, '李四', '110101199002022345', '13900001002', '风景,建筑,商业', 'https://example.com/portfolio/lisi', 0, NOW(), NOW()),
(136, '王五', '110101199003033456', '13900001003', '婚礼,纪实,旅行', 'https://example.com/portfolio/wangwu', 0, NOW(), NOW());

-- 插入模特入驻申请（待审核状态 status=0）
INSERT INTO `model_card` (`user_id`, `name`, `height`, `weight`, `styles`, `is_paid`, `availability`, `portfolio`, `introduction`, `status`, `create_time`, `update_time`) VALUES
(137, '小红', 168, 48, '时尚,写真,商业', 0, '周末可预约', 'https://example.com/model/xiaohong', '热爱摄影，擅长各种风格的人像拍摄', 0, NOW(), NOW()),
(138, '小丽', 170, 50, 'T台,时尚,美妆', 1, '工作日可预约', 'https://example.com/model/xiaoli', '专业模特，有丰富的拍摄经验', 0, NOW(), NOW()),
(139, '小美', 165, 45, '甜美,清新,私房', 0, '随时可预约', 'https://example.com/model/xiaomei', '新人模特，期待与优秀摄影师合作', 0, NOW(), NOW());

-- 插入已拒绝的摄影师申请（用于测试）
INSERT INTO `photographer_application` (`user_id`, `real_name`, `id_number`, `phone`, `styles`, `portfolio`, `status`, `reject_reason`, `auditor_id`, `audit_time`, `create_time`, `update_time`) VALUES
(140, '赵六', '110101199004044567', '13900003001', '人像', 'https://example.com/portfolio/zhaoliu', 2, '作品集不符合要求，请重新提交', 29, NOW(), NOW(), NOW());

-- 插入已拒绝的模特申请（用于测试）
INSERT INTO `model_card` (`user_id`, `name`, `height`, `weight`, `styles`, `is_paid`, `availability`, `portfolio`, `introduction`, `status`, `reject_reason`, `auditor_id`, `audit_time`, `create_time`, `update_time`) VALUES
(141, '小翠', 172, 52, '时尚,T台', 1, '周末可预约', 'https://example.com/model/xiaocui', '专业模特', 2, '资料不完整，请补充身高体重信息', 29, NOW(), NOW(), NOW());
