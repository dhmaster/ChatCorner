package com.dhuer.mallchat.common.user.service.impl;

import com.dhuer.mallchat.common.user.service.LoginService;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/10
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public void renewalTokenIfNecessary(String token) {

    }

    @Override
    public String login(Long uid) {
        // TODO 获取 token
        return null;
    }

    @Override
    public Long getValidUid(String token) {
        return null;
    }
}
