CREATE TABLE `max_seq` (
    `uid` BIGINT PRIMARY KEY,
    `max_seqno` BIGINT NOT NULL COMMENT '当前uid可分配的最大消息序列号'
) ENGINE = InnoDB,
CHARSET = utf8mb4 COMMENT '指定uid的id对话id分配';