package com.mdc.mim.thrift.dto;

import java.util.Map;

import com.mdc.mim.base.BaseResp;
import com.mdc.mim.common.enumeration.ResponseCodeEnum;

public class R {
    public static BaseResp ok() {
        return new BaseResp("", ResponseCodeEnum.SUCCESS.getCode());
    }

    public static BaseResp ok(String msg) {
        return new BaseResp(msg, ResponseCodeEnum.SUCCESS.getCode());
    }

    public static BaseResp ok(Map<String, String> extra, String msg, int code) {
        var result = new BaseResp(msg, code);
        result.Extra = extra;
        return result;
    }

    public static BaseResp error(String msg) {
        return new BaseResp(msg, ResponseCodeEnum.FAILED.getCode());
    }

    public static BaseResp error(int code, String msg) {
        return new BaseResp(msg, code);
    }

    public static BaseResp error(Map<String, String> extra, String msg, int code) {
        var result = new BaseResp(msg, code);
        result.Extra = extra;
        return result;
    }
}
