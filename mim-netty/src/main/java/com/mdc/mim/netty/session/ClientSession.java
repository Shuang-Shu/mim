package com.mdc.mim.netty.session;

import com.mdc.mim.common.dto.Message;

import com.mdc.mim.netty.session.state.IClientSessionState;
import com.mdc.mim.netty.session.state.impl.client.ClientNotLoginState;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 胶水类，该类是对会话的抽象，其用于保存用户信息和连接通道，同时记录连接状态，
 * 该类同时还与确定的通道绑定，负责相互信息的交换
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClientSession extends AbstractSession implements IClientSessionState {
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
    public void sendFail() {
        ((IClientSessionState) state).sendFail();
    }

    @Override
    public void connectSuccess() {
        ((IClientSessionState) state).connectSuccess();
    }
}
