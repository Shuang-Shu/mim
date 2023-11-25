package com.mdc.mim.rest.service.impl;

import com.mdc.mim.rest.entity.FriendEntity;
import com.mdc.mim.rest.mapper.FriendMapper;
import com.mdc.mim.rest.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 22:23
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    FriendMapper friendMapper;

    @Override
    public FriendEntity findByUidAndFriendUid(Long uid, Long friendUid) {
        return friendMapper.findByUidAndFriendUid(uid, friendUid);
    }
}
