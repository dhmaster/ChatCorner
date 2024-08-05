package com.dhuer.mallchat.common.user.domain.vo.resp.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplyResp {

    @ApiModelProperty("申请 id")
    private Long applyId;

    @ApiModelProperty("申请人 uid")
    private Long uid;

    @ApiModelProperty("申请类型  1：加好友")
    private Integer type;

    @ApiModelProperty("申请信息")
    private String msg;

    @ApiModelProperty("申请状态  1：待审批 2：同意")
    private Integer status;
}
