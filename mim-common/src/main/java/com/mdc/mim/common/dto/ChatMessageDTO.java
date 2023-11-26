package com.mdc.mim.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 12:47
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    Long id; // 消息id，用于在客户端和服务端之间同步消息，单调递增
    Long fromUid;
    Long toUid;
    Integer type;
    String content;
    Date createTime;
}
