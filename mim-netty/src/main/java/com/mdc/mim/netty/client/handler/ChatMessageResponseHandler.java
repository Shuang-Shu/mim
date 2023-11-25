package com.mdc.mim.netty.client.handler;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            log.info("receive message response, id: {}", message.getMessageResponse().getId());
        } else {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
        }
    }
}
