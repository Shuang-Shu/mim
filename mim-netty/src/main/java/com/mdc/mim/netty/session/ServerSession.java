package com.mdc.mim.netty.session;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.state.IServerSessionState;
import com.mdc.mim.netty.session.state.ISessionState;
import com.mdc.mim.netty.session.state.impl.client.ClientNotLoginState;
import com.mdc.mim.netty.session.state.impl.server.ServerNotLoginState;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ServerSession extends AbstractSession implements IServerSessionState {
    public ServerSession(Channel channel) {
        this.channel = channel;
        this.state = new ServerNotLoginState(this);
        // 将当前ServerSession绑定到channel中
        this.bindChannel(channel);
    }

    @Override
    public void loginSuccess(Message message) {
        state.loginSuccess(message);
    }

    @Override
    public void logoutSuccess(Message message) {
        state.logoutSuccess(message);
    }

    @Override
    public String stateDescription() {
        return state.stateDescription();
    }
}
