package com.mdc.mim.rest.controller;

import com.mdc.mim.common.dto.ChatMessageDTO;
import com.mdc.mim.common.dto.R;
import com.mdc.mim.rest.entity.ChatMessageEntity;
import com.mdc.mim.rest.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 12:59
 */
@RestController
public class ChatMessageController {
    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/message/save")
    public R saveMessage(@RequestBody ChatMessageDTO messageDTO) {
        try {
            chatMessageService.insertChatMessage(ChatMessageEntity.buildWith(messageDTO));
            return R.ok();
        } catch (Exception e) {
            return R.error().put("error", e.getMessage());
        }
    }
}
