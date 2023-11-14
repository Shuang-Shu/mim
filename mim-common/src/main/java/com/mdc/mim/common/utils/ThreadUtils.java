package com.mdc.mim.common.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/14 21:01
 */
public class ThreadUtils {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 定制的线程工厂
     */
    private static class CustomThreadFactory implements ThreadFactory {
        //线程池数量
        public static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        //线程数量
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String threadTag;

        CustomThreadFactory(String threadTag) {
            // 获取线程group
            group = Thread.currentThread().getThreadGroup();
            this.threadTag = "apppool-" + poolNumber.getAndIncrement() + "-" + threadTag + "-";
        }

        @Override
        public Thread newThread(Runnable target) {
            Thread t = new Thread(group, target,
                    threadTag + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public static ThreadFactory getThreadFactory(String threadTag) {
        return new CustomThreadFactory(threadTag);
    }
}
