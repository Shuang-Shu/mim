package com.mdc.mim.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdc.mim.user.dao.UserMapper;
import com.mdc.mim.user.dao.UserStatusMapper;
import com.mdc.mim.user.domain.User;
import com.mdc.mim.user.domain.UserStatus;
import com.mdc.mim.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserStatusMapper userStatusMapper;

    @Override
    public List<User> findByUids(List<Long> uids) {
        return userMapper.findByUids(uids);
    }

    @Override
    public List<User> findAll() {
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
        var uids = userMapper.findByNames(userNames).stream().map(User::getUid).toList();
        userStatusMapper.deleteByUids(uids); // 删除存在的状态
        return userMapper.deleteByNames(userNames);
    }

    @Override
    public int deleteByIds(List<Long> uids) {
        userStatusMapper.deleteByUids(uids);
        return userMapper.deleteByIds(uids);
    }

    @Override
    public int updateUserEntity(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int insertUserEntity(User user) {
        var userStatus = new UserStatus(user.getUid());
        userStatusMapper.insertUserStatus(userStatus);
        return userMapper.insertUser(user);
    }

    @Override
    public User findByName(String userName) {
        return userMapper.findByName(userName);
    }
}