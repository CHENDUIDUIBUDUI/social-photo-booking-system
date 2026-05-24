-- 更新user表，添加openid列
ALTER TABLE user ADD COLUMN openid VARCHAR(100) UNIQUE COMMENT '微信openid' AFTER id;

-- 添加索引
CREATE INDEX idx_openid ON user(openid);

-- 查看表结构
DESCRIBE user;
