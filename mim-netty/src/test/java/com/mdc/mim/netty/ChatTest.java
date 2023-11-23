package com.mdc.mim.netty;

import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.netty.client.NettyClient;
import com.mdc.mim.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/23 15:35
 */
@Slf4j
@SpringBootTest
public class ChatTest implements InitializingBean {
    @Autowired
    private NettyServer nettyServer;

    @Autowired
    private NettyClient nettyClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        var t1 = new Thread(() -> {
            nettyServer.start();
        });
        t1.start();
        Thread.sleep(500);
        var user = UserDTO.builder().userName("shuangshu").passwdMd5(DigestUtils.md5("12345")).build();
        nettyClient.init(user);
        nettyClient.doConnect().sync();
    }

    @Test
    public void testBasic() throws InterruptedException {
        nettyClient.doLogin();
        Thread.sleep(500);
        nettyClient.doSend(3L, "test_message_content");
    }
}
