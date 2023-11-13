INSERT INTO user (passwdMd5, userName, nickName, registerDate)
VALUES (MD5 ("12345"), "shuangshu", "双竖", "2017-01-01"),
    (MD5 ("12345"), "huamao", "花猫", "2017-01-01"),
    (
        MD5 ("12345"),
        "local_tyrant_451",
        "451",
        "2017-01-01"
    ),
    (MD5 ("12345"), "He_Aaron", "何", "2017-01-01");