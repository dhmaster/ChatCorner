package com.dhuer.mallchat.common.user.domain.vo.resp.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:好友检验
 * Author: Jintao Li
 * Date: 2024/5/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendCheckResp {
    @ApiModelProperty("检验结果")
    private List<FriendCheck> checkList;

    @Data
    public static class FriendCheck {
        private Long uid;
        private Boolean isFriend;
    }
}
