package com.mdc.mim.rest.entity;

import com.mdc.mim.common.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author ShuangShus
 * @version 1.0
 * @description: 用于储存离线消息的实体类
 * @date 2023/11/20 17:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEntity implements Serializable {
    public static final int MAX_CONTENT_LENGTH = 512;

    public static ChatMessageEntity buildWith(ChatMessageDTO chatMessageDTO) {
        return ChatMessageEntity.builder()
                .id(chatMessageDTO.getId())
                .fromUid(chatMessageDTO.getFromUid())
                .toUid(chatMessageDTO.getToUid())
                .type(chatMessageDTO.getType())
                .content(chatMessageDTO.getContent())
                .createTime(chatMessageDTO.getCreateTime())
                .build();
    }

    Long id;
    Long fromUid;
    Long toUid;
    Integer type;
    String content;
    Date createTime;
}
