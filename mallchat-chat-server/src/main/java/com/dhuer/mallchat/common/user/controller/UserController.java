package com.dhuer.mallchat.common.user.controller;

import com.dhuer.mallchat.common.common.domain.vo.resp.ApiResult;
import com.dhuer.mallchat.common.common.utils.RequestHolder;
import com.dhuer.mallchat.common.user.domain.vo.req.ModifyNameReq;
import com.dhuer.mallchat.common.user.domain.vo.req.WearingBadgeReq;
import com.dhuer.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.dhuer.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.dhuer.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-04-03
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户相关信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }
    @PutMapping("/name")
    @ApiOperation("修改用户名")
    // 想要校验入参的话要加 @Valid 注解
    public ApiResult<Void> modifyNmae(@Valid @RequestBody ModifyNameReq req){
        userService.modifyName(RequestHolder.get().getUid(), req.getName());
        return ApiResult.success();
    }

    @GetMapping("/badges")
    @ApiOperation("可选徽章预览")
    public ApiResult<List<BadgeResp>> badges() {
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/badges")
    @ApiOperation("佩戴徽章")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody WearingBadgeReq req) {
        userService.wearingBadge(RequestHolder.get().getUid(), req.getItemId());
        return ApiResult.success();
    }
}