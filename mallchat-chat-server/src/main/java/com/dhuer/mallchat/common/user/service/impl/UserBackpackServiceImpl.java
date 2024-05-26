package com.dhuer.mallchat.common.user.service.impl;

import com.dhuer.mallchat.common.common.annotation.RedissonLock;
import com.dhuer.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.dhuer.mallchat.common.common.service.LockService;
import com.dhuer.mallchat.common.common.utils.AssertUtil;
import com.dhuer.mallchat.common.user.dao.UserBackpackDao;
import com.dhuer.mallchat.common.user.domain.entity.UserBackpack;
import com.dhuer.mallchat.common.user.domain.enums.IdempoteneEnum;
import com.dhuer.mallchat.common.user.service.IUserBackpackService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/18
 */
@Service
public class UserBackpackServiceImpl implements IUserBackpackService {
    @Autowired
    private LockService lockService;
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    @Lazy
    private UserBackpackServiceImpl userBackpackService;

    @Override
    public void acquireItem(Long uid, Long itemId, IdempoteneEnum idempoteneEnum, String businessId) {
        String idempotent = getIdepotent(itemId, idempoteneEnum, businessId);
        lockService.executeWithLock("acquireItem" + idempotent, ()->{
            UserBackpack userBackpack = userBackpackDao.getByIdempotent(idempotent);
            userBackpackService.doAcquireItem(uid, itemId, idempotent);
        });
    }

    // 锁等待，不是立即失败，不会立即抛出异常
    @RedissonLock(key = "#idempotent", waitTime = 5000)
    public void doAcquireItem(Long uid, Long itemId, String idempotent) {
        UserBackpack userBackpack = userBackpackDao.getByIdempotent(idempotent);
        if (Objects.nonNull(userBackpack)) {
            return;
        }
        // 发放物品
        UserBackpack insert = UserBackpack.builder()
                .uid(uid)
                .itemId(itemId)
                .status(YesOrNoEnum.NO.getStatus())
                .idempotent(idempotent)
                .build();
        userBackpackDao.save(insert);
    }

    private String getIdepotent(Long itemId, IdempoteneEnum idepoteneEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idepoteneEnum.getType(), businessId);
    }
}
