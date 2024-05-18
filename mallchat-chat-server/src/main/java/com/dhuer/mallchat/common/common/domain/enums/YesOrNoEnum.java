package com.dhuer.mallchat.common.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/8
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    NO(0,"false"),
    YES(1,"true"),
    ;
    private final Integer status;
    private final String desc;
}
