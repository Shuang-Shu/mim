package com.mdc.mim.client.test.config;

import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.netty.client.NettyClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/12/2 23:34
 */
@Configuration
public class MimNettyClientConfiguration implements DisposableBean {
    @Value("${mim.client.test.username}")
    private String username;
    @Value("${mim.client.test.password}")
    private String password;
    @Value("${mim.client.test.host}")
    private String host;
    @Value("${mim.client.test.port}")
    private int port;
    @Value("${mim.client.test.name}")
    private String clientName;

    private NettyClient nettyClient;

    @Bean
    public NettyClient initNettyClient() throws InterruptedException {
        this.nettyClient = new NettyClient(host, port);
        // 使用user初始化
        var user = UserDTO.builder().userName(username).passwdMd5(DigestUtils.md5(password)).build();
        nettyClient.init(user);
        nettyClient.setClientName(clientName);
        nettyClient.doConnect().sync();
        Thread.sleep(250);
        nettyClient.doLogin().sync();
        Thread.sleep(250);
        return nettyClient;
    }

    @Override
    public void destroy() throws Exception {
        this.nettyClient.close();
    }
}
