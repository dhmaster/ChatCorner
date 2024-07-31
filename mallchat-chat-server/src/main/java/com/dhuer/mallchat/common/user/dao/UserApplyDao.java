package com.dhuer.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dhuer.mallchat.common.user.domain.entity.UserApply;
import com.dhuer.mallchat.common.user.mapper.UserApplyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-07-07
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> implements IService<UserApply> {

}
