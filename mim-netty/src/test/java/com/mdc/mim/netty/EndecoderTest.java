package com.mdc.mim.netty;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.ResponsesCodeEnum;
import com.mdc.mim.common.dto.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mdc.mim.netty.codec.KryoContentDecoder;
import com.mdc.mim.netty.codec.KryoContentEncoder;
import com.mdc.mim.netty.codec.MIMByteDecoder;
import com.mdc.mim.netty.codec.MIMByteEncoder;
import com.mdc.mim.common.constant.CommonConstant;
import com.mdc.mim.common.enumeration.PlatformEnum;
import com.mdc.mim.common.dto.Message.LogInResponse;
import com.mdc.mim.common.dto.Message.LogOutRequest;
import com.mdc.mim.common.dto.Message.LogOutResponse;
import com.mdc.mim.common.dto.Message.MessageRequest;
import com.mdc.mim.common.dto.Message.MessageResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

public class EndecoderTest {
    static EmbeddedChannel channel;

    static Message message = Message.builder().sessionId("testSessionId").build();

    @BeforeAll
    static void init() {
        initialChannelWith(channelHandlers);
    }

    static void initialChannelWith(ChannelHandler[] handlers) {
        var initializer = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                for (var handler : handlers) {
                    ch.pipeline().addLast(handler);
                }
            }
        };
        channel = new EmbeddedChannel(initializer);
    }

    static UserDTO userDTO = UserDTO.builder().userName("test").passwdMd5("test").devId("dev-1").platformEnum(PlatformEnum.LINUX).build();

    static ChannelHandler[] channelHandlers;

    static {
        var byteEncoder = new MIMByteEncoder();
        var byteDecoder = new MIMByteDecoder();
        var kryoSupplier = CommonConstant.supplier;
        var kryoContentEncoder = new KryoContentEncoder(kryoSupplier);
        var kryoContentDecoder = new KryoContentDecoder(kryoSupplier);
        channelHandlers = new ChannelHandler[]{
                byteDecoder, kryoContentDecoder, byteEncoder, kryoContentEncoder
        };
    }

    /**
     * 先将对象写入输出端，再将结果从输入端写入，检查解码结果与原始结果是否一致
     *
     * @param channel
     * @param msg
     */
    public void doTestPipeline(EmbeddedChannel channel, Object msg) {
        // 输出
        channel.writeOutbound(msg);
        var output = ((ByteBuf) channel.readOutbound()).slice();
        // 输入
        channel.writeInbound(output);
        var result = channel.readInbound();
        // 打印输出
        System.out.println(result);
    }

    @Test
    public void testKryoDecoder() {
//        initialChannelWith(channelHandlers);
        doTestPipeline(channel, message);
    }

    @Test
    public void testLoginTransport() {
//        initialChannelWith(channelHandlers);
        var req = Message.LogInRequest.buildWith(userDTO, 1L);
        doTestPipeline(channel, req);
        var resp = LogInResponse.builder().code(ResponsesCodeEnum.SUCCESS).id(123L).info("success").expose(9).build();
        doTestPipeline(channel, resp);
    }

    @Test
    public void testLogoutTransport() {
//        initialChannelWith(channelHandlers);
        var req = LogOutRequest.builder().id(123L).build();
        var resp = LogOutResponse.builder().id(321L).build();
        doTestPipeline(channel, req);
        doTestPipeline(channel, resp);
    }

    @Test
    public void testMessageTransport() {
//        initialChannelWith(channelHandlers);
        var req = MessageRequest.builder().id(123L).from(1234L).to(12345L).time(System.currentTimeMillis())
                .messageType(com.mdc.mim.common.dto.Message.ChatMessageType.TEXT).content("hello world")
                .url("mimprotocol").property("").fromNick("shuangshu-nick")
                .json("{\"hello\": \"good\" }").build();
        var resp = MessageResponse.builder().id(321L).build();
        doTestPipeline(channel, req);
        doTestPipeline(channel, resp);
    }
}
