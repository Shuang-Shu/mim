package com.mdc.mim.user.service;

import java.util.List;

import com.mdc.mim.user.entity.UserEntity;

/*
 * 用于查询用户信息
 */
public interface UserEntityService {
    UserEntity queryByName(String userName);

    List<UserEntity> queryByIds(List<Long> uids);

    List<UserEntity> queryAll();

    int deleteByName(String userName);

    int deleteByNames(List<String> userNames);

    int deleteByIds(List<Long> uids);
}
