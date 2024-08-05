package com.dhuer.mallchat.common.chat.service.adapter;

import com.dhuer.mallchat.common.chat.domain.enums.HotFlagEnum;
import com.dhuer.mallchat.common.chat.domain.enums.RoomTypeEnum;
import com.dhuer.mallchat.common.common.domain.enums.NormalOrInnormalEnum;
import com.dhuer.mallchat.common.user.domain.entity.Room;
import com.dhuer.mallchat.common.user.domain.entity.RoomFriend;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/8/4
 */
public class ChatAdapter {

    public static final String SEPARATOR = ",";

    /**
     * 生成单聊房间钥匙，小数在前
     * @param uidList
     * @return
     */
    public static String generateRoomKey(List<Long> uidList) {
        return uidList.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(SEPARATOR));
    }

    /**
     * 创建聊天室
     * @param typeEnum
     * @return
     */
    public static Room buildRoom(RoomTypeEnum typeEnum) {
        Room room = new Room();
        room.setType(typeEnum.getType());
        room.setHotFlag(HotFlagEnum.NO.getType());
        return room;
    }

    public static RoomFriend buildFriendRoom(Long roomId, List<Long> uidList) {
        List<Long> collect = uidList.stream().sorted().collect(Collectors.toList());
        RoomFriend roomFriend = new RoomFriend();
        roomFriend.setRoomId(roomId);
        roomFriend.setUid1(collect.get(0));
        roomFriend.setUid2(collect.get(1));
        roomFriend.setRoomKey(generateRoomKey(uidList));
        roomFriend.setStatus(NormalOrInnormalEnum.NORMAL.getStatus());
        return roomFriend;
    }
}
