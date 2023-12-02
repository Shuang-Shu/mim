package com.mdc.mim.netty.session;

import com.mdc.mim.common.dto.ChatMessageDTO;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.netty.session.state.IClientSessionState;
import com.mdc.mim.netty.session.state.impl.client.ClientNotLoginState;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 胶水类，该类是对会话的抽象，其用于保存用户信息和连接通道，同时记录连接状态，
 * 该类同时还与确定的通道绑定，负责相互信息的交换
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClientSession extends AbstractSession implements IClientSessionState {
    private final Map<Long, List<Message>> receivedMessageMap = new HashMap<>();
    private final Set<Long> receivedMessageSet = new HashSet<>();

    public ClientSession(Channel channel) {
        this.channel = channel;
        this.state = new ClientNotLoginState(this);
        // 将ClientSession绑定到channel
        this.bindChannel(channel);
    }

    public void logInSuccess(Message message) {
        this.sessionId = message.getLogInResponse().getSessionId(); // 获取sessionId
        state.logOutSuccess(message);
    }

    @Override
    public void logOutSuccess(Message message) {
        state.logOutSuccess(message);
    }

    @Override
    public String stateDescription() {
        return state.stateDescription();
    }

    @Override
    public synchronized void pushMessage(Message message) {
        if (message.getMessageType().equals(MessageTypeEnum.MESSAGE_NOTIFY)) {
            if (!receivedMessageSet.contains(message.getId())) {
                receivedMessageSet.add(message.getId());
            } else {
                return;
            }
            var chatMessage = message.getMessageNotify().getChatMessageDTO();
            var uid = chatMessage.getFromUid();
            if (!receivedMessageMap.containsKey(uid)) {
                List<Message> messageList = new ArrayList<>();
                receivedMessageMap.put(uid, messageList);
            }
            receivedMessageMap.get(uid).add(message);
        }
    }

    public List<ChatMessageDTO> getChatMessages(Long uid) {
        return receivedMessageMap.get(uid).stream().map(message -> message.getMessageNotify().getChatMessageDTO()).toList();
    }

    @Override
    public void sendFail() {
        ((IClientSessionState) state).sendFail();
    }

    @Override
    public void connectSuccess() {
        ((IClientSessionState) state).connectSuccess();
    }

}
