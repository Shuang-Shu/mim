package com.mdc.mim.netty.server.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.enumeration.ResponsesCodeEnum;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.netty.feign.UserService;
import com.mdc.mim.netty.server.handler.LogInRequestHandler;
import com.mdc.mim.netty.server.handler.LogOutRequestHandler;
import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import com.mdc.mim.netty.session.state.StateConstant;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogInProcessor implements AbstractProcessor {
    public static final String NAME = "loginProcessor";
    @Autowired
    private UserService userService;
    @Autowired
    private ServerSessionManager sessionManager;
    @Autowired
    private LogOutRequestHandler logOutRequestHandler;

    @Override
    public MessageTypeEnum supportType() {
        return MessageTypeEnum.LOGIN_REQ;
    }

    @Override
    public Boolean process(ServerSession serverSession, Message message) {
        var loginReq = message.getLogInRequest();
        var loginResp = Message.LogInResponse.builder().id(loginReq.getId())
                .info("successfully identified").sessionId(serverSession.getSessionId()).expose(0).build();
        var respMessage = Message.builder().messageType(MessageTypeEnum.LOGIN_RESP).logInResponse(loginResp).build();
        boolean result = false;
        // 检查是否已经登录
        if (serverSession.getState().stateDescription().equals(StateConstant.NOT_LOGIN)) {
            var user = loginReq.getUser();
            var r = userService.identify(user.getUserName(), user.getPasswdMd5());
            var ok = (Boolean) r.get("valid");
            if (!ok) {
                loginResp.setCode(ResponsesCodeEnum.FAILED);
                loginResp.setInfo("identify failed, wrong username or password");
            } else {
                loginResp.setCode(ResponsesCodeEnum.SUCCESS);
                // TODO 此处暂时手动进行类型转换
                var objectMapper = new ObjectMapper();
                var userDto = objectMapper.convertValue(r.get("user"), UserDTO.class);
                loginResp.setUser(userDto);
                result = true;
                // 修改session状态为登录成功
                serverSession.logInSuccess(message);
                serverSession.setUser(userDto);
            }
        } else if (serverSession.getState().stateDescription().equals(StateConstant.LOGIN)) {
            // 已登录
            serverSession.logOutSuccess(message);
            loginResp.setCode(ResponsesCodeEnum.SUCCESS);
            result = true;
        }
        if (result) {
            // 成功登录，删除当前的LogInRequestHandler，并添加LogOutRequestHandler
            var pipeline = serverSession.getChannel().pipeline();
            pipeline.addBefore(LogInRequestHandler.NAME, LogOutRequestHandler.NAME, logOutRequestHandler);
            serverSession.getChannel().pipeline().remove(LogInRequestHandler.NAME);
        }
        var f = serverSession.writeAndFlush(respMessage);
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
