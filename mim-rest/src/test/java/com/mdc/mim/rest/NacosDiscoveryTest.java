package com.mdc.mim.rest;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/13 21:28
 */
@SpringBootTest
public class NacosDiscoveryTest {
    @NacosInjected
    NamingService namingService;

    @Test
    public void basicTest() {
        Assertions.assertNotNull(namingService);
    }

    @Test
    public void servicesDiscoveryTest() throws Exception {
        var services = namingService.getAllInstances("example");
        System.out.println(services);
    }
}
