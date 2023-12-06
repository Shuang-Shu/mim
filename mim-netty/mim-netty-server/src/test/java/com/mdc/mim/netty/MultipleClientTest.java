package com.mdc.mim.netty;

import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.netty.client.NettyClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/26 12:06
 */
@Slf4j
@SpringBootTest
public class MultipleClientTest implements InitializingBean {
    NettyClient[] nettyClients = new NettyClient[2];
    String[] userNames = {"shuangshu", "huamao"};
    String passwd = "12345";

    {
        // 连接服务器
        for (int i = 0; i < nettyClients.length; i++) {
            nettyClients[i] = new NettyClient("localhost", 8080);
            var user = UserDTO.builder().userName(userNames[i]).passwdMd5(DigestUtils.md5(passwd)).build();
            nettyClients[i].init(user);
            nettyClients[i].setClientName("client_" + i);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread.sleep(500);
        UserDTO[] users = {
                UserDTO.builder().userName("shuangshu").passwdMd5(DigestUtils.md5("12345")).build(),
                UserDTO.builder().userName("huamao").passwdMd5(DigestUtils.md5("12345")).build()
        };
        // 所有客户端连接服务器
        Arrays.stream(nettyClients).forEach(NettyClient::doConnect);
        Thread.sleep(500);
        for (int i = 0; i < nettyClients.length; i++) {
            nettyClients[i].init(users[i]);
            nettyClients[i].doLogin().sync();
        }
        Thread.sleep(500);
    }

    @Test
    public void testConnectAndLogIn() {
    }


    @Test
    public void testSend() throws InterruptedException {
        nettyClients[0].doSend(2L, "test_message_content");
        Thread.sleep(500);
    }

    @Test
    public void testMultipleSend() throws InterruptedException {
        for (int ii = 0; ii < 10; ii++) {
            for (int i = 0; i < 200; i++) {
                nettyClients[0].doSend(2L, "test_message_content_" + i);
            }
            Thread.sleep(500);
        }
    }
}

