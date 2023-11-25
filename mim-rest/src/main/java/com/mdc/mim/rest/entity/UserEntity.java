package com.mdc.mim.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {
    // 持久化属性
    private Long uid;
    private String userName;
    private String passwdMd5;
    private String nickName;
    private Date registerDate;
}
