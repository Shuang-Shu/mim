package com.mdc.mim.netty.server.handler;

import com.mdc.mim.netty.session.AbstractSession;
import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ServerSessionManager sessionManager;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("server channel inactive: {}", ctx.channel());
        var session = (ServerSession) ctx.channel().attr(AbstractSession.SESSION_KEY).get();
        if (session != null) {
            var sessionId = session.getSessionId();
            sessionManager.removeSession(sessionId);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("server event triggered: {}", evt);
            var state = (IdleStateEvent) evt;
            if (state.state() == IdleState.READER_IDLE) {
                // 超时，主动关闭连接
                var session = (ServerSession) ctx.channel().attr(AbstractSession.SESSION_KEY).get();
                if (session != null) {
                    log.info("heartbeat timeout, close session: {}", session);
                    session.close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
