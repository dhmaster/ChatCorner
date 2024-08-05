package com.dhuer.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申请状态枚举
 */
@AllArgsConstructor
@Getter
public enum ApplyStatusEnum {
    WAIT_APPROVAL(1, "待审批"),
    APPROVE(2, "同意");
    private final Integer code;
    private final String desc;
}
