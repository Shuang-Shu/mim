package com.mdc.mim.netty.server.handler;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerExceptionHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "serverExceptionHandler";

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        cause.printStackTrace();
        var message = Message.builder()
                .messageType(MessageTypeEnum.ERR_RESP)
                .info(cause.getMessage())
                .build();
        ctx.channel().writeAndFlush(message);
        super.exceptionCaught(ctx, cause);
    }
}
