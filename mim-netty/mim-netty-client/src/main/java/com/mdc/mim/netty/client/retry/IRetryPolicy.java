package com.mdc.mim.netty.client.retry;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/23 10:43
 */
public interface IRetryPolicy {
    void resetCount();

    int getCurrentRetryCount();

    boolean allowRetry();

    long getSleepTimeMs();
}
