package com.mdc.mim.common;

import com.mdc.mim.common.concurrent.CallbackExecutor;
import com.mdc.mim.common.concurrent.CallbackTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/14 20:55
 */
public class ConcurrentTest {
    private static final String OK_TARGET = "ok_target";
    private static final String ERR_TARGET = "err_target";
    private String test = "";
    private boolean err = false;

    CallbackTask<String> demoTask = new CallbackTask<String>() {
        @Override
        public String call() throws Exception {
            if (err) {
                var a = 1 / 0;
            }
            return OK_TARGET;
        }

        @Override
        public void onBack(String r) {
            System.out.println("result=" + r);
            test = OK_TARGET;
        }

        @Override
        public void onException(Throwable t) {
            System.out.println("err happened");
            test = ERR_TARGET;
        }
    };

    @Test
    public void testCallbackExecutor() throws InterruptedException {
        var executor = CallbackExecutor.instance();
        executor.execute(demoTask);
        Thread.sleep(500);
        Assertions.assertEquals(OK_TARGET, test);
        err = true;
        executor.execute(demoTask);
        Thread.sleep(500);
        Assertions.assertEquals(ERR_TARGET, test);
    }
}
