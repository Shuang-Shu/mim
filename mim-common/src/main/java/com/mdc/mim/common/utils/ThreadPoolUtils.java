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
                ThreadUtils.CPU_COUNT >> 1,
                ThreadUtils.CPU_COUNT << 1,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(64),
                ThreadUtils.getThreadFactory("default-fixed"),
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
    }
}
