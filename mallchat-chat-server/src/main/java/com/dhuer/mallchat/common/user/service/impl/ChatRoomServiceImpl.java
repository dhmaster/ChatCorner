package com.dhuer.mallchat.common.user.service.impl;

import com.dhuer.mallchat.common.chat.domain.enums.RoomTypeEnum;
import com.dhuer.mallchat.common.chat.service.adapter.ChatAdapter;
import com.dhuer.mallchat.common.common.domain.enums.NormalOrInnormalEnum;
import com.dhuer.mallchat.common.common.utils.AssertUtil;
import com.dhuer.mallchat.common.user.dao.RoomDao;
import com.dhuer.mallchat.common.user.dao.RoomFriendDao;
import com.dhuer.mallchat.common.user.domain.entity.Room;
import com.dhuer.mallchat.common.user.domain.entity.RoomFriend;
import com.dhuer.mallchat.common.user.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/8/4
 */
@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private RoomDao roomDao;

    /**
     * 创建双人单聊房间
     * @param uidList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomFriend createFriendRoom(List<Long> uidList) {
        AssertUtil.isNotEmpty(uidList, "房间创建失败，好友数量不对！！！");
        AssertUtil.equal(uidList.size(), 2, "房间创建失败，好友数量不对！！！");
        // 生成双人单聊房间钥匙
        String key = ChatAdapter.generateRoomKey(uidList);
        // 房间是否已经存在
        RoomFriend friendRoom = roomFriendDao.getByKey(key);
        // 如果存在，设置状态为正常
        if (Objects.nonNull(friendRoom)) {
            restoreRoom(friendRoom);
        } else {
            // 不存在则新建房间
            Room room = createRoom(RoomTypeEnum.FRIEND);
            friendRoom = createNewFriendRoom(room.getId(), uidList);
        }
        return friendRoom;
    }

    /**
     * 设置房间状态为正常
     * @param oldRoom
     */
    private void restoreRoom(RoomFriend oldRoom) {
        if (Objects.equals(oldRoom.getStatus(), NormalOrInnormalEnum.IN_NORMAL.getStatus())) {
            roomFriendDao.restoreRoom(oldRoom.getId());
        }
    }

    /**
     * 创建聊天室
     * @param typeEnum
     * @return
     */
    public Room createRoom(RoomTypeEnum typeEnum) {
        Room insert = ChatAdapter.buildRoom(typeEnum);
        roomDao.save(insert);
        return insert;
    }

    private RoomFriend createNewFriendRoom(Long roomId, List<Long> uidList) {
        RoomFriend insert = ChatAdapter.buildFriendRoom(roomId, uidList);
        roomFriendDao.save(insert);
        return insert;
    }
}
