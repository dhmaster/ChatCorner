package com.dhuer.mallchat.common.user.service.impl;

import com.dhuer.mallchat.common.common.annotation.RedissonLock;
import com.dhuer.mallchat.common.common.event.UserBlackEvent;
import com.dhuer.mallchat.common.common.event.UserRegisterEvent;
import com.dhuer.mallchat.common.common.utils.AssertUtil;
import com.dhuer.mallchat.common.user.dao.BlackDao;
import com.dhuer.mallchat.common.user.dao.ItemConfigDao;
import com.dhuer.mallchat.common.user.dao.UserBackpackDao;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.domain.entity.*;
import com.dhuer.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.dhuer.mallchat.common.user.domain.enums.ItemEnum;
import com.dhuer.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.dhuer.mallchat.common.user.domain.vo.req.BlackReq;
import com.dhuer.mallchat.common.user.domain.vo.resp.user.BadgeResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.user.UserInfoResp;
import com.dhuer.mallchat.common.user.service.UserService;
import com.dhuer.mallchat.common.user.service.adapter.UserAdapter;
import com.dhuer.mallchat.common.user.service.cache.ItemCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/9
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private ItemConfigDao itemConfigDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private BlackDao blackDao;

    @Override
    @Transactional
    public Long register(User insert) {
        userDao.save(insert);
        // 用户注册事件
        // 可以使用 MQ 实现，也可以使用 Spring 事务，订阅者模式更加灵活，可以在事务前执行，也可以事务后执行
        // this 表示事件的订阅者需要知道信息从哪个类发出
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, insert));
        return insert.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer modifyNameCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user, modifyNameCount);
    }

    // 告诉 Spring 这个方法是事务性的，即方法内部的操作要么全部成功，要么在发生异常时全部回滚。
    // rollbackFor = Exception.class，指示 Spring 在捕获到任何类型的 Exception（包括检查型异常和运行时异常）时都回滚事务。
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        AssertUtil.isEmpty(oldUser,"名称已经被抢占了，请换一个！");
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem, "改名卡数量不足。。");
        // 使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if (success) {
            // 改名
            userDao.modifyName(uid, name);
        }
    }

    @Override
    public List<BadgeResp> badges(Long uid) {
        // 查询所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        // 查询用户拥有的徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        // 用户当前佩戴的徽章
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs, backpacks, user);
    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
        // 确保有徽章
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, itemId);
        AssertUtil.isNotEmpty(firstValidItem, "您还没有获得此徽章。");
        // 确保物品是徽章
        ItemConfig itemConfig = itemConfigDao.getById(firstValidItem.getItemId());
        AssertUtil.equal(itemConfig.getType(), ItemTypeEnum.BADGE.getType(), "只有徽章才能佩戴。");
        userDao.wearingBadge(uid, itemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void black(BlackReq req) {
        Long uid = req.getUid();
        Black user = new Black();
        user.setType(BlackTypeEnum.UID.getType());
        user.setTarget(uid.toString());
        blackDao.save(user);
        // 拉黑所有 IP
        User byId = userDao.getById(uid);
        blackIp(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getCreateIp).orElse(null));
        blackIp(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getUpdateIp).orElse(null));
        applicationEventPublisher.publishEvent(new UserBlackEvent(this, byId));
    }

    private void blackIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return;
        }
        // 防止 CreateIp 和 UpdateIp 相同时拉黑两次同一 ip 而报错
        try {
            Black insert = new Black();
            insert.setType(BlackTypeEnum.IP.getType());
            insert.setTarget(ip);
            blackDao.save(insert);
        } catch (Exception e) {}
    }
}
