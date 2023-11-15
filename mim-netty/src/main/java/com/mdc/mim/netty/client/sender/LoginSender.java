package com.mdc.mim.netty.client.sender;

import com.mdc.mim.common.dto.UserDTO;
import org.springframework.stereotype.Service;

import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;

import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("loginSender")
public class LoginSender extends AbstractSender {
    public ChannelFuture sendLogin(UserDTO user) {
        var loginRequest = Message.LoginRequest.buildWith(user, getId());
        var msg = Message.builder().messageType(MessageTypeEnum.LOGIN_REQ).loginRequest(loginRequest).build();
        log.debug("sending log request: {}", msg);
        return super.sendMessage(msg);
    }
}
