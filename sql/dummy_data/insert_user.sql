INSERT INTO user (uid, passwdMd5, userName, nickName, registerDate)
VALUES (1, MD5 ("12345"), "shuangshu", "双竖", "2017-01-01"),
    (2, MD5 ("12345"), "huamao", "花猫", "2017-01-01"),
    (
        3,
        MD5 ("12345"),
        "local_tyrant_451",
        "451",
        "2017-01-01"
    ),
    (4, MD5 ("12345"), "He_Aaron", "何", "2017-01-01");

-- 插入用户状态
INSERT INTO user_status (uid, status, lastOnlineTime, doNotify)
VALUES (1, 0, "2017-01-01", TRUE),
    (2, 0, "2017-01-01", TRUE),
    (3, 0, "2017-01-01", TRUE),
    (4, 0, "2017-01-01", TRUE);