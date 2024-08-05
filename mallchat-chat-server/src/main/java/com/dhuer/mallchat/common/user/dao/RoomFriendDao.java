package com.dhuer.mallchat.common.user.dao;

import com.dhuer.mallchat.common.common.domain.enums.NormalOrInnormalEnum;
import com.dhuer.mallchat.common.user.domain.entity.RoomFriend;
import com.dhuer.mallchat.common.user.mapper.RoomFriendMapper;
import com.dhuer.mallchat.common.user.service.ChatRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单聊房间表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-08-04
 */
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend> {
    /**
     * 根据房间号查询房间
     * @param key
     * @return
     */
    public RoomFriend getByKey(String key) {
        return lambdaQuery().eq(RoomFriend::getRoomKey, key).one();
    }

    /**
     * 设置房间状态为正常
     * @param id
     */
    public void restoreRoom(Long id) {
        lambdaUpdate().eq(RoomFriend::getId, id)
                .set(RoomFriend::getStatus, NormalOrInnormalEnum.NORMAL.getStatus())
                .update();
    }
}
