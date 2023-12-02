package com.mdc.mim.netty.server.handler;

import com.mdc.mim.common.concurrent.CallbackExecutor;
import com.mdc.mim.common.concurrent.CallbackTask;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.utils.IDUtils;
import com.mdc.mim.netty.server.processor.LogInProcessor;
import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import com.mdc.mim.netty.utils.ChatMessageSeqNoManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/24 9:04
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class LogInRequestHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "logInRequestHandler";

    @Autowired
    private ServerSessionManager sessionManager;

    @Autowired
    private LogInProcessor loginProcessor;

    @Autowired
    private ChatMessageSeqNoManager chatMessageSeqNoManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var message = (Message) msg;
        if (message.getMessageType() != MessageTypeEnum.LOGIN_REQ) {
            // 当LoginHandler存在时，用户必须先登录才能进行其他操作
            processNotLogin(ctx);
            return;
        }
        log.info("successfully receiving loginRequest(server)");
        ServerSession serverSession = null;
        if (message.getSessionId() != null) {
            serverSession = sessionManager.getSession(message.getSessionId());
        }
        if (serverSession == null) {
            // 尚未保存会话，添加到SessionManager中
            serverSession = new ServerSession(ctx.channel());
            serverSession.setChatMessageSeqNoManager(chatMessageSeqNoManager);
            serverSession.setSessionId(IDUtils.getSessionId());
            sessionManager.addSession(serverSession.getSessionId(), serverSession);
        }
        final var session = serverSession;
        session.pushMessage(message); // 消息加入队列
        // 异步处理认证流程
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
    }

    /**
     * @description: 未登陆时的处理，直接返回一个错误消息
     * @param:
     * @return:
     * @author ShuangShu
     * @date: 2023/11/24 9:21
     */
    private void processNotLogin(ChannelHandlerContext ctx) {
        var respMessage = Message.builder().messageType(MessageTypeEnum.ERR_RESP).info("client is not login").build();
        ctx.channel().writeAndFlush(respMessage);
    }
}
