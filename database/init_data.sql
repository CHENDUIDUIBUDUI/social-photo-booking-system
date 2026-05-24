-- 初始化数据库数据

-- 插入系统配置数据
INSERT INTO `system_config` (`key`, `value`, `description`) VALUES
('banner', '[{"image": "https://example.com/banner1.jpg", "url": "/pages/content/detail?id=1", "sort": 1, "status": true}, {"image": "https://example.com/banner2.jpg", "url": "/pages/content/detail?id=2", "sort": 2, "status": true}, {"image": "https://example.com/banner3.jpg", "url": "/pages/content/detail?id=3", "sort": 3, "status": false}]', '轮播图配置'),
('credit_rules', '{"initialScore": 100, "onTimeScore": 5, "completeScore": 10, "lateScore": -10, "cancelScore": -15}', '信用规则配置'),
('system_announcement', '约拍平台正式上线，欢迎大家使用！', '系统公告');

-- 插入初始用户数据
INSERT INTO `user` (`openid`, `nickname`, `avatar`, `gender`, `phone`, `email`, `role`, `status`, `credit_score`) VALUES
('openid1', '摄影师1', 'https://picsum.photos/100/100?random=1', 1, '13800138001', 'photographer1@example.com', 1, 1, 100),
('openid2', '模特1', 'https://picsum.photos/100/100?random=2', 2, '13800138002', 'model1@example.com', 2, 1, 100),
('openid3', '用户1', 'https://picsum.photos/100/100?random=3', 1, '13800138003', 'user1@example.com', 0, 1, 100),
('openid4', '摄影师2', 'https://picsum.photos/100/100?random=4', 1, '13800138004', 'photographer2@example.com', 1, 1, 100),
('openid5', '摄影师3', 'https://picsum.photos/100/100?random=5', 1, '13800138005', 'photographer3@example.com', 1, 1, 100),
('openid6', '摄影师4', 'https://picsum.photos/100/100?random=6', 1, '13800138006', 'photographer4@example.com', 1, 1, 100),
('openid7', '摄影师5', 'https://picsum.photos/100/100?random=7', 1, '13800138007', 'photographer5@example.com', 1, 1, 100),
('openid8', '摄影师6', 'https://picsum.photos/100/100?random=8', 1, '13800138008', 'photographer6@example.com', 1, 1, 100),
('openid9', '摄影师7', 'https://picsum.photos/100/100?random=9', 1, '13800138009', 'photographer7@example.com', 1, 1, 100),
('openid10', '摄影师8', 'https://picsum.photos/100/100?random=10', 1, '13800138010', 'photographer8@example.com', 1, 1, 100),
('openid11', '摄影师9', 'https://picsum.photos/100/100?random=11', 1, '13800138011', 'photographer9@example.com', 1, 1, 100),
('openid12', '摄影师10', 'https://picsum.photos/100/100?random=12', 1, '13800138012', 'photographer10@example.com', 1, 1, 100);

