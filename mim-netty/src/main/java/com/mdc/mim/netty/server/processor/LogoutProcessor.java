package com.mdc.mim.netty.server.processor;

import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.constant.ResponsesCodeEnum;
import com.mdc.mim.common.dto.Message;
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
public class LogoutProcessor implements AbstractProcessor {
    @Autowired
    private ServerSessionManager sessionManager;

    @Override
    public MessageTypeEnum supportType() {
        return MessageTypeEnum.LOGOUT_REQ;
    }

    @Override
    public Boolean process(ServerSession serverSession, Message message) {
        var logoutReq = message.getLogoutRequest();
        var logoutResp = Message.LogoutResponse.builder().id(logoutReq.getId()).build();
        var respMessage = Message.builder().messageType(MessageTypeEnum.LOGOUT_RESP).logoutResponse(logoutResp).build();
        if (sessionManager.contains(message.getSessionId())) {
            sessionManager.removeSession(message.getSessionId());
            logoutResp.setCode(ResponsesCodeEnum.SUCCESS);
            // 发送消息
            try {
                serverSession.writeAndFlush(respMessage).sync();
                serverSession.logoutSuccess(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            logoutResp.setCode(ResponsesCodeEnum.FAILED);
            logoutResp.setInfo("user: " + logoutReq.getUser().getUserName() + " is not login");
        }
        return false;
    }
}
