package com.dhuer.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.service.UserService;
import com.dhuer.mallchat.common.user.service.WXMsgService;
import com.dhuer.mallchat.common.user.service.adapter.TextBuilder;
import com.dhuer.mallchat.common.user.service.adapter.UserAdapter;
import com.dhuer.mallchat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/9
 */
@Slf4j
@Service
public class WXMsgServiceImpl implements WXMsgService {
    /**
     * openId 和登录 code 的关系 map
     */
    private static final ConcurrentHashMap<String, Integer> WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();

    @Value("${wx.mp.callback}")
    private String callback;

    public static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    @Lazy
    private WxMpService wxMpService;
    @Autowired
    private WebSocketService webSocketService;

    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        String openId = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)) {
            return null;
        }
        User user = userDao.getByOpenId(openId);
        boolean registered = Objects.nonNull(user);
        boolean authorized = registered && StrUtil.isNotBlank(user.getAvatar());
        // 已注册并已授权
        if (registered && authorized) {
            // TODO 走登录成功的逻辑，通过 code 找到给 channel 推送消息
            webSocketService.scanLoginSuccess(code, user.getId());
            return null;
        }
        // 未注册,就先注册的逻辑
        if (!registered) {
            User insert = UserAdapter.buildUserSave(openId);
            userService.register(insert);
        }
        // 推送链接让用户授权
        WAIT_AUTHORIZE_MAP.put(openId, code);
        webSocketService.waitAuthorize(code);
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback+"/wx/portal/public/callBack"));
        return new TextBuilder().build("请点击登录：<a href=\"" + authorizeUrl + "\">登录</a>",wxMpXmlMessage);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            String eventKey = wxMpXmlMessage.getEventKey();
            String code = eventKey.replace("qrscene_", "");
            return Integer.parseInt(code);
        }catch (Exception e) {
            log.error("getEventKey error! EventKey:{}",wxMpXmlMessage.getEventKey(),e);
            return null;
        }

    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openId = userInfo.getOpenid();
        User user = userDao.getByOpenId(openId);
        // 更新用户信息
        if (StrUtil.isBlank(user.getAvatar())) {
            fillUserInfo(user.getId(), userInfo);
        }
        // 通过登录 code 找到用户 channel 进行登录
        Integer code = WAIT_AUTHORIZE_MAP.remove(openId);
        webSocketService.scanLoginSuccess(code, user.getId());
    }

    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(uid, userInfo);
        userDao.updateById(user);
    }
}
