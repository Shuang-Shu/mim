package com.mdc.mim.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdc.mim.user.entity.UserEntity;
import com.mdc.mim.user.mapper.UserEntityMapper;
import com.mdc.mim.user.service.UserEntityService;

@Service
public class UserEntityServiceImpl implements UserEntityService {
    @Autowired
    private UserEntityMapper userEntityMapper;

    @Override
    public List<UserEntity> queryByIds(List<Long> uids) {
        return userEntityMapper.queryByIds(uids);
    }

    @Override
    public List<UserEntity> queryAll() {
        return userEntityMapper.queryAll();
    }

    @Override
    public int deleteByName(String userName) {
        return userEntityMapper.deleteByName(userName);
    }

    @Override
    public int deleteByNames(List<String> userNames) {
        return userEntityMapper.deleteByNames(userNames);
    }

    @Override
    public int deleteByIds(List<Long> uids) {
        return userEntityMapper.deleteByIds(uids);
    }

    @Override
    public UserEntity queryByName(String userName) {
        return userEntityMapper.queryByName(userName);
    }
}