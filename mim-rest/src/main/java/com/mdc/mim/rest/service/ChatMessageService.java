package com.mdc.mim.rest.service;

import com.mdc.mim.rest.entity.ChatMessageEntity;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 12:21
 */
public interface ChatMessageService {
    // 保存消息
    int insertChatMessage(ChatMessageEntity chatMessageEntity);

    ChatMessageEntity findById(Long id);

    List<ChatMessageEntity> findByFromUidAndToUid(Long fromUid, Long toUid);

    // 删除消息
    int deleteById(Long id);
}
