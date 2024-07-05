package com.mdc.mim.common.enumeration;

public enum ResponseCodeEnum {
    SUCCESS(200), FAILED(500);

    ResponseCodeEnum(int code) {
        this.code = code;
    }

    int code;

    public int getCode() {
        return code;
    }
}
