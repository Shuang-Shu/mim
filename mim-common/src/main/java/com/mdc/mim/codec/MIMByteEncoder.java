package com.mdc.mim.codec;

import com.mdc.mim.constant.CommonConstant;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MIMByteEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
        var length = msg.length;
        out.writeShort(CommonConstant.MAGIC_NUMBER);
        out.writeShort(CommonConstant.APP_VERSION);
        out.writeInt(length);
        out.writeBytes(msg);
    }

}
