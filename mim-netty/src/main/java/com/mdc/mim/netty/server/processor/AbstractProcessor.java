package com.mdc.mim.netty.server.processor;

import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.netty.session.ServerSession;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/15 0:23
 */
public interface AbstractProcessor {
    MessageTypeEnum supportType();

    Boolean process(ServerSession serverSession, Message message);
}
