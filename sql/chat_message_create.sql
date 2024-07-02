CREATE TABLE `chat_message`(
    `id` BIGINT PRIMARY KEY COMMENT '消息id',
    `from_uid` BIGINT NOT NULL COMMENT '发送者的uid',
    `to_uid` BIGINT NOT NULL COMMENT '接收者的uid',
    `type` INT NOT NULL COMMENT '消息类型：1: text, 2: long-text, 3: image, 4: audio, 5: video',
    `content` VARCHAR(512) NOT NULL COMMENT '消息内容，text类型直接存储，其余类型存储oss的url',
    `create_time` DATE NOT NULL COMMENT '创建时间',
    INDEX `idx_from_type_time` (`from_uid`, `type`, `create_time`)
) ENGINE = InnoDB,
CHARSET = utf8mb4;