package com.mdc.mim.common.dto;

import com.mdc.mim.common.constant.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/15 10:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long uid;
    private String userName;
    private String passwdMd5;
    private String nickName;
    private String devId;
    private String token; // 用于鉴权
    private Platform platform;
}
