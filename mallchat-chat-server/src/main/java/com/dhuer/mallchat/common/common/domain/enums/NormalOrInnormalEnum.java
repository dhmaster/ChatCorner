package com.dhuer.mallchat.common.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 是否正常通用枚举
 * Author: Jintao Li
 * Date: 2024/5/8
 */
@AllArgsConstructor
@Getter
public enum NormalOrInnormalEnum {
    NORMAL(0,"正常"),
    IN_NORMAL(1,"不正常"),
    ;

    private final Integer status;
    private final String desc;

    public static Map<Integer, NormalOrInnormalEnum> cache;
    static {
        cache = Arrays.stream(NormalOrInnormalEnum.values()).collect(Collectors.toMap(NormalOrInnormalEnum::getStatus, Function.identity()));
    }

    public static NormalOrInnormalEnum of(Integer type) {
        return cache.get(type);
    }

    public static Integer toStatus(Boolean bool) {
        return bool ? NORMAL.getStatus() : IN_NORMAL.getStatus();
    }
}
