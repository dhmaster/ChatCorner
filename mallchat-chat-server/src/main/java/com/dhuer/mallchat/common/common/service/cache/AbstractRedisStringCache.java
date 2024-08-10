package com.dhuer.mallchat.common.common.service.cache;

import cn.hutool.core.collection.CollectionUtil;
import com.dhuer.mallchat.common.common.utils.RedisUtils;
import org.springframework.data.util.Pair;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: Redis String 类型的批量缓存框架
 * Author: Jintao Li
 * Date: 2024/8/9
 */
public abstract class AbstractRedisStringCache<IN, OUT> implements BatchCache<IN, OUT> {
    private Class<OUT> outClass;

    protected AbstractRedisStringCache() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        // [0] 是 IN，[1] 是 OUT
        this.outClass = (Class<OUT>) genericSuperclass.getActualTypeArguments()[1];
    }

    protected abstract String getKey(IN req);

    protected abstract Long getExpireSeconds();

    protected abstract Map<IN, OUT> load(List<IN> req);

    @Override
    public OUT get(IN req) {
        return getBatch(Collections.singletonList(req)).get(req);
    }

    @Override
    public Map<IN, OUT> getBatch(List<IN> req) {
        // 防御性编程
        if (CollectionUtil.isEmpty(req)) {
            return new HashMap<>();
        }
        // 去重
        req = req.stream().distinct().collect(Collectors.toList());
        // 组装 Key
        List<String> keys = req.stream().map(this::getKey).collect(Collectors.toList());
        // 批量 get 难点：获取 outClass，不能直接 OUT。class
        List<OUT> valueList = RedisUtils.mget(keys, outClass);
        // 差集计算 (Redis 中没有缓存的)
        List<IN> loadReqs = new ArrayList<>();
        for (int i=0; i<valueList.size(); i++) {
            // key 没有命中时，返回的 value 为 null
            if (Objects.isNull(valueList.get(i))) {
                loadReqs.add(req.get(i));
            }
        }
        Map<IN, OUT> load = new HashMap<>();
        // 没有缓存的重新加载进 Redis
        if (CollectionUtil.isNotEmpty(load)) {
            // 批量 load
            load = load(loadReqs);
            Map<String, OUT> loadMap = load.entrySet().stream()
                    .map(a -> Pair.of(getKey(a.getKey()), a.getValue()))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            RedisUtils.mset(loadMap, getExpireSeconds());
        }
        // 组装最后的结果
        Map<IN, OUT> resultMap = new HashMap<>();
        for (int i=0; i<req.size(); i++) {
            IN in = req.get(i);
            OUT out = Optional.ofNullable(valueList.get(i)).orElse(load.get(in));
            resultMap.put(in, out);
        }
        return resultMap;
    }

    @Override
    public void delete(IN req) {
        deleteBatch(Collections.singletonList(req));
    }

    @Override
    public void deleteBatch(List<IN> req) {
        List<String> keys = req.stream().map(this::getKey).collect(Collectors.toList());
        RedisUtils.del(keys);
    }
}
