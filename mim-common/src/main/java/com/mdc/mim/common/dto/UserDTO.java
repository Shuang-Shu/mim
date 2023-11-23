package com.mdc.mim.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mdc.mim.common.enumeration.PlatformEnum;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long uid;
    private String userName;
    private String passwdMd5;
    private String nickName;
    private String devId;
    private String token; // 用于鉴权
    private PlatformEnum platformEnum;
}
