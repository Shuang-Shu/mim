package com.mdc.mim.rest.service.impl;

import com.mdc.mim.rest.entity.ChatMessageEntity;
import com.mdc.mim.rest.mapper.ChatMessageMapper;
import com.mdc.mim.rest.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 12:21
 */
@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    ChatMessageMapper chatMessageMapper;

    // 保存消息
    @Override
    @CachePut(value = "chatMessage", key = "#chatMessageEntity.id")
    public int insertChatMessage(ChatMessageEntity chatMessageEntity) {
        return chatMessageMapper.insertChatMessage(chatMessageEntity);
    }

    @Override
    @Cacheable(value = "chatMessage", key = "#id")
    public ChatMessageEntity findById(Long id) {
        return chatMessageMapper.findById(id);
    }

    @Override
    public List<ChatMessageEntity> findByFromUidAndToUid(Long fromUid, Long toUid) {
        return chatMessageMapper.findByFromUidAndToUid(fromUid, toUid);
    }

    @Override
    public int deleteById(Long id) {
        return chatMessageMapper.deleteById(id);
    }
}
