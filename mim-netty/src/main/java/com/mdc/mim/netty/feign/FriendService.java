package com.mdc.mim.netty.feign;

import com.mdc.mim.common.dto.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: feign 调用用户服务
 * @date 2023/11/23 11:52
 */
@FeignClient(value = "mim-user", contextId = "friend-service")
public interface FriendService {
    @PostMapping("/user/friend/is-friend")
    public R isFriend(@RequestParam("uid") Long uid, @RequestParam("friendUid") Long friendUid);
}