-- 插入用户资料数据
INSERT INTO `user_profile` (`user_id`, `real_name`, `id_card`, `city`, `introduction`, `tags`, `portfolio`, `service_type`, `price`) VALUES
(1, '张三', '110101199001011234', '北京市', '专业摄影师，擅长人像、风景摄影', '人像,风景,纪实', 'https://picsum.photos/800/600?random=10', 1, 500.00),
(2, '李四', '110101199001011235', '上海市', '专业模特，经验丰富', '模特,时尚,人像', 'https://picsum.photos/800/600?random=11', 2, 800.00),
(3, '王五', '110101199001011236', '杭州市', '摄影爱好者', '摄影爱好者', '', 0, 0.00),
(4, '赵六', '110101199001011237', '广州市', '专业摄影师，擅长时尚、商业摄影', '时尚,商业,人像', 'https://picsum.photos/800/600?random=12', 1, 600.00),
(5, '钱七', '110101199001011238', '深圳市', '专业摄影师，擅长婚礼、纪实摄影', '婚礼,纪实,人像', 'https://picsum.photos/800/600?random=13', 1, 700.00),
(6, '孙八', '110101199001011239', '成都市', '专业摄影师，擅长儿童、家庭摄影', '儿童,家庭,人像', 'https://picsum.photos/800/600?random=14', 1, 450.00),
(7, '周九', '110101199001011240', '武汉市', '专业摄影师，擅长建筑、城市摄影', '建筑,城市,风光', 'https://picsum.photos/800/600?random=15', 1, 550.00),
(8, '吴十', '110101199001011241', '重庆市', '专业摄影师，擅长美食、静物摄影', '美食,静物,商业', 'https://picsum.photos/800/600?random=16', 1, 580.00),
(9, '郑十一', '110101199001011242', '南京市', '专业摄影师，擅长宠物、动物摄影', '宠物,动物,纪实', 'https://picsum.photos/800/600?random=17', 1, 480.00),
(10, '王十二', '110101199001011243', '天津市', '专业摄影师，擅长运动、纪实摄影', '运动,纪实,人像', 'https://picsum.photos/800/600?random=18', 1, 620.00),
(11, '陈十三', '110101199001011244', '苏州市', '专业摄影师，擅长艺术、创意摄影', '艺术,创意,人像', 'https://picsum.photos/800/600?random=19', 1, 650.00),
(12, '林十四', '110101199001011245', '长沙市', '专业摄影师，擅长古风、汉服摄影', '古风,汉服,人像', 'https://picsum.photos/800/600?random=20', 1, 680.00);

-- 插入摄影师数据
INSERT INTO `photographer` (`user_id`, `certified`, `certified_time`, `orders`, `rating`, `price`, `region`, `styles`, `cover_image`, `bio`, `works`, `status`) VALUES
(1, 1, '2024-01-01 10:00:00', 128, 98.00, 500.00, '北京市', '人像,风景,纪实', 'https://picsum.photos/800/600?random=10', '专业摄影师，擅长人像、风景摄影', '["https://picsum.photos/300/300?random=101","https://picsum.photos/300/300?random=102","https://picsum.photos/300/300?random=103"]', 1),
(4, 1, '2024-01-02 10:00:00', 96, 95.00, 600.00, '广州市', '时尚,商业,人像', 'https://picsum.photos/800/600?random=12', '专业摄影师，擅长时尚、商业摄影', '["https://picsum.photos/300/300?random=106","https://picsum.photos/300/300?random=107","https://picsum.photos/300/300?random=108"]', 1),
(5, 1, '2024-01-03 10:00:00', 85, 94.00, 700.00, '深圳市', '婚礼,纪实,人像', 'https://picsum.photos/800/600?random=13', '专业摄影师，擅长婚礼、纪实摄影', '["https://picsum.photos/300/300?random=110","https://picsum.photos/300/300?random=111","https://picsum.photos/300/300?random=112"]', 1),
(6, 1, '2024-01-04 10:00:00', 76, 93.00, 450.00, '成都市', '儿童,家庭,人像', 'https://picsum.photos/800/600?random=14', '专业摄影师，擅长儿童、家庭摄影', '["https://picsum.photos/300/300?random=114","https://picsum.photos/300/300?random=115","https://picsum.photos/300/300?random=116"]', 1),
(7, 1, '2024-01-05 10:00:00', 68, 92.00, 550.00, '武汉市', '建筑,城市,风光', 'https://picsum.photos/800/600?random=15', '专业摄影师，擅长建筑、城市摄影', '["https://picsum.photos/300/300?random=117","https://picsum.photos/300/300?random=118","https://picsum.photos/300/300?random=119"]', 1),
(8, 1, '2024-01-06 10:00:00', 62, 91.00, 580.00, '重庆市', '美食,静物,商业', 'https://picsum.photos/800/600?random=16', '专业摄影师，擅长美食、静物摄影', '["https://picsum.photos/300/300?random=120","https://picsum.photos/300/300?random=121","https://picsum.photos/300/300?random=122"]', 1),
(9, 1, '2024-01-07 10:00:00', 58, 90.00, 480.00, '南京市', '宠物,动物,纪实', 'https://picsum.photos/800/600?random=17', '专业摄影师，擅长宠物、动物摄影', '["https://picsum.photos/300/300?random=123","https://picsum.photos/300/300?random=124","https://picsum.photos/300/300?random=125"]', 1),
(10, 1, '2024-01-08 10:00:00', 55, 89.00, 620.00, '天津市', '运动,纪实,人像', 'https://picsum.photos/800/600?random=18', '专业摄影师，擅长运动、纪实摄影', '["https://picsum.photos/300/300?random=126","https://picsum.photos/300/300?random=127","https://picsum.photos/300/300?random=128"]', 1),
(11, 1, '2024-01-09 10:00:00', 52, 88.00, 650.00, '苏州市', '艺术,创意,人像', 'https://picsum.photos/800/600?random=19', '专业摄影师，擅长艺术、创意摄影', '["https://picsum.photos/300/300?random=129","https://picsum.photos/300/300?random=130","https://picsum.photos/300/300?random=131"]', 1),
(12, 1, '2024-01-10 10:00:00', 50, 87.00, 680.00, '长沙市', '古风,汉服,人像', 'https://picsum.photos/800/600?random=20', '专业摄影师，擅长古风、汉服摄影', '["https://picsum.photos/300/300?random=132","https://picsum.photos/300/300?random=133","https://picsum.photos/300/300?random=134"]', 1);

