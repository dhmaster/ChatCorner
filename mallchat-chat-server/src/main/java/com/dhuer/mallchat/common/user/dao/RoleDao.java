package com.dhuer.mallchat.common.user.dao;

import com.dhuer.mallchat.common.user.domain.entity.Role;
import com.dhuer.mallchat.common.user.mapper.RoleMapper;
import com.dhuer.mallchat.common.user.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-06-01
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {

}
