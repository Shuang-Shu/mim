package com.mdc.mim.netty.server.processor;

import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.ServerSession;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/17 17:18
 */
public class ChatMessageRedirectProcessor implements AbstractProcessor {
    @Override
    public MessageTypeEnum supportType() {
        return MessageTypeEnum.MESSAGE_REQ;
    }

    @Override
    public Boolean process(ServerSession serverSession, Message message) {
        var messageReq = message.getMessageRequest();
        var messageResp = Message.MessageResponse.builder().id(messageReq.getId()).info("redirected message").build();
        var respMessge = Message.builder().messageType(MessageTypeEnum.MESSAGE_RESP).messageResponse(messageResp).build();
        return null;
    }
}
