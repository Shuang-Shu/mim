package com.mdc.mim.netty.server.handler;

import com.mdc.mim.common.concurrent.CallbackExecutor;
import com.mdc.mim.common.concurrent.CallbackTask;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.netty.server.processor.LogOutProcessor;
import com.mdc.mim.netty.session.AbstractSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 用于处理登出请求的Handler，该Handler包括2个功能：
 * 1. 处理合法的登出请求
 * 2. 请求发起时检查连接的状态，如果已经超时则自动执行登出操作
 * @date 2023/11/24 0:52
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class LogOutRequestHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "logOutRequestHandler";

    @Autowired
    private ServerSessionManager sessionManager;

    @Autowired
    private LogOutProcessor logOutProcessor;

    @Autowired
    private LogInRequestHandler logInRequestHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var message = (Message) msg;
        // TODO 此处还应当添加一个长连接权限校验逻辑，因为长连接可能也已经过期，此时需要重新要求用户登录
        var serverSession = sessionManager.getSession(message.getSessionId());
        if (serverSession == null) {
            // session不存在，直接恢复未登录状态
            ctx.channel().pipeline().addBefore(LogOutRequestHandler.NAME, LogInRequestHandler.NAME, logInRequestHandler);
            ctx.channel().pipeline().remove(LogOutRequestHandler.NAME);
            serverSession.logOutSuccess(message);
            // 将自身从sessionManager中移除
            var session = ctx.channel().attr(AbstractSession.SESSION_KEY).get();
            sessionManager.removeSession(session.getSessionId());
            return;
        }
        if (message.getMessageType() != MessageTypeEnum.LOGOUT_REQ) {
            // 不是登出请求，直接转发
            super.channelRead(ctx, msg);
            return;
        }
        serverSession.pushMessage(message);
        log.info("successfully receiving logoutRequest(server)");

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
                        return logOutProcessor.process(session, (Message) msg);
                    }
                }
        );
    }
}
