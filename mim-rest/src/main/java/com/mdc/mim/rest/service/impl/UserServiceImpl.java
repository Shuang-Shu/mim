package com.mdc.mim.rest.service.impl;

import com.mdc.mim.rest.entity.UserEntity;
import com.mdc.mim.rest.entity.UserStatusEntity;
import com.mdc.mim.rest.mapper.UserMapper;
import com.mdc.mim.rest.mapper.UserStatusMapper;
import com.mdc.mim.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserStatusMapper userStatusMapper;

    @Override
    public List<UserEntity> findByUids(List<Long> uids) {
        return userMapper.findByUids(uids);
    }

    @Override
    public List<UserEntity> findAll() {
        return userMapper.findAll();
    }

    @Override
    public int deleteByName(String userName) {
        var uid = userMapper.findByName(userName).getUid();
        userStatusMapper.deleteByUid(uid);
        return userMapper.deleteByName(userName);
    }

    @Override
    public int deleteByNames(List<String> userNames) {
        var uids = userMapper.findByNames(userNames).stream().map(UserEntity::getUid).toList();
        userStatusMapper.deleteByUids(uids); // 删除存在的状态
        return userMapper.deleteByNames(userNames);
    }

    @Override
    public int deleteByIds(List<Long> uids) {
        userStatusMapper.deleteByUids(uids);
        return userMapper.deleteByIds(uids);
    }

    @Override
    public int updateUserEntity(UserEntity userEntity) {
        return userMapper.updateUser(userEntity);
    }

    @Override
    public int insertUserEntity(UserEntity userEntity) {
        var userStatus = UserStatusEntity.buildWith(userEntity);
        userStatusMapper.insertUserStatus(userStatus);
        return userMapper.insertUser(userEntity);
    }

    @Override
    public UserEntity findByName(String userName) {
        return userMapper.findByName(userName);
    }
}
