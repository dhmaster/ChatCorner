package com.dhuer.mallchat.common.user.controller;

import com.dhuer.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.resp.ApiResult;
import com.dhuer.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.dhuer.mallchat.common.common.utils.RequestHolder;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendResp;
import com.dhuer.mallchat.common.user.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 好友相关接口
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-07-07
 */
@RestController
@RequestMapping("/capi/user/friend")
@Api(tags = "好友相关接口")
@Slf4j
public class FriendController {
    @Resource
    private FriendService friendService;

    @GetMapping("/page")
    @ApiOperation("联系人列表")
    public ApiResult<CursorPageBaseResp<FriendResp>> friendList(@Valid CursorPageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.friendList(uid, request));
    }
}

