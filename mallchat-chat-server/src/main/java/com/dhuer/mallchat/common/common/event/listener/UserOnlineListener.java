package com.dhuer.mallchat.common.common.event.listener;

import com.dhuer.mallchat.common.common.event.UserOnlineEvent;
import com.dhuer.mallchat.common.common.event.UserRegisterEvent;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.domain.enums.IdempoteneEnum;
import com.dhuer.mallchat.common.user.domain.enums.ItemEnum;
import com.dhuer.mallchat.common.user.domain.enums.UserActiveStatusEnum;
import com.dhuer.mallchat.common.user.service.IUserBackpackService;
import com.dhuer.mallchat.common.user.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserOnlineListener {
    @Autowired
    private IpService ipService;
    @Autowired
    private UserDao userDao;

    // fallbackExecution 没有事务时也会执行
    @Async
    @TransactionalEventListener(classes = UserOnlineEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void saveDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        // 用户 IP 详情解析
        ipService.refreshIpDetailAsync(user.getId());
    }
}