-- 插入内容数据
INSERT INTO `content` (`user_id`, `title`, `description`, `images`, `cover_image`, `tags`, `price`, `location`, `type`, `status`, `view_count`, `like_count`, `comment_count`) VALUES
(1, '一组人像摄影作品', '这是一组人像摄影作品，拍摄于北京市朝阳区，使用了专业的摄影设备和灯光。', 'https://picsum.photos/800/600?random=101,https://picsum.photos/800/600?random=102,https://picsum.photos/800/600?random=103', 'https://picsum.photos/800/600?random=101', '人像,摄影,艺术', 500.00, '北京市朝阳区', 0, 1, 100, 20, 5),
(2, '寻找模特拍摄时尚大片', '寻找专业模特拍摄时尚大片，地点在上海市浦东新区，有兴趣的模特请联系。', 'https://picsum.photos/800/600?random=114', 'https://picsum.photos/800/600?random=114', '模特,时尚,招聘', 1000.00, '上海市浦东新区', 1, 1, 50, 10, 3),
(1, '风景摄影作品', '这是一组风景摄影作品，拍摄于杭州市西湖区，展示了西湖的美丽风光。', 'https://picsum.photos/800/600?random=104,https://picsum.photos/800/600?random=105', 'https://picsum.photos/800/600?random=104', '风景,摄影,自然', 300.00, '杭州市西湖区', 0, 1, 80, 15, 4),
(4, '时尚摄影作品', '这是一组时尚摄影作品，拍摄于广州市天河区，展示了最新的时尚潮流。', 'https://picsum.photos/800/600?random=106,https://picsum.photos/800/600?random=107', 'https://picsum.photos/800/600?random=106', '时尚,摄影,潮流', 600.00, '广州市天河区', 0, 1, 120, 25, 8),
(4, '商业摄影作品', '这是一组商业摄影作品，拍摄于广州市海珠区，为某品牌拍摄的产品宣传照。', 'https://picsum.photos/800/600?random=108,https://picsum.photos/800/600?random=109', 'https://picsum.photos/800/600?random=108', '商业,摄影,产品', 800.00, '广州市海珠区', 0, 1, 90, 18, 6),
(5, '婚礼摄影作品', '这是一组婚礼摄影作品，拍摄于深圳市南山区，记录了一对新人的幸福时刻。', 'https://picsum.photos/800/600?random=110,https://picsum.photos/800/600?random=111', 'https://picsum.photos/800/600?random=110', '婚礼,摄影,纪实', 700.00, '深圳市南山区', 0, 1, 110, 22, 7),
(5, '纪实摄影作品', '这是一组纪实摄影作品，拍摄于深圳市福田区，记录了城市的日常生活。', 'https://picsum.photos/800/600?random=112,https://picsum.photos/800/600?random=113', 'https://picsum.photos/800/600?random=112', '纪实,摄影,城市', 400.00, '深圳市福田区', 0, 1, 70, 12, 4),
(6, '儿童摄影作品', '这是一组儿童摄影作品，拍摄于成都市锦江区，记录了孩子们的快乐时光。', 'https://picsum.photos/800/600?random=115,https://picsum.photos/800/600?random=116', 'https://picsum.photos/800/600?random=115', '儿童,摄影,家庭', 450.00, '成都市锦江区', 0, 1, 95, 18, 5),
(7, '建筑摄影作品', '这是一组建筑摄影作品，拍摄于武汉市江汉区，展示了城市的现代建筑。', 'https://picsum.photos/800/600?random=117,https://picsum.photos/800/600?random=118', 'https://picsum.photos/800/600?random=117', '建筑,摄影,城市', 550.00, '武汉市江汉区', 0, 1, 85, 16, 4),
(8, '美食摄影作品', '这是一组美食摄影作品，拍摄于重庆市渝中区，展示了当地的特色美食。', 'https://picsum.photos/800/600?random=119,https://picsum.photos/800/600?random=120', 'https://picsum.photos/800/600?random=119', '美食,摄影,静物', 580.00, '重庆市渝中区', 0, 1, 105, 21, 7),
(9, '宠物摄影作品', '这是一组宠物摄影作品，拍摄于南京市玄武区，记录了宠物们的可爱瞬间。', 'https://picsum.photos/800/600?random=121,https://picsum.photos/800/600?random=122', 'https://picsum.photos/800/600?random=121', '宠物,摄影,动物', 480.00, '南京市玄武区', 0, 1, 90, 17, 5),
(10, '运动摄影作品', '这是一组运动摄影作品，拍摄于天津市和平区，记录了运动中的精彩瞬间。', 'https://picsum.photos/800/600?random=123,https://picsum.photos/800/600?random=124', 'https://picsum.photos/800/600?random=123', '运动,摄影,纪实', 620.00, '天津市和平区', 0, 1, 115, 23, 6),
(11, '艺术摄影作品', '这是一组艺术摄影作品，拍摄于苏州市姑苏区，展示了创意艺术的魅力。', 'https://picsum.photos/800/600?random=125,https://picsum.photos/800/600?random=126', 'https://picsum.photos/800/600?random=125', '艺术,摄影,创意', 650.00, '苏州市姑苏区', 0, 1, 100, 20, 8),
(12, '古风摄影作品', '这是一组古风摄影作品，拍摄于长沙市岳麓区，展示了传统汉服的魅力。', 'https://picsum.photos/800/600?random=127,https://picsum.photos/800/600?random=128', 'https://picsum.photos/800/600?random=127', '古风,摄影,汉服', 680.00, '长沙市岳麓区', 0, 1, 125, 26, 9);

