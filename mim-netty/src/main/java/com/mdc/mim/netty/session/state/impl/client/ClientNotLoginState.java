package com.mdc.mim.netty.session.state.impl.client;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.AbstractSession;
import com.mdc.mim.netty.session.state.AbstractClientSessionState;
import com.mdc.mim.netty.session.state.StateConstant;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 16:23
 */
public class ClientNotLoginState extends AbstractClientSessionState {
    public ClientNotLoginState(AbstractSession session) {
        this.session = session;
    }

    @Override
    public void sendFail() {
        var newState = new ClientNotLoginState(this.session);
        session.setState(newState);
    }

    @Override
    public void loginSuccess(Message message) {
        var newState = new ClientLoginState(this.session);
        newState.setUser(message.getLoginResponse().getUser());
        session.setState(newState);
    }

    @Override
    public String stateDescription() {
        return StateConstant.NOT_LOGIN;
    }
}
