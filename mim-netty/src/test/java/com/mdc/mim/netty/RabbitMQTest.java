package com.mdc.mim.netty;

import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/26 15:45
 */

public class RabbitMQTest {
    @Test
    public void testBasic() throws IOException, TimeoutException {
        var factory = new ConnectionFactory();
        factory.setUsername("ss");
        factory.setPassword("1234");
        var con = factory.newConnection();
        Assertions.assertNotNull(con);
    }
}
