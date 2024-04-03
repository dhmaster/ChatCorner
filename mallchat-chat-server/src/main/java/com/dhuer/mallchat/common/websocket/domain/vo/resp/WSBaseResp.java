package com.dhuer.mallchat.common.websocket.domain.vo.resp;

public class WSBaseResp<T> {
    /**
     * @see com.dhuer.mallchat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;
    private T data;
}
