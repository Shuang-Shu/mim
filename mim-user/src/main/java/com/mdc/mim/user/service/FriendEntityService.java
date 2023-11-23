package com.mdc.mim.user.service;

import com.mdc.mim.user.entity.FriendEntity;
import org.springframework.stereotype.Service;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 22:19
 */
public interface FriendEntityService {
    public FriendEntity findByUidAndFriendUid(Long uid, Long friendUid);
}
