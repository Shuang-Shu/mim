CREATE TABLE user (
    uid BIGINT PRIMARY KEY AUTO_INCREMENT,
    passwdMd5 CHAR(32) NOT NULL,
    userName VARCHAR(40) NOT NULL,
    nickName VARCHAR(40) NOT NULL,
    registerDate DATE NOT NULL
) ENGINE = InnoDB,
Charset = utf8;