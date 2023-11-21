package com.mdc.mim.netty;

import com.mdc.mim.common.constant.HeartBeatConstant;
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

    /**
     * @description: 测试是否能够正常检测Idle状态，注意调整Client的心跳发送时间超过过期时间
     * @param:
     * @return:
     * @author ShuangShu
     * @date: 2023/11/21 20:55
     */
    @Test
    public void testServerIdleTimeoutHandler() throws InterruptedException {
        nettyClient.doConnect();
        Thread.sleep(HeartBeatConstant.READ_IDLE_TIME * 1000 + 1000);
    }

    @Test
    public void testReconnect() {
        nettyClient.doConnect();
    }
}
