package com.mdc.mim.client.sender;

import org.springframework.stereotype.Service;

import com.mdc.mim.constant.CommonConstant;
import com.mdc.mim.constant.MessageTypeEnum;
import com.mdc.mim.dto.Message;
import com.mdc.mim.entity.UserEntity;

import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("loginSender")
public class LoginSender extends AbstractSender {
    public ChannelFuture sendLogin(UserEntity user) {
        var loginRequest = Message.LoginRequest.builder().id(getId()).uid(user.getUid()).deviceId(user.getDevId())
                .token(user.getToken()).platform(user.getPlatform()).appVersion(CommonConstant.APP_VERSION).build();
        var msg = Message.builder().messageType(MessageTypeEnum.LOGIN_REQ).loginRequest(loginRequest).build();
        log.debug("sending log request: {}", msg);
        return super.sendMessage(msg);
    }
}
