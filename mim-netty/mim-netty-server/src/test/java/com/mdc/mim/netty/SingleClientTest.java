package com.mdc.mim.netty;

import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.netty.client.NettyClient;
import com.mdc.mim.netty.server.NettyServer;
import com.mdc.mim.netty.session.state.StateConstant;
import com.mdc.mim.netty.session.state.impl.client.ClientNotLoginState;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SingleClientTest implements InitializingBean {

    @Autowired
    NettyServer nettyServer;

    NettyClient nettyClient = new NettyClient("localhost", 8080);

    @Override
    public void afterPropertiesSet() throws Exception {
        var t1 = new Thread(() -> {
            nettyServer.init();
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
        Assertions.assertEquals(true, nettyClient.getClientSession().getState().stateDescription().equals(StateConstant.NOT_LOGIN));
        // 测试客户端登录功能
        nettyClient.doLogin().sync();
        Thread.sleep(200);
        Assertions.assertEquals(true, nettyClient.getClientSession().getState().stateDescription().equals(StateConstant.LOGIN));
        nettyClient.doLogout().sync();
        Thread.sleep(200);
        Assertions.assertEquals(true, nettyClient.getClientSession().getState().stateDescription().equals(StateConstant.NOT_LOGIN));
    }

    @Test
    public void testMultipleLoginOut() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            testSingleLoginOut();
        }
    }

    @Test
    public void testNotLogIn() {
        nettyClient.doLogout();
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
        nettyClient.doSend(3L, "test_content");
    }
}