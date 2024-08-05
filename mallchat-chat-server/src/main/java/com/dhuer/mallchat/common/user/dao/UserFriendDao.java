package com.dhuer.mallchat.common.user.dao;

import com.dhuer.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.dhuer.mallchat.common.common.utils.CursorUtils;
import com.dhuer.mallchat.common.user.domain.entity.UserFriend;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendResp;
import com.dhuer.mallchat.common.user.mapper.UserFriendMapper;
import com.dhuer.mallchat.common.user.service.FriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户联系人表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-07-07
 */
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {

    /**
     * 获取好友列表
     * @param uid
     * @param cursorPageBaseReq
     * @return
     */
    public CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq cursorPageBaseReq) {
        return CursorUtils.getCursorPageByMysql(this, cursorPageBaseReq,
                wrapper -> wrapper.eq(UserFriend::getUid, uid), UserFriend::getId);
    }

    /**
     * 查询是否是好友关系
     * @param uid
     * @param targetUid
     * @return
     */
    public UserFriend getByFriend(Long uid, Long targetUid) {
        return lambdaQuery().eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .one();
    }
}
