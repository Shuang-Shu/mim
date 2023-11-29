package com.mdc.mim.netty.server.processor;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.netty.feign.ChatFeignMessageService;
import com.mdc.mim.netty.feign.FriendFeignService;
import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import com.mdc.mim.netty.utils.ChatMessageSeqNoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/17 17:18
 */
@Slf4j
@Component
public class ChatMessageRedirectProcessor implements AbstractProcessor {
    @Autowired
    private FriendFeignService friendFeignService;

    @Autowired
    private ServerSessionManager sessionManager;

    @Autowired
    private ChatFeignMessageService chatFeignMessageService;

    @Autowired
    private ChatMessageSeqNoManager chatMessageSeqNoManager;

    @Override
    public MessageTypeEnum supportType() {
        return MessageTypeEnum.MESSAGE_REQ;
    }

    @Override
    public Boolean process(ServerSession serverSession, Message message) {
        // TODO 格式可优化
        var chatMessageReq = message.getMessageRequest();
        var chatMessageResp = Message.MessageResponse.builder().build();
        // 鉴别发送对象是否为好友
        var target = chatMessageReq.getChatMessage().getToUid();
        var uid = serverSession.getUser().getUid();
        var r = friendFeignService.isFriend(uid, target);
        if (r == null || !(Boolean) r.get("isFriend")) {
            // 非好友，响应错误消息
            serverSession.writeAndFlush(Message.builder().id(message.getId()).messageType(MessageTypeEnum.ERR_RESP).info("target is not your friend").build());
            log.info("not friend");
            return false;
        }
        var messagesToBeSent = serverSession.getContinuousMessages();
        var sessions = sessionManager.getSessionsByUid(target);
        // 消息持久化
        var chatMessageDTOs = messagesToBeSent.stream().map(t -> t.getMessageRequest().getChatMessage().setCreateTime(new Date(System.currentTimeMillis()))).toList();
        if (chatMessageDTOs.size() > 0) {
            chatFeignMessageService.saveMessage(chatMessageDTOs);
            log.info("saving messages, len= {}", chatMessageDTOs.size());
        }
        // 写回响应消息
        serverSession.writeAndFlush(Message.builder().id(message.getId()).messageType(MessageTypeEnum.MESSAGE_RESP).messageResponse(chatMessageResp).info("send message success").build());
        if (sessions == null || sessions.isEmpty()) {
            log.info("target {} not online", target);
            return true;
        }
        for (var messageToBeSent : messagesToBeSent) {
            var chatMessage = messageToBeSent.getMessageRequest().getChatMessage();
            // 鉴定为好友，向对方发送消息
            var notify = Message.MessageNotify.builder().chatMessageDTO(chatMessageReq.getChatMessage().setId(chatMessage.getId())).build();
            var notifyMessage = Message.builder().id(message.getId()).messageType(MessageTypeEnum.MESSAGE_NOTIFY).messageNotify(notify).build();
            try {
                for (var session : sessions) {
                    session.writeAndFlush(notifyMessage);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
