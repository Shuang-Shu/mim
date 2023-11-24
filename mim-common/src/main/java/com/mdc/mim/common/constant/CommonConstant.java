package com.mdc.mim.common.constant;

import java.util.function.Supplier;

import com.esotericsoftware.kryo.Kryo;
import com.mdc.mim.common.dto.Message;
import com.mdc.mim.common.dto.Message.KeepAliveRequest;
import com.mdc.mim.common.dto.Message.KeepAliveResponse;
import com.mdc.mim.common.dto.Message.LogInResponse;
import com.mdc.mim.common.dto.Message.LogOutRequest;
import com.mdc.mim.common.dto.Message.LogOutResponse;
import com.mdc.mim.common.dto.Message.MessageNotification;
import com.mdc.mim.common.dto.Message.MessageRequest;
import com.mdc.mim.common.dto.Message.MessageResponse;
import com.mdc.mim.common.dto.UserDTO;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.enumeration.PlatformEnum;
import com.mdc.mim.common.enumeration.ResponsesCodeEnum;
import com.mdc.mim.common.utils.ClassIdUtils;

public class CommonConstant {
    public static final int MAX_FRAME_LENGTH = 1 << 15;
    public static final int MAGIC_NUMBER = 0x66AF;
    public static final int APP_VERSION = 1;
    public static final int CONTENT_LENGTH = 4;

    private static final Class<?>[] messageClasses = {
            String.class, Message.class, Message.ChatMessageType.class, MessageTypeEnum.class, ResponsesCodeEnum.class, UserDTO.class,
            Message.LogInRequest.class, LogInResponse.class, LogOutRequest.class,
            LogOutResponse.class,
            KeepAliveRequest.class, KeepAliveResponse.class,
            MessageRequest.class, MessageResponse.class, MessageNotification.class, PlatformEnum.class,
            ResponsesCodeEnum.class
    };

    public static final Supplier<Kryo> supplier = () -> {
        var kryo = new Kryo();
        // kryo.register(Message.class);
        for (var clazz : messageClasses) {
            kryo.register(clazz, ClassIdUtils.generateClassId(clazz, CommonConstant.APP_VERSION));
        }
        return kryo;
    };
}
