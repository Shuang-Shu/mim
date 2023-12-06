package com.mdc.mim.common.enumeration;


public enum MessageTypeEnum {
    LOGIN_REQ(0), LOGIN_RESP(1), LOGOUT_REQ(2), LOGOUT_RESP(3), HEARTBEAT_REQ(4), MESSAGE_REQ(6),
    MESSAGE_RESP(7), MESSAGE_NOTIFY(8), ERR_RESP(9);

    private int val;

    MessageTypeEnum(int val) {
        this.val = val;
    }

    public int value() {
        return val;
    }
}
