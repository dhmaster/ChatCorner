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
public class FriendApplyUnreadResp {

    @ApiModelProperty("好友申请未读数")
    private Integer unreadCount;
}
