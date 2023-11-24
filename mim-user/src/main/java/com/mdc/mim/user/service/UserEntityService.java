package com.mdc.mim.user.service;

import java.util.List;

import com.mdc.mim.user.entity.UserEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/*
 * 用于查询用户信息
 */
public interface UserEntityService {
    @Cacheable(value = "user", key = "#userName", condition = "#result != null")
    UserEntity findByName(String userName);

    @Cacheable(value = "user", condition = "#result != null and #result.size() > 0")
    List<UserEntity> findByUids(List<Long> uids);

    List<UserEntity> findAll();

    @CacheEvict(value = "user", key = "#userName")
    int deleteByName(String userName);

    @CacheEvict(value = "user", allEntries = true)
    int deleteByNames(List<String> userNames);

    @CacheEvict(value = "user", allEntries = true)
    int deleteByIds(List<Long> uids);
}
