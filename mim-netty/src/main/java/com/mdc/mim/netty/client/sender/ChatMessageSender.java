package com.mdc.mim.netty.client.sender;

import org.springframework.stereotype.Service;

import com.mdc.mim.common.constant.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;

import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("chatMessageSender")
public class ChatMessageSender extends AbstractSender {
    /**
     * 发送消息content到toUid
     * 
     * @param toUid   目标uid
     * @param content 消息内容
     */
    public ChannelFuture sendChatMessage(Long toUid, String content) {
        var user = getUser();
        var chatMessageRequest = Message.MessageRequest.builder().id(getId()).from(user.getUid()).to(toUid)
                .time(System.currentTimeMillis()).messageType(Message.ChatMessageType.TEXT).content(content)
                .property(null).fromNick(user.getNickName()).json(null).build();
        var msg = Message.builder().sessionId(getClientSession().getSessionId())
                .messageType(MessageTypeEnum.MESSAGE_REQ).messageRequest(chatMessageRequest).build();
        log.debug("sending chat message: {}", msg);
        return super.sendMessage(msg);
    }
}
