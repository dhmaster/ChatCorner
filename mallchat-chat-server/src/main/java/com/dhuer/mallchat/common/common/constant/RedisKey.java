package com.dhuer.mallchat.common.common.constant;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/17
 */
public class RedisKey {
    private static final String BASE_KEY = "mallchat:chat";
    /**
     * 用户 token 的 key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    public static String getKey(String key, Object... o) {
        return String.format(key, o);
    }
}
