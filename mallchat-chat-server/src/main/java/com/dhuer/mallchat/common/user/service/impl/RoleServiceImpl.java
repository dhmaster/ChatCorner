package com.dhuer.mallchat.common.user.service.impl;

import com.dhuer.mallchat.common.user.domain.enums.RoleEnum;
import com.dhuer.mallchat.common.user.service.RoleService;
import com.dhuer.mallchat.common.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/6/1
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasRight(Long uid, RoleEnum roleEnum) {
        Set<Long> roleSet = userCache.getRoleSet(uid);
        return isAdmin(roleSet) || roleSet.contains(roleEnum.getId());
    }

    private boolean isAdmin(Set<Long> roleSet) {
        return roleSet.contains(RoleEnum.ADMIN.getId());
    }
}
