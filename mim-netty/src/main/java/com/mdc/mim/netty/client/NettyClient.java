package com.mdc.mim.netty.client;

import com.mdc.mim.common.constant.CommonConst;
import com.mdc.mim.common.constant.HeartBeatConst;
import com.mdc.mim.common.dto.ChatMessageDTO;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.netty.client.handler.*;
import com.mdc.mim.netty.client.sender.ChatMessageSender;
import com.mdc.mim.netty.client.sender.LogInOutSender;
import com.mdc.mim.netty.codec.KryoContentDecoder;
import com.mdc.mim.netty.codec.KryoContentEncoder;
import com.mdc.mim.netty.codec.MIMByteDecoder;
import com.mdc.mim.netty.codec.MIMByteEncoder;
import com.mdc.mim.netty.server.handler.MessageFormatFilter;
import com.mdc.mim.netty.session.ClientSession;
import com.mdc.mim.netty.session.state.StateConstant;
import com.mdc.mim.netty.session.state.impl.client.ClientNotConnectState;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/*
 * 基于Netty的客户端，是参考代码中NettyClient
 * 和CommandController的功能组合
 */
@Data
@Slf4j
@NoArgsConstructor
public class NettyClient {
    private final int RESEND_INTERVAL = 5000;

    /**
     * @description: 本地Netty客户端构造函数，分别传入服务器地址和端口
     * @author ShuangShu
     * @date: 2023/12/2 23:35
     */
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // 本地客户端相关
    private String clientName;

    // 远程服务器相关
    private String host;
    private int port;

    // senders of netty
    private LogInOutSender loginoutSender = new LogInOutSender(this);
    private ChatMessageSender chatMessageSender = new ChatMessageSender(this);

    private final Map<Long, Message> notAckedMessage = new HashMap<>(); // 未被确认的消息，确认消息由XXXResponse实现

    public void addNotAckedMessage(Message message) {
        synchronized (notAckedMessage) {
            notAckedMessage.put(message.getId(), message);
        }
    }

    public synchronized void ackMessage(Message message) {
        synchronized (notAckedMessage) {
            notAckedMessage.remove(message.getId());
        }
    }

    {
        // 设置id生成器
        var atomicId = new AtomicLong(0L);
        loginoutSender.setAtomicId(atomicId);
        chatMessageSender.setAtomicId(atomicId);
    }

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
            log.info("{} successfully connected IM server!", clientName);
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
                        ch.pipeline().addLast(new IdleStateHandler(0, HeartBeatConst.WRITE_IDLE_TIME, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new ClientHeartBeatTimeoutHandler());
                        ch.pipeline().addLast(new ClientHeartBeatSendingHandler());
                        // 解编码
                        // 入站
                        ch.pipeline().addLast(new MIMByteDecoder());
                        ch.pipeline().addLast(new KryoContentDecoder(CommonConst.supplier));
                        // 出站
                        ch.pipeline().addLast(new MIMByteEncoder());
                        ch.pipeline().addLast(new KryoContentEncoder(CommonConst.supplier));
                        // 业务处理
                        ch.pipeline().addLast(MessageFormatFilter.NAME, new MessageFormatFilter()); // 过滤格式不正确的消息
                        ch.pipeline().addLast(LogInOutResponesHandler.NAME, new LogInOutResponesHandler(NettyClient.this));
                        ch.pipeline().addLast(ChatMessageResponseHandler.NAME, new ChatMessageResponseHandler(NettyClient.this));
                        ch.pipeline().addLast(ChatMessageNotifyHandler.NAME, new ChatMessageNotifyHandler(NettyClient.this));
                        // exception处理
                        ch.pipeline().addLast(ClientExceptionHandler.NAME, new ClientExceptionHandler());
                    }
                });
        log.info("client connecting");
        // 启动消息重发守护线程
        var resendThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(RESEND_INTERVAL);
                } catch (InterruptedException e) {
                }
                resend();
            }
        });
        resendThread.setDaemon(true);
        resendThread.start();
    }

    private void resend() {
        if (notAckedMessage.isEmpty()) {
            return;
        }
        synchronized (notAckedMessage) {
            // 目前简单地进行重发
            var ids = notAckedMessage.keySet();
            for (Long id : ids) {
                var message = notAckedMessage.get(id);
                log.info("resending message's id={}", message.getId());
                // 重发
                clientSession.writeAndFlush(message);
            }
        }
    }

    public ChannelFuture doConnect() {
        if (user == null) {
            log.error("user is not set yet");
            throw new RuntimeException("user is not set yet");
        }
        loginoutSender.resetIdGenerator();
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

    public List<ChatMessageDTO> getChatMessage(Long fromUid) {
        return clientSession.getChatMessages(fromUid);
    }

    public void closeSession() {
        if (clientSession != null) {
            clientSession.close();
        }
    }

    public Long getChatMessageId() {
        // TODO 简单实现，直接返回系统当前时间，未来需要优化
        return System.currentTimeMillis();
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
