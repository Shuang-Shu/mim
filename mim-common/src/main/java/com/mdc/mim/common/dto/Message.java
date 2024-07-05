package com.mdc.mim.common.dto;

import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.enumeration.ResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    Long id;
    MessageTypeEnum messageType; // 消息类型
    String sessionId; // 请求中携带的sessionId
    String info; // 响应中携带的信息

    // 登入
    LogInRequest logInRequest;
    LogInResponse logInResponse;
    // 登出
    LogOutRequest logOutRequest;
    LogOutResponse logOutResponse;
    // 聊天
    MessageRequest messageRequest;
    MessageResponse messageResponse;
    // 通知
    MessageNotify messageNotify;

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogInRequest {
        UserDTO user;
        Integer appVersion;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogInResponse {
        ResponseCodeEnum code;
        UserDTO user;
        String info;
        String sessionId;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogOutRequest {
        UserDTO user;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogOutResponse {
        String info;
        ResponseCodeEnum code;
    }

    // 发送方-->服务器的聊天消息请求
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageRequest {
        ChatMessageDTO chatMessage;
    }

    // 服务器-->发送方的聊天消息发送，用于告知发送方消息已经成功发送到服务器；只包含用于确认的id
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        long id; // 仅包含请求id
    }

    // 服务器-->接收方的聊天消息通知，用于告知接收方有新的消息；包含消息内容
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageNotify {
        long id;
        ChatMessageDTO chatMessageDTO;
    }
}