-- 插入内容图片数据
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
-- 摄影师1的作品
(1, 'https://picsum.photos/800/600?random=101', 1),
(1, 'https://picsum.photos/800/600?random=102', 2),
(1, 'https://picsum.photos/800/600?random=103', 3),
(3, 'https://picsum.photos/800/600?random=104', 1),
(3, 'https://picsum.photos/800/600?random=105', 2),
-- 模特的需求
(2, 'https://picsum.photos/800/600?random=114', 1),
-- 摄影师2的作品
(4, 'https://picsum.photos/800/600?random=106', 1),
(4, 'https://picsum.photos/800/600?random=107', 2),
(5, 'https://picsum.photos/800/600?random=108', 1),
(5, 'https://picsum.photos/800/600?random=109', 2),
-- 摄影师3的作品
(6, 'https://picsum.photos/800/600?random=110', 1),
(6, 'https://picsum.photos/800/600?random=111', 2),
(7, 'https://picsum.photos/800/600?random=112', 1),
(7, 'https://picsum.photos/800/600?random=113', 2),
-- 摄影师4的作品
(8, 'https://picsum.photos/800/600?random=115', 1),
(8, 'https://picsum.photos/800/600?random=116', 2),
-- 摄影师5的作品
(9, 'https://picsum.photos/800/600?random=117', 1),
(9, 'https://picsum.photos/800/600?random=118', 2),
-- 摄影师6的作品
(10, 'https://picsum.photos/800/600?random=119', 1),
(10, 'https://picsum.photos/800/600?random=120', 2),
-- 摄影师7的作品
(11, 'https://picsum.photos/800/600?random=121', 1),
(11, 'https://picsum.photos/800/600?random=122', 2),
-- 摄影师8的作品
(12, 'https://picsum.photos/800/600?random=123', 1),
(12, 'https://picsum.photos/800/600?random=124', 2),
-- 摄影师9的作品
(13, 'https://picsum.photos/800/600?random=125', 1),
(13, 'https://picsum.photos/800/600?random=126', 2),
-- 摄影师10的作品
(14, 'https://picsum.photos/800/600?random=127', 1),
(14, 'https://picsum.photos/800/600?random=128', 2);

