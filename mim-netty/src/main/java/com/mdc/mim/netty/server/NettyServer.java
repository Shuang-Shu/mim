package com.mdc.mim.netty.server;

import com.mdc.mim.common.constant.HeartBeatConstant;
import com.mdc.mim.netty.codec.KryoContentDecoder;
import com.mdc.mim.netty.codec.KryoContentEncoder;
import com.mdc.mim.netty.codec.MIMByteDecoder;
import com.mdc.mim.netty.codec.MIMByteEncoder;
import com.mdc.mim.common.constant.CommonConstant;
import com.mdc.mim.netty.server.handler.*;

import com.mdc.mim.netty.session.ServerSessionManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@NoArgsConstructor
public class NettyServer {
    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Value("${mim.server.host}")
    private String host;
    @Value("${mim.server.port}")
    private int port;
    // handlers
    @Autowired
    private ChatMessageRedirectHandler chatMessageRedirectHandler;
    @Autowired
    private LogInRequestHandler logInRequestHandler;
    @Autowired
    private LogInOutRequestHandler loginOutRequestHandler;
    @Autowired
    private ServerExceptionHandler serverExceptionHandler;
    @Autowired
    private ServerHeartBeatTimeoutHandler serverHeartBeatTimeoutHandler;
    @Autowired
    private ServerSessionManager sessionManager;
    private ServerBootstrap b = new ServerBootstrap();

    public void start() {
        var bossLoopGroup = new NioEventLoopGroup(1);
        var workerLoopGroup = new NioEventLoopGroup();
        try {
            b.group(bossLoopGroup, workerLoopGroup);
            b.channel(NioServerSocketChannel.class);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 心跳相关
                    ch.pipeline().addLast(new IdleStateHandler(HeartBeatConstant.READ_IDLE_TIME, 0, 0, TimeUnit.SECONDS));
                    ch.pipeline().addLast(serverHeartBeatTimeoutHandler);
                    // 解编码
                    // inbouund
                    ch.pipeline().addLast(new MIMByteDecoder());
                    ch.pipeline().addLast(new KryoContentDecoder(CommonConstant.supplier));
                    // outbound
                    ch.pipeline().addLast(new MIMByteEncoder());
                    ch.pipeline().addLast(new KryoContentEncoder(CommonConstant.supplier));
                    // handlers
                    ch.pipeline().addLast(MessageFormatFilter.NAME, new MessageFormatFilter()); // 过滤格式不正确的消息
                    ch.pipeline().addLast(LogInRequestHandler.NAME, logInRequestHandler); // 登入处理器
//                    ch.pipeline().addLast(loginOutRequestHandler);
                    ch.pipeline().addLast(ChatMessageRedirectHandler.NAME, chatMessageRedirectHandler);
                    // exception处理
                    ch.pipeline().addLast(ServerExceptionHandler.NAME, serverExceptionHandler);
                }
            });
            var channelFuture = b.bind(this.host, this.port).sync();
            log.info("server started");
            channelFuture.channel().closeFuture().sync(); // 等待关闭的同步事件，保持服务器常开
        } catch (Exception e) {
            log.error("exception: {}", e);
        } finally {
            bossLoopGroup.shutdownGracefully();
            workerLoopGroup.shutdownGracefully();
        }
    }

    public void closeServerSession(String sessionId) {
        var session = sessionManager.getSession(sessionId);
        if (session != null) {
            session.close();
            sessionManager.removeSession(sessionId);
        }
    }
}
