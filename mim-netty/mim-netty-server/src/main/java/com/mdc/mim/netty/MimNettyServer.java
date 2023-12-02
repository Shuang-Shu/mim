package com.mdc.mim.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/13 22:35
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, })
@ComponentScan("com.mdc.mim.netty")
@EnableFeignClients // 启用openfeign
public class MimNettyServer {
    public static void main(String[] args) {
        SpringApplication.run(MimNettyServer.class, args);
    }
}
