package com.mdc.mim.rest.service;

import com.mdc.mim.rest.entity.UserStatusEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/20 17:03
 */
public interface UserStatusService {
    // 修改用户状态的接口
    // 上线
    @CacheEvict(value = "userStatus", key = "#uid")
    void online(Long uid);

    // 离线
    @CacheEvict(value = "userStatus", key = "#uid")
    void offline(Long uid);

    // 隐身
    @CacheEvict(value = "userStatus", key = "#uid")
    void invisible(Long uid);

    // 获取用户状态
    @Cacheable(value = "userStatus", key = "#uid", condition = "#result != null")
    UserStatusEntity findByUid(Long uid);
}
