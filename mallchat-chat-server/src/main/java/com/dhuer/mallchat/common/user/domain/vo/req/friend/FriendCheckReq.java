package com.dhuer.mallchat.common.user.domain.vo.req.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 检查是否为好友
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendCheckReq {

    @NotEmpty
    @Size(max = 50)
    @ApiModelProperty("检验的好友 uid")
    private List<Long> uidList;
}
