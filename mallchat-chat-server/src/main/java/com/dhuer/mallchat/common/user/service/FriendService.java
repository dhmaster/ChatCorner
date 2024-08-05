package com.dhuer.mallchat.common.user.service;

import com.dhuer.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.req.PageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.dhuer.mallchat.common.common.domain.vo.resp.PageBaseResp;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendApplyReq;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendApproveReq;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendCheckReq;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendApplyResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendApplyUnreadResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendCheckResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendResp;

/**
 * <p>
 * 好友
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-07-07
 */
public interface FriendService {
    /**
     * 好友列表
     * @param uid
     * @param request
     * @return
     */
    CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);

    /**
     * 申请好友
     * @param uid
     * @param request
     */
    void apply(Long uid, FriendApplyReq request);

    /**
     * 同意好友申请
     * @param uid
     * @param request
     */
    void applyApprove(Long uid, FriendApproveReq request);

    /**
     * 好友申请列表
     * @param uid
     * @param request
     * @return
     */
    PageBaseResp<FriendApplyResp> applyList(Long uid, PageBaseReq request);

    /**
     * 好友申请未读数
     * @param uid
     * @return
     */
    FriendApplyUnreadResp unreadCount(Long uid);

    /**
     * 删除好友
     * @param uid
     * @param targetUid
     */
    void deleteFriend(Long uid, Long targetUid);

    /**
     * 批量判断是否为自己好友
     * @param uid
     * @param request
     * @return
     */
    FriendCheckResp check(Long uid, FriendCheckReq request);
}
