package com.mdc.mim.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mdc.mim.common.dto.R;
import com.mdc.mim.user.service.UserEntityService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserLoginController {
    @Autowired
    private UserEntityService userEntityService;

    @PostMapping("/user/identify")
    public R identify(@RequestParam("userName") String userName, String passwdMd5) {
        if (passwdMd5 == null) {
            return R.error("passwdMd5 is null");
        }
        var user = userEntityService.queryByName(userName);

        if (user == null || !passwdMd5.equals(user.getPasswdMd5())) {
            return R.ok().put("valid", false);
        } else {
            return R.ok().put("valid", true);
        }
    }
}
