package com.mdc.mim.netty.session;

import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.netty.session.state.ISessionState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 14:54
 */
@Data
@Slf4j
public abstract class AbstractSession {
    public static final AttributeKey<AbstractSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    protected Channel channel;
    protected ISessionState state;
    protected String sessionId;
    protected UserDTO user;

    public void bindChannel(Channel channel) {
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
        var cf = channel.close();
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (cf.isSuccess()) {
                    log.info("has closed session in channel: {}", channel);
                }
            }
        });
    }
}
