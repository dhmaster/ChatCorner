package com.dhuer.mallchat.common.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Optional;

/**
 * Description:获取请求头
 * Author: Jintao Li
 * Date: 2024/4/25
 */
public class MyHeadCollectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.getUri());
            Optional<String> tokenOptional = Optional.of(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(k -> k.get("token"))
                    .map(CharSequence::toString);
            if (tokenOptional.isPresent()) {
                NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, tokenOptional.get());
            }
            // 将 url 携带的 token 移除
            request.setUri(urlBuilder.getPath().toString());
            // 获取用户 ip
            // 获取的是 Nginx 的远端地址
            String ip = request.headers().get("X-Real-IP");
            if (StringUtils.isBlank(ip)) {
                // 直连 ip 地址
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                ip = address.getAddress().getHostAddress();
            }
            // 保存到 channel 附件
            NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);
            // 处理器只需要用一次
            ctx.pipeline().remove(this);
        }
        ctx.fireChannelRead(msg);
    }
}
