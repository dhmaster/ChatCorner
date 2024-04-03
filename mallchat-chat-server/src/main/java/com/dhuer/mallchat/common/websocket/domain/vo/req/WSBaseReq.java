package com.dhuer.mallchat.common.websocket.domain.vo.req;

import lombok.Data;

/**
 * 前端请求格式
 */
@Data
public class WSBaseReq {
    /**
     * @see com.dhuer.mallchat.common.websocket.domain.enums.WSReqTypeEnum
     */
    private Integer type;
    private String data;
}
