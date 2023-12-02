package com.mdc.mim.netty.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ClientExceptionHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "clientExceptionHandler";

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}
