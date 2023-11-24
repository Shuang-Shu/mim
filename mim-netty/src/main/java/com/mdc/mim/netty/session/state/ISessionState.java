package com.mdc.mim.netty.session.state;

import com.mdc.mim.common.dto.Message;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 11:41
 */
public interface ISessionState {
    void logInSuccess(Message message);

    void logOutSuccess(Message message);

    String stateDescription();
}
