package com.mdc.mim.user;

import com.github.pagehelper.PageHelper;
import com.mdc.mim.user.mapper.UserEntityMapper;
import com.mdc.mim.user.service.UserEntityService;
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
public class RedisTest {
    @Autowired
    UserEntityMapper userEntityMapper;
    @Autowired
    UserEntityService userEntityService;

    @Test
    public void testBasic() {
        System.out.println(userEntityService.findAll());
    }

    @Test
    public void testUserEntityService() {
        var user = userEntityService.findByName("shuangshu");
        Assertions.assertNotNull(user);
        PageHelper.startPage(1, 10);
        var uidList = List.of(1L, 2L, 3L, 4L);
        var userList = userEntityService.findByUids(uidList);
        Assertions.assertEquals(4, userList.size());
    }

    @Test
    public void testDeleteCache() {
        var user = userEntityService.findByName("shuangshu");
        Assertions.assertNotNull(user);
        userEntityService.deleteByName("shuangshu");
        var tmp = userEntityService.findByName("shuangshu");
        Assertions.assertNull(tmp);
        userEntityMapper.insertUser(user);
    }
}
