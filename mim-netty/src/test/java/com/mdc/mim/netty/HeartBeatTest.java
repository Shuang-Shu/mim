package com.mdc.mim.netty;

import com.mdc.mim.netty.client.NettyClient;
import com.mdc.mim.netty.server.NettyServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ThemeResolver;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/20 19:47
 */
@SpringBootTest
public class HeartBeatTest {
    @Autowired
    NettyServer nettyServer;

    @Autowired
    NettyClient nettyClient;

    @BeforeEach
    public void init() throws InterruptedException {
        var t1 = new Thread(
                () -> {
                    nettyServer.start();
                }
        );
        t1.start();
        Thread.sleep(500);
    }

    @Test
    public void testBasic() throws InterruptedException {
        nettyClient.doConnect();
        Thread.sleep(10000);
    }
}
