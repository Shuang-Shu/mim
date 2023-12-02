package com.mdc.mim.netty.session.state.impl.server;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.AbstractSession;
import com.mdc.mim.netty.session.state.AbstractServerSessionState;
import com.mdc.mim.netty.session.state.StateConstant;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 16:19
 */
public class ServerNotLoginState extends AbstractServerSessionState {
    public ServerNotLoginState(AbstractSession session) {
        this.session = session;
    }

    @Override
    public void logInSuccess(Message message) {
        var newState = new ServerLoginState(this.session);
        newState.setUser(message.getLogInRequest().getUser());
        this.session.setState(newState);
    }

    @Override
    public String stateDescription() {
        return StateConstant.NOT_LOGIN;
    }
}
