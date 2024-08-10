package com.dhuer.mallchat.common.common.constant;

/**
 * Description:组装 Redis 的 Key
 * Author: Jintao Li
 * Date: 2024/4/17
 */
public class RedisKey {
    private static final String BASE_KEY = "mallchat:chat";
    /**
     * 用户 token 的 key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    /**
     * 用户信息的更新时间
     */
    public static final String USER_MODIFY_STRING = "userModify:uid_%d";

    /**
     * 用户信息汇总
     */
    public static final String USER_SUMMARY_STRING = "userSummary:uid_%d";

    /**
     * 用户信息
     */
    public static final String USER_INFO_STRING = "userInfo:uid_%d";

    /**
     * 获取 key
     * @param key
     * @param o
     * @return
     */
    public static String getKey(String key, Object... o) {
        return String.format(key, o);
    }
}
