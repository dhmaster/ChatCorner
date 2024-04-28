package com.dhuer.mallchat.common.user.controller;

import com.dhuer.mallchat.common.user.domain.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/public/userInfo")
    @ApiOperation("获取用户相关信息")
    public UserInfoResp getUserInfo(@RequestParam Long uid) {
        return null;
    }

}