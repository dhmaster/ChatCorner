package com.dhuer.mallchat.common.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/8/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoDTO {
    @ApiModelProperty("徽章id")
    private Long itemId;

    @ApiModelProperty("是否需要刷新")
    private Boolean needRefresh = Boolean.TRUE;

    @ApiModelProperty("徽章图像")
    private String img;

    @ApiModelProperty("徽章说明")
    private String desc;

    public static ItemInfoDTO skip(Long itemId) {
        ItemInfoDTO dto = new ItemInfoDTO();
        dto.setItemId(itemId);
        dto.setNeedRefresh(Boolean.FALSE);
        return dto;
    }
}
