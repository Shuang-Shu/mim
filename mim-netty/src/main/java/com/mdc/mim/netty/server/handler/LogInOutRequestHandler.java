package com.mdc.mim.netty.server.handler;

import com.mdc.mim.common.concurrent.CallbackExecutor;
import com.mdc.mim.common.concurrent.CallbackTask;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;

import com.mdc.mim.common.utils.IDUtils;
import com.mdc.mim.netty.server.processor.LogoutProcessor;
import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mdc.mim.netty.server.processor.LoginProcessor;

@Slf4j
@Component
@ChannelHandler.Sharable
public class LogInOutRequestHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private ServerSessionManager sessionManager;

    @Autowired
    private LoginProcessor loginProcessor;

    @Autowired
    private LogoutProcessor logoutProcessor;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null || !(msg instanceof Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        var message = (Message) msg;
        if (message.getMessageType() == null) {
            return;
        }
        // 0 判断消息是否为Message.LoginRequest
        if (message.getMessageType() == null || !message.getMessageType().equals(MessageTypeEnum.LOGIN_REQ) && !message.getMessageType().equals(MessageTypeEnum.LOGOUT_REQ)) {
            super.channelRead(ctx, msg);
            return;
        }
        // 1 分别处理不同请求
        if (message.getMessageType().equals(MessageTypeEnum.LOGIN_REQ)) {
            log.info("successfully receiving loginRequest(server)");
            log.debug("successfully receiving loginRequest(server): {}", msg);
            ServerSession serverSession = null;
            if (message.getSessionId() != null) {
                serverSession = sessionManager.getSession(message.getSessionId());
            }
            if (serverSession == null) {
                serverSession = new ServerSession(ctx.channel());
                serverSession.setSessionId(IDUtils.getSessionId());
                sessionManager.addSession(serverSession.getSessionId(), serverSession);
            }
            final var session = serverSession;
            // 1 异步处理认证流程
            CallbackExecutor.instance().execute(
                    new CallbackTask<Boolean>() {
                        @Override
                        public void onBack(Boolean success) {
                            if (success) {
                                log.info("login success");
                            } else {
                                log.error("login failed");
                                sessionManager.removeSession(session.getSessionId());
                            }
                        }

                        @Override
                        public void onException(Throwable t) {
                            log.error("login failed: {}", t.getMessage());
                            t.printStackTrace();
                        }

                        @Override
                        public Boolean call() throws Exception {
                            return loginProcessor.process(session, (Message) msg);
                        }
                    }
            );
        } else {
            log.info("successfully receiving logoutRequest(server)");
            log.debug("successfully receiving logoutRequest(server): {}", msg);

            var serverSession = sessionManager.getSession(message.getSessionId());
            if (serverSession == null) {
                // 未登陆过，直接关闭连接
                ctx.channel().close();
                return;
            }
            final var session = serverSession;

            CallbackExecutor.instance().execute(
                    new CallbackTask<Boolean>() {
                        @Override
                        public void onBack(Boolean success) {
                            if (success) {
                                log.info("logout success");
                            } else {
                                log.error("logout failed");
                            }
                        }

                        @Override
                        public void onException(Throwable t) {
                            log.error("login failed: {}", t.getMessage());
                            t.printStackTrace();
                        }

                        @Override
                        public Boolean call() throws Exception {
                            return logoutProcessor.process(session, (Message) msg);
                        }
                    }
            );
        }
    }
}