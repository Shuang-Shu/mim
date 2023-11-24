package com.mdc.mim.netty.client.sender;

import com.mdc.mim.common.dto.UserDTO;

import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;

import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogInOutSender extends AbstractSender {
    public ChannelFuture sendLogin(UserDTO user) {
        var loginRequest = Message.LogInRequest.buildWith(user, getId());
        var msg = Message.builder().messageType(MessageTypeEnum.LOGIN_REQ).logInRequest(loginRequest).build();
        log.debug("sending login request: {}", msg);
        return super.sendMessage(msg);
    }

    public ChannelFuture sendLogout(UserDTO user) {
        var logoutRequest = Message.LogOutRequest.buildWith(user, getId());
        var msg = Message.builder().messageType(MessageTypeEnum.LOGOUT_REQ).logOutRequest(logoutRequest).sessionId(clientSession.getSessionId()).build();
        log.debug("sending logout request: {}", msg);
        return super.sendMessage(msg);
    }
}
