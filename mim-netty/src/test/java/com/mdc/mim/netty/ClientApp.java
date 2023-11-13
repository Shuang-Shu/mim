package com.mdc.mim.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mdc.mim.netty.client.NettyClient;
import com.mdc.mim.user.entity.UserEntity;

@ComponentScan("com.mdc.mim.netty")
@SpringBootApplication
public class ClientApp {
    public static void main(String[] args) {
        var context = SpringApplication.run(ClientApp.class, args);
        var nettyClient = context.getBean(NettyClient.class);
        var user = UserEntity.builder().uid(12345L).devId("wsl-linux-dajfo").token("testToken")
                .nickname("ShuangShu").build();
        nettyClient.setUser(user);
        nettyClient.doConnect();
        System.out.println(nettyClient);
    }
}