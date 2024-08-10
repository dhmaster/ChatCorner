package com.dhuer.mallchat.common.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/8/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryInfoDTO {
    @ApiModelProperty("用户 id")
    private Long uid;

    @ApiModelProperty("是否需要刷新")
    private Boolean needRefresh = Boolean.TRUE;

    @ApiModelProperty("用户昵称")
    private String name;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("归属地")
    private String locPlace;

    @ApiModelProperty("佩戴的徽章 id")
    private Long wearingItemId;

    @ApiModelProperty("用户拥有的徽章 id 列表")
    List<Long> itemIds;

    public static SummaryInfoDTO skip(Long uid) {
        SummaryInfoDTO dto = new SummaryInfoDTO();
        dto.setUid(uid);
        dto.setNeedRefresh(Boolean.FALSE);
        return dto;
    }
}
