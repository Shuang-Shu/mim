package com.mdc.mim.netty.session.state;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 16:12
 */
public interface IClientSessionState extends ISessionState {
    void sendFail();

    void connectSuccess();
}
