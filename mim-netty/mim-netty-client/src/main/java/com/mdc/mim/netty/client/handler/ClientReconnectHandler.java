package com.mdc.mim.netty.client.handler;

import com.mdc.mim.netty.client.NettyClient;
import com.mdc.mim.netty.client.retry.IRetryPolicy;
import com.mdc.mim.netty.client.retry.impl.SimpleRetryPolicy;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 当channel进入不活跃状态时，触发重连机制
 * @date 2023/11/23 10:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
@ChannelHandler.Sharable
public class ClientReconnectHandler extends ChannelInboundHandlerAdapter {
    public ClientReconnectHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    private NettyClient nettyClient;
    private IRetryPolicy retryPolicy = new SimpleRetryPolicy(10, 1000);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (retryPolicy.getCurrentRetryCount() > 0) {
            log.info("successfully reconnect to server");
            retryPolicy.resetCount();
            super.channelActive(ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("client channel inactive: {}", ctx.channel());
        if (retryPolicy.allowRetry()) {
            long sleepTimeMs = getRetryPolicy().getSleepTimeMs();
            log.info(String.format("Try to reconnect to the server after %dms. Retry count: %d.", sleepTimeMs, retryPolicy.getCurrentRetryCount()));

            final EventLoop eventLoop = ctx.channel().eventLoop();
            eventLoop.schedule(() -> {
                log.info("Reconnecting ...");
                nettyClient.doConnect();
            }, sleepTimeMs, TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException("Failed to reconnect to the server.");
        }
    }
}
