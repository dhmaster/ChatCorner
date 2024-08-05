package com.dhuer.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申请类型枚举
 */
@AllArgsConstructor
@Getter
public enum ApplyTypeEnum {
    ADD_FRIEND(1, "加好友");
    private final Integer code;
    private final String desc;
}
