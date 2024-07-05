package com.mdc.mim.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 描述用户当前状态，包括在线、离线和隐身
 * @date 2023/11/20 17:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusEntity implements Serializable {
    public static UserStatusEntity buildWith(UserEntity user) {
        return UserStatusEntity.builder().uid(user.getUid()).status(0)
                .lastOnlineTime(new Date(System.currentTimeMillis())).doNotify(true).build();
    }

    private Long uid;
    // 用户状态：在线(2)、隐身(1)和离线(0)等
    private Integer status;
    // 用户最后一次在线时间
    private Date lastOnlineTime;
    // 用户是否接收消息通知
    private Boolean doNotify;
}
