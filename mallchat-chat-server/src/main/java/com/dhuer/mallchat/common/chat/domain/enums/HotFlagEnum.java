package com.dhuer.mallchat.common.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 是否为热点枚举
 * Author: Jintao Li
 * Date: 2024/8/4
 */
@AllArgsConstructor
@Getter
public enum HotFlagEnum {
    NO(0,"非热点"),
    YES(1,"热点"),
    ;

    private final Integer type;
    private final String desc;

    public static Map<Integer, HotFlagEnum> cache;
    static {
        cache = Arrays.stream(HotFlagEnum.values()).collect(Collectors.toMap(HotFlagEnum::getType, Function.identity()));
    }

    public static HotFlagEnum of(Integer type) {
        return cache.get(type);
    }
}
