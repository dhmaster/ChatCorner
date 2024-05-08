package com.dhuer.mallchat.common.common.utils;

import com.dhuer.mallchat.common.common.domain.dto.RequestInfo;

/**
 * Description: 请求上下文
 * Author: Jintao Li
 * Date: 2024/4/28
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<RequestInfo>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
