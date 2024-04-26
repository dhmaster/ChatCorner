package com.dhuer.mallchat.common.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/21
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread:", e);
    }
}
