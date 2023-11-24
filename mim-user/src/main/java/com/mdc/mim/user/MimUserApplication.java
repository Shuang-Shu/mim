package com.mdc.mim.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan("com.mdc.mim.user")
public class MimUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(MimUserApplication.class, args);
    }
}
