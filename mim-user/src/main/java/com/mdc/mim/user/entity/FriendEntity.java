package com.mdc.mim.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 好友关系实体类
 * @date 2023/11/21 21:53
 */
@Data
public class FriendEntity {
    private Long uid;
    private Long friendUid;
    private Date createTime;
}
