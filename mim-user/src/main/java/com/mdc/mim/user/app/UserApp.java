package com.mdc.mim.user.app;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mdc.mim.thrift.dto.R;
import com.mdc.mim.user.FindUidReq;
import com.mdc.mim.user.FindUidResp;
import com.mdc.mim.user.IdentifyReq;
import com.mdc.mim.user.IdentifyResp;
import com.mdc.mim.user.domain.User;
import com.mdc.mim.user.service.UserService;

@Component
public class UserApp {
    @Autowired
    UserService userService;

    public IdentifyResp Identify(IdentifyReq req) throws TException {
        var result = new IdentifyResp();
        var username = req.getUserName();
        var passwd = req.getPasswdMd5();
        if (username == null || passwd == null) {
            return result.setBaseResp(R.error("invalid req"));
        }
        User user = userService.findByName(username);
        if (user == null || !passwd.equals(user.getPasswdMd5())) {
            return result.setBaseResp(R.error("invalid user"));
        } else {
            return result.setBaseResp(R.ok("login success"));
        }
    }

    public FindUidResp FindUidByUserName(FindUidReq req) throws TException {
        var result = new FindUidResp();
        User user = userService.findByName(req.getUserName());
        if (user == null) {
            return result.setBaseResp(R.error("user not found"));
        } else {
            return result.setBaseResp(R.ok()).setUid(user.getUid());
        }
    }
}