-- 插入订单数据
INSERT INTO `order` (`order_no`, `user_id`, `photographer_id`, `content_id`, `deposit`, `balance`, `total_amount`, `location`, `shoot_time`, `notes`, `status`, `user_rating`, `user_comment`, `photographer_rating`, `photographer_comment`) VALUES
('202401010001', 3, 1, 1, 250.00, 250.00, 500.00, '北京市朝阳区', '2024-01-05 14:00:00', '需要拍摄一组人像照片', 4, 5, '摄影师技术很好，拍摄效果非常满意！', 5, '用户很配合，拍摄过程很愉快！'),
('202401010002', 3, 1, 3, 150.00, 150.00, 300.00, '杭州市西湖区', '2024-01-06 10:00:00', '需要拍摄西湖的风景照片', 1, NULL, NULL, NULL, NULL);

-- 插入支付记录数据
INSERT INTO `payment` (`order_no`, `user_id`, `amount`, `payment_method`, `transaction_id`, `type`, `status`) VALUES
('202401010001', 3, 250.00, '微信支付', 'wx1234567890', 0, 1),
('202401010001', 3, 250.00, '微信支付', 'wx0987654321', 1, 1),
('202401010002', 3, 150.00, '微信支付', 'wx1122334455', 0, 1);

-- 插入消息数据
INSERT INTO `message` (`send_user_id`, `receive_user_id`, `content`, `type`, `status`, `related_id`) VALUES
(1, 3, '您好，我看到您下单了我的摄影服务，请问您有什么具体要求吗？', 0, 0, 1),
(3, 1, '您好，我希望拍摄一组人像照片，风格偏向自然清新。', 0, 0, 1),
(1, 3, '好的，我明白了，我们可以在拍摄前详细沟通一下具体的拍摄方案。', 0, 0, 1),
(1, 3, '您的订单已支付定金，拍摄时间为2024年1月5日14:00，地点在北京市朝阳区。', 1, 0, 1),
(1, 3, '拍摄已完成，请到订单页面支付尾款。', 1, 0, 1),
(1, 3, '感谢您的信任，期待与您再次合作！', 1, 0, 1);

-- 插入评论数据
INSERT INTO `comment` (`content_id`, `user_id`, `content`, `status`) VALUES
(1, 3, '这组照片拍得真好看！', 1),
(1, 2, '摄影师技术很棒，构图和光线都处理得很好。', 1),
(3, 3, '西湖的风景真美，照片拍得很有意境。', 1);

-- 插入收藏数据
INSERT INTO `collection` (`user_id`, `content_id`) VALUES
(3, 1),
(3, 3),
(2, 1);

-- 插入关注数据
INSERT INTO `follow` (`follower_id`, `followed_id`) VALUES
(3, 1),
(3, 2),
(2, 1);

-- 插入举报数据
INSERT INTO `report` (`reporter_id`, `reported_id`, `content_id`, `type`, `content`, `status`) VALUES
(3, 1, 1, 0, '该用户发布的内容含有违规信息', 0);
