package com.mdc.mim.client.test.controller;

import com.mdc.mim.common.dto.R;
import com.mdc.mim.netty.client.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/12/2 23:29
 */
@RestController
public class MimTestRestController {
    @Autowired
    NettyClient nettyClient;

    @PostMapping("/mim/client/test/send/{toUid}")
    public R send(@RequestBody String content, @PathVariable("toUid") Long toUid) {
        nettyClient.doSend(toUid, content);
        return R.ok();
    }

    @GetMapping("/mim/client/test/message/{fromUid}")
    public R getMessage(@PathParam("fromUid") Long fromUid) {
        var chatMessages = nettyClient.getChatMessage(fromUid);
        return R.ok().put("chatMessages", chatMessages);
    }
}
