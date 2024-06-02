package com.dhuer.mallchat.common.common.event.listener;

import com.dhuer.mallchat.common.common.event.UserBlackEvent;
import com.dhuer.mallchat.common.common.event.UserOnlineEvent;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.domain.enums.UserActiveStatusEnum;
import com.dhuer.mallchat.common.user.service.IpService;
import com.dhuer.mallchat.common.user.service.cache.UserCache;
import com.dhuer.mallchat.common.websocket.service.WebSocketService;
import com.dhuer.mallchat.common.websocket.service.adapter.WebSocketAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/6/1
 */
@Component
public class UserBlackListener {
    @Autowired
    private UserDao userDao;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserCache userCache;

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void sendMsg(UserBlackEvent event) {
        User user = event.getUser();
        webSocketService.sendMsgToAll(WebSocketAdapter.buildBlack(user));
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void changeUserStatus(UserBlackEvent event) {
        userDao.invalidUid(event.getUser().getId());
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void evictCache(UserBlackEvent event) {
        userCache.evictBlackMap();
    }
}
