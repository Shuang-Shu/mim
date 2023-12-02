package com.mdc.mim.netty.feign;

import com.mdc.mim.common.dto.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/13 22:12
 */
@FeignClient(value = "mim-rest", contextId = "user-service")
public interface UserFeignService {
    @PostMapping("/user/identify")
    public R identify(@RequestParam("userName") String userName, @RequestParam("passwdMd5") String passwdMd5);

    @PostMapping("/user/find-uid")
    public R findUidByUserName(@RequestParam("userName") String userName);
}
