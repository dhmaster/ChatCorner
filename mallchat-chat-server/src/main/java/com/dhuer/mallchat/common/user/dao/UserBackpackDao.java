package com.dhuer.mallchat.common.user.dao;

import com.dhuer.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.dhuer.mallchat.common.user.domain.entity.UserBackpack;
import com.dhuer.mallchat.common.user.mapper.UserBackpackMapper;
import com.dhuer.mallchat.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-05-08
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack>{

    public Integer getCountByValidItemId(Long uid, Long itemId) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
    }

    public UserBackpack getFirstValidItem(Long uid, Long itemId) {
        // orderByAsc() 选择最旧的条目
        // last("limit 1") 直接修改 SQL 查询，包括一个 LIMIT 1 子句，确保数据库仅检索第一个匹配的记录。
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .orderByAsc(UserBackpack::getId)
                .last("limit 1")
                .one();
    }

    public boolean useItem(UserBackpack item) {
        // 乐观更新，先判断状态为 NO，再执行后面的 SQL 语句。
        // 在并发过程中也只有一个人可以成功，因为数据库是中心化的
        return lambdaUpdate()
                .eq(UserBackpack::getId, item.getId())
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .set(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus())
                .update();
    }

    public List<UserBackpack> getByItemIds(Long uid, List<Long> itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .in(UserBackpack::getItemId, itemId)
                .list();
    }

    public UserBackpack getByIdempotent(String idempotent) {
        return lambdaQuery()
                .eq(UserBackpack::getIdempotent, idempotent)
                .one();
    }
}
