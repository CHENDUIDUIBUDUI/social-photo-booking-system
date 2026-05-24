-- 清理现有测试数据
DELETE FROM user_credit WHERE user_id > 20;
DELETE FROM credit_record WHERE user_id > 20;
DELETE FROM photographer_application WHERE user_id > 20;
DELETE FROM model_card WHERE user_id > 20;
DELETE FROM content_tag WHERE content_id > 58;
DELETE FROM content_image WHERE content_id > 58;
DELETE FROM content WHERE user_id > 20;
DELETE FROM photographer WHERE user_id > 20;
DELETE FROM user_profile WHERE user_id > 20;
DELETE FROM user WHERE id > 20;

-- 重置自增ID
ALTER TABLE user AUTO_INCREMENT = 21;
ALTER TABLE user_profile AUTO_INCREMENT = 20;
ALTER TABLE photographer AUTO_INCREMENT = 21;
ALTER TABLE content AUTO_INCREMENT = 59;
ALTER TABLE content_image AUTO_INCREMENT = 146;
ALTER TABLE content_tag AUTO_INCREMENT = 10495;
