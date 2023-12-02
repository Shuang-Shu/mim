package com.mdc.mim.client.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/12/2 23:30
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,})
public class MimTestClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(MimTestClientApplication.class, args);
    }
}
