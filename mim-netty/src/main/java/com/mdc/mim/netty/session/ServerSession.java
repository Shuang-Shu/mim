package com.mdc.mim.netty.session;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.netty.session.state.IServerSessionState;
import com.mdc.mim.netty.session.state.impl.server.ServerNotLoginState;
import com.mdc.mim.netty.utils.ChatMessageSeqNoManager;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ServerSession extends AbstractSession implements IServerSessionState {
    private PriorityQueue<Message> chatMessageHeap = new PriorityQueue<>(
            new Comparator<Message>() {
                @Override
                public int compare(Message m1, Message m2) {
                    return m1.getId().compareTo(m2.getId());
                }
            }
    );

    private Long nextMessageId = 0L;
    private ChatMessageSeqNoManager chatMessageSeqNoManager;

    // 目前直接使用synchronized来实现线程安全，后续可以考虑使用更高效的方法
    public synchronized void pushMessage(Message message) {
        chatMessageHeap.add(message);
    }

    public synchronized List<Message> getContinuousMessages() {
        // TODO 目前简单地使用优先队列来实现，后续可以考虑使用更高效的数据结构
        var continuousMessages = new ArrayList<Message>();
        while (!chatMessageHeap.isEmpty()) {
            var message = chatMessageHeap.peek();
            if (message.getId().equals(nextMessageId)) {
                message = chatMessageHeap.poll();
                if (message.getMessageType().equals(MessageTypeEnum.MESSAGE_REQ)) {
                    message.getMessageRequest().getChatMessage().setId(chatMessageSeqNoManager.getSeqNo(getUser().getUid()));
                    continuousMessages.add(message);
                }
                nextMessageId++;
            } else if (message.getId() < nextMessageId) {
                // 重复消息，直接丢弃
                chatMessageHeap.poll();
            } else if (message.getId() > nextMessageId) {
                // 丢失消息，直接返回，等待下一次轮询
                break;
            }
        }
        return continuousMessages;
    }

    public ServerSession(Channel channel) {
        this.channel = channel;
        this.state = new ServerNotLoginState(this);
        // 将当前ServerSession绑定到channel中
        this.bindChannel(channel);
    }

    @Override
    public void logInSuccess(Message message) {
        state.logInSuccess(message);
    }

    @Override
    public void logOutSuccess(Message message) {
        state.logOutSuccess(message);
    }

    @Override
    public String stateDescription() {
        return state.stateDescription();
    }
}
