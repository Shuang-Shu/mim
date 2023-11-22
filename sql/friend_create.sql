CREATE TABLE friend (
    uid BIGINT,
    friendUid BIGINT,
    createTime DATE NOT NULL,
    PRIMARY KEY uf_index (uid, friendUid)
) ENGINE = InnoDB,
CHARSET = utf8;