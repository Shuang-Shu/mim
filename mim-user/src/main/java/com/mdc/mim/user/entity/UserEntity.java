package com.mdc.mim.user.entity;

import java.sql.Date;

import com.mdc.mim.common.constant.Platform;
import com.mdc.mim.common.dto.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    // 持久化属性
    private Long uid;
    private String passwdMd5;
    private String userName;
    private String nickName;
    private Date registerDate;
    // 非持久化属性
    private String devId;
    private String token; // 基于JWT的访问token
    @Builder.Default
    private Platform platform = Platform.NOT_DEFINED; // 用户使用平台

    public static UserEntity parseFromLoginMsg(Message.LoginRequest req) {
        return UserEntity.builder().uid(req.getUid()).devId(req.getDeviceId()).token(req.getToken())
                .platform(req.getPlatform()).build();
    }
}
