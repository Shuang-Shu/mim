package com.mdc.mim.user.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus {
    public UserStatus(Long uid) {
        this.uid = uid;
        this.status = 0;
        this.doNotify = true;
    }

    private Long uid;
    // 用户状态：在线(2)、隐身(1)和离线(0)等
    private Integer status;
    // 用户是否接收消息通知
    private Boolean doNotify;
}
