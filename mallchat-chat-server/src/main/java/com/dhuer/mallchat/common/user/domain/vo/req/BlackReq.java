package com.dhuer.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/6/1
 */
@Data
public class BlackReq {
    @ApiModelProperty("拉黑用户的 uid")
    @NotNull
    private Long uid;
}
