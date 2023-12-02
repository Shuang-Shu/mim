package com.mdc.mim.netty.client.sender;

import com.mdc.mim.common.constant.CommonConst;
import com.mdc.mim.common.dto.UserDTO;

import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;

import com.mdc.mim.netty.client.NettyClient;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogInOutSender extends AbstractSender {
    public LogInOutSender(NettyClient client) {
        this.client = client;
    }

    private NettyClient client;

    public ChannelFuture sendLogin(UserDTO user) {
        var loginRequest = Message.LogInRequest.builder().user(user).appVersion(CommonConst.APP_VERSION).build();
        var msg = Message.builder().id(getId()).messageType(MessageTypeEnum.LOGIN_REQ).logInRequest(loginRequest).build();
        log.info("sending login request: {}", msg);
        return super.sendMessage(msg);
    }

    public ChannelFuture sendLogout(UserDTO user) {
        var logoutRequest = Message.LogOutRequest.builder().user(user).build();
        var msg = Message.builder().id(getId()).messageType(MessageTypeEnum.LOGOUT_REQ).logOutRequest(logoutRequest).sessionId(clientSession.getSessionId()).build();
        log.debug("sending logout request: {}", msg);
        return super.sendMessage(msg);
    }
}
