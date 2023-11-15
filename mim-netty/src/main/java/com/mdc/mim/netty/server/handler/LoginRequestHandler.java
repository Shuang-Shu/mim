package com.mdc.mim.netty.server.handler;

import java.util.UUID;

import com.mdc.mim.common.concurrent.CallbackExecutor;
import com.mdc.mim.common.concurrent.CallbackTask;
import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.constant.ResponsesCodeEnum;
import com.mdc.mim.common.dto.Message;

import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mdc.mim.netty.server.processor.LoginProcessor;

@Slf4j
@Component
@ChannelHandler.Sharable
public class LoginRequestHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private ServerSessionManager sessionManager;

    @Autowired
    private LoginProcessor loginProcessor;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 0 判断消息是否为Message.LoginRequest
        if (msg == null || !(msg instanceof Message)
                || !((Message) msg).getMessageType().equals(MessageTypeEnum.LOGIN_REQ)) {
            super.channelRead(ctx, msg);
            return;
        }
        log.info("successfully receiving loginRequest(server)");
        log.debug("successfully receiving loginRequest(server): {}", msg);
        // TODO 1 构建ServerSession
        var loginReq = (Message.LoginRequest) ((Message) msg).getLoginRequest();
        var serverSession = new ServerSession(ctx.channel(), loginReq.getUser());
        // 1 异步处理认证流程
        CallbackExecutor.instance().execute(
                new CallbackTask<Boolean>() {
                    @Override
                    public void onBack(Boolean success) {
                        if (success) {
                            log.info("success");
                        } else {
                            log.error("login failed");
                        }
                    }

                    @Override
                    public void onException(Throwable t) {
                        log.error("login failed: {}", t.getMessage());
                        t.printStackTrace();
                    }

                    @Override
                    public Boolean call() throws Exception {
                        return loginProcessor.process(serverSession, (Message) msg);
                    }
                }
        );
    }
}
