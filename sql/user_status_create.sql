CREATE TABLE user_status (
    -- 注意，创建/删除User时也需要同步创建/删除相同Uid的UserStatus
    uid BIGINT PRIMARY KEY,
    status INT NOT NULL, -- 在线(2)、隐身(1)和离线(0)等
    lastOnlineTime DATE NOT NULL, -- 最后一次在线时间，当用户由在线变为离线/隐身时更新；如果用户当前状态为在线，则该字段无意义
    doNotify BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE = InnoDB,
CHARSET = utf8;