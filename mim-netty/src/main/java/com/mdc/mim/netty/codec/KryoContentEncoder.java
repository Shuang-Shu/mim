package com.mdc.mim.netty.codec;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Supplier;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mdc.mim.common.constant.CommonConstant;
import com.mdc.mim.common.utils.ClassIdUtils;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.stereotype.Component;

/*
 * Use Kryo to convert byte array to object
 */
public class KryoContentEncoder extends MessageToMessageEncoder<Object> {

    final ThreadLocal<Kryo> serializerThreadLocal;

    public KryoContentEncoder() {
        serializerThreadLocal = ThreadLocal.withInitial(() -> CommonConstant.supplier.get());
    }

    public KryoContentEncoder(Supplier<Kryo> supplier) {
        serializerThreadLocal = ThreadLocal.withInitial(() -> supplier.get());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        Output output = null;
        try (var bos = new ByteArrayOutputStream()) {
            output = new Output(bos);
            int classId = ClassIdUtils.generateClassId(msg.getClass(), CommonConstant.APP_VERSION);
            // 写入注册id
            output.writeInt(classId);
            serializerThreadLocal.get().writeObject(output, msg);
            bos.flush();
            out.add(output.toBytes());
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

}
