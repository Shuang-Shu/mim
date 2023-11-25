package com.mdc.mim.rest.service.impl;

import com.mdc.mim.common.constant.UserStatusConst;
import com.mdc.mim.rest.entity.UserStatusEntity;
import com.mdc.mim.rest.mapper.UserStatusMapper;
import com.mdc.mim.rest.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 22:24
 */
@Service
public class UserStatusServiceImpl implements UserStatusService {
    @Autowired
    private UserStatusMapper userStatusMapper;

    private void updateTo(Long uid, Integer status) {
        var userStatus = userStatusMapper.findByUid(uid);
        if (userStatus == null) {
            throw new RuntimeException("user status not found");
        }
        userStatus.setStatus(status);
        userStatusMapper.updateUserStatus(userStatus);
    }

    @Override
    public void online(Long uid) {
        updateTo(uid, UserStatusConst.ONLINE);
    }

    @Override
    public void offline(Long uid) {
        updateTo(uid, UserStatusConst.OFFLINE);
    }

    @Override
    public void invisible(Long uid) {
        updateTo(uid, UserStatusConst.INVISIBLE);
    }

    @Override
    public UserStatusEntity findByUid(Long uid) {
        return userStatusMapper.findByUid(uid);
    }
}
