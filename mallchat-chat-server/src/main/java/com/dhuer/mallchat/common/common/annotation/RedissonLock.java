package com.dhuer.mallchat.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Description: 注解式分布式锁
 * Author: Jintao Li
 * Date: 2024/5/18
 */
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
@Target(ElementType.METHOD)         // 作用在方法上
public @interface RedissonLock {
    /**
     * key 的前缀，默认读取方法全限定名，可以自己指定
     */
    String prefixKey() default "";

    /**
     * 支持 springEl 表达式的 key
     */
    String key();

    /**
     * 等待锁的排队时间，默认快速失败
     */
    int waitTime() default -1;

    /**
     * 时间单位，默认毫秒
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
