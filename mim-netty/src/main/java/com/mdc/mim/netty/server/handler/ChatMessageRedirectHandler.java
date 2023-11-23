package com.mdc.mim.netty.server.handler;

import com.mdc.mim.common.concurrent.CallbackExecutor;
import com.mdc.mim.common.concurrent.CallbackTask;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;

import com.mdc.mim.netty.server.processor.ChatMessageRedirectProcessor;
import com.mdc.mim.netty.session.ServerSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatMessageRedirectHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ServerSessionManager sessionManager;

    @Autowired
    private ChatMessageRedirectProcessor chatMessageRedirectProcessor;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 0 判断消息是否为Message
        if (msg == null || !(msg instanceof Message)
                || !((Message) msg).getMessageType().equals(MessageTypeEnum.MESSAGE_REQ)) {
            super.channelRead(ctx, msg);
            return;
        }
        var message = (Message) msg;
        final var serverSession = sessionManager.getSession(message.getSessionId());
        if (serverSession == null) {
            log.error("serverSession is null");
            throw new RuntimeException("serverSession is null");
        }
        log.info("successfully receiving chatMessage(server)");
        log.debug("successfully receiving chatMessage(server): {}", msg);
        // 1 异步处理鉴权及消息转发
        CallbackExecutor.instance().execute(
                new CallbackTask<Boolean>() {
                    @Override
                    public void onBack(Boolean success) {
                        if (success) {
                            log.info("redirect message success");
                        } else {
                            log.error("redirect message failed");
                        }
                    }

                    @Override
                    public void onException(Throwable t) {
                        log.error("redirect message failed", t);
                    }

                    @Override
                    public Boolean call() throws Exception {
                        return chatMessageRedirectProcessor.process(serverSession, message);
                    }
                }
        );
    }
}
