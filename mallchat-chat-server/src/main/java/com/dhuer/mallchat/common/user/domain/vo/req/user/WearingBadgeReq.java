package com.dhuer.mallchat.common.user.domain.vo.req.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/8
 */
@Data
public class WearingBadgeReq {
    @ApiModelProperty("徽章 id")
    @NotNull
    private Long itemId;
}
