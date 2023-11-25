package com.mdc.mim.netty.server.processor;

import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.feign.ChatFeignMessageService;
import com.mdc.mim.netty.feign.FriendFeignService;
import com.mdc.mim.netty.session.ServerSession;
import com.mdc.mim.netty.session.ServerSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Override
    public MessageTypeEnum supportType() {
        return MessageTypeEnum.MESSAGE_REQ;
    }

    @Override
    public Boolean process(ServerSession serverSession, Message message) {
        var chatMessageReq = message.getMessageRequest();
        var chatMessageResp = Message.MessageResponse.builder().id(chatMessageReq.getId()).build();
        // 发送鉴权
        var target = chatMessageReq.getChatMessage().getToUid();
        var uid = serverSession.getUser().getUid();
        var r = friendFeignService.isFriend(uid, target);
        if (r == null || !(Boolean) r.get("isFriend")) {
            log.info("not friend");
            return false;
        }
        var messageResp = Message.builder().messageType(MessageTypeEnum.MESSAGE_RESP).messageResponse(chatMessageResp).build();
        var sessions = sessionManager.getSessionsByUid(target);
        if (sessions == null || sessions.isEmpty()) {
            // TODO 此处需要实现离线消息，如果用户不在线，将消息存储到数据库中，等用户上线后再发送
            log.info("target {} not online", target);
            return true;
        }
        try {
            for (var session : sessions) {
                session.writeAndFlush(messageResp);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
