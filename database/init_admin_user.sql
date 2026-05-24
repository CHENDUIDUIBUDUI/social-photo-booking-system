CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `real_name` VARCHAR(100),
  `avatar` VARCHAR(500),
  `role` INT DEFAULT 0 COMMENT '0: 普通管理员, 1: 超级管理员',
  `status` INT DEFAULT 1 COMMENT '0: 禁用, 1: 正常',
  `last_login_time` DATETIME,
  `last_login_ip` VARCHAR(50),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO `admin_user` (`username`, `password`, `real_name`, `role`, `status`) VALUES
('admin', 'admin123', '系统管理员', 1, 1);
