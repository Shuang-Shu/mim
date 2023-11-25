package com.mdc.mim.netty.codec;

import com.mdc.mim.common.constant.CommonConst;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MIMByteEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
        var length = msg.length;
        out.writeShort(CommonConst.MAGIC_NUMBER);
        out.writeShort(CommonConst.APP_VERSION);
        out.writeInt(length);
        out.writeBytes(msg);
    }

}
