package com.mdc.mim.user.controller;

import com.mdc.mim.common.dto.R;
import com.mdc.mim.user.service.FriendEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: FriendController
 * @date 2023/11/21 22:17
 */
@RestController
public class FriendController {
    @Autowired
    FriendEntityService friendEntityService;

    @PostMapping("/user/friend/is-friend")
    public R isFriend(@RequestParam("uid") Long uid, @RequestParam("friendUid") Long friendUid) {
        var friendEntity = friendEntityService.findByUidAndFriendUid(uid, friendUid);
        if (friendEntity == null) {
            return R.ok().put("isFriend", false);
        } else {
            return R.ok().put("isFriend", true);
        }
    }
}
