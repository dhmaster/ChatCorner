package com.dhuer.mallchat.common.user.service;

import com.dhuer.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
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
    CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);
}
