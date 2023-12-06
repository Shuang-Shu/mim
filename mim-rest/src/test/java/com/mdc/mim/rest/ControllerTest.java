package com.mdc.mim.rest;

import java.sql.Date;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.common.utils.JsonUtils;
import com.mdc.mim.rest.utils.MockMvcTestUtils;
import com.mdc.mim.rest.entity.UserEntity;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    MockMvc mvc;

    MockHttpSession session;

    @BeforeEach
    public void setupMockMvc() {
        session = new MockHttpSession();
    }

    @Test
    @SuppressWarnings("unchecked")
    void loginTest() throws Exception {
        var user = UserEntity.builder().userName("shuangshu").passwdMd5(DigestUtils.md5("12345")).nickName("test_nick")
                .registerDate(new Date(System.currentTimeMillis()))
                .build();
        var url = "/user/identify";
        var r1 = MockMvcTestUtils.mockPerformPost(mvc, session, url, JsonUtils.object2Map(user));
        Assertions.assertEquals(true, r1.get("valid"));
        user.setUserName("shuangshu1");
        var r2 = MockMvcTestUtils.mockPerformPost(mvc, session, url, JsonUtils.object2Map(user));
        Assertions.assertEquals(false, r2.get("valid"));
    }

    @Test
    void friendControllerTest() throws Exception {
        var url = "/user/friend/is-friend";
        var map = Map.of("uid", "1", "friendUid", "3");
        var r1 = MockMvcTestUtils.mockPerformPost(mvc, session, url, map);
        Assertions.assertEquals(true, r1.get("isFriend"));
    }
}
