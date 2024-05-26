package com.dhuer.mallchat.common.common.aspect;

import com.dhuer.mallchat.common.common.annotation.RedissonLock;
import com.dhuer.mallchat.common.common.service.LockService;
import com.dhuer.mallchat.common.common.utils.SpElUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Description: 注解式分布式锁的 aspect
 * Author: Jintao Li
 * Date: 2024/5/18
 */
@Component
@Aspect
@Order(0)  // 确保比事务注解先执行，分布式锁在事务外，如果锁在事务内，事务未提交前，读取数据全是脏读
public class RedissonLockAspect {
    @Autowired
    private LockService lockService;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String prefix = StrUtil.isBlank(redissonLock.prefixKey()) ? SpElUtils.getMethodKey(method) : redissonLock.prefixKey();
        String key = SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        return lockService.executeWithLock(prefix + ":" + key, redissonLock.waitTime(), redissonLock.unit(), joinPoint::proceed);
    }
}
