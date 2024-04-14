package com.dhuer.mallchat.common.websocket.service;

import io.netty.channel.Channel;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/8
 */
public interface WebSocketService {
    void connect(Channel channel);

    void handlerLoginReq(Channel channel);

    void offLine(Channel channel);

    void scanLoginSuccess(Integer code, Long uid);

    void waitAuthorize(Integer code);
}
