package com.mdc.mim.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mdc.mim.user.mapper.UserLoginMapper;

@SpringBootTest
public class MybatisTest {

    /**
     * 数据库内容与`duumy_data/insert_user.sql`应相同
     */

    @Autowired
    private UserLoginMapper userLoginMapper;

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
}
