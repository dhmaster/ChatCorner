package com.dhuer.mallchat.common.common.thread;

import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/21
 */
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {
    private static final MyUncaughtExceptionHandler MY_UNCAUGHT_EXCEPTION_HANDLER = new MyUncaughtExceptionHandler();
    private ThreadFactory original;
    @Override
    public Thread newThread(Runnable r) {
        // 执行 spring 线程自己的创建逻辑
        Thread thread = original.newThread(r);
        // 额外装饰我们需要的的创建逻辑
        thread.setUncaughtExceptionHandler(MY_UNCAUGHT_EXCEPTION_HANDLER);
        return thread;
    }
}
