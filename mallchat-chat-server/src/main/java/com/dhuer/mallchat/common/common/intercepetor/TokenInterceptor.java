package com.dhuer.mallchat.common.common.intercepetor;

import cn.hutool.http.ContentType;
import com.dhuer.mallchat.common.common.exception.HttpErrorEum;
import com.dhuer.mallchat.common.user.service.LoginService;
import com.google.common.base.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * Description: 鉴权拦截器
 * Author: Jintao Li
 * Date: 2024/4/28
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_SCHEMA = "Bearer ";
    public static final String UID = "uid";

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) { // 用户有登录态
            request.setAttribute(UID, validUid);
        } else { // 用户未登录
            boolean isPublicURI = isPublicURI(request);
            if (!isPublicURI) {
                // 401
                HttpErrorEum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }
        }
        return true;
    }

    private boolean isPublicURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        return split.length>3 && "public".equals(split[3]);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        // 将变量 header 装箱成一个 Optional 对象，这样可以安全的处理可能为 null 的情况。
        // Optional 是 java 8 引入的一个类，用于处理可能为空的对象，以防止空指针异常。
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(AUTHORIZATION_SCHEMA))
                .map(h -> h.replace(AUTHORIZATION_SCHEMA, ""))
                .orElse(null);
    }
}
