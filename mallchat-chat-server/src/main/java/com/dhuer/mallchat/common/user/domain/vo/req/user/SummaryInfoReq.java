package com.dhuer.mallchat.common.user.domain.vo.req.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Description: 批量查询用户汇总详情
 * Author: Jintao Li
 * Date: 2024/8/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryInfoReq {
    @ApiModelProperty("用户信息入参")
    @Size(max = 50)
    private List<infoReq> reqList;

    @Data
    public static class infoReq {
        @ApiModelProperty("uid")
        private Long uid;

        @ApiModelProperty("最近一次更新用户信息时间")
        private Long lastModifyTime;
    }
}
