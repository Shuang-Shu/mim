package com.mdc.mim.common.dto;

import com.mdc.mim.common.constant.CommonConstant;
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

    // 登入
    LoginRequest loginRequest;
    LoginResponse loginResponse;
    // 登出
    LogoutRequest logoutRequest;
    LogoutResponse logoutResponse;
    // keep-alive
    KeepAliveRequest keepAliveRequest;
    KeepAliveResponse keepAliveResponse;
    // 聊天
    MessageRequest messageRequest;
    MessageResponse messageResponse;
    // 通知
    MessageNotification messageNotification;

    public enum ChatMessageType {
        TEXT, COMPLEX
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        public static LoginRequest buildWith(UserDTO user, long messageId) {
            return LoginRequest.builder().user(user).id(messageId).appVersion(CommonConstant.APP_VERSION).build();
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
    public static class LoginResponse {
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
    public static class LogoutRequest {
        public static LogoutRequest buildWith(UserDTO user, long messageId) {
            return LogoutRequest.builder().user(user).id(messageId).build();
        }

        UserDTO user;
        long id;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogoutResponse {
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

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageRequest {
        long id;
        Long from;
        Long to;
        long time;
        Message.ChatMessageType messageType;
        String content;
        String url;
        String property;
        String fromNick;
        String json;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        public static MessageResponse buildWith(MessageRequest messageRequest) {
            // 使用所有字段build
            return MessageResponse.builder().id(messageRequest.getId()).from(messageRequest.getFrom())
                    .to(messageRequest.getTo()).time(messageRequest.getTime())
                    .messageType(messageRequest.getMessageType()).content(messageRequest.getContent())
                    .url(messageRequest.getUrl()).property(messageRequest.getProperty())
                    .fromNick(messageRequest.getFromNick()).json(messageRequest.getJson()).build();
        }

        long id;
        Long from;
        Long to;
        long time;
        Message.ChatMessageType messageType;
        String content;
        String url;
        String property;
        String fromNick;
        String json;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageNotification {
        long id;
        int messagType;
        boolean sender;
        String json;
        long time;
    }
}
