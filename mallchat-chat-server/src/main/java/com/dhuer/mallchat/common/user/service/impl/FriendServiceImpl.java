package com.dhuer.mallchat.common.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dhuer.mallchat.common.common.annotation.RedissonLock;
import com.dhuer.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.req.PageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.dhuer.mallchat.common.common.domain.vo.resp.PageBaseResp;
import com.dhuer.mallchat.common.common.event.UserApplyEvent;
import com.dhuer.mallchat.common.common.utils.AssertUtil;
import com.dhuer.mallchat.common.user.dao.UserApplyDao;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.dao.UserFriendDao;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.domain.entity.UserApply;
import com.dhuer.mallchat.common.user.domain.entity.UserFriend;
import com.dhuer.mallchat.common.user.domain.enums.ApplyStatusEnum;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendApplyReq;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendApproveReq;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendApplyResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendResp;
import com.dhuer.mallchat.common.user.service.FriendService;
import com.dhuer.mallchat.common.user.service.ChatRoomService;
import com.dhuer.mallchat.common.user.service.adapter.FriendAdapter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/9
 */
@Service
@Slf4j
public class FriendServiceImpl implements FriendService {
    @Autowired
    private UserFriendDao userFriendDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserApplyDao userApplyDao;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 好友列表
     * @param uid
     * @param request
     * @return
     */
    @Override
    public CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request) {
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, request);
        if (CollectionUtils.isEmpty(friendPage.getList())) {
            return CursorPageBaseResp.empty();
        }
        // 从friendPage对象中获取好友列表，并提取每个好友的UID，最后将这些UID收集到一个List<Long>中。
        List<Long> friendUids = friendPage.getList()
                .stream().map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        // 获取用户头像、昵称、状态等信息
        List<User> userList = userDao.getFriendList(friendUids);
        // 组装返回结果
        return CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriend(friendPage.getList(), userList));
    }

    /**
     * 申请好友
     * @param uid
     * @param request
     */
    @Override
    @RedissonLock(key = "#uid")
    public void apply(Long uid, FriendApplyReq request) {
        // 是否有好友关系
        UserFriend friend = userFriendDao.getByFriend(uid, request.getTargetUid());
        AssertUtil.isEmpty(friend, "你们已经是好友了！！！");
        // 是否有待审批的好友申请（自己请求目标对象的）
        UserApply selfApproving = userApplyDao.getFriendApproving(uid, request.getTargetUid());
        if (Objects.nonNull(selfApproving)) {
            log.info("已有好友申请记录，uid：{}  targetUid：{}", uid, request.getTargetUid());
            return;
        }
        // 是否有待审批的好友申请（目标对象请求自己的）
        UserApply friendApproving = userApplyDao.getFriendApproving(request.getTargetUid(), uid);
        if (Objects.nonNull(friendApproving)) {
            ((FriendService)AopContext.currentProxy()).applyApprove(uid, new FriendApproveReq(friendApproving.getId()));
            return;
        }
        // 之前没有任何申请记录，将申请入库
        UserApply insert = FriendAdapter.buildFriendApply(uid, request);
        userApplyDao.save(insert);
        // 发布申请事件
        applicationEventPublisher.publishEvent(new UserApplyEvent(this, insert));
    }

    /**
     * 同意好友申请
     * @param uid
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void applyApprove(Long uid, FriendApproveReq request) {
        UserApply userApply = userApplyDao.getById(request.getApplyId());
        AssertUtil.isNotEmpty(userApply, "不存在申请记录！！！");
        AssertUtil.equal(userApply.getTargetId(), uid, "不存在申请记录！！！");
        AssertUtil.equal(userApply.getStatus(), ApplyStatusEnum.WAIT_APPROVAL.getCode(), "已同意好友申请");
        // 同意申请
        userApplyDao.approve(request.getApplyId());
        // 创建双方好友关系
        createFriend(uid, userApply.getUid());
        // 创建一个聊天房间
        chatRoomService.createFriendRoom(Arrays.asList(uid, userApply.getUid()));
        // TODO 同意的一方发送一条消息：我们已经是好友了，开始聊天吧！
    }

    /**
     * 创建双方好友关系，在 user_friend 表中保存两条记录
     * @param uid
     * @param targetUid
     */
    private void createFriend(Long uid, Long targetUid) {
        UserFriend userFriend1 = new UserFriend();
        userFriend1.setUid(uid);
        userFriend1.setFriendUid(targetUid);
        UserFriend userFriend2 = new UserFriend();
        userFriend2.setUid(targetUid);
        userFriend2.setFriendUid(uid);
        userFriendDao.saveBatch(Lists.newArrayList(userFriend1, userFriend2));
    }

    /**
     * 分页查询好友申请
     * @param uid
     * @param request
     * @return
     */
    @Override
    public PageBaseResp<FriendApplyResp> applyList(Long uid, PageBaseReq request) {
        IPage<UserApply> userApplyIPage = userApplyDao.friendApplyList(uid, request.plusPage());
        if (CollectionUtil.isEmpty(userApplyIPage.getRecords())) {
            return PageBaseResp.empty();
        }
        // 将这些申请设置为已读
        readApply(uid, userApplyIPage);
        return PageBaseResp.init(userApplyIPage, FriendAdapter.buildFriendApplyList(userApplyIPage.getRecords()));
    }

    /**
     * 已读所有申请
     * @param uid
     * @param userApplyIPage
     */
    private void readApply(Long uid, IPage<UserApply> userApplyIPage) {
        List<Long> applyIds = userApplyIPage.getRecords()
                .stream().map(UserApply::getId)
                .collect(Collectors.toList());
        userApplyDao.readApply(uid, applyIds);
    }
}
