package com.mdc.mim.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@SpringBootApplication
@ComponentScan("com.mdc.mim.rest")
@EnableDiscoveryClient
public class MimRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MimRestApplication.class, args);
    }
}
