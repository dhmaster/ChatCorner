package com.dhuer.mallchat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.service.LoginService;
import com.dhuer.mallchat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.dhuer.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import com.dhuer.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.dhuer.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;
import com.dhuer.mallchat.common.websocket.service.WebSocketService;
import com.dhuer.mallchat.common.websocket.service.adapter.WebSocketAdapter;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:专门管理 websocket 逻辑，包括推拉
 * Author: Jintao Li
 * Date: 2024/4/8
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    @Lazy
    private WxMpService wxMpService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;
    /***
     * 管理所有用户的连接（登录态/游客）
     * */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    public static final int MAXIMUM_SIZE = 1000;
    public static final Duration DURATION = Duration.ofHours(1);
    /**
     * 临时保存登录 code 与 channel 的映射关系
     */
    private static final Cache<Integer,Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();
    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @Override
    public void offLine(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
        // TODO 用户下线
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        // 确认连接在机器上
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if(Objects.isNull(channel)) {
            return;
        }
        User user = userDao.getById(uid);
        // 移除 code
        WAIT_LOGIN_MAP.invalidate(code);
        // 调用登录模块获取 token
        String token = loginService.login(uid);
        // 用户登录
        loginSuccess(channel, user, token);
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorizeResp());
    }

    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if(Objects.nonNull(validUid)) {
            User user = userDao.getById(validUid);
            loginSuccess(channel, user, token);
        } else {
            sendMsg(channel, WebSocketAdapter.buildInvalidTokenResp());
        }
    }

    private void loginSuccess(Channel channel, User user, String token) {
        // 保存 channel 对应的 uid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        // TODO 用户上线成功的事件
        // 推送登陆成功消息
        sendMsg(channel, WebSocketAdapter.buildResp(user, token));
    }

    @SneakyThrows
    @Override
    public void handlerLoginReq(Channel channel) {
        // 生成随机码
        Integer code = generateLoginCode(channel);
        // 找微信申请带参二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code,(int)DURATION.getSeconds());
        // 把二维码推给前端
        sendMsg(channel, WebSocketAdapter.buildResp(wxMpQrCodeTicket));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    private Integer generateLoginCode(Channel channel) {
        Integer code;
        // 每次循环结束，会检查 WAIT_LOGIN_MAP 中是否存在以生成的 code 为键的映射，如果存在，则 code 被使用过，要重新生成。
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code,channel)));
        return code;
    }
}
