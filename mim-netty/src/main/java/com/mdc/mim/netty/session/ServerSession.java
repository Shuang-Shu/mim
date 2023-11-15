package com.mdc.mim.netty.session;

import com.mdc.mim.common.dto.UserDTO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerSession {
    public static final AttributeKey<ServerSession> SESSION_KEY = AttributeKey.valueOf("SERVER_SESSION_KEY");
    private UserDTO user;
    private String sessionId;
    private Channel channel;

    public ServerSession(Channel channel, UserDTO user) {
        this.channel = channel;
        this.user = user;
        // 将ClientSession绑定到channel
        this.channel.attr(SESSION_KEY).set(this);
    }

    public void bindChannel() {
        channel.attr(SESSION_KEY).set(this);
    }

    public ChannelFuture writeAndFlush(Object pojo) {
        return channel.writeAndFlush(pojo);
    }

    public void writeAndClose(Object pojo) {
        var cf = channel.writeAndFlush(pojo);
        cf.addListener(ChannelFutureListener.CLOSE); // 添加关闭Listener
    }

    public void close() {
        var cf = channel.closeFuture();
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (cf.isSuccess()) {
                    log.info("has closed session");
                }
            }

        });
    }
}
