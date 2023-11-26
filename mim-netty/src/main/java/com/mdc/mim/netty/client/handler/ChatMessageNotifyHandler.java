package com.mdc.mim.netty.client.handler;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.netty.client.NettyClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/26 15:07
 */
@Slf4j
@ChannelHandler.Sharable
public class ChatMessageNotifyHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "chatMessageNotifyHandler";

    public ChatMessageNotifyHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    private NettyClient nettyClient;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var message = (Message) msg;
        if (message.getMessageType().equals(MessageTypeEnum.MESSAGE_NOTIFY)) {
            // TODO 目前只实现TEXT类型的消息
            log.info("{} receive message content: {}", nettyClient.getClientName(), message.getMessageNotify().getChatMessageDTO().getContent());
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
