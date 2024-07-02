CREATE TABLE `friend` (
    `uid` BIGINT,
    `friend_uid` BIGINT,
    `create_time` DATE NOT NULL,
    PRIMARY KEY uf_index (uid, friendUid)
) ENGINE = InnoDB,
CHARSET = utf8mb4;