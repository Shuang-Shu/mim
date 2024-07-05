INSERT INTO user (uid, passwd_md5, username, register_date)
VALUES (1, MD5("12345"), "双竖", "2017-01-01"),
    (2, MD5("12345"), "花猫", "2017-01-01"),
    (3, MD5("12345"), "451", "2017-01-01"),
    (4, MD5("12345"), "何", "2017-01-01");
-- 插入用户状态
INSERT INTO user_status (uid, status, notify_status)
VALUES (1, 0, 1),
    (2, 0, 1),
    (3, 0, 1),
    (4, 0, 1);
-- 插入用户对应的最大序号
INSERT INTO max_seq (uid, max_seqno)
VALUES (1, 0),
    (2, 0),
    (3, 0),
    (4, 0);