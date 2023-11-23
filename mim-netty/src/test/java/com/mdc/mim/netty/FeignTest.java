package com.mdc.mim.netty;

import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.netty.feign.FriendService;
import com.mdc.mim.netty.feign.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/13 22:48
 */
@SpringBootTest
public class FeignTest {
    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Test
    public void basicTest() {
        Assertions.assertNotNull(userService);
    }

    @Test
    public void testUserService() {
        var r = userService.identify("shuangshu", DigestUtils.md5("12345"));
        Assertions.assertEquals(true, r.get("valid"));
        r = userService.identify("shuangshu1", DigestUtils.md5("12345"));
        Assertions.assertEquals(false, r.get("valid"));
        r = userService.findUidByUserName("shuangshu");
        Assertions.assertEquals(1L, Long.valueOf((Integer) r.get("uid")));
    }

    @Test
    public void testFriendService() {
//        userService.isFriend(1L, 2L);
        friendService.isFriend(1L, 2L);
    }
}
