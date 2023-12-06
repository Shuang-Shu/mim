package com.mdc.mim.netty.handler;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/24 9:08
 */
public class MessageFormatFilter extends ChannelInboundHandlerAdapter {
    public static final String NAME = "messageFormatFilter";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null || !(msg instanceof Message)) {
            throw new RuntimeException("message: " + msg + " format error!");
        }
        var message = (Message) msg;
        if (message.getMessageType() == null) {
            throw new RuntimeException("message: " + msg + " format error! messageType is null");
        } else if (message.getMessageType().equals(MessageTypeEnum.ERR_RESP)) {
            var info = message.getInfo();
            throw new RuntimeException(info);
        } else if (message.getMessageType().equals(MessageTypeEnum.HEARTBEAT_REQ)) {
            return;
        }
        super.channelRead(ctx, msg);
    }
}
