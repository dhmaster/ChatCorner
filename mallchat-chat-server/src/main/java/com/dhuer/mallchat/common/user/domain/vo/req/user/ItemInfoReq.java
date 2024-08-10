package com.dhuer.mallchat.common.user.domain.vo.req.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:批量查询徽章详情
 * Author: Jintao Li
 * Date: 2024/8/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoReq {
    @ApiModelProperty("徽章信息入参")
    private List<infoReq> reqList;

    @Data
    public static class infoReq {
        @ApiModelProperty("徽章 id")
        private Long itemId;

        @ApiModelProperty("最近一次更新徽章信息的时间")
        private Long lastModifyTime;
    }
}
