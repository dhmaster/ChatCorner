package com.dhuer.mallchat.common.common.event.listener;

import com.dhuer.mallchat.common.common.event.UserRegisterEvent;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.domain.enums.IdempoteneEnum;
import com.dhuer.mallchat.common.user.domain.enums.ItemEnum;
import com.dhuer.mallchat.common.user.service.IUserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/26
 */
@Component
public class UserRegisterListener {
    @Autowired
    private IUserBackpackService userBackpackService;
    @Autowired
    private UserDao userDao;

    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendModifyNameCard(UserRegisterEvent event) {
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempoteneEnum.UID, user.getId().toString());
    }

    @TransactionalEventListener(classes = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendBadge(UserRegisterEvent event) {
        // 徽章发放
        User user = event.getUser();
        int registeredCount = userDao.count();
        if (registeredCount < 10) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempoteneEnum.UID, user.getId().toString());
        } else if (registeredCount < 100) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempoteneEnum.UID, user.getId().toString());
        }

    }
}
