package com.mdc.mim.netty.client.handler;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatMessageResponseHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null || !(msg instanceof Message)) {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
            return;
        }
        var message = (Message) msg;
        if (message.getMessageType().equals(MessageTypeEnum.MESSAGE_RESP)) {
            var content = message.getMessageResponse().getContent();
            log.info("receive message response: {}", content);
        } else {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
        }
    }
}
