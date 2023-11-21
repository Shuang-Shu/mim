CREATE TABLE user_status (
    uid BIGINT PRIMARY KEY,
    status INT NOT NULL,
    lastOnlineTime DATE NOT NULL,
    doNotify BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE = InnoDB,
CHARSET = utf8;