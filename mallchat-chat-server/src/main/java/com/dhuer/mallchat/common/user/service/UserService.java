package com.dhuer.mallchat.common.user.service;

import com.dhuer.mallchat.common.user.domain.dto.ItemInfoDTO;
import com.dhuer.mallchat.common.user.domain.dto.SummaryInfoDTO;
import com.dhuer.mallchat.common.user.domain.entity.User;
import com.dhuer.mallchat.common.user.domain.vo.req.user.BlackReq;
import com.dhuer.mallchat.common.user.domain.vo.req.user.ItemInfoReq;
import com.dhuer.mallchat.common.user.domain.vo.req.user.SummaryInfoReq;
import com.dhuer.mallchat.common.user.domain.vo.resp.user.BadgeResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.user.UserInfoResp;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-04-03
 */
public interface UserService {
    /**
     * 用户注册，需要获得 id
     * @param insert
     */
    Long register(User insert);

    /**
     * 获取前端展示信息
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);

    /**
     * 获取用户汇总信息
     * @param req
     * @return
     */
    List<SummaryInfoDTO> getSummaryUserInfo(SummaryInfoReq req);

    /**
     * 获取徽章汇总信息
     * @param req
     * @return
     */
    List<ItemInfoDTO> getItemInfo(ItemInfoReq req);

    /**
     * 修改用户名
     * @param uid
     * @param name
     */
    void modifyName(Long uid, String name);

    /**
     * 用户徽章列表
     * @param uid
     * @return
     */
    List<BadgeResp> badges(Long uid);

    /**
     * 佩戴徽章
     * @param uid
     * @param itemId
     */
    void wearingBadge(Long uid, Long itemId);

    /**
     * 拉黑用户
     * @param req
     */
    void black(BlackReq req);
}
