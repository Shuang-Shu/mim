package com.mdc.mim.netty.session.state.impl.server;

import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.netty.session.AbstractSession;
import com.mdc.mim.netty.session.state.AbstractServerSessionState;
import com.mdc.mim.netty.session.state.StateConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 16:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerLoginState extends AbstractServerSessionState {
    private UserDTO user;

    public ServerLoginState(AbstractSession session) {
        this.session = session;
    }

    @Override
    public void logOutSuccess(Message message) {
        var newState = new ServerNotLoginState(this.session);
        this.session.setState(newState);
    }

    @Override
    public String stateDescription() {
        return StateConstant.LOGIN;
    }
}
