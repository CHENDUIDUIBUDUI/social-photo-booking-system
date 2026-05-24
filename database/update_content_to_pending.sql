-- 将部分内容更新为待审核状态，用于测试数据看板待审核显示功能
-- 将最新的5条内容状态改为待审核
UPDATE content SET status = 0 WHERE id IN (
    SELECT id FROM (
        SELECT id FROM content WHERE status = 1 ORDER BY create_time DESC LIMIT 3
    ) AS tmp
);

-- 验证更新结果
SELECT id, title, status, create_time FROM content WHERE status = 0;
