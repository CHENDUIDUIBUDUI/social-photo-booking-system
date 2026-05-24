-- 检查内容表数据
USE social_shoot;

-- 检查内容表记录数
SELECT COUNT(*) AS total_content FROM content;

-- 检查内容表中的图片字段
SELECT id, title, images FROM content LIMIT 10;