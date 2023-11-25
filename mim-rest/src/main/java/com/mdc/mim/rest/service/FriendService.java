package com.mdc.mim.rest.service;

import com.mdc.mim.rest.entity.FriendEntity;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 22:19
 */
public interface FriendService {
    public FriendEntity findByUidAndFriendUid(Long uid, Long friendUid);
}
