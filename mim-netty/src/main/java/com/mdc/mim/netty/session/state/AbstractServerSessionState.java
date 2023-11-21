package com.mdc.mim.netty.session.state;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.AbstractSession;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 15:25
 */
public abstract class AbstractServerSessionState implements ISessionState {
    protected AbstractSession session;

    @Override
    public void loginSuccess(Message message) {
        // do nothing
    }

    @Override
    public void logoutSuccess(Message message) {
        // do nothing
    }

}
