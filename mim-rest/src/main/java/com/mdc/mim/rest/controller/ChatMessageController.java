package com.mdc.mim.rest.controller;

import com.mdc.mim.common.dto.ChatMessageDTO;
import com.mdc.mim.common.dto.R;
import com.mdc.mim.rest.entity.ChatMessageEntity;
import com.mdc.mim.rest.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 12:59
 */
@Slf4j
@RestController
public class ChatMessageController {
    @Autowired
    private ChatMessageService chatMessageService;

    @Transactional
    @PostMapping("/message/save")
    public R saveMessage(@RequestBody List<ChatMessageDTO> messageDTOs) {
        try {
            log.info("rest service saving chatMessages: len={}", messageDTOs.size());
            chatMessageService.insertChatMessages(messageDTOs.stream().map(t -> ChatMessageEntity.buildWith(t)).toList());
            return R.ok();
        } catch (Exception e) {
            return R.error().put("error", e.getMessage());
        }
    }

    @PostMapping("/message/count")
    public R countUnreadMessage() {
        try {
            log.info("rest service counting unread messages");
            var count = chatMessageService.findAll().size();
            return R.ok().put("count", count);
        } catch (Exception e) {
            return R.error().put("error", e.getMessage());
        }
    }
}
