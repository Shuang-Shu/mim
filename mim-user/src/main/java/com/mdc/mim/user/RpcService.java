package com.mdc.mim.user;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mdc.mim.thrift.CommonRpcService;
import com.mdc.mim.user.UnifyService.Iface;
import com.mdc.mim.user.app.UserApp;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class RpcService extends CommonRpcService implements Iface {
    @Autowired
    UserApp userApp;

    public RpcService(int port) {
        super(port);
    }

    @Override
    protected TProcessor getProcessor() {
        return new UnifyService.Processor<Iface>(this);
    }

    @Override
    public IdentifyResp Identify(IdentifyReq req) throws TException {
        return userApp.Identify(req);
    }

    @Override
    public FindUidResp FindUidByUserName(FindUidReq req) throws TException {
        return userApp.FindUidByUserName(req);
    }
}