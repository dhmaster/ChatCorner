package com.dhuer.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dhuer.mallchat.common.user.domain.entity.UserApply;
import com.dhuer.mallchat.common.user.domain.enums.ApplyStatusEnum;
import com.dhuer.mallchat.common.user.domain.enums.ApplyTypeEnum;
import com.dhuer.mallchat.common.user.mapper.UserApplyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-07-07
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> {

    /**
     * 获取好友申请记录
     * @param uid
     * @param targetUid
     * @return
     */
    public UserApply getFriendApproving(Long uid, Long targetUid) {
        return lambdaQuery().eq(UserApply::getUid, uid)
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL.getCode())
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .one();
    }

    /**
     * 同意好友申请
     * @param applyId
     */
    public void approve(Long applyId) {
        lambdaUpdate().set(UserApply::getStatus, ApplyStatusEnum.APPROVE.getCode())
                .eq(UserApply::getId, applyId)
                .update();
    }
}
