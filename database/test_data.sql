-- 插入用户数据
INSERT INTO user (openid, nickname, avatar, gender, phone, email, role, status, credit_score, create_time, update_time) VALUES
('openid_1', '摄影小王', 'https://via.placeholder.com/150', 1, '13800138001', 'wang@example.com', 1, 1, 95, NOW(), NOW()),
('openid_2', '小李摄影', 'https://via.placeholder.com/150', 1, '13800138002', 'li@example.com', 1, 1, 92, NOW(), NOW()),
('openid_3', '张摄影师', 'https://via.placeholder.com/150', 1, '13800138003', 'zhang@example.com', 1, 1, 98, NOW(), NOW()),
('openid_4', '摄影师小陈', 'https://via.placeholder.com/150', 2, '13800138004', 'chen@example.com', 1, 1, 90, NOW(), NOW()),
('openid_5', '王摄影', 'https://via.placeholder.com/150', 1, '13800138005', 'wang2@example.com', 1, 1, 94, NOW(), NOW());

-- 插入摄影师数据
INSERT INTO photographer (user_id, real_name, phone, email, bio, specialty, equipment, experience, education, rating, order_count, status, create_time, update_time) VALUES
(1, '王小明', '13800138001', 'wang@example.com', '专业人像摄影师，5年拍摄经验', '人像', '佳能EOS R5, 24-70mm f/2.8, 85mm f/1.4', '5年', '摄影专业', 4.8, 120, 1, NOW(), NOW()),
(2, '李华', '13800138002', 'li@example.com', '擅长风景和建筑摄影', '风景', '索尼A7R4, 16-35mm f/2.8, 70-200mm f/2.8', '4年', '美术专业', 4.6, 80, 1, NOW(), NOW()),
(3, '张伟', '13800138003', 'zhang@example.com', '婚礼摄影专家', '婚礼', '尼康Z7 II, 24-70mm f/2.8, 50mm f/1.2', '6年', '摄影专业', 4.9, 200, 1, NOW(), NOW()),
(4, '陈静', '13800138004', 'chen@example.com', '时尚人像摄影师', '时尚', '富士X-T4, 35mm f/1.4, 56mm f/1.2', '3年', '时尚设计', 4.7, 60, 1, NOW(), NOW()),
(5, '王强', '13800138005', 'wang2@example.com', '商业产品摄影师', '商业', '佳能EOS R6, 24-70mm f/2.8, 100mm f/2.8', '7年', '视觉传达', 4.8, 150, 1, NOW(), NOW());

-- 插入作品数据（用于轮播图）
INSERT INTO content (user_id, title, description, cover_image, price, location, type, status, view_count, like_count, comment_count, create_time, update_time) VALUES
(1, '夏日人像写真', '夏季户外人像拍摄', 'https://via.placeholder.com/750x300?text=夏日人像', 500, '北京', 0, 1, 200, 80, 25, NOW(), NOW()),
(2, '长城风光', '八达岭长城日出拍摄', 'https://via.placeholder.com/750x300?text=长城风光', 300, '北京', 0, 1, 150, 60, 15, NOW(), NOW()),
(3, '婚礼现场', '浪漫婚礼纪实摄影', 'https://via.placeholder.com/750x300?text=婚礼现场', 800, '上海', 0, 1, 250, 100, 30, NOW(), NOW()),
(4, '时尚街拍', '都市时尚人像', 'https://via.placeholder.com/750x300?text=时尚街拍', 600, '北京', 0, 1, 180, 70, 20, NOW(), NOW()),
(5, '产品摄影', '电子产品商业拍摄', 'https://via.placeholder.com/750x300?text=产品摄影', 400, '深圳', 0, 1, 120, 50, 10, NOW(), NOW());

-- 插入标签数据
INSERT INTO tag (name, type, status, create_time, update_time) VALUES
('人像', 0, 1, NOW(), NOW()),
('风景', 0, 1, NOW(), NOW()),
('婚礼', 0, 1, NOW(), NOW()),
('商业', 0, 1, NOW(), NOW()),
('时尚', 0, 1, NOW(), NOW()),
('写真', 0, 1, NOW(), NOW()),
('街拍', 0, 1, NOW(), NOW()),
('产品', 0, 1, NOW(), NOW());