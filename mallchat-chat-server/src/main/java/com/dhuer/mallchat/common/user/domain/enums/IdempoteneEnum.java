package com.dhuer.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 发放的幂等类型
 * Author: Jintao Li
 * Date: 2024/5/18
 */
@AllArgsConstructor
@Getter
public enum IdempoteneEnum {
    UID(1, "uid"),
    MSG_ID(2, "消息 id");
    private final Integer type;
    private final String desc;
}
