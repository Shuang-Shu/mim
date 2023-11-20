package com.mdc.mim.netty.server.handler;

import com.mdc.mim.common.concurrent.CallbackExecutor;
import com.mdc.mim.common.concurrent.CallbackTask;
import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatMessageRedirectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var message = (Message) msg;
        // 0 判断消息是否为Message
        if (msg == null || !(msg instanceof Message)
                || !((Message) msg).getMessageType().equals(MessageTypeEnum.MESSAGE_REQ)) {
            super.channelRead(ctx, msg);
            return;
        }
        log.debug("successfully receiving chatMessage(server): {}", msg);
        var chatMessageReq = ((Message.MessageRequest) message.getMessageRequest());
        // 1 异步处理鉴权及消息转发
        CallbackExecutor.instance().execute(
                new CallbackTask<Boolean>() {
                    @Override
                    public void onBack(Boolean success) {
                        if (success) {
                            log.info("login success");
                        } else {
                            log.error("login failed");
                        }
                    }

                    @Override
                    public void onException(Throwable t) {
                        log.error("login failed", t);
                    }

                    @Override
                    public Boolean call() throws Exception {
                        return false;
                    }
                }
        );
    }

    private void doRedirectMessage(ChannelHandlerContext ctx, Message msg) {
    }

    private void doAuthenticate(Message msg) {
    }
}
