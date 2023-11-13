package com.mdc.mim.netty;

import org.junit.jupiter.api.Test;

import com.esotericsoftware.kryo.Kryo;
import com.mdc.mim.netty.codec.KryoContentDecoder;
import com.mdc.mim.netty.codec.KryoContentEncoder;
import com.mdc.mim.netty.codec.MIMByteDecoder;
import com.mdc.mim.netty.codec.MIMByteEncoder;
import com.mdc.mim.common.constant.CommonConstant;
import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.constant.Platform;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.server.handler.LoginRequestHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

public class HandlerTest {
    static ChannelHandler[] channelHandlers;

    static Kryo kryo = CommonConstant.supplier.get();

    static {
        var byteEncoder = new MIMByteEncoder();
        var byteDecoder = new MIMByteDecoder();
        var kryoSupplier = CommonConstant.supplier;
        var kryoContentEncoder = new KryoContentEncoder(kryoSupplier);
        var kryoContentDecoder = new KryoContentDecoder(kryoSupplier);
        channelHandlers = new ChannelHandler[] {
                byteDecoder, kryoContentDecoder, byteEncoder, kryoContentEncoder
        };
    }

    @Test
    public void testBasicHandler() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        for (var handler : channelHandlers) {
                            ch.pipeline().addLast(handler);
                        }
                        ch.pipeline().addLast(new LoginRequestHandler());
                    }
                });
        var loginReq = Message.LoginRequest.builder().id(1).uid(12345L).deviceId("dev-1").token("test-token")
                .platform(Platform.LINUX).appVersion(CommonConstant.APP_VERSION).build();
        var message = Message.builder().loginRequest(loginReq).messageType(MessageTypeEnum.LOGIN_REQ).build();
        channel.writeOutbound(message);
        var buf = ((ByteBuf) channel.readOutbound()).slice();
        channel.writeInbound(buf); // 将请求写入服务器
    }
}