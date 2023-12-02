package com.mdc.mim.netty.client.handler;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.netty.client.NettyClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ChatMessageResponseHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "chatMessageResponseHandler";

    public ChatMessageResponseHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    private NettyClient nettyClient;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var message = (Message) msg;
        if (message.getMessageType().equals(MessageTypeEnum.MESSAGE_RESP)) {
            nettyClient.ackMessage(message); // 收到消息响应，从未响应列表中移除
            log.info("{} receive message response, id: {}", nettyClient.getClientName(), message.getId());
        } else {
            super.channelRead(ctx, msg); // 将消息传递给下一个handler
        }
    }
}
