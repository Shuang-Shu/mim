package com.mdc.mim.netty.feign;

import com.mdc.mim.common.dto.ChatMessageDTO;
import com.mdc.mim.common.dto.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 12:57
 */
@FeignClient(value = "mim-rest", contextId = "chat-message-service")
public interface ChatFeignMessageService {
    @PostMapping("/message/save")
    public R saveMessage(@RequestBody ChatMessageDTO messageDTO);
}
