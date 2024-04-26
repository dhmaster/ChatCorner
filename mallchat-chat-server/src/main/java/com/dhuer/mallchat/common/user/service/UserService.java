package com.dhuer.mallchat.common.user.service;

import com.dhuer.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-04-03
 */
public interface UserService {
    Long register(User insert);
}
