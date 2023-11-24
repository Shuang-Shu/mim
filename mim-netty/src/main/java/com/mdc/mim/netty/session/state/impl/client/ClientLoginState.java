package com.mdc.mim.netty.session.state.impl.client;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.netty.session.AbstractSession;
import com.mdc.mim.netty.session.state.AbstractClientSessionState;
import com.mdc.mim.netty.session.state.StateConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 16:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientLoginState extends AbstractClientSessionState {
    private UserDTO user;

    public ClientLoginState(AbstractSession session) {
        this.session = session;
    }

    @Override
    public void logOutSuccess(Message message) {
        var newState = new ClientNotLoginState(this.session);
        session.setState(newState);
    }

    @Override
    public void sendFail() {
        var newState = new ClientNotLoginState(this.session);
        session.setState(newState);
    }

    @Override
    public String stateDescription() {
        return StateConstant.LOGIN;
    }
}
