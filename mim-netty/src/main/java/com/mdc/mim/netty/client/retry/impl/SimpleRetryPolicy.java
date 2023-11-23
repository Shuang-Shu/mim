package com.mdc.mim.netty.client.retry.impl;

import com.mdc.mim.netty.client.retry.IRetryPolicy;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 简单的重试策略，最多重试N次，每次重试间隔固定时间
 * @date 2023/11/23 10:43
 */
public class SimpleRetryPolicy implements IRetryPolicy {
    private final int maxRetryCount;
    private final long sleepTimeMs;
    private int currentRetryCount;

    public SimpleRetryPolicy(int maxRetryCount, long sleepTimeMs) {
        this.maxRetryCount = maxRetryCount;
        this.sleepTimeMs = sleepTimeMs;
        this.currentRetryCount = 0;
    }

    @Override
    public void resetCount() {
        currentRetryCount = 0;
    }

    @Override
    public int getCurrentRetryCount() {
        return 0;
    }

    @Override
    public boolean allowRetry() {
        return currentRetryCount++ < maxRetryCount;
    }

    @Override
    public long getSleepTimeMs() {
        return sleepTimeMs;
    }
}
