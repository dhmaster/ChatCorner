package com.dhuer.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 好友状态类型
 * Author: Jintao Li
 * Date: 2024/5/18
 */
@AllArgsConstructor
@Getter
public enum ChatActiveStatusEnum {
    ONLINE(1, "在线"),
    OFFLINE(2, "离线"),
    ;

    private final Integer status;
    private final String desc;

    private static Map<Integer, ChatActiveStatusEnum> cache;

    // 键（Key）：ChatActiveStatusEnum枚举常量的状态码，由ChatActiveStatusEnum::getStatus提供。
    // 值（Value）：ChatActiveStatusEnum枚举常量本身，由Function.identity()提供。
    static {
        cache = Arrays.stream(ChatActiveStatusEnum.values())
                .collect(Collectors.toMap(ChatActiveStatusEnum::getStatus, Function.identity()));
    }

    public static ChatActiveStatusEnum of(Integer type) {
        return cache.get(type);
    }
}
