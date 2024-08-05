package com.dhuer.mallchat.common.user.controller;

import com.dhuer.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.req.PageBaseReq;
import com.dhuer.mallchat.common.common.domain.vo.resp.ApiResult;
import com.dhuer.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.dhuer.mallchat.common.common.domain.vo.resp.PageBaseResp;
import com.dhuer.mallchat.common.common.utils.RequestHolder;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendApplyReq;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendApproveReq;
import com.dhuer.mallchat.common.user.domain.vo.req.friend.FriendDeleteReq;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendApplyResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendApplyUnreadResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.friend.FriendResp;
import com.dhuer.mallchat.common.user.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/apply")
    @ApiOperation("申请好友")
    public ApiResult<Void> apply(@Valid @RequestBody FriendApplyReq request) {
        Long uid = RequestHolder.get().getUid();
        friendService.apply(uid, request);
        return ApiResult.success();
    }

    @PutMapping("/apply")
    @ApiOperation("审批同意")
    public ApiResult<Void> applyApprove(@Valid @RequestBody FriendApproveReq request) {
        friendService.applyApprove(RequestHolder.get().getUid(), request);
        return ApiResult.success();
    }

    @GetMapping("/apply/list")
    @ApiOperation("好友申请列表")
    public ApiResult<PageBaseResp<FriendApplyResp>> applyList(@Valid PageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.applyList(uid, request));
    }

    @GetMapping("/apply/unreadCount")
    @ApiOperation("好友申请未读数")
    public ApiResult<FriendApplyUnreadResp> unreadCount() {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.unreadCount(uid));
    }

    @DeleteMapping()
    @ApiOperation("删除好友")
    public ApiResult<Void> delete(@Valid @RequestBody FriendDeleteReq request) {
        Long uid = RequestHolder.get().getUid();
        friendService.deleteFriend(uid, request.getTargetUid());
        return ApiResult.success();
    }
}

