package com.dhuer.mallchat.common.common.event;

import com.dhuer.mallchat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/26
 */
@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private User user;

    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
