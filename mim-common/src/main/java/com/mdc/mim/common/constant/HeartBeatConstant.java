package com.mdc.mim.common.constant;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/20 22:55
 */
public class HeartBeatConstant {
    public static final int READ_IDLE_TIME = 8;
    public static final int WRITE_IDLE_TIME = 8;
    public static final int HEART_BEAT_TIME = READ_IDLE_TIME >>> 2;
}
