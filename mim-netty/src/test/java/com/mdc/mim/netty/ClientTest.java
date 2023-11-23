package com.mdc.mim.netty;

import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.netty.session.state.impl.client.ClientLoginState;
import com.mdc.mim.netty.session.state.impl.client.ClientNotLoginState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mdc.mim.netty.client.NettyClient;
import com.mdc.mim.netty.server.NettyServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ClientTest implements InitializingBean {

    @Autowired
    NettyServer nettyServer;

    @Autowired
    NettyClient nettyClient;

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
    public void testBasic() {
        Assertions.assertNotNull(nettyClient);
        Assertions.assertNotNull(nettyClient.getUser());
    }

    @Test
    public void testConnect() throws InterruptedException {
        nettyClient.doConnect();
        Thread.sleep(1000); // waiting for connectings
        Assertions.assertEquals(true, nettyClient.getClientSession().getState() instanceof ClientNotLoginState);
    }

    public void testSingleLoginOut() throws InterruptedException {
        Assertions.assertEquals(true, nettyClient.getClientSession().getState() instanceof ClientNotLoginState);
        // 测试客户端登录功能
        nettyClient.doLogin().sync();
        Thread.sleep(200);
        Assertions.assertEquals(true, nettyClient.getClientSession().getState() instanceof ClientLoginState);
        nettyClient.doLogout().sync();
        Thread.sleep(200);
        Assertions.assertEquals(true, nettyClient.getClientSession().getState() instanceof ClientNotLoginState);
    }

    @Test
    public void testMultipleLoginOut() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            testSingleLoginOut();
        }
    }

    /**
     * 测试消息发送功能
     *
     * @throws InterruptedException
     */
    @Test
    public void testSending() throws InterruptedException {
        nettyClient.doLogin().sync();
        Thread.sleep(500);
        nettyClient.doSend(12345L, "test_content");
    }
}