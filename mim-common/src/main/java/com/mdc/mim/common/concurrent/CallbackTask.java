package com.mdc.mim.common.concurrent;

import java.util.concurrent.Callable;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/14 20:36
 */
public interface CallbackTask<R> extends Callable<R> {
    void onBack(R r);

    void onException(Throwable t);
}
