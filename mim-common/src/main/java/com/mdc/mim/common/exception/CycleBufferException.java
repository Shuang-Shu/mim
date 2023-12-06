package com.mdc.mim.common.exception;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/12/6 20:38
 */
public class CycleBufferException extends RuntimeException {
    public CycleBufferException(String message) {
        super(message);
    }
}
