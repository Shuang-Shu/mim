package com.mdc.mim.common.utils;

import java.util.concurrent.*;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/14 20:42
 */
public class ThreadPoolUtils {
    public static ExecutorService getDefaultFixedThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                // TODO 此处使用无界队列，需要考虑OOM问题
                ThreadUtils.CPU_COUNT >> 1,
                ThreadUtils.CPU_COUNT << 1,
                120L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                ThreadUtils.getThreadFactory("default-fixed"),
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
    }
}
