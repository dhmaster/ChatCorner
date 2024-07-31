package com.dhuer.mallchat.common.user.service.impl;

import com.dhuer.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.dhuer.mallchat.common.user.dao.UserDao;
import com.dhuer.mallchat.common.user.dao.UserFriendDao;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.domain.entity.UserFriend;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendResp;
import com.dhuer.mallchat.common.user.service.FriendService;
import com.dhuer.mallchat.common.user.service.adapter.FriendAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/4/9
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private UserFriendDao userFriendDao;
    @Autowired
    private UserDao userDao;

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
}
