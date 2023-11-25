package com.mdc.mim.rest;

import com.mdc.mim.common.constant.UserStatusConst;
import com.mdc.mim.rest.mapper.UserMapper;
import com.mdc.mim.rest.service.UserService;
import com.mdc.mim.rest.service.UserStatusService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/24 19:48
 */
@SpringBootTest
public class RedisCacheTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserStatusService userStatusService;

    @Test
    public void testBasic() {
        System.out.println(userService.findAll());
    }

    @Test
    public void testUserEntityService() {
        var user = userService.findByName("shuangshu");
        Assertions.assertNotNull(user);
        var uidList = List.of(1L, 2L, 3L, 4L);
        var userList = userService.findByUids(uidList);
        Assertions.assertEquals(4, userList.size());
    }

    @Test
    public void testDeleteCache() {
        var user = userService.findByName("shuangshu");
        Assertions.assertNotNull(user);
        userService.deleteByName("shuangshu");
        var tmp = userService.findByName("shuangshu");
        Assertions.assertNull(tmp);
        userMapper.insertUser(user);
    }

    @Test
    public void testUserStatus() {
        var uid = 1L;
        userStatusService.online(uid);
        var userStatus = userStatusService.findByUid(uid);
        Assertions.assertEquals(UserStatusConst.ONLINE, userStatus.getStatus());
        userStatusService.offline(uid);
        userStatus = userStatusService.findByUid(uid);
        Assertions.assertEquals(UserStatusConst.OFFLINE, userStatus.getStatus());
        userStatusService.invisible(uid);
        userStatus = userStatusService.findByUid(uid);
        Assertions.assertEquals(UserStatusConst.INVISIBLE, userStatus.getStatus());
    }
}
