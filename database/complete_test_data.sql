-- ========================================
-- 社交摄影平台测试数据
-- 风格：轻透氛围感极简风
-- 照片来源：Unsplash (真实可访问)
-- 网名：流行、时尚的中文网名
-- ========================================

-- ----------------------------------------
-- 1. 用户数据（使用流行网名）
-- ----------------------------------------

-- 模特用户 (roles: model)
INSERT INTO `user` (`openid`, `nickname`, `avatar`, `phone`, `status`, `credit_score`, `roles`, `create_time`, `update_time`) VALUES
('model_001', '樱桃花开', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&h=150&fit=crop', '13712345601', 1, 100, 'user,model', '2026-04-01 10:00:00', '2026-04-01 10:00:00'),
('model_002', '奶油小生', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&h=150&fit=crop', '13712345602', 1, 100, 'user,model', '2026-04-02 10:00:00', '2026-04-02 10:00:00'),
('model_003', '林间小鹿', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&h=150&fit=crop', '13712345603', 1, 100, 'user,model', '2026-04-03 10:00:00', '2026-04-03 10:00:00'),
('model_004', '星河漫步', 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=150&h=150&fit=crop', '13712345604', 1, 100, 'user,model', '2026-04-04 10:00:00', '2026-04-04 10:00:00'),
('model_005', '暮光之城', 'https://images.unsplash.com/photo-1502823403499-6ccfcf4fb453?w=150&h=150&fit=crop', '13712345605', 1, 100, 'user,model', '2026-04-05 10:00:00', '2026-04-05 10:00:00'),
('model_006', '清欢不渡', 'https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=150&h=150&fit=crop', '13712345606', 1, 100, 'user,model', '2026-04-06 10:00:00', '2026-04-06 10:00:00'),
('model_007', '初晴时分', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&h=150&fit=crop', '13712345607', 1, 100, 'user,model', '2026-04-07 10:00:00', '2026-04-07 10:00:00'),
('model_008', '深海之谜', 'https://images.unsplash.com/photo-1502685104226-ee32379fefbe?w=150&h=150&fit=crop', '13712345608', 1, 100, 'user,model', '2026-04-08 10:00:00', '2026-04-08 10:00:00');

-- 摄影师用户 (roles: photographer)
INSERT INTO `user` (`openid`, `nickname`, `avatar`, `phone`, `status`, `credit_score`, `roles`, `create_time`, `update_time`) VALUES
('photographer_001', '光影捕手', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop', '13812345601', 1, 100, 'user,photographer', '2026-04-01 10:00:00', '2026-04-01 10:00:00'),
('photographer_002', '镜头诗人', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop', '13812345602', 1, 100, 'user,photographer', '2026-04-02 10:00:00', '2026-04-02 10:00:00'),
('photographer_003', '时光定格', 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150&h=150&fit=crop', '13812345603', 1, 100, 'user,photographer', '2026-04-03 10:00:00', '2026-04-03 10:00:00'),
('photographer_004', '瞬间永恒', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&h=150&fit=crop', '13812345604', 1, 100, 'user,photographer', '2026-04-04 10:00:00', '2026-04-04 10:00:00'),
('photographer_005', '光与影', 'https://images.unsplash.com/photo-1519345182560-3f2917c472ef?w=150&h=150&fit=crop', '13812345605', 1, 100, 'user,photographer', '2026-04-05 10:00:00', '2026-04-05 10:00:00');

-- 两者兼有 (roles: user,model,photographer)
INSERT INTO `user` (`openid`, `nickname`, `avatar`, `phone`, `status`, `credit_score`, `roles`, `create_time`, `update_time`) VALUES
('multi_001', '云端漫步', 'https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=150&h=150&fit=crop', '13912345601', 1, 100, 'user,model,photographer', '2026-04-01 10:00:00', '2026-04-01 10:00:00'),
('multi_002', '城市猎人', 'https://images.unsplash.com/photo-1463453091185-61582044d556?w=150&h=150&fit=crop', '13912345602', 1, 100, 'user,model,photographer', '2026-04-02 10:00:00', '2026-04-02 10:00:00');

-- 普通用户 (roles: user)
INSERT INTO `user` (`openid`, `nickname`, `avatar`, `phone`, `status`, `credit_score`, `roles`, `create_time`, `update_time`) VALUES
('user_001', '小鱼儿', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&h=150&fit=crop', '13612345601', 1, 100, 'user', '2026-04-01 10:00:00', '2026-04-01 10:00:00'),
('user_002', '天空之城', 'https://images.unsplash.com/photo-1599566150163-29194dcabd36?w=150&h=150&fit=crop', '13612345602', 1, 100, 'user', '2026-04-02 10:00:00', '2026-04-02 10:00:00'),
('user_003', '竹林深处', 'https://images.unsplash.com/photo-1560250097-0b93528c311a?w=150&h=150&fit=crop', '13612345603', 1, 100, 'user', '2026-04-03 10:00:00', '2026-04-03 10:00:00');

-- ----------------------------------------
-- 2. 模特卡片数据（贴合标签的展示照片）
-- ----------------------------------------

INSERT INTO `model_card` (`user_id`, `nickname`, `avatar`, `gender`, `height`, `weight`, `city`, `styles`, `experience`, `introduction`, `cover_images`, `status`, `create_time`, `reject_reason`) VALUES
(21, '樱桃花开', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&h=400&fit=crop', 2, 168, 48, '北京', '时尚,日系,小清新', 3, '专业模特，擅长时尚街拍和日系小清新风格，期待与优秀摄影师合作', '["https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=800&h=1000&fit=crop"]', 1, '2026-04-01 10:30:00', NULL),
(22, '奶油小生', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&h=400&fit=crop', 2, 172, 52, '上海', '古风,汉服,武侠', 5, '古风御用模特，曾参与多部影视剧拍摄，擅长古风武侠风格', '["https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1502823403499-6ccfcf4fb453?w=800&h=1000&fit=crop"]', 1, '2026-04-02 10:30:00', NULL),
(23, '林间小鹿', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400&h=400&fit=crop', 2, 165, 45, '杭州', '森系,文艺,复古', 2, '森系文艺少女，喜欢在大自然中寻找灵感，追求唯美复古风格', '["https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop"]', 1, '2026-04-03 10:30:00', NULL),
(24, '星河漫步', 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400&h=400&fit=crop', 2, 170, 50, '广州', '夜景,都市,轻奢', 4, '都市夜景女王，擅长在霓虹都市中捕捉璀璨瞬间', '["https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1502685104226-ee32379fefbe?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=800&h=1000&fit=crop"]', 1, '2026-04-04 10:30:00', NULL),
(25, '暮光之城', 'https://images.unsplash.com/photo-1502823403499-6ccfcf4fb453?w=400&h=400&fit=crop', 2, 175, 55, '深圳', '私房,性感,艺术', 6, '艺术人体模特，注重情感表达，作品充满艺术感', '["https://images.unsplash.com/photo-1502823403499-6ccfcf4fb453?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop"]', 1, '2026-04-05 10:30:00', NULL),
(26, '清欢不渡', 'https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=400&h=400&fit=crop', 2, 163, 46, '成都', '甜美,可爱,少女', 1, '邻家女孩风格，笑容甜美，治愈系模特', '["https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=800&h=1000&fit=crop"]', 1, '2026-04-06 10:30:00', NULL),
(27, '初晴时分', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&h=400&fit=crop', 2, 167, 49, '重庆', '青春,阳光,运动', 2, '运动系少女，健康阳光，擅长户外运动风格拍摄', '["https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1502685104226-ee32379fefbe?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop"]', 1, '2026-04-07 10:30:00', NULL),
(28, '深海之谜', 'https://images.unsplash.com/photo-1502685104226-ee32379fefbe?w=400&h=400&fit=crop', 2, 169, 47, '厦门', '海景,度假,浪漫', 3, '海边度假风模特，喜欢在海边拍摄浪漫唯美作品', '["https://images.unsplash.com/photo-1502685104226-ee32379fefbe?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=800&h=1000&fit=crop","https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=800&h=1000&fit=crop"]', 1, '2026-04-08 10:30:00', NULL);

-- ----------------------------------------
-- 3. 摄影师数据（贴合风格的照片）
-- ----------------------------------------

INSERT INTO `photographer` (`user_id`, `name`, `avatar`, `introduction`, `equipment`, `experience_years`, `works_count`, `fans_count`, `order_count`, `rating`, `cover_image`, `city`, `price_range`, `status`, `create_time`, `certified`, `styles`, `bio`, `price`, `orders`) VALUES
(29, '光影捕手', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop', '专注于人像摄影，擅长捕捉人物内心情感', 'Canon R5 + 85mm f/1.2', 8, 520, 12000, 480, 4.9, 'https://images.unsplash.com/photo-1552168324-d612d77725e3?w=800&h=600&fit=crop', '北京', '500-2000', 1, '2026-04-01 11:00:00', 1, '人像,情绪,文艺', '用镜头讲述故事，捕捉每一个动人瞬间', 500.00, 480),
(30, '镜头诗人', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop', '风光摄影师，喜欢探索大自然的美丽', 'Sony A7R4 + 16-35mm f/2.8', 10, 680, 15000, 620, 4.8, 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&h=600&fit=crop', '上海', '800-3000', 1, '2026-04-02 11:00:00', 1, '风光,自然,旅行', '走遍千山万水，只为捕捉那一抹光影', 800.00, 620),
(31, '时光定格', 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=400&h=400&fit=crop', '婚礼摄影师，记录幸福时刻', 'Nikon Z9 + 24-70mm f/2.8', 12, 890, 20000, 850, 5.0, 'https://images.unsplash.com/photo-1519741497674-611481863552?w=800&h=600&fit=crop', '广州', '1000-5000', 1, '2026-04-03 11:00:00', 1, '婚礼,纪实,人文', '为每一对新人记录人生最重要的时刻', 1000.00, 850),
(32, '瞬间永恒', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&h=400&fit=crop', '时尚摄影师，服务于各大品牌', 'Canon R5 + 70-200mm f/2.8', 6, 420, 18000, 380, 4.7, 'https://images.unsplash.com/photo-1469334031218-e382a71b716b?w=800&h=600&fit=crop', '深圳', '1500-8000', 1, '2026-04-04 11:00:00', 1, '时尚,商业,人像', '让每一帧都成为杂志封面', 1500.00, 380),
(33, '光与影', 'https://images.unsplash.com/photo-1519345182560-3f2917c472ef?w=400&h=400&fit=crop', '艺术摄影师，追求极致的美', 'Hasselblad X2D + 90mm f/3.2', 15, 350, 8000, 290, 4.9, 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=600&fit=crop', '杭州', '2000-10000', 1, '2026-04-05 11:00:00', 1, '艺术,创意,人体', '在光影中寻找艺术的真谛', 2000.00, 290);

-- ----------------------------------------
-- 4. 内容/作品数据（贴合标签的真实照片）
-- ----------------------------------------

-- 摄影师1的作品
INSERT INTO `content` (`user_id`, `type`, `title`, `description`, `cover_image`, `price_type`, `price`, `location`, `deposit`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`) VALUES
(29, 1, '情绪人像组照', '一组表达内心情感的人像作品，使用自然光捕捉最真实的情绪', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 1, 800.00, '北京', 160.00, 1250, 328, 45, 1, '2026-04-10 14:00:00'),
(29, 1, '城市街拍', '记录城市中每一个动人的瞬间，时尚与街头的完美结合', 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=800&h=1000&fit=crop', 1, 600.00, '北京', 120.00, 980, 256, 38, 1, '2026-04-11 14:00:00'),
(29, 1, '文艺复古风', '复古色调搭配精致构图，打造独特的文艺气质', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop', 1, 700.00, '北京', 140.00, 1100, 289, 42, 1, '2026-04-12 14:00:00');

-- 摄影师2的作品
INSERT INTO `content` (`user_id`, `type`, `title`, `description`, `cover_image`, `price_type`, `price`, `location`, `deposit`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`) VALUES
(30, 1, '日出云海', '捕捉日出时分云海翻涌的壮阔景象，大自然的鬼斧神工', 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&h=1000&fit=crop', 1, 1200.00, '黄山', 240.00, 2100, 567, 89, 1, '2026-04-10 15:00:00'),
(30, 1, '森林秘境', '穿梭于原始森林之中，寻找那片未被触及的宁静', 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800&h=1000&fit=crop', 1, 1000.00, '西双版纳', 200.00, 1850, 445, 67, 1, '2026-04-11 15:00:00'),
(30, 1, '星空银河', '远离城市喧嚣，追逐那片璀璨的星空', 'https://images.unsplash.com/photo-1507400492013-162706c8c05e?w=800&h=1000&fit=crop', 1, 1500.00, '青海', 300.00, 2300, 623, 95, 1, '2026-04-12 15:00:00');

-- 摄影师3的作品
INSERT INTO `content` (`user_id`, `type`, `title`, `description`, `cover_image`, `price_type`, `price`, `location`, `deposit`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`) VALUES
(31, 1, '草坪婚礼', '在阳光下的草坪婚礼，温馨而浪漫', 'https://images.unsplash.com/photo-1519741497674-611481863552?w=800&h=1000&fit=crop', 2, 3000.00, '广州', 600.00, 3200, 890, 134, 1, '2026-04-10 16:00:00'),
(31, 1, '中式婚礼', '传承千年的中式婚礼，喜庆而庄重', 'https://images.unsplash.com/photo-1529634806980-85c3dd6d34ac?w=800&h=1000&fit=crop', 2, 3500.00, '广州', 700.00, 2800, 756, 112, 1, '2026-04-11 16:00:00'),
(31, 1, '婚纱外景', '在海边拍摄的浪漫婚纱照，留下最美的回忆', 'https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=800&h=1000&fit=crop', 2, 4000.00, '三亚', 800.00, 3500, 923, 145, 1, '2026-04-12 16:00:00');

-- 摄影师4的作品
INSERT INTO `content` (`user_id`, `type`, `title`, `description`, `cover_image`, `price_type`, `price`, `location`, `deposit`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`) VALUES
(32, 1, '时尚大片', '为时尚品牌打造的视觉盛宴，展现品牌魅力', 'https://images.unsplash.com/photo-1469334031218-e382a71b716b?w=800&h=1000&fit=crop', 1, 2500.00, '深圳', 500.00, 4100, 1023, 167, 1, '2026-04-10 17:00:00'),
(32, 1, '商业人像', '专业商业人像拍摄，塑造完美品牌形象', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 1, 1800.00, '深圳', 360.00, 3200, 812, 128, 1, '2026-04-11 17:00:00'),
(32, 1, '产品摄影', '精致产品摄影，让产品说话', 'https://images.unsplash.com/photo-1509942774463-acf3390a252d?w=800&h=1000&fit=crop', 1, 1500.00, '深圳', 300.00, 2800, 689, 98, 1, '2026-04-12 17:00:00');

-- 摄影师5的作品
INSERT INTO `content` (`user_id`, `type`, `title`, `description`, `cover_image`, `price_type`, `price`, `location`, `deposit`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`) VALUES
(33, 1, '人体艺术', '探索人体之美，用光影勾勒艺术', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop', 1, 3000.00, '杭州', 600.00, 1800, 456, 78, 1, '2026-04-10 18:00:00'),
(33, 1, '创意概念', '打破常规，用创意诠释个性', 'https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=800&h=1000&fit=crop', 1, 2500.00, '杭州', 500.00, 1600, 398, 65, 1, '2026-04-11 18:00:00'),
(33, 1, '艺术人像', '将人像摄影升华为艺术作品', 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop', 1, 2000.00, '杭州', 400.00, 2100, 534, 87, 1, '2026-04-12 18:00:00');

-- 模特的约拍需求
INSERT INTO `content` (`user_id`, `type`, `title`, `description`, `cover_image`, `price_type`, `price`, `location`, `deposit`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`) VALUES
(21, 2, '寻找时尚摄影师', '日系时尚风格，能拍出杂志感的照片', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 1, 500.00, '北京', 100.00, 680, 189, 34, 1, '2026-04-13 10:00:00'),
(22, 2, '古风摄影合作', '寻找擅长古风汉服的摄影师，长期合作', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop', 1, 800.00, '上海', 160.00, 520, 145, 28, 1, '2026-04-13 11:00:00'),
(23, 2, '森系风格拍摄', '喜欢森林系风格，期待唯美浪漫的成片', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=800&h=1000&fit=crop', 1, 400.00, '杭州', 80.00, 450, 123, 25, 1, '2026-04-13 12:00:00'),
(24, 2, '夜景人像合作', '寻找擅长夜景的摄影师，一起创作大片', 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop', 1, 600.00, '广州', 120.00, 380, 98, 19, 1, '2026-04-13 13:00:00'),
(25, 2, '私房艺术摄影', '寻找有艺术感的摄影师，创作有质感的作品', 'https://images.unsplash.com/photo-1502823403499-6ccfcf4fb453?w=800&h=1000&fit=crop', 1, 1000.00, '深圳', 200.00, 620, 167, 31, 1, '2026-04-13 14:00:00');

-- ----------------------------------------
-- 5. 内容图片（多角度展示）
-- ----------------------------------------

-- 摄影师1作品图片
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(59, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 1),
(59, 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop', 2),
(59, 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=800&h=1000&fit=crop', 3),
(60, 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=800&h=1000&fit=crop', 1),
(60, 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800&h=1000&fit=crop', 2),
(60, 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=800&h=1000&fit=crop', 3),
(61, 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop', 1),
(61, 'https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=800&h=1000&fit=crop', 2),
(61, 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop', 3);

-- 摄影师2作品图片
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(62, 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&h=1000&fit=crop', 1),
(62, 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800&h=1000&fit=crop', 2),
(62, 'https://images.unsplash.com/photo-1501854140801-50d01698950b?w=800&h=1000&fit=crop', 3),
(63, 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800&h=1000&fit=crop', 1),
(63, 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&h=1000&fit=crop', 2),
(63, 'https://images.unsplash.com/photo-1507400492013-162706c8c05e?w=800&h=1000&fit=crop', 3),
(64, 'https://images.unsplash.com/photo-1507400492013-162706c8c05e?w=800&h=1000&fit=crop', 1),
(64, 'https://images.unsplash.com/photo-1501854140801-50d01698950b?w=800&h=1000&fit=crop', 2),
(64, 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&h=1000&fit=crop', 3);

-- 摄影师3作品图片
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(65, 'https://images.unsplash.com/photo-1519741497674-611481863552?w=800&h=1000&fit=crop', 1),
(65, 'https://images.unsplash.com/photo-1529634806980-85c3dd6d34ac?w=800&h=1000&fit=crop', 2),
(65, 'https://images.unsplash.com/photo-1519225421980-715cb0215aed?w=800&h=1000&fit=crop', 3),
(66, 'https://images.unsplash.com/photo-1529634806980-85c3dd6d34ac?w=800&h=1000&fit=crop', 1),
(66, 'https://images.unsplash.com/photo-1519741497674-611481863552?w=800&h=1000&fit=crop', 2),
(66, 'https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=800&h=1000&fit=crop', 3),
(67, 'https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=800&h=1000&fit=crop', 1),
(67, 'https://images.unsplash.com/photo-1502685104226-ee32379fefbe?w=800&h=1000&fit=crop', 2),
(67, 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop', 3);

-- 摄影师4作品图片
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(68, 'https://images.unsplash.com/photo-1469334031218-e382a71b716b?w=800&h=1000&fit=crop', 1),
(68, 'https://images.unsplash.com/photo-1509942774463-acf3390a252d?w=800&h=1000&fit=crop', 2),
(68, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 3),
(69, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 1),
(69, 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop', 2),
(69, 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop', 3),
(70, 'https://images.unsplash.com/photo-1509942774463-acf3390a252d?w=800&h=1000&fit=crop', 1),
(70, 'https://images.unsplash.com/photo-1469334031218-e382a71b716b?w=800&h=1000&fit=crop', 2),
(70, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 3);

-- 摄影师5作品图片
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(71, 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop', 1),
(71, 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop', 2),
(71, 'https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=800&h=1000&fit=crop', 3),
(72, 'https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=800&h=1000&fit=crop', 1),
(72, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 2),
(72, 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&h=1000&fit=crop', 3),
(73, 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop', 1),
(73, 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop', 2),
(73, 'https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=800&h=1000&fit=crop', 3);

-- 模特需求图片
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(74, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 1),
(74, 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop', 2),
(75, 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&h=1000&fit=crop', 1),
(75, 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop', 2),
(76, 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=800&h=1000&fit=crop', 1),
(76, 'https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=800&h=1000&fit=crop', 2),
(77, 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&h=1000&fit=crop', 1),
(77, 'https://images.unsplash.com/photo-1502685104226-ee32379fefbe?w=800&h=1000&fit=crop', 2),
(78, 'https://images.unsplash.com/photo-1502823403499-6ccfcf4fb453?w=800&h=1000&fit=crop', 1),
(78, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&h=1000&fit=crop', 2);

-- ----------------------------------------
-- 6. 标签数据
-- ----------------------------------------

INSERT INTO `tag` (`name`) VALUES
('人像'), ('风光'), ('纪实'), ('婚礼'), ('商业'),
('时尚'), ('日系'), ('小清新'), ('古风'), ('汉服'),
('森系'), ('文艺'), ('复古'), ('夜景'), ('都市'),
('轻奢'), ('私房'), ('性感'), ('艺术'), ('甜美'),
('可爱'), ('少女'), ('青春'), ('阳光'), ('运动'),
('海景'), ('度假'), ('浪漫');

-- ----------------------------------------
-- 7. 内容标签关联
-- ----------------------------------------

INSERT INTO `content_tag` (`content_id`, `tag_id`) VALUES
-- 摄影师1作品标签
(59, 1), (59, 6), (59, 7),  -- 情绪人像组照: 人像,日系,小清新
(60, 1), (60, 5), (60, 14), -- 城市街拍: 人像,商业,夜景
(61, 1), (60, 12), (60, 7), -- 文艺复古风: 人像,复古,小清新
-- 摄影师2作品标签
(62, 2), (62, 6), (62, 5),  -- 日出云海: 风光,时尚,商业
(63, 2), (63, 10), (63, 5), -- 森林秘境: 风光,森系,商业
(64, 2), (64, 5), (64, 1),  -- 星空银河: 风光,商业,人像
-- 摄影师3作品标签
(65, 4), (65, 26), (65, 27), -- 草坪婚礼: 婚礼,海景,度假
(66, 4), (66, 8), (66, 9),  -- 中式婚礼: 婚礼,古风,汉服
(67, 4), (65, 26), (65, 27), -- 婚纱外景: 婚礼,海景,浪漫
-- 摄影师4作品标签
(68, 5), (68, 6), (68, 1),  -- 时尚大片: 商业,时尚,人像
(69, 5), (69, 1), (69, 15), -- 商业人像: 商业,人像,轻奢
(70, 5), (70, 15), (70, 1), -- 产品摄影: 商业,轻奢,人像
-- 摄影师5作品标签
(71, 18), (71, 19), (71, 1), -- 人体艺术: 性感,艺术,人像
(72, 19), (72, 6), (72, 1), -- 创意概念: 艺术,时尚,人像
(73, 19), (73, 1), (73, 7), -- 艺术人像: 艺术,人像,小清新
-- 模特需求标签
(74, 6), (74, 7), (74, 1),   -- 时尚摄影师: 时尚,小清新,人像
(75, 8), (75, 9), (75, 1),   -- 古风摄影: 古风,汉服,人像
(76, 10), (76, 11), (76, 2), -- 森系风格: 森系,文艺,风光
(77, 14), (77, 15), (77, 1), -- 夜景人像: 夜景,都市,人像
(78, 16), (77, 17), (77, 19); -- 私房艺术: 私房,性感,艺术

-- ----------------------------------------
-- 8. 用户资料
-- ----------------------------------------

INSERT INTO `user_profile` (`user_id`, `gender`, `city`, `bio`, `create_time`) VALUES
(21, 2, '北京', '专业模特，擅长时尚街拍和日系小清新风格', '2026-04-01 10:00:00'),
(22, 2, '上海', '古风御用模特，曾参与多部影视剧拍摄', '2026-04-02 10:00:00'),
(23, 2, '杭州', '森系文艺少女，喜欢在大自然中寻找灵感', '2026-04-03 10:00:00'),
(24, 2, '广州', '都市夜景女王，擅长在霓虹都市中捕捉璀璨瞬间', '2026-04-04 10:00:00'),
(25, 2, '深圳', '艺术人体模特，注重情感表达', '2026-04-05 10:00:00'),
(26, 2, '成都', '邻家女孩风格，笑容甜美，治愈系模特', '2026-04-06 10:00:00'),
(27, 2, '重庆', '运动系少女，健康阳光', '2026-04-07 10:00:00'),
(28, 2, '厦门', '海边度假风模特，喜欢在海边拍摄浪漫唯美作品', '2026-04-08 10:00:00'),
(29, 1, '北京', '专注于人像摄影，擅长捕捉人物内心情感', '2026-04-01 10:00:00'),
(30, 1, '上海', '风光摄影师，喜欢探索大自然的美丽', '2026-04-02 10:00:00'),
(31, 1, '广州', '婚礼摄影师，记录幸福时刻', '2026-04-03 10:00:00'),
(32, 1, '深圳', '时尚摄影师，服务于各大品牌', '2026-04-04 10:00:00'),
(33, 1, '杭州', '艺术摄影师，追求极致的美', '2026-04-05 10:00:00'),
(35, 1, '北京', NULL, '2026-04-01 10:00:00'),
(36, 0, '上海', NULL, '2026-04-02 10:00:00'),
(37, 1, '杭州', NULL, '2026-04-03 10:00:00');

-- ----------------------------------------
-- 9. 订单数据
-- ----------------------------------------

INSERT INTO `order` (`order_no`, `user_id`, `photographer_id`, `content_id`, `total_amount`, `deposit_amount`, `paid_deposit`, `paid_balance`, `location`, `shoot_time`, `notes`, `contact_name`, `contact_phone`, `status`, `create_time`) VALUES
('SP20260420001', 35, 29, 59, 800.00, 160.00, 160.00, 0.00, '北京市朝阳区798艺术区', '2026-04-25 14:00:00', '希望拍摄一组情绪人像，风格偏文艺', '小鱼儿', '13612345601', 1, '2026-04-20 10:00:00'),
('SP20260420002', 36, 30, 62, 1200.00, 240.00, 240.00, 0.00, '黄山风景区', '2026-04-28 06:00:00', '日出云海拍摄，需要早起', '天空之城', '13612345602', 1, '2026-04-20 11:00:00'),
('SP20260421001', 35, 31, 65, 3000.00, 600.00, 600.00, 2400.00, '广州市白云区草坪婚礼场地', '2026-05-01 10:00:00', '草坪婚礼全天拍摄', '小鱼儿', '13612345601', 4, '2026-04-21 09:00:00'),
('SP20260422001', 37, 32, 68, 2500.00, 500.00, 500.00, 0.00, '深圳市南山区时尚创意园', '2026-04-30 15:00:00', '时尚大片拍摄，合作期刊', '竹林深处', '13612345603', 1, '2026-04-22 14:00:00');

-- ----------------------------------------
-- 10. 支付记录
-- ----------------------------------------

INSERT INTO `payment_record` (`order_no`, `user_id`, `photographer_id`, `amount`, `payment_method`, `transaction_id`, `type`, `status`, `create_time`) VALUES
('SP20260420001', 35, 29, 160.00, '微信支付', 'WX20260420001001', 0, 1, '2026-04-20 10:05:00'),
('SP20260420002', 36, 30, 240.00, '微信支付', 'WX20260420002001', 0, 1, '2026-04-20 11:05:00'),
('SP20260421001', 35, 31, 600.00, '微信支付', 'WX20260421001001', 0, 1, '2026-04-21 09:05:00'),
('SP20260421001', 35, 31, 2400.00, '微信支付', 'WX20260421001002', 1, 1, '2026-05-01 18:00:00'),
('SP20260422001', 37, 32, 500.00, '微信支付', 'WX20260422001001', 0, 1, '2026-04-22 14:05:00');

-- ----------------------------------------
-- 11. 消息数据
-- ----------------------------------------

INSERT INTO `message` (`send_user_id`, `receive_user_id`, `content`, `type`, `status`, `related_id`, `create_time`) VALUES
(35, 29, '您好，看了您的作品很喜欢，想预约一组情绪人像拍摄', 0, 0, 1, '2026-04-20 10:00:00'),
(29, 35, '您好，感谢关注！请问您想拍摄什么风格的呢？', 0, 0, 1, '2026-04-20 10:30:00'),
(35, 29, '想要文艺一点的，参考您那组情绪人像组照', 0, 0, 1, '2026-04-20 10:35:00'),
(29, 35, '好的，我们可以在拍摄前详细沟通一下具体的拍摄方案。请问您对场地有要求吗？', 0, 0, 1, '2026-04-20 10:40:00'),
(35, 29, '想去798艺术区，那边场景比较丰富', 0, 0, 1, '2026-04-20 10:45:00'),
(29, 35, '没问题，798确实很适合拍文艺风格。已为您创建订单，请支付定金', 1, 0, 1, '2026-04-20 10:50:00');

-- ----------------------------------------
-- 12. 公告数据
-- ----------------------------------------

INSERT INTO `announcement` (`title`, `content`, `author_id`, `status`, `create_time`) VALUES
('🌸 春日摄影大赛开启', '春暖花开的季节，用镜头捕捉美好！参与我们的春日摄影大赛，有机会获得丰厚奖品和官方推荐。', 29, 1, '2026-04-15 10:00:00'),
('📸 新功能上线：在线预约', '现在可以直接在小程序中预约摄影师啦！支持在线支付定金，让预约更便捷。', 30, 1, '2026-04-16 10:00:00'),
('🎁 五一假期特惠活动', '五一假期期间，所有摄影服务享受8折优惠！快来预约你心仪的摄影师吧。', 29, 1, '2026-04-17 10:00:00');
