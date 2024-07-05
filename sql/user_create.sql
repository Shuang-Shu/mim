CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `uid` BIGINT NOT NULL UNIQUE,
    `passwd_md5` CHAR(32) NOT NULL,
    `username` VARCHAR(40) NOT NULL,
    `register_date` DATE NOT NULL,
    INDEX `idx_nick_name_uid` (`username`, `uid`)
) ENGINE = InnoDB,
Charset = utf8mb4;