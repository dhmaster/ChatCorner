package com.dhuer.mallchat.common.user.service;

import com.dhuer.mallchat.common.user.domain.enums.IdempoteneEnum;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-05-08
 */
public interface IUserBackpackService {
    /**
     * 给用户发放一个物品
     * @param uid           用户 id
     * @param itemId        物品 id
     * @param idepoteneEnum 幂等类型
     * @param businessId    幂等唯一标识
     */
    void acquireItem(Long uid, Long itemId, IdempoteneEnum idepoteneEnum, String businessId);
}
