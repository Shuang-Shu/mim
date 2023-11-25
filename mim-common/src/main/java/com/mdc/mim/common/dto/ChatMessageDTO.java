package com.mdc.mim.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 12:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    Long id;
    Long fromUid;
    Long toUid;
    Integer type;
    String content;
    Date createTime;
}
