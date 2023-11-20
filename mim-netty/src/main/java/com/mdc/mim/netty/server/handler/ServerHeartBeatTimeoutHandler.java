package com.mdc.mim.netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/20 22:48
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerHeartBeatTimeoutHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("server event triggered: {}", evt);
            var state = (IdleStateEvent) evt;
            if (state.state() == IdleState.READER_IDLE) {
                log.info("heartbeat timeout: {}", evt);
                // 超时，主动关闭连接
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
