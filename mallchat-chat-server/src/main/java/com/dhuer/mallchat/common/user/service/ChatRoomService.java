package com.dhuer.mallchat.common.user.service;

import com.dhuer.mallchat.common.user.domain.entity.RoomFriend;

import java.util.List;

/**
 * <p>
 * 聊天房间底层管理
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-08-04
 */
public interface ChatRoomService {
    /**
     * 创建一个单聊房间
     * @param uidList
     * @return
     */
    RoomFriend createFriendRoom(List<Long> uidList);
}
