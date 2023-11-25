package com.mdc.mim.common.dto;

import com.mdc.mim.common.constant.CommonConst;
import com.mdc.mim.common.enumeration.MessageTypeEnum;
import com.mdc.mim.common.enumeration.ResponsesCodeEnum;
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
    MessageTypeEnum messageType; // 消息类型
    String sessionId; // 请求中携带的sessionId
    String info; // 响应中携带的信息

    // 登入
    LogInRequest logInRequest;
    LogInResponse logInResponse;
    // 登出
    LogOutRequest logOutRequest;
    LogOutResponse logOutResponse;
    // keep-alive
    KeepAliveRequest keepAliveRequest;
    KeepAliveResponse keepAliveResponse;
    // 聊天
    MessageRequest messageRequest;
    MessageResponse messageResponse;
    // 通知
    MessageNotification messageNotification;

//    public enum ChatMessageType {
//        TEXT, COMPLEX
//    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogInRequest {
        public static LogInRequest buildWith(UserDTO user, long messageId) {
            return LogInRequest.builder().user(user).id(messageId).appVersion(CommonConst.APP_VERSION).build();
        }

        UserDTO user;
        long id;
        int appVersion;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogInResponse {
        long id;
        ResponsesCodeEnum code;
        UserDTO user;
        String info;
        String sessionId;
        int expose; // 存疑
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogOutRequest {
        public static LogOutRequest buildWith(UserDTO user, long messageId) {
            return LogOutRequest.builder().user(user).id(messageId).build();
        }

        UserDTO user;
        long id;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogOutResponse {
        long id;
        String info;
        ResponsesCodeEnum code;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeepAliveRequest {
        long id;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeepAliveResponse {
        long id;
    }

    // 发送方-->服务器的聊天消息请求
    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageRequest {
        long id; // 请求的id
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
    public static class MessageNotification {
        long id;
        ChatMessageDTO chatMessageDTO;
    }
}
