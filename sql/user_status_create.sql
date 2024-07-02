CREATE TABLE `user_status` (
    `uid` BIGINT PRIMARY KEY,
    `status` TINYINT NOT NULL,
    -- 0: 未知，1；在线，2：离线， 3：隐身
    `notify_status` TINYINT NOT NULL DEFAULT 0,
    -- 通知状态，0：不通知，1：通知
) ENGINE = InnoDB,
CHARSET = utf8mb4;