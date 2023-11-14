package com.mdc.mim.netty.client;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.mdc.mim.netty.client.handler.ChatMessageResponseHandler;
import com.mdc.mim.netty.client.handler.ClientHeartbeatHandler;
import com.mdc.mim.netty.client.handler.ExceptionHandler;
import com.mdc.mim.netty.client.handler.LoginResponesHandler;
import com.mdc.mim.netty.client.sender.ChatMessageSender;
import com.mdc.mim.netty.client.sender.LoginSender;
import com.mdc.mim.netty.codec.KryoContentDecoder;
import com.mdc.mim.netty.codec.KryoContentEncoder;
import com.mdc.mim.netty.codec.MIMByteDecoder;
import com.mdc.mim.netty.session.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mdc.mim.netty.codec.MIMByteEncoder;
import com.mdc.mim.common.constant.CommonConstant;
import com.mdc.mim.user.entity.UserEntity;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
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
@Slf4j
@Data
@Service("nettyClient")
public class NettyClient {
    @Value("${mim.client.server.host}")
    private String host;
    @Value("${mim.client.server.port}")
    private int port;

    // handlers of netty
    @Autowired
    private LoginResponesHandler loginResponesHandler;
    @Autowired
    private ExceptionHandler exceptionHandler;
    @Autowired
    private ChatMessageResponseHandler chatMessageResponseHandler;
    @Autowired
    private ClientHeartbeatHandler clientHeartbeatHandler;
    // senders of netty
    @Autowired
    private LoginSender loginSender;
    @Autowired
    private ChatMessageSender chatMessageSender;

    private UserEntity user;

    // netty相关
    private Bootstrap b;
    private EventLoopGroup g = new NioEventLoopGroup();
    // 连接通道相关
    private Channel channel;
    private ClientSession clientSession;

    // listener定义
    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f) -> {
        log.info(new Date() + ": connection cloesd...s");
        channel = f.channel();
        // 关闭会话
        clientSession.close();
    };

    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) -> {
        final EventLoop eventLoop = f.channel().eventLoop();
        if (!f.isSuccess()) {
            log.info("Connect failed, retry in 10 sec");
            eventLoop.schedule(
                    () -> doConnect(),
                    10,
                    TimeUnit.SECONDS);
        } else {
            log.info("Successfully connected IM server!");
            channel = f.channel();

            // 创建会话
            clientSession = new ClientSession(channel, user);
            clientSession.setConnected(true);
            // 为sender添加通道
            this.loginSender.setClientSession(clientSession);
            this.chatMessageSender.setClientSession(clientSession);
            // 添加close listener
            channel.closeFuture().addListener(closeListener);
        }

    };

    public ChannelFuture doConnect() {
        if (user == null) {
            log.error("user is not set yet");
        }
        try {
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
                            // 解编码
                            // 入站
                            ch.pipeline().addLast("mimDecoder", new MIMByteDecoder());
                            ch.pipeline().addLast("kryoDecoder", new KryoContentDecoder(CommonConstant.supplier));
                            // 出站
                            ch.pipeline().addLast("mimEncoder", new MIMByteEncoder());
                            ch.pipeline().addLast("kryoEncoder", new KryoContentEncoder(CommonConstant.supplier));
                            // 业务处理
                            ch.pipeline().addLast("loginReqHandler", loginResponesHandler);
                            // 异常处理
                            ch.pipeline().addLast("exceptionHandler", exceptionHandler);
                        }
                    });

            log.info("client connecting");
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
        if (clientSession == null || !clientSession.isConnected()) {
            log.error("connecting {}:{} failed", host, port);
        }
        return loginSender.sendLogin(user);
    }

    /**
     * 发送消息到uid
     *
     * @param toUid
     * @param content
     */
    public ChannelFuture doSend(Long toUid, String content) {
        if (clientSession == null || !clientSession.isConnected()) {
            log.error("connecting {}:{} failed", host, port);
        }
        return chatMessageSender.sendChatMessage(toUid, content);
    }
}
