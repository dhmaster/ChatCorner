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
public enum UserActiveStatusEnum {
    ONLINE(1, "在线"),
    OFFLINE(2, "离线");
    private final Integer status;
    private final String desc;
}
