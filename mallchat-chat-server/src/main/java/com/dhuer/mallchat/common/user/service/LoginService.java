package com.dhuer.mallchat.common.user.service;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/10
 */
public interface LoginService {
    /**
     * 刷新 token 有效期
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    /**
     * 登录成功，获取 token
     * @param uid
     * @return
     */
    String login(Long uid);

    /**
     * 如果 token 有效，返回 uid
     * @param token
     * @return
     */
    Long getValidUid(String token);
}
