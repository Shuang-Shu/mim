package com.mdc.mim.user.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.mdc.mim.user.domain.User;

import java.util.List;

public interface UserService {
    int insertUserEntity(User userEntity);

    @Cacheable(value = "user", key = "#userName", condition = "#result != null")
    User findByName(String userName);

    @Cacheable(value = "user", condition = "#result != null and #result.size() > 0")
    List<User> findByUids(List<Long> uids);

    List<User> findAll();

    @CacheEvict(value = "user", key = "#userName")
    int deleteByName(String userName);

    @CacheEvict(value = "user", allEntries = true)
    int deleteByNames(List<String> userNames);

    @CacheEvict(value = "user", allEntries = true)
    int deleteByIds(List<Long> uids);

    // 更新user
    @CacheEvict(value = "user", key = "#userName")
    int updateUserEntity(User userEntity);
}
