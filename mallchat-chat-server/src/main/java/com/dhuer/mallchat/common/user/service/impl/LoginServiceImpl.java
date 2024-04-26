package com.dhuer.mallchat.common.user.service.impl;

import com.dhuer.mallchat.common.common.constant.RedisKey;
import com.dhuer.mallchat.common.common.utils.JwtUtils;
import com.dhuer.mallchat.common.common.utils.RedisUtils;
import com.dhuer.mallchat.common.user.service.LoginService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/10
 */
@Service
public class LoginServiceImpl implements LoginService {
    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEW_DAYS = 1;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        // key 不存在
        if (expireDays == -2) {
            return;
        }
        if (expireDays < TOKEN_RENEW_DAYS) {
            RedisUtils.expire(getUserTokenKey(uid), TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }
        ArrayList<Integer> list = new ArrayList<>();
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(uid), token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return null;
        }
        String oldToken = RedisUtils.getStr(getUserTokenKey(uid));
        if (StringUtils.isBlank(oldToken)) {
            return null;
        }
        return Objects.equals(oldToken, token)? uid : null;
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }
}
