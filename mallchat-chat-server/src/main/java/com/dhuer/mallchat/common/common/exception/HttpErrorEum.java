package com.dhuer.mallchat.common.common.exception;

import cn.hutool.http.ContentType;
import com.dhuer.mallchat.common.common.domain.vo.resp.ApiResult;
import com.dhuer.mallchat.common.common.utils.JsonUtils;
import com.google.common.base.Charsets;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 用户鉴权失败，返回 401
 * Author: Jintao Li
 * Date: 2024/4/28
 */
@AllArgsConstructor
public enum HttpErrorEum {
    ACCESS_DENIED(401,"登录失效请重新登录！");
    private Integer httpCode;
    private String desc;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(httpCode,desc)));
    }
}