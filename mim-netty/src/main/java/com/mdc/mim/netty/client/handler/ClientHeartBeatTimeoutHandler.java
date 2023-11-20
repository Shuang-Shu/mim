package com.mdc.mim.netty.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ClientHeartBeatTimeoutHandler extends ChannelInboundHandlerAdapter {
    /**
     * @description: 如果接收到IdleState.WRITE_IDLE事件，则向服务器发送一个心跳包
     * @param:
     * @return:
     * @author ShuangShu
     * @date: 2023/11/20 19:17
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("user event triggered: {}", evt);
            var state = (IdleStateEvent) evt;
            if (state.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(ClientHeartBeatSendingHandler.HEART_BEAT);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
