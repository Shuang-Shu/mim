package com.mdc.mim.user.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    // 持久化属性
    private Long id;
    private Long uid;
    private String username;
    private String passwdMd5;
    private Date registerDate;
}
