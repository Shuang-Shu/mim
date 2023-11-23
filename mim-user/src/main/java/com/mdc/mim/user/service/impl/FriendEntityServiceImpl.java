package com.mdc.mim.user.service.impl;

import com.mdc.mim.user.entity.FriendEntity;
import com.mdc.mim.user.mapper.FriendEntityMapper;
import com.mdc.mim.user.service.FriendEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 22:23
 */
@Service
public class FriendEntityServiceImpl implements FriendEntityService {
    @Autowired
    FriendEntityMapper friendEntityMapper;

    @Override
    public FriendEntity findByUidAndFriendUid(Long uid, Long friendUid) {
        return friendEntityMapper.findByUidAndFriendUid(uid, friendUid);
    }
}
