package com.mdc.mim.netty.client;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.mdc.mim.common.constant.HeartBeatConstant;
import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.netty.client.handler.*;
import com.mdc.mim.netty.client.sender.ChatMessageSender;
import com.mdc.mim.netty.client.sender.LogInOutSender;
import com.mdc.mim.netty.codec.KryoContentDecoder;
import com.mdc.mim.netty.codec.KryoContentEncoder;
import com.mdc.mim.netty.codec.MIMByteDecoder;
import com.mdc.mim.netty.server.handler.MessageFormatFilter;
import com.mdc.mim.netty.session.ClientSession;
import com.mdc.mim.netty.session.state.StateConstant;
import com.mdc.mim.netty.session.state.impl.client.ClientLoginState;
import com.mdc.mim.netty.session.state.impl.client.ClientNotConnectState;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mdc.mim.netty.codec.MIMByteEncoder;
import com.mdc.mim.common.constant.CommonConstant;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/*
 * 基于Netty的客户端，是参考代码中NettyClient
 * 和CommandController的功能组合
 */
@Data
@Slf4j
@NoArgsConstructor
public class NettyClient {
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // 远程服务器相关
    private String host;
    private int port;

    // senders of netty
    private LogInOutSender loginoutSender = new LogInOutSender();
    private ChatMessageSender chatMessageSender = new ChatMessageSender();

    private UserDTO user;

    // netty相关
    private Bootstrap b;
    private EventLoopGroup g = new NioEventLoopGroup();
    // 客户端会话
    private ClientSession clientSession;

    // listener定义
    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f) -> {
        log.info(new Date() + ": connection cloesd...");
        // 关闭会话
        clientSession.close();
    };

    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) -> {
        final EventLoop eventLoop = f.channel().eventLoop();
        if (!f.isSuccess()) {
            log.info("Connect failed, retry in 5 sec");
            eventLoop.schedule(
                    () -> doConnect(),
                    5,
                    TimeUnit.SECONDS);
        } else {
            // 连接成功，创建ClientSession
            log.info("successfully connected IM server!");
            var channel = f.channel();
            // 创建会话
            clientSession = new ClientSession(channel);
            // 为sender添加通道
            this.loginoutSender.setClientSession(clientSession);
            this.chatMessageSender.setClientSession(clientSession);
            // 添加close listener
            channel.closeFuture().addListener(closeListener);
        }
    };

    public void init(UserDTO user) {
        // 设置当前client对应的user
        this.user = user;

        // 初始化bootstrap
        b = new Bootstrap();
        b.group(g);
        b.channel(NioSocketChannel.class); // 客户端使用BIO实现
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // 设置默认内存分配器
        b.remoteAddress(host, port);

        // 设置handlers
        b.handler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 重连机制
                        ch.pipeline().addLast(new ClientReconnectHandler(NettyClient.this));
                        // 心跳相关
                        ch.pipeline().addLast(new IdleStateHandler(0, HeartBeatConstant.WRITE_IDLE_TIME, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new ClientHeartBeatTimeoutHandler());
                        ch.pipeline().addLast(new ClientHeartBeatSendingHandler());
                        // 解编码
                        // 入站
                        ch.pipeline().addLast(new MIMByteDecoder());
                        ch.pipeline().addLast(new KryoContentDecoder(CommonConstant.supplier));
                        // 出站
                        ch.pipeline().addLast(new MIMByteEncoder());
                        ch.pipeline().addLast(new KryoContentEncoder(CommonConstant.supplier));
                        // 业务处理
                        ch.pipeline().addLast(MessageFormatFilter.NAME, new MessageFormatFilter()); // 过滤格式不正确的消息
                        ch.pipeline().addLast(LogInOutResponesHandler.NAME, new LogInOutResponesHandler());
                        // exception处理
                        ch.pipeline().addLast(ClientExceptionHandler.NAME, new ClientExceptionHandler());
                    }
                });

        log.info("client connecting");
    }

    public ChannelFuture doConnect() {
        if (user == null) {
            log.error("user is not set yet");
            throw new RuntimeException("user is not set yet");
        }
        try {
            var cf = b.connect();
            cf.addListener(connectedListener); // 添加连接监听器
            return cf;
        } catch (Exception e) {
            log.info("connect failed!");
        }
        return null;
    }

    /**
     * 发送登录消息
     */
    public ChannelFuture doLogin() {
        if (clientSession == null || clientSession.getState().stateDescription().equals(StateConstant.NOT_CONNECT)) {
            log.error("not connected yet");
        }
        return loginoutSender.sendLogin(user);
    }

    /**
     * @description: 退出登录
     * @param:
     * @return:
     * @author ShuangShu
     * @date: 2023/11/15 18:54
     */
    public ChannelFuture doLogout() {
        if (clientSession == null || (clientSession.getState() instanceof ClientNotConnectState)) {
            log.error("connecting {}:{} failed", host, port);
        }
        return loginoutSender.sendLogout(user);
    }

    /**
     * 发送消息到uid
     *
     * @param toUid
     * @param content
     */
    public ChannelFuture doSend(Long toUid, String content) {
        if (clientSession == null || (clientSession.getState() instanceof ClientNotConnectState)) {
            log.error("connecting {}:{} failed", host, port);
        }
        return chatMessageSender.sendChatMessage(toUid, content);
    }

    public void closeSession() {
        if (clientSession != null) {
            clientSession.close();
        }
    }

    public void close() {
        try {
            if (clientSession != null) {
                clientSession.close();
            }
        } finally {
            g.shutdownGracefully();
        }
    }
}
