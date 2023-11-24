package com.mdc.mim.netty.client.handler;

import com.mdc.mim.common.enumeration.ResponsesCodeEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.ClientSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class LogInOutResponesHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "logInOutResponesHandler";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null || !(msg instanceof Message)) {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
            return;
        }
        var message = (Message) msg;
        var clientSession = ctx.channel().attr(ClientSession.SESSION_KEY).get();
        if (message.getMessageType().equals(MessageTypeEnum.LOGIN_RESP)) {
            log.debug("client receive login response: {}", msg);
            var loginResp = message.getLogInResponse();
            clientSession.setSessionId(message.getLogInResponse().getSessionId());
            if (loginResp.getCode().equals(ResponsesCodeEnum.SUCCESS)) {
                // 设置会话的sessionId
                clientSession.getState().logInSuccess(message);
                clientSession.setUser(loginResp.getUser());
                log.info("added heartbeat handler");
            } else {
                log.error("login failed: {}", loginResp.getInfo());
            }
        } else if (message.getMessageType().equals(MessageTypeEnum.LOGOUT_RESP)) {
            log.debug("client receive logout response: {}", msg);
            var logoutResp = message.getLogOutResponse();
            if (logoutResp.getCode().equals(ResponsesCodeEnum.SUCCESS)) {
                // Client状态转换为未登录
                clientSession.getState().logOutSuccess(message);
                clientSession.setUser(null);
            } else {
                log.error("logout failed: {}", logoutResp.getInfo());
            }
        } else {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
        }
    }
}
