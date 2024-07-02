package com.mdc.mim.common.concurrent;

import com.google.common.util.concurrent.*;
import com.mdc.mim.common.utils.ThreadPoolUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.*;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/14 20:38
 */
public class CallbackExecutor {
    private CallbackExecutor() {
    }

    private ExecutorService delegate = ThreadPoolUtils.getDefaultFixedThreadPoolExecutor();
    private ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);

    private static final CallbackExecutor executor = new CallbackExecutor();

    public static CallbackExecutor instance() {
        return executor;
    }

    public <R> void execute(CallbackTask<R> task) {
        ListenableFuture<R> f = (ListenableFuture<R>) executorService.submit(task);
        Futures.addCallback(f, new FutureCallback<R>() {
            @Override
            public void onSuccess(@Nullable R result) {
                task.onBack(result);
            }

            @Override
            public void onFailure(Throwable t) {
                task.onException(t);
            }
        }, executorService);
    }

    public int activeCount() {
        return ((ThreadPoolExecutor) delegate).getActiveCount();
    }
}
