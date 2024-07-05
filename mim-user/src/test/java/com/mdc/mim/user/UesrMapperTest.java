package com.mdc.mim.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mdc.mim.user.dao.UserMapper;

@SpringBootTest
public class UesrMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void testBasic() {
        System.out.println(userMapper);
    }

    @Test
    public void testGetUsers() {
        var users = userMapper.findAll();
        System.out.println(users);
    }
}
