package com.mdc.mim.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 好友关系实体类
 * @date 2023/11/21 21:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendEntity implements Serializable {
    private Long uid;
    private Long friendUid;
    private Date createTime;
}
