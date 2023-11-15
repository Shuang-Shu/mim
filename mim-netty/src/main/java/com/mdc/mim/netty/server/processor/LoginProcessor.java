package com.mdc.mim.netty.server.processor;

import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.constant.ResponsesCodeEnum;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.feign.UserLoginService;
import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class LoginProcessor implements AbstractProcessor {
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private ServerSessionManager sessionManager;

    @Override
    public MessageTypeEnum supportType() {
        return MessageTypeEnum.LOGIN_REQ;
    }

    private String getSessionId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Boolean process(ServerSession session, Message message) {
        var loginReq = message.getLoginRequest();
        var loginResp = Message.LoginResponse.builder().id(loginReq.getId())
                .info("successfully identified").expose(0).build();
        var respMessage = Message.builder().messageType(MessageTypeEnum.LOGIN_RESP).loginResponse(loginResp).build();
        boolean result = false;
        // 1 检查是否已登录
        if (!sessionManager.contains(message.getSessionId())) {
            // 未登录
            var user = loginReq.getUser();
            var r = userLoginService.identify(user.getUserName(), user.getPasswdMd5());
            var ok = (Boolean) r.get("valid");
            if (!ok) {
                loginResp.setCode(ResponsesCodeEnum.FAILED);
                loginResp.setInfo("identify failed, wrong username or password");
            } else {
                loginResp.setSessionId(getSessionId());
                loginResp.setCode(ResponsesCodeEnum.SUCCESS);
                sessionManager.addSession(loginResp.getSessionId(), session);
                result = true;
            }
        } else {
            // 已登录
            loginResp.setCode(ResponsesCodeEnum.SUCCESS);
            result = true;
        }
        var f = session.writeAndFlush(respMessage);
        f.addListener(
                new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            log.info("successfully response client");
                            log.debug("successfully response client: {}", respMessage);
                        } else {
                            future.cause().printStackTrace();
                        }
                    }
                });
        return result;
    }
}
