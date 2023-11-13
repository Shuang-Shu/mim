package com.mdc.mim.user;

import java.sql.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.pagehelper.PageHelper;
import com.mdc.mim.user.entity.UserEntity;
import com.mdc.mim.user.mapper.UserEntityMapper;
import com.mdc.mim.user.mapper.UserLoginMapper;

@SpringBootTest
public class MybatisTest {

    /**
     * 数据库内容与`duumy_data/insert_user.sql`应相同
     */

    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private UserEntityMapper userEntityMapper;

    @Test
    public void basicTest() {
        Assertions.assertNotNull(userLoginMapper);
    }

    @Test
    public void testQuery() {
        var user = userLoginMapper.queryByName("shuangshu");
        Assertions.assertEquals("827ccb0eea8a706c4c34a16891f84e7b", user.getPasswdMd5());
        var users = userLoginMapper.queryAll();
        Assertions.assertEquals(4, users.size());
    }

    @Test
    public void testAdd() {
        var user = UserEntity.builder().userName("test").passwdMd5("827ccb0eea8a706c4c34a16891f84e7b")
                .nickName("test_nickname")
                .registerDate(new Date(System.currentTimeMillis())).build();
        var ret = userLoginMapper.add(user);
        Assertions.assertEquals(1, ret);
        ret = userLoginMapper.deleteByName("test");
        Assertions.assertEquals(1, ret);
    }

    @Test
    public void testPage() {
        PageHelper.startPage(2, 2);
        var users = userLoginMapper.queryAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void testUserEntityMapper() {
        var users = userEntityMapper.queryAll();
        Assertions.assertEquals(4, users.size());
        userEntityMapper.addAll(users);
        Assertions.assertNotEquals(1, users.get(0).getUid());
        userEntityMapper.deleteByIds(users.stream().map(u -> u.getUid()).toList());
        users = userEntityMapper.queryAll();
        Assertions.assertEquals(4, users.size());
    }
}
