package com.mdc.mim.user.entity;

import java.io.Serializable;
import java.sql.Date;

import com.mdc.mim.common.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static UserDTO getUserLoginDto(UserEntity user) {
        return UserDTO.builder().userName(user.userName).passwdMd5(user.passwdMd5).build();
    }
}
