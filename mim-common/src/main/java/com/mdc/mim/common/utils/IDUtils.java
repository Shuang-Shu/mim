package com.mdc.mim.common.utils;

import java.util.UUID;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 21:25
 */
public class IDUtils {
    public static String getSessionId() {
        return UUID.randomUUID().toString();
    }
}
