package com.dhuer.mallchat.common.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 消息类型
 * Author: Jintao Li
 * Date: 2024/8/4
 */
@AllArgsConstructor
@Getter
public enum RoomTypeEnum {
    GROUP(1,"群聊"),
    FRIEND(2,"单聊"),
    ;

    private final Integer type;
    private final String desc;

    public static Map<Integer, RoomTypeEnum> cache;
    static {
        cache = Arrays.stream(RoomTypeEnum.values()).collect(Collectors.toMap(RoomTypeEnum::getType, Function.identity()));
    }

    public static RoomTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
