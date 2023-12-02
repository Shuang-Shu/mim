package com.mdc.mim.netty.session.state;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.AbstractSession;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 16:15
 */
public abstract class AbstractClientSessionState implements IClientSessionState {
    protected AbstractSession session;

    @Override
    public void sendFail() {
        // do nothing
    }

    @Override
    public void connectSuccess() {
        // do nothing
    }

    @Override
    public void logInSuccess(Message message) {
        // do nothing
    }

    @Override
    public void logOutSuccess(Message message) {
        // do nothing
    }
}
