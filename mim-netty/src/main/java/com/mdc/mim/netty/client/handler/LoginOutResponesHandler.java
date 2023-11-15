package com.mdc.mim.netty.client.handler;

import com.mdc.mim.common.constant.ResponsesCodeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.ClientSession;

import io.netty.channel.ChannelHandler.Sharable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Sharable
@Component
public class LoginOutResponesHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var message = (Message) msg;
        if (message == null || !(msg instanceof Message)) {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
            return;
        }
        if (message.getMessageType().equals(MessageTypeEnum.LOGIN_RESP)) {
            log.debug("client receive login response: {}", msg);
            var loginResp = message.getLoginResponse();
            if (loginResp.getCode().equals(ResponsesCodeEnum.SUCCESS)) {
                var pipeline = ctx.pipeline();
                // 注册对应的sessionId
                var sessionId = message.getLoginResponse().getSessionId();
                // 设置sessionId
                var clientSession = ctx.channel().attr(ClientSession.SESSION_KEY).get();
                clientSession.setSessionId(sessionId);
                clientSession.setHasLogined(true);
                // 添加心跳处理器
                pipeline.addAfter("kryoEncoder", "heartbeatHandler", new ClientHeartbeatHandler());
                log.info("added heartbeat handler");
            } else {
                log.error("login failed: {}", loginResp.getInfo());
            }
        } else if (message.getMessageType().equals(MessageTypeEnum.LOGOUT_RESP)) {
            log.debug("client receive logout response: {}", msg);
            var logoutResp = message.getLogoutResponse();
            if (logoutResp.getCode().equals(ResponsesCodeEnum.SUCCESS)) {
                var pipeline = ctx.pipeline();
                // 设置sessionId
                var clientSession = ctx.channel().attr(ClientSession.SESSION_KEY).get();
                clientSession.setSessionId(null);
                clientSession.setHasLogined(false);
                // 已登出，删除心跳处理器
                pipeline.remove("heartbeatHandler");
                log.info("removed heartbeat handler");
            } else {
                log.error("logout failed: {}", logoutResp.getInfo());
            }
        } else {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
        }
    }
}
