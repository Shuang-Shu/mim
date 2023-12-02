package com.mdc.mim.netty.session.state.impl.client;

import com.mdc.mim.netty.session.AbstractSession;
import com.mdc.mim.netty.session.state.AbstractClientSessionState;
import com.mdc.mim.netty.session.state.StateConstant;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 16:23
 */
public class ClientNotConnectState extends AbstractClientSessionState {
    public ClientNotConnectState(AbstractSession session) {
        this.session = session;
    }

    @Override
    public void connectSuccess() {
        var newState = new ClientNotLoginState(this.session);
        session.setState(newState);
    }

    @Override
    public String stateDescription() {
        return StateConstant.NOT_CONNECT;
    }
}
