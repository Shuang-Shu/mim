CREATE TABLE chat_message(
    id BIGINT, -- 消息id
    fromUid BIGINT NOT NULL, -- 发送者的uid
    toUid BIGINT NOT NULL, -- 接收者的uid
    type INT NOT NULL, -- 1: text, 2: long-text, 3: image, 4: audio, 5: video
    content VARCHAR(512) NOT NULL, -- text类型直接存储，其余类型存储oss的url
    createTime DATE NOT NULL, -- 创建时间
    PRIMARY KEY (id),
    INDEX uf_index (fromUid, toUid)
) ENGINE = InnoDB, CHARSET = utf8;