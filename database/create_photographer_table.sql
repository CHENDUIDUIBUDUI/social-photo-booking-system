-- 创建摄影师表
CREATE TABLE IF NOT EXISTS `photographer` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '摄影师ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) COMMENT '摄影师昵称',
  `avatar` VARCHAR(255) COMMENT '摄影师头像',
  `certified` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '认证状态: 0-未认证, 1-已认证',
  `certified_time` DATETIME COMMENT '认证时间',
  `orders` INT(11) NOT NULL DEFAULT 0 COMMENT '接单量',
  `rating` DECIMAL(3,2) NOT NULL DEFAULT 0.00 COMMENT '好评率',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '起拍价格',
  `region` VARCHAR(50) COMMENT '所在区域',
  `styles` VARCHAR(255) COMMENT '擅长风格，逗号分隔',
  `cover_image` VARCHAR(500) COMMENT '封面图片',
  `bio` TEXT COMMENT '个人简介',
  `works` TEXT COMMENT '作品集，JSON数组',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`),
  KEY `idx_certified` (`certified`),
  KEY `idx_region` (`region`),
  KEY `idx_price` (`price`),
  KEY `idx_rating` (`rating`),
  KEY `idx_orders` (`orders`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_photographer_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄影师表';

-- 插入测试数据
INSERT INTO `photographer` (`user_id`, `name`, `avatar`, `certified`, `certified_time`, `orders`, `rating`, `price`, `region`, `styles`, `cover_image`, `bio`, `works`, `status`) VALUES
(1, '摄影师1', 'https://picsum.photos/100/100?random=1', 1, '2024-01-01 10:00:00', 128, 98.00, 1200.00, '上海', '人像,街拍,时尚', 'https://picsum.photos/id/1018/750/300', '专业人像摄影师，拥有5年拍摄经验，擅长捕捉人物最真实的情感表达。曾为多家时尚杂志拍摄封面，作品风格清新自然。', '["https://picsum.photos/id/1011/300/300","https://picsum.photos/id/1012/300/300","https://picsum.photos/id/1013/300/300","https://picsum.photos/id/1014/300/300","https://picsum.photos/id/1015/300/300","https://picsum.photos/id/1016/300/300"]', 1),
(2, '模特1', 'https://picsum.photos/100/100?random=2', 1, '2024-01-02 10:00:00', 96, 95.00, 800.00, '北京', '风景,纪实,婚礼', 'https://picsum.photos/id/1025/750/300', '专注风景和纪实摄影，作品多次获得摄影比赛奖项。擅长用镜头记录生活中的美好瞬间。', '["https://picsum.photos/id/1021/300/300","https://picsum.photos/id/1022/300/300","https://picsum.photos/id/1023/300/300","https://picsum.photos/id/1024/300/300"]', 1),
(3, '用户1', 'https://picsum.photos/100/100?random=3', 0, NULL, 45, 92.00, 500.00, '广州', '人像,证件照,儿童', 'https://picsum.photos/id/1032/750/300', '专业人像摄影师，擅长证件照和儿童摄影。耐心细致，善于与客户沟通。', '["https://picsum.photos/id/1027/300/300","https://picsum.photos/id/1028/300/300","https://picsum.photos/id/1029/300/300"]', 1),
(4, '摄影师2', 'https://picsum.photos/100/100?random=4', 1, '2024-01-04 10:00:00', 156, 99.00, 1800.00, '上海', '商业,产品,人像', 'https://picsum.photos/id/1039/750/300', '资深商业摄影师，拥有10年商业摄影经验。曾为多家知名品牌拍摄产品广告，作品风格高端大气。', '["https://picsum.photos/id/1034/300/300","https://picsum.photos/id/1035/300/300","https://picsum.photos/id/1036/300/300","https://picsum.photos/id/1037/300/300","https://picsum.photos/id/1038/300/300"]', 1),
(5, '摄影师3', 'https://picsum.photos/100/100?random=5', 0, NULL, 67, 94.00, 600.00, '成都', '街拍,纪实,旅行', 'https://picsum.photos/id/1046/750/300', '热爱街拍和旅行摄影，擅长用镜头记录城市的人文风情。作品风格真实自然，充满生活气息。', '["https://picsum.photos/id/1041/300/300","https://picsum.photos/id/1042/300/300","https://picsum.photos/id/1043/300/300","https://picsum.photos/id/1044/300/300"]', 1);