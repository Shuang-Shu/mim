package com.mdc.mim.netty.server;

import java.io.Closeable;
import java.io.IOException;

import com.mdc.mim.netty.codec.KryoContentDecoder;
import com.mdc.mim.netty.codec.KryoContentEncoder;
import com.mdc.mim.netty.codec.MIMByteDecoder;
import com.mdc.mim.netty.codec.MIMByteEncoder;
import com.mdc.mim.common.constant.CommonConstant;
import com.mdc.mim.netty.server.handler.*;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
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
    private ChatMessageRequestHandler chatMessageRequestHandler;
    @Autowired
    private ChatMessageRedirectHandler chatMessageRedirectHandler;
    @Autowired
    private LoginRequestHandler loginRequestHandler;
    @Autowired
    private LogoutRequestHandler logoutRequestHandler;
    @Autowired
    private ServerExceptionHandler serverExceptionHandler;
    private ServerBootstrap b = new ServerBootstrap();

    public void start() {
        var bossLoopGroup = new NioEventLoopGroup(1);
        var workerLoopGroup = new NioEventLoopGroup();
        try {
            b.group(bossLoopGroup, workerLoopGroup);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 解编码
                    // inbouund
                    ch.pipeline().addLast(new MIMByteDecoder());
                    ch.pipeline().addLast(new KryoContentDecoder(CommonConstant.supplier));
                    // outbound
                    ch.pipeline().addLast(new MIMByteEncoder());
                    ch.pipeline().addLast(new KryoContentEncoder(CommonConstant.supplier));
                    // handlers
                    ch.pipeline().addLast(loginRequestHandler);
                    ch.pipeline().addLast(chatMessageRedirectHandler);
                }
            });
            var channelFuture = b.bind(this.host, this.port).sync();
            channelFuture.channel().closeFuture().sync(); // 等待关闭的同步事件
        } catch (Exception e) {
            log.error("exception: {}", e);
        } finally {
            bossLoopGroup.shutdownGracefully();
            workerLoopGroup.shutdownGracefully();
        }
    }
}
