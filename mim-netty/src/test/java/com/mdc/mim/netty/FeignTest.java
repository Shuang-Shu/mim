package com.mdc.mim.netty;

import com.mdc.mim.common.constant.ChatMessageTypeConst;
import com.mdc.mim.common.dto.ChatMessageDTO;
import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.netty.feign.ChatMessageFeignService;
import com.mdc.mim.netty.feign.FriendFeignService;
import com.mdc.mim.netty.feign.UserFeignService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/13 22:48
 */
@SpringBootTest
public class FeignTest {
    @Autowired
    private UserFeignService userFeignService;

    @Autowired
    private FriendFeignService friendFeignService;

    @Autowired
    private ChatMessageFeignService chatMessageFeignService;

    ChatMessageDTO message = ChatMessageDTO.builder().id(0L).fromUid(1L).toUid(2L).content("hello").type(ChatMessageTypeConst.TEXT).createTime(new Date(System.currentTimeMillis())).build();

    @Test
    public void basicTest() {
        Assertions.assertNotNull(userFeignService);
    }

    @Test
    public void testUserService() {
        var r = userFeignService.identify("shuangshu", DigestUtils.md5("12345"));
        Assertions.assertEquals(true, r.get("valid"));
        r = userFeignService.identify("shuangshu1", DigestUtils.md5("12345"));
        Assertions.assertEquals(false, r.get("valid"));
        r = userFeignService.findUidByUserName("shuangshu");
        Assertions.assertEquals(1L, Long.valueOf((Integer) r.get("uid")));
    }

    @Test
    public void testFriendService() {
//        userFeignService.isFriend(1L, 2L);
        friendFeignService.isFriend(1L, 2L);
    }

    @Test
    public void testChatMessageService() {
        Assertions.assertNotNull(chatMessageFeignService);
        var r = chatMessageFeignService.saveMessage(List.of(message));
        // r.get("code")不等于0时，断言报错并打印r.get("error")
        System.out.println(r.get("error"));
    }
}
