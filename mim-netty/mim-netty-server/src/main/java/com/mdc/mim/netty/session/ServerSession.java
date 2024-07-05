package com.mdc.mim.netty.session;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.exception.CycleBufferException;
import com.mdc.mim.common.utils.CycleBuffer;
import com.mdc.mim.netty.session.state.IServerSessionState;
import com.mdc.mim.netty.session.state.impl.server.ServerNotLoginState;
import com.mdc.mim.netty.utils.ChatMessageSeqNoManager;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ServerSession extends AbstractSession implements IServerSessionState {
    private static final long LOCK_FLAG = 1L << 63;
    private static final int MESSAGE_BUFFER_CAPACITY = 128;

    private CycleBuffer<Message> messageBuffer = new CycleBuffer<>(MESSAGE_BUFFER_CAPACITY);
    private ChatMessageSeqNoManager chatMessageSeqNoManager;
    private AtomicLong nextId = new AtomicLong(0L); // 当前组写入DB的首个消息的ID
    private long nextNextId = 0L; // 下一组写入DB的首个消息ID

    // 目前直接使用synchronized来实现线程安全，后续可以考虑使用更高效的方法
    public synchronized void pushMessage(Message message) {
        try {
            messageBuffer.setAt(message.getId(), message);
        } catch (CycleBufferException e) {
            // 消息过旧或超限
            log.error("pushMessage error", e);
        }
    }

    public List<Message> getContinuousMessages() {
        var continuousMessages = new ArrayList<Message>();
        Message nextMessage;
        if ((LOCK_FLAG & (nextId.get())) == 0) {
            return continuousMessages;
        }
        nextNextId = nextId.get() & (~LOCK_FLAG);
        while ((nextMessage = messageBuffer.next()) != null) {
            nextNextId++;
            if (nextMessage.getMessageType().equals(MessageTypeEnum.MESSAGE_REQ)) {
                nextMessage.getMessageRequest().getChatMessage()
                        .setId(chatMessageSeqNoManager.getSeqNo(getUser().getUid()));
                continuousMessages.add(nextMessage);
            }
        }
        return continuousMessages;
    }

    public Message peekMessage() {
        return messageBuffer.peek();
    }

    public ServerSession(Channel channel) {
        this.channel = channel;
        this.state = new ServerNotLoginState(this);
        // 将当前ServerSession绑定到channel中
        this.bindChannel(channel);
    }

    /**
     * @description: 锁住首个消息的id，保证只有一个processor能够将消息写入DB
     * @param:
     * @return:
     * @author ShuangShu
     * @date: 2023/12/6 21:15
     */
    public boolean lockId(long expected) {
        long val = nextId.get();
        if ((val & LOCK_FLAG) != 0) {
            return false;
        }
        return nextId.compareAndSet(expected, expected ^ LOCK_FLAG);
    }

    public void unlockId(long oldId) {
        long val = nextId.get();
        if ((val & LOCK_FLAG) == 0) {
            return;
        }
        nextId.compareAndSet(oldId | LOCK_FLAG, nextNextId);
    }

    public long getNextId() {
        return nextId.get();
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
