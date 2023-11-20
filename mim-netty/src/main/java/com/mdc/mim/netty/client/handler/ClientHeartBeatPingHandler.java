package com.mdc.mim.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 一定周期内向服务器发送一个心跳包
 * @date 2023/11/20 19:14
 */
@Slf4j
@Component
public class ClientHeartBeatPingHandler extends ChannelInboundHandlerAdapter {
    private static final int DELAY_TIME = 2;
    public static final String HEART_BEAT = "heartBeat";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("client channel active... ");
        ping(ctx);
    }

    private void ping(ChannelHandlerContext ctx) {
        var future = ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                if (ctx.channel().isActive()) {
                    log.info("sending heartbeat msg: {}", HEART_BEAT);
                    ctx.writeAndFlush(ctx);
                } else {
                    log.error("server is not active, close this channel");
                    ctx.channel().close();
                    throw new RuntimeException("server is not active, close this channel");
                }

            }
        }, DELAY_TIME, TimeUnit.SECONDS);
        future.addListener(
                new GenericFutureListener() {
                    public void operationComplete(Future future) throws Exception {
                        if (future.isSuccess()) {
                            ping(ctx);
                        }
                    }
                }
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当Channel已经断开的情况下, 仍然发送数据, 会抛异常，此时该方法会被调用
        cause.printStackTrace();
        ctx.close();
    }
}
