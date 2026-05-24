-- 删除所有图片数据
DELETE FROM `content_image`;

-- 更新content表中的images字段为空
UPDATE `content` SET `images` = '' WHERE 1=1;

-- 为摄影师1（user_id=1）添加新的测试图片
-- 作品1：人像摄影
UPDATE `content` SET `images` = 'https://picsum.photos/800/600?random=101,https://picsum.photos/800/600?random=102,https://picsum.photos/800/600?random=103' WHERE `id` = 1;
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(1, 'https://picsum.photos/800/600?random=101', 1),
(1, 'https://picsum.photos/800/600?random=102', 2),
(1, 'https://picsum.photos/800/600?random=103', 3);

-- 作品2：风景摄影
UPDATE `content` SET `images` = 'https://picsum.photos/800/600?random=104,https://picsum.photos/800/600?random=105' WHERE `id` = 3;
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(3, 'https://picsum.photos/800/600?random=104', 1),
(3, 'https://picsum.photos/800/600?random=105', 2);

-- 为摄影师2（user_id=4）添加新的测试图片
-- 作品1：时尚摄影
UPDATE `content` SET `images` = 'https://picsum.photos/800/600?random=106,https://picsum.photos/800/600?random=107' WHERE `id` = 4;
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(4, 'https://picsum.photos/800/600?random=106', 1),
(4, 'https://picsum.photos/800/600?random=107', 2);

-- 作品2：商业摄影
UPDATE `content` SET `images` = 'https://picsum.photos/800/600?random=108,https://picsum.photos/800/600?random=109' WHERE `id` = 5;
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(5, 'https://picsum.photos/800/600?random=108', 1),
(5, 'https://picsum.photos/800/600?random=109', 2);

-- 为摄影师3（user_id=5）添加新的测试图片
-- 作品1：婚礼摄影
UPDATE `content` SET `images` = 'https://picsum.photos/800/600?random=110,https://picsum.photos/800/600?random=111' WHERE `id` = 6;
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(6, 'https://picsum.photos/800/600?random=110', 1),
(6, 'https://picsum.photos/800/600?random=111', 2);

-- 作品2：纪实摄影
UPDATE `content` SET `images` = 'https://picsum.photos/800/600?random=112,https://picsum.photos/800/600?random=113' WHERE `id` = 7;
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(7, 'https://picsum.photos/800/600?random=112', 1),
(7, 'https://picsum.photos/800/600?random=113', 2);

-- 为模特（user_id=2）添加新的测试图片
-- 需求：寻找模特拍摄时尚大片
UPDATE `content` SET `images` = 'https://picsum.photos/800/600?random=114' WHERE `id` = 2;
INSERT INTO `content_image` (`content_id`, `image_url`, `sort_order`) VALUES
(2, 'https://picsum.photos/800/600?random=114', 1);

-- 提交事务
COMMIT;