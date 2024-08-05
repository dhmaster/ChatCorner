package com.dhuer.mallchat.common.user.service.adapter;

import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.domain.entity.UserApply;
import com.dhuer.mallchat.common.user.domain.entity.UserFriend;
import com.dhuer.mallchat.common.user.domain.enums.ApplyReadStatusEnum;
import com.dhuer.mallchat.common.user.domain.enums.ApplyStatusEnum;
import com.dhuer.mallchat.common.user.domain.enums.ApplyTypeEnum;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendApplyReq;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendApplyResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendResp;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: 好友适配器
 * Author: Jintao Li
 * Date: 2024/4/9
 */
public class FriendAdapter {

    /**
     * 好友列表消息体组装
     * @param list
     * @param userList
     * @return
     */
    public static List<FriendResp> buildFriend(List<UserFriend> list, List<User> userList) {
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        return list.stream().map(userFriend -> {
            FriendResp resp = new FriendResp();
            resp.setUid(userFriend.getFriendUid());
            User user = userMap.get(userFriend.getFriendUid());
            if (Objects.nonNull(user)) {
                resp.setActiveStatus(user.getActiveStatus());
            }
            return resp;
        }).collect(Collectors.toList());
    }

    public static UserApply buildFriendApply(Long uid, FriendApplyReq request) {
        UserApply userApply = new UserApply();
        userApply.setUid(uid);
        userApply.setMsg(request.getMsg());
        userApply.setType(ApplyTypeEnum.ADD_FRIEND.getCode());
        userApply.setTargetId(request.getTargetUid());
        userApply.setStatus(ApplyStatusEnum.WAIT_APPROVAL.getCode());
        userApply.setReadStatus(ApplyReadStatusEnum.UNREAD.getCode());
        return userApply;
    }

    public static List<FriendApplyResp> buildFriendApplyList(List<UserApply> records) {
        return records.stream().map(userApply -> {
            FriendApplyResp friendApplyResp = new FriendApplyResp();
            friendApplyResp.setUid(userApply.getUid());
            friendApplyResp.setType(userApply.getType());
            friendApplyResp.setApplyId(userApply.getId());
            friendApplyResp.setMsg(userApply.getMsg());
            friendApplyResp.setStatus(friendApplyResp.getStatus());
            return friendApplyResp;
        }).collect(Collectors.toList());
    }
}
