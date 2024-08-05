package com.dhuer.mallchat.common.user.domain.vo.req.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 申请好友信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendApproveReq {

    @NotNull
    @ApiModelProperty("申请 Id")
    private Long applyId;
}
