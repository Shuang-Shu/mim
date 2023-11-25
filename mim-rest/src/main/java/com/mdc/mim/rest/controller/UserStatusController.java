package com.mdc.mim.rest.controller;

import com.mdc.mim.rest.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/20 17:03
 */
@RestController
public class UserStatusController {
    @Autowired
    UserStatusService userStatusService;

    @PostMapping("/user/status/change-status")
    public void changeStatus(Long uid, Integer status) {
        switch (status) {
            case 0:
                userStatusService.online(uid);
                break;
            case 1:
                userStatusService.offline(uid);
                break;
            case 2:
                userStatusService.invisible(uid);
                break;
            default:
                throw new RuntimeException("status error");
        }
    }
}
