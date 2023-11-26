package com.mdc.mim.netty.server.processor;

import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.enumeration.ResponsesCodeEnum;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.server.handler.LogInRequestHandler;
import com.mdc.mim.netty.server.handler.LogOutRequestHandler;
import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/15 19:06
 */
@Slf4j
@Component
public class LogOutProcessor implements AbstractProcessor {
    @Autowired
    private ServerSessionManager sessionManager;

    @Autowired
    private LogInRequestHandler logInRequestHandler;

    @Override
    public MessageTypeEnum supportType() {
        return MessageTypeEnum.LOGOUT_REQ;
    }

    @Override
    public Boolean process(ServerSession serverSession, Message message) {
        var logoutReq = message.getLogOutRequest();
        var logoutResp = Message.LogOutResponse.builder().build();
        var respMessage = Message.builder().id(message.getId()).messageType(MessageTypeEnum.LOGOUT_RESP).logOutResponse(logoutResp).build();
        sessionManager.removeSession(message.getSessionId());
        logoutResp.setCode(ResponsesCodeEnum.SUCCESS);
        // 发送消息
        try {
            serverSession.writeAndFlush(respMessage).sync();
            serverSession.logOutSuccess(message);
            // 将serverSession从SessionManager中移除
            sessionManager.userLogOut(serverSession);
            sessionManager.removeSession(serverSession.getSessionId());
            serverSession.setUser(null);
            // 重新添加LogInRequestHandler
            var pipeline = serverSession.getChannel().pipeline();
            pipeline.addBefore(LogOutRequestHandler.NAME, LogInRequestHandler.NAME, logInRequestHandler);
            pipeline.remove(LogOutRequestHandler.NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
