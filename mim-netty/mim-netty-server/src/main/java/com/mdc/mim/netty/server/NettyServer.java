package com.mdc.mim.netty.server;

import com.mdc.mim.common.constant.CommonConst;
import com.mdc.mim.common.constant.HeartBeatConst;
import com.mdc.mim.netty.codec.KryoContentDecoder;
import com.mdc.mim.netty.codec.KryoContentEncoder;
import com.mdc.mim.netty.codec.MIMByteDecoder;
import com.mdc.mim.netty.codec.MIMByteEncoder;
import com.mdc.mim.netty.feign.MaxSeqFeignService;
import com.mdc.mim.netty.handler.MessageFormatFilter;
import com.mdc.mim.netty.server.handler.ChatMessageRedirectHandler;
import com.mdc.mim.netty.server.handler.LogInRequestHandler;
import com.mdc.mim.netty.server.handler.ServerExceptionHandler;
import com.mdc.mim.netty.server.handler.ServerHeartBeatTimeoutHandler;
import com.mdc.mim.netty.session.ServerSessionManager;
import com.mdc.mim.netty.utils.ChatMessageSeqNoManager;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@NoArgsConstructor
public class NettyServer implements InitializingBean {
    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Value("${mim.server.host}")
    private String host;
    @Value("${mim.server.port}")
    private int port;
    @Value("${mim.server.test.enabled}")
    private boolean testEnabled;
    // handlers
    @Autowired
    private ChatMessageRedirectHandler chatMessageRedirectHandler;
    @Autowired
    private LogInRequestHandler logInRequestHandler;
    @Autowired
    private ServerExceptionHandler serverExceptionHandler;
    @Autowired
    private ServerHeartBeatTimeoutHandler serverHeartBeatTimeoutHandler;
    @Autowired
    private ServerSessionManager sessionManager;
    @Autowired
    private MaxSeqFeignService maxSeqFeignService;
    @Autowired
    private ChatMessageSeqNoManager chatMessageSeqNoManager;

    private final ServerBootstrap b = new ServerBootstrap();

    private NioEventLoopGroup bossLoopGroup;
    private NioEventLoopGroup workerLoopGroup;

    public void init() {
        // 配置bootstrap
        bossLoopGroup = new NioEventLoopGroup(1); // boss线程组只需要一个线程
        workerLoopGroup = new NioEventLoopGroup();
        b.group(bossLoopGroup, workerLoopGroup);
        b.channel(NioServerSocketChannel.class);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 心跳相关
                ch.pipeline().addLast(new IdleStateHandler(HeartBeatConst.READ_IDLE_TIME, 0, 0, TimeUnit.SECONDS));
                ch.pipeline().addLast(serverHeartBeatTimeoutHandler);
                // 解编码
                // inbouund
                ch.pipeline().addLast(new MIMByteDecoder());
                ch.pipeline().addLast(new KryoContentDecoder(CommonConst.supplier));
                // outbound
                ch.pipeline().addLast(new MIMByteEncoder());
                ch.pipeline().addLast(new KryoContentEncoder(CommonConst.supplier));
                // handlers
                ch.pipeline().addLast(MessageFormatFilter.NAME, new MessageFormatFilter()); // 过滤格式不正确的消息
                ch.pipeline().addLast(LogInRequestHandler.NAME, logInRequestHandler); // 登入处理器
                ch.pipeline().addLast(ChatMessageRedirectHandler.NAME, chatMessageRedirectHandler);
                // exception处理
                ch.pipeline().addLast(ServerExceptionHandler.NAME, serverExceptionHandler);
            }
        });
    }

    public void start() {
        try {
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

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!testEnabled) {
            var serverThread = new Thread(
                    () -> {
                        init();
                        start();
                    }
            );
            serverThread.start();
        } else {
            log.info("test mode, should start server manually");
        }
    }
}