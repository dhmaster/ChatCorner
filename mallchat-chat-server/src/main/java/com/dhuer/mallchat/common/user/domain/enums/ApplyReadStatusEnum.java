package com.dhuer.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申请状态枚举
 */
@AllArgsConstructor
@Getter
public enum ApplyReadStatusEnum {
    UNREAD(1, "未读"),
    READ(2, "已读");
    private final Integer code;
    private final String desc;
}
