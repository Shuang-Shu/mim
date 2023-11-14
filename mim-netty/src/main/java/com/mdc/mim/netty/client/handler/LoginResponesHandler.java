package com.mdc.mim.netty.client.handler;

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
public class LoginResponesHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var message = (Message) msg;
        if (message == null || !(msg instanceof Message)) {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
            return;
        }
        if (message.getMessageType().equals(MessageTypeEnum.LOGIN_RESP)) {
            log.debug("client receive response: {}", msg);
            var pipeline = ctx.pipeline();
            // 注册对应的sessionId
            var sessionId = message.getLoginResponse().getSessionId();
            // 设置sessionId
            ctx.channel().attr(ClientSession.SESSION_KEY).get().setSessionId(sessionId);
            ;
            // 完成登录，此时登录处理器不需要在被调用，移除登录响应处理器
            pipeline.remove(this);
            pipeline.addAfter("kryoEncoder", "heartbeatHandler", new ClientHeartbeatHandler());
        } else {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
            return;
        }
    }
}
