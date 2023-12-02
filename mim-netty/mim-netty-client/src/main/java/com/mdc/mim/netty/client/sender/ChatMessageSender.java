package com.mdc.mim.netty.client.sender;

import com.mdc.mim.common.dto.ChatMessageDTO;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.netty.client.NettyClient;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatMessageSender extends AbstractSender {
    public ChatMessageSender(NettyClient client) {
        this.client = client;
    }

    private NettyClient client;

    /**
     * 发送消息content到toUid
     *
     * @param toUid   目标uid
     * @param content 消息内容
     */
    public ChannelFuture sendChatMessage(Long toUid, String content) {
        var user = getUser();
        if (user != null) {
            // TODO 此处需要自动生成消息id
            var chatMessage = ChatMessageDTO.builder().id(client.getChatMessageId()).fromUid(user.getUid()).toUid(toUid).type(0).content(content).build();
            var chatMessageRequest = Message.MessageRequest.builder().chatMessage(chatMessage).build();
            var msg = Message.builder().id(getId()).sessionId(getClientSession().getSessionId())
                    .messageType(MessageTypeEnum.MESSAGE_REQ).messageRequest(chatMessageRequest).build();
            client.addNotAckedMessage(msg);
            log.debug("sending chat message: {}", msg);
            return super.sendMessage(msg);
        } else {
            throw new RuntimeException("user is null");
        }
    }
}
